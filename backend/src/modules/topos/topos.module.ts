import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ToposService } from './topos.service';
import { ToposController } from './topos.controller';
import { ToposD } from './entities/topos-d.entity';

@Module({
  imports: [TypeOrmModule.forFeature([ToposD])],
  controllers: [ToposController],
  providers: [ToposService],
  exports: [ToposService],
})
export class ToposModule {}
