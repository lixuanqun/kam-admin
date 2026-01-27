import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Silo } from './entities/silo.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class MsiloService {
  constructor(
    @InjectRepository(Silo)
    private siloRepository: Repository<Silo>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<Silo>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.username = Like(`%${keyword}%`);
    const [items, total] = await this.siloRepository.findAndCount({
      where, skip, take: limit, order: { incTime: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async findByUser(username: string, domain: string): Promise<Silo[]> {
    return this.siloRepository.find({
      where: { username, domain },
      order: { incTime: 'DESC' },
    });
  }

  async remove(id: number): Promise<void> {
    const item = await this.siloRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('消息不存在');
    await this.siloRepository.remove(item);
  }

  async cleanExpired(): Promise<number> {
    const now = Math.floor(Date.now() / 1000);
    const result = await this.siloRepository
      .createQueryBuilder()
      .delete()
      .where('exp_time < :now', { now })
      .execute();
    return result.affected || 0;
  }

  async getStats(): Promise<any> {
    const total = await this.siloRepository.count();
    const now = Math.floor(Date.now() / 1000);
    const expired = await this.siloRepository
      .createQueryBuilder('s')
      .where('s.exp_time < :now', { now })
      .getCount();
    return { total, expired, active: total - expired };
  }

  async dump(): Promise<void> {
    await this.kamailioRpcService.call('msilo.dump');
  }
}
