import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Between, Like, MoreThanOrEqual, LessThanOrEqual } from 'typeorm';
import { Acc } from './entities/acc.entity';
import { MissedCall } from './entities/missed-call.entity';
import { QueryAccDto } from './dto/query-acc.dto';
import { PaginatedResult } from '../../common/dto/pagination.dto';

@Injectable()
export class AccService {
  constructor(
    @InjectRepository(Acc)
    private accRepository: Repository<Acc>,
    @InjectRepository(MissedCall)
    private missedCallRepository: Repository<MissedCall>,
  ) {}

  /**
   * 查询 CDR 记录
   */
  async findAllAcc(queryDto: QueryAccDto): Promise<PaginatedResult<Acc>> {
    const { page, limit, srcUser, dstUser, callid, startTime, endTime, sipCode } = queryDto;
    const skip = (page - 1) * limit;

    const queryBuilder = this.accRepository.createQueryBuilder('acc');

    if (srcUser) {
      queryBuilder.andWhere('acc.src_user LIKE :srcUser', { srcUser: `%${srcUser}%` });
    }

    if (dstUser) {
      queryBuilder.andWhere('acc.dst_user LIKE :dstUser', { dstUser: `%${dstUser}%` });
    }

    if (callid) {
      queryBuilder.andWhere('acc.callid LIKE :callid', { callid: `%${callid}%` });
    }

    if (startTime) {
      queryBuilder.andWhere('acc.time >= :startTime', { startTime });
    }

    if (endTime) {
      queryBuilder.andWhere('acc.time <= :endTime', { endTime });
    }

    if (sipCode) {
      queryBuilder.andWhere('acc.sip_code = :sipCode', { sipCode });
    }

    queryBuilder.orderBy('acc.time', 'DESC');
    queryBuilder.skip(skip).take(limit);

    const [items, total] = await queryBuilder.getManyAndCount();

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 查询未接来电
   */
  async findAllMissedCalls(queryDto: QueryAccDto): Promise<PaginatedResult<MissedCall>> {
    const { page, limit, srcUser, dstUser, startTime, endTime } = queryDto;
    const skip = (page - 1) * limit;

    const queryBuilder = this.missedCallRepository.createQueryBuilder('mc');

    if (srcUser) {
      queryBuilder.andWhere('mc.src_user LIKE :srcUser', { srcUser: `%${srcUser}%` });
    }

    if (dstUser) {
      queryBuilder.andWhere('mc.dst_user LIKE :dstUser', { dstUser: `%${dstUser}%` });
    }

    if (startTime) {
      queryBuilder.andWhere('mc.time >= :startTime', { startTime });
    }

    if (endTime) {
      queryBuilder.andWhere('mc.time <= :endTime', { endTime });
    }

    queryBuilder.orderBy('mc.time', 'DESC');
    queryBuilder.skip(skip).take(limit);

    const [items, total] = await queryBuilder.getManyAndCount();

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 获取统计信息
   */
  async getStats(startTime?: string, endTime?: string): Promise<{
    totalCalls: number;
    missedCalls: number;
    successCalls: number;
    failedCalls: number;
    callsByHour: { hour: string; count: number }[];
    callsBySipCode: { sipCode: string; count: number }[];
  }> {
    const accQuery = this.accRepository.createQueryBuilder('acc');
    const missedQuery = this.missedCallRepository.createQueryBuilder('mc');

    if (startTime) {
      accQuery.andWhere('acc.time >= :startTime', { startTime });
      missedQuery.andWhere('mc.time >= :startTime', { startTime });
    }

    if (endTime) {
      accQuery.andWhere('acc.time <= :endTime', { endTime });
      missedQuery.andWhere('mc.time <= :endTime', { endTime });
    }

    const totalCalls = await accQuery.getCount();
    const missedCalls = await missedQuery.getCount();

    // 成功呼叫 (2xx 响应码)
    const successCalls = await this.accRepository
      .createQueryBuilder('acc')
      .where('acc.sip_code LIKE :code', { code: '2%' })
      .andWhere(startTime ? 'acc.time >= :startTime' : '1=1', { startTime })
      .andWhere(endTime ? 'acc.time <= :endTime' : '1=1', { endTime })
      .getCount();

    const failedCalls = totalCalls - successCalls;

    // 按小时统计
    const callsByHour = await this.accRepository
      .createQueryBuilder('acc')
      .select("DATE_FORMAT(acc.time, '%Y-%m-%d %H:00')", 'hour')
      .addSelect('COUNT(*)', 'count')
      .where(startTime ? 'acc.time >= :startTime' : '1=1', { startTime })
      .andWhere(endTime ? 'acc.time <= :endTime' : '1=1', { endTime })
      .groupBy('hour')
      .orderBy('hour', 'DESC')
      .limit(24)
      .getRawMany();

    // 按 SIP 响应码统计
    const callsBySipCode = await this.accRepository
      .createQueryBuilder('acc')
      .select('acc.sip_code', 'sipCode')
      .addSelect('COUNT(*)', 'count')
      .where(startTime ? 'acc.time >= :startTime' : '1=1', { startTime })
      .andWhere(endTime ? 'acc.time <= :endTime' : '1=1', { endTime })
      .groupBy('acc.sip_code')
      .orderBy('count', 'DESC')
      .getRawMany();

    return {
      totalCalls,
      missedCalls,
      successCalls,
      failedCalls,
      callsByHour,
      callsBySipCode,
    };
  }

  /**
   * 获取今日统计
   */
  async getTodayStats(): Promise<{
    totalCalls: number;
    missedCalls: number;
    successRate: number;
  }> {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const todayStr = today.toISOString();

    const totalCalls = await this.accRepository
      .createQueryBuilder('acc')
      .where('acc.time >= :today', { today: todayStr })
      .getCount();

    const missedCalls = await this.missedCallRepository
      .createQueryBuilder('mc')
      .where('mc.time >= :today', { today: todayStr })
      .getCount();

    const successCalls = await this.accRepository
      .createQueryBuilder('acc')
      .where('acc.sip_code LIKE :code', { code: '2%' })
      .andWhere('acc.time >= :today', { today: todayStr })
      .getCount();

    const successRate = totalCalls > 0 ? (successCalls / totalCalls) * 100 : 0;

    return {
      totalCalls,
      missedCalls,
      successRate: Math.round(successRate * 100) / 100,
    };
  }
}
