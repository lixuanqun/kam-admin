import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { LcrGw } from './entities/lcr-gw.entity';
import { LcrRule } from './entities/lcr-rule.entity';
import { LcrRuleTarget } from './entities/lcr-rule-target.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class LcrService {
  constructor(
    @InjectRepository(LcrGw)
    private gwRepository: Repository<LcrGw>,
    @InjectRepository(LcrRule)
    private ruleRepository: Repository<LcrRule>,
    @InjectRepository(LcrRuleTarget)
    private targetRepository: Repository<LcrRuleTarget>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  // ========== Gateway ==========
  async findAllGateways(dto: PaginationDto): Promise<PaginatedResult<LcrGw>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.gwName = Like(`%${keyword}%`);
    const [items, total] = await this.gwRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createGateway(data: Partial<LcrGw>): Promise<LcrGw> {
    const gw = this.gwRepository.create(data);
    return this.gwRepository.save(gw);
  }

  async updateGateway(id: number, data: Partial<LcrGw>): Promise<LcrGw> {
    const gw = await this.gwRepository.findOne({ where: { id } });
    if (!gw) throw new NotFoundException('网关不存在');
    Object.assign(gw, data);
    return this.gwRepository.save(gw);
  }

  async removeGateway(id: number): Promise<void> {
    await this.gwRepository.delete(id);
  }

  // ========== Rule ==========
  async findAllRules(dto: PaginationDto): Promise<PaginatedResult<LcrRule>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.prefix = Like(`%${keyword}%`);
    const [items, total] = await this.ruleRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createRule(data: Partial<LcrRule>): Promise<LcrRule> {
    const rule = this.ruleRepository.create(data);
    return this.ruleRepository.save(rule);
  }

  async updateRule(id: number, data: Partial<LcrRule>): Promise<LcrRule> {
    const rule = await this.ruleRepository.findOne({ where: { id } });
    if (!rule) throw new NotFoundException('规则不存在');
    Object.assign(rule, data);
    return this.ruleRepository.save(rule);
  }

  async removeRule(id: number): Promise<void> {
    await this.ruleRepository.delete(id);
  }

  // ========== Target ==========
  async findAllTargets(dto: PaginationDto): Promise<PaginatedResult<LcrRuleTarget>> {
    const { page, limit } = dto;
    const skip = (page - 1) * limit;
    const [items, total] = await this.targetRepository.findAndCount({
      skip, take: limit, order: { priority: 'ASC', id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createTarget(data: Partial<LcrRuleTarget>): Promise<LcrRuleTarget> {
    const target = this.targetRepository.create(data);
    return this.targetRepository.save(target);
  }

  async updateTarget(id: number, data: Partial<LcrRuleTarget>): Promise<LcrRuleTarget> {
    const target = await this.targetRepository.findOne({ where: { id } });
    if (!target) throw new NotFoundException('目标不存在');
    Object.assign(target, data);
    return this.targetRepository.save(target);
  }

  async removeTarget(id: number): Promise<void> {
    await this.targetRepository.delete(id);
  }

  // ========== RPC ==========
  async reload(): Promise<void> {
    await this.kamailioRpcService.call('lcr.reload');
  }

  async dumpGws(): Promise<any> {
    return this.kamailioRpcService.call('lcr.dump_gws');
  }

  async dumpRules(): Promise<any> {
    return this.kamailioRpcService.call('lcr.dump_rules');
  }
}
