import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Dialplan } from './entities/dialplan.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class DialplanService {
  constructor(
    @InjectRepository(Dialplan)
    private dialplanRepository: Repository<Dialplan>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto & { dpid?: number }): Promise<PaginatedResult<Dialplan>> {
    const { page, limit, dpid } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (dpid !== undefined) where.dpid = dpid;
    const [items, total] = await this.dialplanRepository.findAndCount({
      where, skip, take: limit, order: { dpid: 'ASC', priority: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<Dialplan>): Promise<Dialplan> {
    return this.dialplanRepository.save(this.dialplanRepository.create(data));
  }

  async update(id: number, data: Partial<Dialplan>): Promise<Dialplan> {
    const item = await this.dialplanRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('规则不存在');
    Object.assign(item, data);
    return this.dialplanRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.dialplanRepository.delete(id);
  }

  async reload(): Promise<void> {
    await this.kamailioRpcService.call('dialplan.reload');
  }

  async translate(dpid: number, input: string): Promise<any> {
    return this.kamailioRpcService.call('dialplan.translate', [dpid, input]);
  }

  async dump(dpid?: number): Promise<any> {
    if (dpid !== undefined) {
      return this.kamailioRpcService.call('dialplan.dump', [dpid]);
    }
    return this.kamailioRpcService.call('dialplan.dump');
  }
}
