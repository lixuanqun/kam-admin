import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { UsrPreferences } from './entities/usr-preferences.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';

@Injectable()
export class UsrpreferencesService {
  constructor(
    @InjectRepository(UsrPreferences)
    private usrPrefRepository: Repository<UsrPreferences>,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<UsrPreferences>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.username = Like(`%${keyword}%`);
    const [items, total] = await this.usrPrefRepository.findAndCount({
      where, skip, take: limit, order: { id: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async findByUser(username: string, domain: string): Promise<UsrPreferences[]> {
    return this.usrPrefRepository.find({
      where: { username, domain },
      order: { attribute: 'ASC' },
    });
  }

  async create(data: Partial<UsrPreferences>): Promise<UsrPreferences> {
    return this.usrPrefRepository.save(this.usrPrefRepository.create(data));
  }

  async update(id: number, data: Partial<UsrPreferences>): Promise<UsrPreferences> {
    const item = await this.usrPrefRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('记录不存在');
    Object.assign(item, data);
    return this.usrPrefRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.usrPrefRepository.delete(id);
  }

  async removeByUser(username: string, domain: string): Promise<void> {
    await this.usrPrefRepository.delete({ username, domain });
  }
}
