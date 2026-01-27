import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like, Between } from 'typeorm';
import { SipTrace } from './entities/sip-trace.entity';
import { PaginatedResult } from '../../common/dto/pagination.dto';

export interface SipTraceQueryDto {
  page?: number;
  limit?: number;
  callid?: string;
  tracedUser?: string;
  method?: string;
  fromIp?: string;
  toIp?: string;
  startTime?: string;
  endTime?: string;
}

@Injectable()
export class SiptraceService {
  constructor(
    @InjectRepository(SipTrace)
    private sipTraceRepository: Repository<SipTrace>,
  ) {}

  async findAll(dto: SipTraceQueryDto): Promise<PaginatedResult<SipTrace>> {
    const { page = 1, limit = 20, callid, tracedUser, method, fromIp, toIp, startTime, endTime } = dto;
    const skip = (page - 1) * limit;
    
    const qb = this.sipTraceRepository.createQueryBuilder('t');
    
    if (callid) qb.andWhere('t.callid LIKE :callid', { callid: `%${callid}%` });
    if (tracedUser) qb.andWhere('t.traced_user LIKE :tracedUser', { tracedUser: `%${tracedUser}%` });
    if (method) qb.andWhere('t.method = :method', { method });
    if (fromIp) qb.andWhere('t.fromip LIKE :fromIp', { fromIp: `%${fromIp}%` });
    if (toIp) qb.andWhere('t.toip LIKE :toIp', { toIp: `%${toIp}%` });
    if (startTime) qb.andWhere('t.time_stamp >= :startTime', { startTime });
    if (endTime) qb.andWhere('t.time_stamp <= :endTime', { endTime });
    
    qb.orderBy('t.time_stamp', 'DESC').addOrderBy('t.time_us', 'DESC');
    qb.skip(skip).take(limit);
    
    const [items, total] = await qb.getManyAndCount();
    return new PaginatedResult(items, total, page, limit);
  }

  async getByCallId(callid: string): Promise<SipTrace[]> {
    return this.sipTraceRepository.find({
      where: { callid },
      order: { timeStamp: 'ASC', timeUs: 'ASC' },
    });
  }

  async getStats(): Promise<any> {
    const total = await this.sipTraceRepository.count();
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const todayCount = await this.sipTraceRepository
      .createQueryBuilder('t')
      .where('t.time_stamp >= :today', { today })
      .getCount();
    
    const methodStats = await this.sipTraceRepository
      .createQueryBuilder('t')
      .select('t.method', 'method')
      .addSelect('COUNT(*)', 'count')
      .groupBy('t.method')
      .getRawMany();
    
    return { total, todayCount, methodStats };
  }

  async cleanup(beforeDate: Date): Promise<number> {
    const result = await this.sipTraceRepository
      .createQueryBuilder()
      .delete()
      .where('time_stamp < :beforeDate', { beforeDate })
      .execute();
    return result.affected || 0;
  }
}
