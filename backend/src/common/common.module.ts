import { Module, Global } from '@nestjs/common';
import { KamailioRpcService } from './services/kamailio-rpc.service';

@Global()
@Module({
  providers: [KamailioRpcService],
  exports: [KamailioRpcService],
})
export class CommonModule {}
