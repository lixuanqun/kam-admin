import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UserdataService } from './userdata.service';
import { UserdataController } from './userdata.controller';
import { DbAliases } from './entities/dbaliases.entity';
import { Grp } from './entities/grp.entity';
import { SpeedDial } from './entities/speed-dial.entity';

@Module({
  imports: [TypeOrmModule.forFeature([DbAliases, Grp, SpeedDial])],
  controllers: [UserdataController],
  providers: [UserdataService],
  exports: [UserdataService],
})
export class UserdataModule {}
