import { Module } from '@nestjs/common';
import { MonitoringService } from './monitoring.service';
import { MonitoringController } from './monitoring.controller';
import { SubscriberModule } from '../subscriber/subscriber.module';
import { LocationModule } from '../location/location.module';
import { AccModule } from '../acc/acc.module';

@Module({
  imports: [SubscriberModule, LocationModule, AccModule],
  controllers: [MonitoringController],
  providers: [MonitoringService],
  exports: [MonitoringService],
})
export class MonitoringModule {}
