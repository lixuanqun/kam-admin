import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Mtree } from './entities/mtree.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class MtreeService {
  constructor(
    @InjectRepository(Mtree)
    private mtreeRepository: Repository<Mtree>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto & { tname?: string }): Promise<PaginatedResult<Mtree>> {
    const { page, limit, keyword, tname } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.tprefix = Like(`%${keyword}%`);
    if (tname) where.tname = tname;
    const [items, total] = await this.mtreeRepository.findAndCount({
      where, skip, take: limit, order: { tname: 'ASC', tprefix: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<Mtree>): Promise<Mtree> {
    return this.mtreeRepository.save(this.mtreeRepository.create(data));
  }

  async update(id: number, data: Partial<Mtree>): Promise<Mtree> {
    const item = await this.mtreeRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('记录不存在');
    Object.assign(item, data);
    return this.mtreeRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.mtreeRepository.delete(id);
  }

  async reload(tname: string): Promise<void> {
    await this.kamailioRpcService.call('mtree.reload', [tname]);
  }

  async match(tname: string, prefix: string): Promise<any> {
    return this.kamailioRpcService.call('mtree.match', [tname, prefix]);
  }

  async summary(tname?: string): Promise<any> {
    if (tname) {
      return this.kamailioRpcService.call('mtree.summary', [tname]);
    }
    return this.kamailioRpcService.call('mtree.summary');
  }
}
