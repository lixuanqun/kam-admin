import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Htable } from './entities/htable.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class HtableService {
  constructor(
    @InjectRepository(Htable)
    private htableRepository: Repository<Htable>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<Htable>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.keyName = Like(`%${keyword}%`);
    const [items, total] = await this.htableRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<Htable>): Promise<Htable> {
    const item = this.htableRepository.create(data);
    return this.htableRepository.save(item);
  }

  async update(id: number, data: Partial<Htable>): Promise<Htable> {
    const item = await this.htableRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('记录不存在');
    Object.assign(item, data);
    return this.htableRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.htableRepository.delete(id);
  }

  // RPC 操作
  async getValue(table: string, key: string): Promise<any> {
    return this.kamailioRpcService.call('htable.get', [table, key]);
  }

  async setString(table: string, key: string, value: string): Promise<void> {
    await this.kamailioRpcService.call('htable.sets', [table, key, value]);
  }

  async setInt(table: string, key: string, value: number): Promise<void> {
    await this.kamailioRpcService.call('htable.seti', [table, key, value]);
  }

  async deleteKey(table: string, key: string): Promise<void> {
    await this.kamailioRpcService.call('htable.delete', [table, key]);
  }

  async dump(table: string): Promise<any> {
    return this.kamailioRpcService.call('htable.dump', [table]);
  }

  async reload(table: string): Promise<void> {
    await this.kamailioRpcService.call('htable.reload', [table]);
  }

  async listTables(): Promise<any> {
    return this.kamailioRpcService.call('htable.listTables');
  }
}
