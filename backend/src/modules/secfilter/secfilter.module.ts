import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { SecfilterService } from './secfilter.service';
import { SecfilterController } from './secfilter.controller';
import { Secfilter } from './entities/secfilter.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Secfilter])],
  controllers: [SecfilterController],
  providers: [SecfilterService],
  exports: [SecfilterService],
})
export class SecfilterModule {}
