import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import axios, { AxiosInstance } from 'axios';

export interface RpcResponse<T = any> {
  jsonrpc: string;
  result?: T;
  error?: {
    code: number;
    message: string;
  };
  id: number;
}

@Injectable()
export class KamailioRpcService {
  private readonly logger = new Logger(KamailioRpcService.name);
  private readonly client: AxiosInstance;
  private requestId = 0;

  constructor(private configService: ConfigService) {
    const rpcConfig = this.configService.get('kamailio.rpc');
    const baseURL = `http://${rpcConfig.host}:${rpcConfig.port}${rpcConfig.path}`;
    
    this.client = axios.create({
      baseURL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  /**
   * 发送 JSONRPC 请求
   */
  async call<T = any>(method: string, params: any[] = []): Promise<T> {
    const id = ++this.requestId;
    const payload = {
      jsonrpc: '2.0',
      method,
      params,
      id,
    };

    try {
      const response = await this.client.post<RpcResponse<T>>('', payload);
      
      if (response.data.error) {
        throw new Error(`RPC Error: ${response.data.error.message}`);
      }
      
      return response.data.result;
    } catch (error) {
      this.logger.error(`RPC call failed: ${method}`, error.message);
      throw error;
    }
  }

  /**
   * 获取统计信息
   */
  async getStatistics(group?: string): Promise<any> {
    const params = group ? [group] : ['all'];
    return this.call('stats.get_statistics', params);
  }

  /**
   * 获取核心信息
   */
  async getCoreInfo(): Promise<any> {
    return this.call('core.info');
  }

  /**
   * 获取正常运行时间
   */
  async getUptime(): Promise<any> {
    return this.call('core.uptime');
  }

  /**
   * 重载调度器
   */
  async reloadDispatcher(): Promise<any> {
    return this.call('dispatcher.reload');
  }

  /**
   * 获取调度器列表
   */
  async getDispatcherList(): Promise<any> {
    return this.call('dispatcher.list');
  }

  /**
   * 获取用户位置信息
   */
  async getUserLocation(table?: string): Promise<any> {
    const params = table ? [table] : [];
    return this.call('ul.dump', params);
  }

  /**
   * 删除用户位置
   */
  async deleteUserLocation(table: string, aor: string): Promise<any> {
    return this.call('ul.rm', [table, aor]);
  }

  /**
   * 重载权限地址表
   */
  async reloadPermissions(): Promise<any> {
    return this.call('permissions.addressReload');
  }

  /**
   * 重载域
   */
  async reloadDomain(): Promise<any> {
    return this.call('domain.reload');
  }

  /**
   * 获取活动对话数
   */
  async getDialogCount(): Promise<any> {
    return this.call('dlg.stats_active');
  }

  /**
   * 获取活动对话列表
   */
  async getDialogList(): Promise<any> {
    return this.call('dlg.list');
  }

  /**
   * 检查 Kamailio 是否在线
   */
  async ping(): Promise<boolean> {
    try {
      await this.call('core.version');
      return true;
    } catch {
      return false;
    }
  }
}
