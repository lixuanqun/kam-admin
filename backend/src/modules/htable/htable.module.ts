import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { HtableService } from './htable.service';
import { HtableController } from './htable.controller';
import { Htable } from './entities/htable.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Htable])],
  controllers: [HtableController],
  providers: [HtableService],
  exports: [HtableService],
})
export class HtableModule {}
