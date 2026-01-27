import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PdtService } from './pdt.service';
import { PdtController } from './pdt.controller';
import { Pdt } from './entities/pdt.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Pdt])],
  controllers: [PdtController],
  providers: [PdtService],
  exports: [PdtService],
})
export class PdtModule {}
