import { Injectable } from '@nestjs/common';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class RtpengineService {
  constructor(private kamailioRpcService: KamailioRpcService) {}

  // RTPEngine 操作
  async showAll(): Promise<any> {
    return this.kamailioRpcService.call('rtpengine.show', ['all']);
  }

  async reload(): Promise<void> {
    await this.kamailioRpcService.call('rtpengine.reload');
  }

  async enable(url: string, flag: number): Promise<void> {
    await this.kamailioRpcService.call('rtpengine.enable', [url, flag]);
  }

  async ping(url: string): Promise<any> {
    return this.kamailioRpcService.call('rtpengine.ping', [url]);
  }

  // RTPProxy 操作
  async rtpproxyEnable(url: string, flag: number): Promise<void> {
    await this.kamailioRpcService.call('rtpproxy.enable', [url, flag]);
  }

  // NAT Helper
  async enableNatPing(flag: number): Promise<void> {
    await this.kamailioRpcService.call('nathelper.enable_ping', [flag]);
  }

  // 获取综合状态
  async getStatus(): Promise<any> {
    try {
      const rtpengine = await this.showAll();
      return { rtpengine, status: 'ok' };
    } catch (error) {
      return { status: 'error', error: error.message };
    }
  }
}
