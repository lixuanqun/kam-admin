import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { DrGateway } from './entities/dr-gateway.entity';
import { DrRule } from './entities/dr-rule.entity';
import { DrGroup } from './entities/dr-group.entity';
import { DrCarrier } from './entities/dr-carrier.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class DroutingService {
  constructor(
    @InjectRepository(DrGateway)
    private gatewayRepository: Repository<DrGateway>,
    @InjectRepository(DrRule)
    private ruleRepository: Repository<DrRule>,
    @InjectRepository(DrGroup)
    private groupRepository: Repository<DrGroup>,
    @InjectRepository(DrCarrier)
    private carrierRepository: Repository<DrCarrier>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  // ========== Gateway ==========
  async findAllGateways(paginationDto: PaginationDto): Promise<PaginatedResult<DrGateway>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) {
      where.gwid = Like(`%${keyword}%`);
    }
    const [items, total] = await this.gatewayRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createGateway(data: Partial<DrGateway>): Promise<DrGateway> {
    const gateway = this.gatewayRepository.create(data);
    return this.gatewayRepository.save(gateway);
  }

  async updateGateway(id: number, data: Partial<DrGateway>): Promise<DrGateway> {
    const gateway = await this.gatewayRepository.findOne({ where: { id } });
    if (!gateway) throw new NotFoundException('网关不存在');
    Object.assign(gateway, data);
    return this.gatewayRepository.save(gateway);
  }

  async removeGateway(id: number): Promise<void> {
    const gateway = await this.gatewayRepository.findOne({ where: { id } });
    if (!gateway) throw new NotFoundException('网关不存在');
    await this.gatewayRepository.remove(gateway);
  }

  // ========== Rule ==========
  async findAllRules(paginationDto: PaginationDto): Promise<PaginatedResult<DrRule>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) {
      where.prefix = Like(`%${keyword}%`);
    }
    const [items, total] = await this.ruleRepository.findAndCount({
      where, skip, take: limit, order: { priority: 'DESC', ruleid: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createRule(data: Partial<DrRule>): Promise<DrRule> {
    const rule = this.ruleRepository.create(data);
    return this.ruleRepository.save(rule);
  }

  async updateRule(id: number, data: Partial<DrRule>): Promise<DrRule> {
    const rule = await this.ruleRepository.findOne({ where: { ruleid: id } });
    if (!rule) throw new NotFoundException('规则不存在');
    Object.assign(rule, data);
    return this.ruleRepository.save(rule);
  }

  async removeRule(id: number): Promise<void> {
    const rule = await this.ruleRepository.findOne({ where: { ruleid: id } });
    if (!rule) throw new NotFoundException('规则不存在');
    await this.ruleRepository.remove(rule);
  }

  // ========== Group ==========
  async findAllGroups(paginationDto: PaginationDto): Promise<PaginatedResult<DrGroup>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) {
      where.username = Like(`%${keyword}%`);
    }
    const [items, total] = await this.groupRepository.findAndCount({
      where, skip, take: limit, order: { groupid: 'ASC', id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createGroup(data: Partial<DrGroup>): Promise<DrGroup> {
    const group = this.groupRepository.create(data);
    return this.groupRepository.save(group);
  }

  async updateGroup(id: number, data: Partial<DrGroup>): Promise<DrGroup> {
    const group = await this.groupRepository.findOne({ where: { id } });
    if (!group) throw new NotFoundException('分组不存在');
    Object.assign(group, data);
    return this.groupRepository.save(group);
  }

  async removeGroup(id: number): Promise<void> {
    const group = await this.groupRepository.findOne({ where: { id } });
    if (!group) throw new NotFoundException('分组不存在');
    await this.groupRepository.remove(group);
  }

  // ========== Carrier ==========
  async findAllCarriers(paginationDto: PaginationDto): Promise<PaginatedResult<DrCarrier>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) {
      where.carrierid = Like(`%${keyword}%`);
    }
    const [items, total] = await this.carrierRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createCarrier(data: Partial<DrCarrier>): Promise<DrCarrier> {
    const carrier = this.carrierRepository.create(data);
    return this.carrierRepository.save(carrier);
  }

  async updateCarrier(id: number, data: Partial<DrCarrier>): Promise<DrCarrier> {
    const carrier = await this.carrierRepository.findOne({ where: { id } });
    if (!carrier) throw new NotFoundException('运营商不存在');
    Object.assign(carrier, data);
    return this.carrierRepository.save(carrier);
  }

  async removeCarrier(id: number): Promise<void> {
    const carrier = await this.carrierRepository.findOne({ where: { id } });
    if (!carrier) throw new NotFoundException('运营商不存在');
    await this.carrierRepository.remove(carrier);
  }

  // ========== RPC ==========
  async reload(): Promise<void> {
    await this.kamailioRpcService.call('drouting.reload');
  }

  async getStats(): Promise<any> {
    const gateways = await this.gatewayRepository.count();
    const rules = await this.ruleRepository.count();
    const groups = await this.groupRepository.count();
    const carriers = await this.carrierRepository.count();
    return { gateways, rules, groups, carriers };
  }
}
