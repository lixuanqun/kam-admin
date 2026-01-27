import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AccService } from './acc.service';
import { AccController } from './acc.controller';
import { Acc } from './entities/acc.entity';
import { MissedCall } from './entities/missed-call.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Acc, MissedCall])],
  controllers: [AccController],
  providers: [AccService],
  exports: [AccService],
})
export class AccModule {}
