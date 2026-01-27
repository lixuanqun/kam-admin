import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { MtreeService } from './mtree.service';
import { MtreeController } from './mtree.controller';
import { Mtree } from './entities/mtree.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Mtree])],
  controllers: [MtreeController],
  providers: [MtreeService],
  exports: [MtreeService],
})
export class MtreeModule {}
