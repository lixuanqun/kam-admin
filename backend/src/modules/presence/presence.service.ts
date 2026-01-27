import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Presentity } from './entities/presentity.entity';
import { ActiveWatchers } from './entities/active-watchers.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class PresenceService {
  constructor(
    @InjectRepository(Presentity)
    private presentityRepository: Repository<Presentity>,
    @InjectRepository(ActiveWatchers)
    private watchersRepository: Repository<ActiveWatchers>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAllPresentities(dto: PaginationDto): Promise<PaginatedResult<Presentity>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.username = Like(`%${keyword}%`);
    const [items, total] = await this.presentityRepository.findAndCount({
      where, skip, take: limit, order: { id: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async findAllWatchers(dto: PaginationDto): Promise<PaginatedResult<ActiveWatchers>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.watcherUsername = Like(`%${keyword}%`);
    const [items, total] = await this.watchersRepository.findAndCount({
      where, skip, take: limit, order: { id: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async cleanExpired(): Promise<void> {
    await this.kamailioRpcService.call('presence.cleanup');
  }

  async refreshWatchers(presentityUri: string, event: string): Promise<void> {
    await this.kamailioRpcService.call('presence.refreshWatchers', [presentityUri, event, 0]);
  }

  async getStats(): Promise<any> {
    const presentities = await this.presentityRepository.count();
    const watchers = await this.watchersRepository.count();
    return { presentities, watchers };
  }
}
