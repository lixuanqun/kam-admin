import { Injectable } from '@nestjs/common';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class SystemService {
  constructor(private kamailioRpcService: KamailioRpcService) {}

  // ========== 核心信息 ==========
  async getCoreInfo(): Promise<any> {
    return this.kamailioRpcService.call('core.info');
  }

  async getVersion(): Promise<any> {
    return this.kamailioRpcService.call('core.version');
  }

  async getUptime(): Promise<any> {
    return this.kamailioRpcService.call('core.uptime');
  }

  async getSharedMemory(): Promise<any> {
    return this.kamailioRpcService.call('core.shmmem');
  }

  async getPrivateMemory(): Promise<any> {
    return this.kamailioRpcService.call('core.ppdefines');
  }

  async getProcessList(): Promise<any> {
    return this.kamailioRpcService.call('core.psx');
  }

  // ========== 配置管理 ==========
  async getConfig(group: string, name: string): Promise<any> {
    return this.kamailioRpcService.call('cfg.get', [group, name]);
  }

  async setConfigNow(group: string, name: string, value: any): Promise<void> {
    if (typeof value === 'number') {
      await this.kamailioRpcService.call('cfg.set_now_int', [group, name, value]);
    } else {
      await this.kamailioRpcService.call('cfg.set_now_string', [group, name, value]);
    }
  }

  async listConfig(): Promise<any> {
    return this.kamailioRpcService.call('cfg.list');
  }

  async diffConfig(): Promise<any> {
    return this.kamailioRpcService.call('cfg.diff');
  }

  // ========== TLS ==========
  async getTlsList(): Promise<any> {
    return this.kamailioRpcService.call('tls.list');
  }

  async getTlsInfo(): Promise<any> {
    return this.kamailioRpcService.call('tls.info');
  }

  async reloadTls(): Promise<void> {
    await this.kamailioRpcService.call('tls.reload');
  }

  // ========== Pike (防攻击) ==========
  async getPikeList(): Promise<any> {
    return this.kamailioRpcService.call('pike.list');
  }

  async getPikeTop(limit: number = 10): Promise<any> {
    return this.kamailioRpcService.call('pike.top', [limit]);
  }

  // ========== 统计信息 ==========
  async getStatistics(group?: string): Promise<any> {
    const params = group ? [group] : ['all'];
    return this.kamailioRpcService.call('stats.get_statistics', params);
  }

  async resetStatistics(name: string): Promise<void> {
    await this.kamailioRpcService.call('stats.reset_statistics', [name]);
  }

  async clearStatistics(name: string): Promise<void> {
    await this.kamailioRpcService.call('stats.clear_statistics', [name]);
  }

  // ========== 模块管理 ==========
  async getModulesList(): Promise<any> {
    return this.kamailioRpcService.call('core.modules');
  }

  // ========== 综合状态 ==========
  async getSystemStatus(): Promise<any> {
    const [info, uptime, shmmem, processes] = await Promise.all([
      this.getCoreInfo().catch(() => null),
      this.getUptime().catch(() => null),
      this.getSharedMemory().catch(() => null),
      this.getProcessList().catch(() => []),
    ]);

    return {
      info,
      uptime,
      memory: shmmem,
      processCount: Array.isArray(processes) ? processes.length : 0,
    };
  }
}
