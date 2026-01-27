import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Dispatcher } from './entities/dispatcher.entity';
import { CreateDispatcherDto } from './dto/create-dispatcher.dto';
import { UpdateDispatcherDto } from './dto/update-dispatcher.dto';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class DispatcherService {
  constructor(
    @InjectRepository(Dispatcher)
    private dispatcherRepository: Repository<Dispatcher>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  /**
   * 创建调度目标
   */
  async create(createDto: CreateDispatcherDto): Promise<Dispatcher> {
    const dispatcher = this.dispatcherRepository.create(createDto);
    const saved = await this.dispatcherRepository.save(dispatcher);

    // 通知 Kamailio 重载调度器
    try {
      await this.kamailioRpcService.reloadDispatcher();
    } catch (error) {
      // 忽略 RPC 错误
    }

    return saved;
  }

  /**
   * 分页查询调度目标
   */
  async findAll(paginationDto: PaginationDto): Promise<PaginatedResult<Dispatcher>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {};
    if (keyword) {
      where.destination = Like(`%${keyword}%`);
    }

    const [items, total] = await this.dispatcherRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { setid: 'ASC', priority: 'DESC', id: 'ASC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 根据调度组获取调度目标
   */
  async findBySetId(setid: number): Promise<Dispatcher[]> {
    return this.dispatcherRepository.find({
      where: { setid },
      order: { priority: 'DESC', id: 'ASC' },
    });
  }

  /**
   * 获取所有调度组
   */
  async getSetIds(): Promise<number[]> {
    const result = await this.dispatcherRepository
      .createQueryBuilder('d')
      .select('DISTINCT d.setid', 'setid')
      .orderBy('d.setid', 'ASC')
      .getRawMany();

    return result.map((r) => r.setid);
  }

  /**
   * 根据 ID 查询调度目标
   */
  async findOne(id: number): Promise<Dispatcher> {
    const dispatcher = await this.dispatcherRepository.findOne({ where: { id } });
    if (!dispatcher) {
      throw new NotFoundException('调度目标不存在');
    }
    return dispatcher;
  }

  /**
   * 更新调度目标
   */
  async update(id: number, updateDto: UpdateDispatcherDto): Promise<Dispatcher> {
    const dispatcher = await this.findOne(id);
    
    Object.assign(dispatcher, updateDto);

    const saved = await this.dispatcherRepository.save(dispatcher);

    // 通知 Kamailio 重载调度器
    try {
      await this.kamailioRpcService.reloadDispatcher();
    } catch (error) {
      // 忽略 RPC 错误
    }

    return saved;
  }

  /**
   * 删除调度目标
   */
  async remove(id: number): Promise<void> {
    const dispatcher = await this.findOne(id);
    await this.dispatcherRepository.remove(dispatcher);

    // 通知 Kamailio 重载调度器
    try {
      await this.kamailioRpcService.reloadDispatcher();
    } catch (error) {
      // 忽略 RPC 错误
    }
  }

  /**
   * 重载调度器配置
   */
  async reload(): Promise<void> {
    await this.kamailioRpcService.reloadDispatcher();
  }

  /**
   * 获取调度器实时状态
   */
  async getStatus(): Promise<any> {
    return this.kamailioRpcService.getDispatcherList();
  }

  /**
   * 获取统计信息
   */
  async getStats(): Promise<{ total: number; groups: { setid: number; count: number }[] }> {
    const total = await this.dispatcherRepository.count();
    
    const groups = await this.dispatcherRepository
      .createQueryBuilder('d')
      .select('d.setid', 'setid')
      .addSelect('COUNT(*)', 'count')
      .groupBy('d.setid')
      .orderBy('d.setid', 'ASC')
      .getRawMany();

    return { total, groups };
  }
}
