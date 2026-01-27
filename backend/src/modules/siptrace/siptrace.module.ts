import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { SiptraceService } from './siptrace.service';
import { SiptraceController } from './siptrace.controller';
import { SipTrace } from './entities/sip-trace.entity';

@Module({
  imports: [TypeOrmModule.forFeature([SipTrace])],
  controllers: [SiptraceController],
  providers: [SiptraceService],
  exports: [SiptraceService],
})
export class SiptraceModule {}
