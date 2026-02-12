import { Injectable } from '@nestjs/common';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';
import { SubscriberService } from '../subscriber/subscriber.service';
import { LocationService } from '../location/location.service';
import { AccService } from '../acc/acc.service';

@Injectable()
export class MonitoringService {
  constructor(
    private kamailioRpcService: KamailioRpcService,
    private subscriberService: SubscriberService,
    private locationService: LocationService,
    private accService: AccService,
  ) {}

  /**
   * 检查 Kamailio 服务状态
   */
  async checkHealth(): Promise<{
    status: 'online' | 'offline';
    uptime?: any;
    version?: string;
  }> {
    try {
      const isOnline = await this.kamailioRpcService.ping();
      if (!isOnline) {
        return { status: 'offline' };
      }

      const [uptime, coreInfo] = await Promise.all([
        this.kamailioRpcService.getUptime(),
        this.kamailioRpcService.getCoreInfo(),
      ]);

      return {
        status: 'online',
        uptime,
        version: coreInfo?.version,
      };
    } catch (error) {
      return { status: 'offline' };
    }
  }

  /**
   * 获取仪表盘数据
   */
  async getDashboardData(): Promise<{
    subscriberCount: number;
    onlineCount: number;
    todayCalls: number;
    todayMissedCalls: number;
    successRate: number;
    kamailioStatus: 'online' | 'offline';
  }> {
    const [subscriberStats, onlineCount, todayStats, health] = await Promise.all([
      this.subscriberService.getStats(),
      this.locationService.getOnlineCount(),
      this.accService.getTodayStats(),
      this.checkHealth(),
    ]);

    return {
      subscriberCount: subscriberStats.total,
      onlineCount,
      todayCalls: todayStats.totalCalls,
      todayMissedCalls: todayStats.missedCalls,
      successRate: todayStats.successRate,
      kamailioStatus: health.status,
    };
  }

  /**
   * 获取 Kamailio 统计信息
   */
  async getStatistics(group?: string): Promise<any> {
    return this.kamailioRpcService.getStatistics(group);
  }

  /**
   * 获取核心信息
   */
  async getCoreInfo(): Promise<any> {
    return this.kamailioRpcService.getCoreInfo();
  }

  /**
   * 获取活动对话
   */
  async getActiveDialogs(): Promise<{
    count: number;
    dialogs: any[];
  }> {
    try {
      const [count, dialogs] = await Promise.all([
        this.kamailioRpcService.getDialogCount(),
        this.kamailioRpcService.getDialogList(),
      ]);

      return {
        count: count?.active || 0,
        dialogs: dialogs || [],
      };
    } catch (error) {
      return { count: 0, dialogs: [] };
    }
  }

  /**
   * 检查 Kamailio 关键模块是否可用
   */
  async checkModules(): Promise<{
    connected: boolean;
    version?: string;
    modules: { name: string; available: boolean; error?: string }[];
  }> {
    const modules: { name: string; rpc: string; params?: any[] }[] = [
      { name: 'core', rpc: 'core.version' },
      { name: 'usrloc', rpc: 'ul.dump', params: [] },
      { name: 'dispatcher', rpc: 'dispatcher.list' },
      { name: 'drouting', rpc: 'drouting.gw_status' },
      { name: 'domain', rpc: 'domain.dump' },
      { name: 'permissions', rpc: 'permissions.addressDump' },
      { name: 'dialog', rpc: 'dlg.stats_active' },
      { name: 'cfg_rpc', rpc: 'cfg.list' },
      { name: 'statistics', rpc: 'stats.get_statistics', params: ['all'] },
      { name: 'tls', rpc: 'tls.list' },
      { name: 'pike', rpc: 'pike.list' },
    ];

    const settled = await Promise.allSettled(
      modules.map((mod) =>
        this.kamailioRpcService.call(mod.rpc, mod.params || []),
      ),
    );

    const results = modules.map((mod, i) => {
      const s = settled[i];
      if (s.status === 'fulfilled') {
        return { name: mod.name, available: true };
      }
      return {
        name: mod.name,
        available: false,
        error: s.reason instanceof Error ? s.reason.message : 'Unknown error',
      };
    });

    const coreIdx = modules.findIndex((m) => m.name === 'core');
    const connected = coreIdx >= 0 && settled[coreIdx].status === 'fulfilled';
    let version: string | undefined;
    if (coreIdx >= 0 && settled[coreIdx].status === 'fulfilled') {
      const v = (settled[coreIdx] as PromiseFulfilledResult<unknown>).value;
      version = typeof v === 'string' ? v : undefined;
    }

    return { connected, version, modules: results };
  }

  /**
   * 获取系统概览
   */
  async getSystemOverview(): Promise<{
    kamailio: {
      status: 'online' | 'offline';
      version?: string;
      uptime?: any;
    };
    database: {
      subscribers: number;
      domains: number;
      dispatchers: number;
    };
    realtime: {
      onlineUsers: number;
      activeDialogs: number;
    };
  }> {
    const health = await this.checkHealth();
    const subscriberStats = await this.subscriberService.getStats();
    const onlineCount = await this.locationService.getOnlineCount();
    
    let activeDialogs = 0;
    try {
      const dialogCount = await this.kamailioRpcService.getDialogCount();
      activeDialogs = dialogCount?.active || 0;
    } catch (error) {
      // 忽略错误
    }

    return {
      kamailio: {
        status: health.status,
        version: health.version,
        uptime: health.uptime,
      },
      database: {
        subscribers: subscriberStats.total,
        domains: subscriberStats.domains.length,
        dispatchers: 0, // 需要注入 DispatcherService
      },
      realtime: {
        onlineUsers: onlineCount,
        activeDialogs,
      },
    };
  }
}
