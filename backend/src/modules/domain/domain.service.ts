import { Injectable, NotFoundException, ConflictException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Domain } from './entities/domain.entity';
import { CreateDomainDto } from './dto/create-domain.dto';
import { UpdateDomainDto } from './dto/update-domain.dto';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class DomainService {
  constructor(
    @InjectRepository(Domain)
    private domainRepository: Repository<Domain>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  /**
   * 创建域
   */
  async create(createDto: CreateDomainDto): Promise<Domain> {
    // 检查是否已存在
    const existing = await this.domainRepository.findOne({
      where: { domain: createDto.domain },
    });

    if (existing) {
      throw new ConflictException('域已存在');
    }

    const domain = this.domainRepository.create(createDto);
    const saved = await this.domainRepository.save(domain);

    // 通知 Kamailio 重载域
    try {
      await this.kamailioRpcService.reloadDomain();
    } catch (error) {
      // 忽略 RPC 错误，可能 Kamailio 未运行
    }

    return saved;
  }

  /**
   * 分页查询域
   */
  async findAll(paginationDto: PaginationDto): Promise<PaginatedResult<Domain>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {};
    if (keyword) {
      where.domain = Like(`%${keyword}%`);
    }

    const [items, total] = await this.domainRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { id: 'DESC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 获取所有域（不分页）
   */
  async findAllDomains(): Promise<Domain[]> {
    return this.domainRepository.find({ order: { domain: 'ASC' } });
  }

  /**
   * 根据 ID 查询域
   */
  async findOne(id: number): Promise<Domain> {
    const domain = await this.domainRepository.findOne({ where: { id } });
    if (!domain) {
      throw new NotFoundException('域不存在');
    }
    return domain;
  }

  /**
   * 更新域
   */
  async update(id: number, updateDto: UpdateDomainDto): Promise<Domain> {
    const domain = await this.findOne(id);
    
    Object.assign(domain, updateDto);
    domain.lastModified = new Date();

    const saved = await this.domainRepository.save(domain);

    // 通知 Kamailio 重载域
    try {
      await this.kamailioRpcService.reloadDomain();
    } catch (error) {
      // 忽略 RPC 错误
    }

    return saved;
  }

  /**
   * 删除域
   */
  async remove(id: number): Promise<void> {
    const domain = await this.findOne(id);
    await this.domainRepository.remove(domain);

    // 通知 Kamailio 重载域
    try {
      await this.kamailioRpcService.reloadDomain();
    } catch (error) {
      // 忽略 RPC 错误
    }
  }

  /**
   * 重载域配置
   */
  async reload(): Promise<void> {
    await this.kamailioRpcService.reloadDomain();
  }

  /**
   * 导出内存中的域列表 (RPC domain.dump)
   */
  async dump(): Promise<any> {
    return this.kamailioRpcService.call('domain.dump');
  }
}
