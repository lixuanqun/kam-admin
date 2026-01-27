import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { MsiloService } from './msilo.service';
import { MsiloController } from './msilo.controller';
import { Silo } from './entities/silo.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Silo])],
  controllers: [MsiloController],
  providers: [MsiloService],
  exports: [MsiloService],
})
export class MsiloModule {}
