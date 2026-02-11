import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like, MoreThan } from 'typeorm';
import { Location } from './entities/location.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class LocationService {
  constructor(
    @InjectRepository(Location)
    private locationRepository: Repository<Location>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  /**
   * 分页查询注册位置（从数据库）
   */
  async findAll(paginationDto: PaginationDto): Promise<PaginatedResult<Location>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {
      expires: MoreThan(new Date()),
    };

    if (keyword) {
      where.username = Like(`%${keyword}%`);
    }

    const [items, total] = await this.locationRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { lastModified: 'DESC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 获取在线用户数量
   */
  async getOnlineCount(): Promise<number> {
    return this.locationRepository.count({
      where: { expires: MoreThan(new Date()) },
    });
  }

  /**
   * 从 Kamailio 内存获取注册位置
   */
  async getFromMemory(table?: string): Promise<any> {
    return this.kamailioRpcService.getUserLocation(table);
  }

  /**
   * 删除用户注册（整个 AOR）
   */
  async deleteUserLocation(username: string, domain: string): Promise<void> {
    const aor = `sip:${username}@${domain}`;
    await this.kamailioRpcService.deleteUserLocation('location', aor);
  }

  /**
   * 精确查找指定用户的注册 (RPC ul.lookup)
   */
  async lookupUser(table: string, aor: string): Promise<any> {
    return this.kamailioRpcService.call('ul.lookup', [table, aor]);
  }

  /**
   * 删除指定联系地址 (RPC ul.rm_contact)
   */
  async deleteContact(table: string, aor: string, contact: string): Promise<void> {
    await this.kamailioRpcService.call('ul.rm_contact', [table, aor, contact]);
  }

  /**
   * 根据用户名查询注册
   */
  async findByUsername(username: string): Promise<Location[]> {
    return this.locationRepository.find({
      where: {
        username,
        expires: MoreThan(new Date()),
      },
      order: { lastModified: 'DESC' },
    });
  }

  /**
   * 获取统计信息
   */
  async getStats(): Promise<{
    total: number;
    byDomain: { domain: string; count: number }[];
    byUserAgent: { userAgent: string; count: number }[];
  }> {
    const now = new Date();
    
    const total = await this.locationRepository.count({
      where: { expires: MoreThan(now) },
    });

    const byDomain = await this.locationRepository
      .createQueryBuilder('l')
      .select('l.domain', 'domain')
      .addSelect('COUNT(*)', 'count')
      .where('l.expires > :now', { now })
      .groupBy('l.domain')
      .getRawMany();

    const byUserAgent = await this.locationRepository
      .createQueryBuilder('l')
      .select('l.user_agent', 'userAgent')
      .addSelect('COUNT(*)', 'count')
      .where('l.expires > :now', { now })
      .groupBy('l.user_agent')
      .orderBy('count', 'DESC')
      .limit(10)
      .getRawMany();

    return { total, byDomain, byUserAgent };
  }
}
