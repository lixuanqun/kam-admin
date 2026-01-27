import { Injectable, NotFoundException, ConflictException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { createHash } from 'crypto';
import { Subscriber } from './entities/subscriber.entity';
import { CreateSubscriberDto } from './dto/create-subscriber.dto';
import { UpdateSubscriberDto } from './dto/update-subscriber.dto';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';

@Injectable()
export class SubscriberService {
  constructor(
    @InjectRepository(Subscriber)
    private subscriberRepository: Repository<Subscriber>,
  ) {}

  /**
   * 生成 HA1 哈希
   * HA1 = MD5(username:realm:password)
   */
  private generateHa1(username: string, domain: string, password: string): string {
    return createHash('md5')
      .update(`${username}:${domain}:${password}`)
      .digest('hex');
  }

  /**
   * 生成 HA1B 哈希
   * HA1B = MD5(username@realm:realm:password)
   */
  private generateHa1b(username: string, domain: string, password: string): string {
    return createHash('md5')
      .update(`${username}@${domain}:${domain}:${password}`)
      .digest('hex');
  }

  /**
   * 创建订阅者
   */
  async create(createDto: CreateSubscriberDto): Promise<Subscriber> {
    // 检查是否已存在
    const existing = await this.subscriberRepository.findOne({
      where: { username: createDto.username, domain: createDto.domain },
    });

    if (existing) {
      throw new ConflictException('用户已存在');
    }

    const subscriber = this.subscriberRepository.create({
      username: createDto.username,
      domain: createDto.domain,
      password: createDto.password,
      ha1: this.generateHa1(createDto.username, createDto.domain, createDto.password),
      ha1b: this.generateHa1b(createDto.username, createDto.domain, createDto.password),
      emailAddress: createDto.emailAddress,
      rpid: createDto.rpid,
    });

    return this.subscriberRepository.save(subscriber);
  }

  /**
   * 分页查询订阅者
   */
  async findAll(paginationDto: PaginationDto): Promise<PaginatedResult<Subscriber>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {};
    if (keyword) {
      where.username = Like(`%${keyword}%`);
    }

    const [items, total] = await this.subscriberRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { id: 'DESC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  /**
   * 根据 ID 查询订阅者
   */
  async findOne(id: number): Promise<Subscriber> {
    const subscriber = await this.subscriberRepository.findOne({ where: { id } });
    if (!subscriber) {
      throw new NotFoundException('用户不存在');
    }
    return subscriber;
  }

  /**
   * 根据用户名和域查询
   */
  async findByUsernameAndDomain(username: string, domain: string): Promise<Subscriber> {
    const subscriber = await this.subscriberRepository.findOne({
      where: { username, domain },
    });
    if (!subscriber) {
      throw new NotFoundException('用户不存在');
    }
    return subscriber;
  }

  /**
   * 更新订阅者
   */
  async update(id: number, updateDto: UpdateSubscriberDto): Promise<Subscriber> {
    const subscriber = await this.findOne(id);

    if (updateDto.password) {
      subscriber.password = updateDto.password;
      subscriber.ha1 = this.generateHa1(subscriber.username, subscriber.domain, updateDto.password);
      subscriber.ha1b = this.generateHa1b(subscriber.username, subscriber.domain, updateDto.password);
    }

    if (updateDto.emailAddress !== undefined) {
      subscriber.emailAddress = updateDto.emailAddress;
    }

    if (updateDto.rpid !== undefined) {
      subscriber.rpid = updateDto.rpid;
    }

    return this.subscriberRepository.save(subscriber);
  }

  /**
   * 删除订阅者
   */
  async remove(id: number): Promise<void> {
    const subscriber = await this.findOne(id);
    await this.subscriberRepository.remove(subscriber);
  }

  /**
   * 批量删除订阅者
   */
  async batchRemove(ids: number[]): Promise<void> {
    await this.subscriberRepository.delete(ids);
  }

  /**
   * 获取统计信息
   */
  async getStats(): Promise<{ total: number; domains: { domain: string; count: number }[] }> {
    const total = await this.subscriberRepository.count();
    
    const domains = await this.subscriberRepository
      .createQueryBuilder('s')
      .select('s.domain', 'domain')
      .addSelect('COUNT(*)', 'count')
      .groupBy('s.domain')
      .getRawMany();

    return { total, domains };
  }
}
