import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Pdt } from './entities/pdt.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class PdtService {
  constructor(
    @InjectRepository(Pdt)
    private pdtRepository: Repository<Pdt>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<Pdt>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.prefix = Like(`%${keyword}%`);
    const [items, total] = await this.pdtRepository.findAndCount({
      where, skip, take: limit, order: { sdomain: 'ASC', prefix: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<Pdt>): Promise<Pdt> {
    return this.pdtRepository.save(this.pdtRepository.create(data));
  }

  async update(id: number, data: Partial<Pdt>): Promise<Pdt> {
    const item = await this.pdtRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('记录不存在');
    Object.assign(item, data);
    return this.pdtRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.pdtRepository.delete(id);
  }

  async reload(): Promise<void> {
    await this.kamailioRpcService.call('pdt.reload');
  }

  async list(): Promise<any> {
    return this.kamailioRpcService.call('pdt.list');
  }
}
