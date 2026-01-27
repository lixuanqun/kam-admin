import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like, LessThan } from 'typeorm';
import { ToposD } from './entities/topos-d.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';

@Injectable()
export class ToposService {
  constructor(
    @InjectRepository(ToposD)
    private toposDRepository: Repository<ToposD>,
  ) {}

  async findAllDialogs(dto: PaginationDto): Promise<PaginatedResult<ToposD>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.a_callid = Like(`%${keyword}%`);
    const [items, total] = await this.toposDRepository.findAndCount({
      where, skip, take: limit, order: { rectime: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async cleanup(beforeDate: Date): Promise<number> {
    const result = await this.toposDRepository.delete({
      rectime: LessThan(beforeDate),
    });
    return result.affected || 0;
  }

  async getStats(): Promise<any> {
    const total = await this.toposDRepository.count();
    return { total };
  }
}
