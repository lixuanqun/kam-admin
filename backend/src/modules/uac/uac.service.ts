import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { UacReg } from './entities/uacreg.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class UacService {
  constructor(
    @InjectRepository(UacReg)
    private uacRegRepository: Repository<UacReg>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<UacReg>> {
    const { page, limit, keyword } = dto;
    const skip = (page - 1) * limit;
    const where: any = {};
    if (keyword) where.lUsername = Like(`%${keyword}%`);
    const [items, total] = await this.uacRegRepository.findAndCount({
      where, skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async create(data: Partial<UacReg>): Promise<UacReg> {
    return this.uacRegRepository.save(this.uacRegRepository.create(data));
  }

  async update(id: number, data: Partial<UacReg>): Promise<UacReg> {
    const item = await this.uacRegRepository.findOne({ where: { id } });
    if (!item) throw new NotFoundException('注册记录不存在');
    Object.assign(item, data);
    return this.uacRegRepository.save(item);
  }

  async remove(id: number): Promise<void> {
    await this.uacRegRepository.delete(id);
  }

  // RPC
  async reload(): Promise<void> {
    await this.kamailioRpcService.call('uac.reg_reload');
  }

  async getInfo(lUuid: string): Promise<any> {
    return this.kamailioRpcService.call('uac.reg_info', [lUuid]);
  }

  async refresh(lUuid: string): Promise<void> {
    await this.kamailioRpcService.call('uac.reg_refresh', [lUuid]);
  }

  async enable(lUuid: string, flag: number): Promise<void> {
    await this.kamailioRpcService.call('uac.reg_enable', [lUuid, flag]);
  }

  async dumpList(): Promise<any> {
    return this.kamailioRpcService.call('uac.reg_dump');
  }
}
