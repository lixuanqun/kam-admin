import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { DbAliases } from './entities/dbaliases.entity';
import { Grp } from './entities/grp.entity';
import { SpeedDial } from './entities/speed-dial.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';

@Injectable()
export class UserdataService {
  constructor(
    @InjectRepository(DbAliases) private aliasRepository: Repository<DbAliases>,
    @InjectRepository(Grp) private grpRepository: Repository<Grp>,
    @InjectRepository(SpeedDial) private speedDialRepository: Repository<SpeedDial>,
  ) {}

  // ========== Aliases ==========
  async findAllAliases(dto: PaginationDto): Promise<PaginatedResult<DbAliases>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.aliasUsername = Like(`%${keyword}%`);
    const [items, total] = await this.aliasRepository.findAndCount({ where, skip, take: limit, order: { id: 'ASC' } });
    return new PaginatedResult(items, total, page, limit);
  }

  async createAlias(data: Partial<DbAliases>): Promise<DbAliases> {
    return this.aliasRepository.save(this.aliasRepository.create(data));
  }

  async updateAlias(id: number, data: Partial<DbAliases>): Promise<DbAliases> {
    const item = await this.aliasRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('别名不存在');
    Object.assign(item, data);
    return this.aliasRepository.save(item);
  }

  async removeAlias(id: number): Promise<void> {
    await this.aliasRepository.delete(id);
  }

  // ========== Groups ==========
  async findAllGroups(dto: PaginationDto): Promise<PaginatedResult<Grp>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.username = Like(`%${keyword}%`);
    const [items, total] = await this.grpRepository.findAndCount({ where, skip, take: limit, order: { id: 'ASC' } });
    return new PaginatedResult(items, total, page, limit);
  }

  async createGroup(data: Partial<Grp>): Promise<Grp> {
    return this.grpRepository.save(this.grpRepository.create(data));
  }

  async updateGroup(id: number, data: Partial<Grp>): Promise<Grp> {
    const item = await this.grpRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('分组不存在');
    Object.assign(item, data);
    return this.grpRepository.save(item);
  }

  async removeGroup(id: number): Promise<void> {
    await this.grpRepository.delete(id);
  }

  // ========== Speed Dial ==========
  async findAllSpeedDials(dto: PaginationDto): Promise<PaginatedResult<SpeedDial>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.username = Like(`%${keyword}%`);
    const [items, total] = await this.speedDialRepository.findAndCount({ where, skip, take: limit, order: { id: 'ASC' } });
    return new PaginatedResult(items, total, page, limit);
  }

  async createSpeedDial(data: Partial<SpeedDial>): Promise<SpeedDial> {
    return this.speedDialRepository.save(this.speedDialRepository.create(data));
  }

  async updateSpeedDial(id: number, data: Partial<SpeedDial>): Promise<SpeedDial> {
    const item = await this.speedDialRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('快捷拨号不存在');
    Object.assign(item, data);
    return this.speedDialRepository.save(item);
  }

  async removeSpeedDial(id: number): Promise<void> {
    await this.speedDialRepository.delete(id);
  }
}
