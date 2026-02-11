import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Dialog } from './entities/dialog.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class DialogService {
  constructor(
    @InjectRepository(Dialog)
    private dialogRepository: Repository<Dialog>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  async findAll(dto: PaginationDto): Promise<PaginatedResult<Dialog>> {
    const { page, limit } = dto;
    const skip = (page - 1) * limit;
    const [items, total] = await this.dialogRepository.findAndCount({
      skip, take: limit, order: { startTime: 'DESC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async getActiveDialogs(): Promise<any> {
    try {
      return await this.kamailioRpcService.call('dlg.list');
    } catch {
      return [];
    }
  }

  async getDialogStats(): Promise<any> {
    try {
      return await this.kamailioRpcService.call('dlg.stats_active');
    } catch {
      return { active: 0 };
    }
  }

  async endDialog(hashEntry: number, hashId: number): Promise<void> {
    await this.kamailioRpcService.call('dlg.end_dlg', [hashEntry, hashId]);
  }

  async getDialogDetail(hashEntry: number, hashId: number): Promise<any> {
    return this.kamailioRpcService.call('dlg.dlg_list', [hashEntry, hashId]);
  }

  async bridgeDialog(from: string, to: string): Promise<void> {
    await this.kamailioRpcService.call('dlg.bridge_dlg', [from, to]);
  }

  async getStats(): Promise<any> {
    const dbCount = await this.dialogRepository.count();
    const memStats = await this.getDialogStats();
    return { dbCount, ...memStats };
  }
}
