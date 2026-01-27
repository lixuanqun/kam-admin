import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Secfilter } from './entities/secfilter.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class SecfilterService {
  constructor(
    @InjectRepository(Secfilter)
    private secfilterRepository: Repository<Secfilter>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<Secfilter>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.data = Like(`%${keyword}%`);
    const [items, total] = await this.secfilterRepository.findAndCount({
      where, skip, take: limit, order: { type: 'ASC', id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<Secfilter>): Promise<Secfilter> {
    return this.secfilterRepository.save(this.secfilterRepository.create(data));
  }

  async update(id: number, data: Partial<Secfilter>): Promise<Secfilter> {
    const item = await this.secfilterRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('记录不存在');
    Object.assign(item, data);
    return this.secfilterRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.secfilterRepository.delete(id);
  }

  async reload(): Promise<void> {
    await this.kamailioRpcService.call('secfilter.reload');
  }

  async print(): Promise<any> {
    return this.kamailioRpcService.call('secfilter.print');
  }

  async stats(): Promise<any> {
    return this.kamailioRpcService.call('secfilter.stats');
  }

  async statsReset(): Promise<void> {
    await this.kamailioRpcService.call('secfilter.stats_reset');
  }

  async addBlacklist(type: number, data: string): Promise<void> {
    await this.kamailioRpcService.call('secfilter.add_bl', [type, data]);
  }

  async addWhitelist(type: number, data: string): Promise<void> {
    await this.kamailioRpcService.call('secfilter.add_wl', [type, data]);
  }
}
