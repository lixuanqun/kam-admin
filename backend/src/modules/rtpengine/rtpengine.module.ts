import { Module } from '@nestjs/common';
import { RtpengineService } from './rtpengine.service';
import { RtpengineController } from './rtpengine.controller';

@Module({
  controllers: [RtpengineController],
  providers: [RtpengineService],
  exports: [RtpengineService],
})
export class RtpengineModule {}
