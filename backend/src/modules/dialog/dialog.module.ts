import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DialogService } from './dialog.service';
import { DialogController } from './dialog.controller';
import { Dialog } from './entities/dialog.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Dialog])],
  controllers: [DialogController],
  providers: [DialogService],
  exports: [DialogService],
})
export class DialogModule {}
