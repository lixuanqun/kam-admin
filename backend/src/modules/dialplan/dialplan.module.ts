import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DialplanService } from './dialplan.service';
import { DialplanController } from './dialplan.controller';
import { Dialplan } from './entities/dialplan.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Dialplan])],
  controllers: [DialplanController],
  providers: [DialplanService],
  exports: [DialplanService],
})
export class DialplanModule {}
