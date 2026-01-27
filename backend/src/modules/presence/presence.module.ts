import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PresenceService } from './presence.service';
import { PresenceController } from './presence.controller';
import { Presentity } from './entities/presentity.entity';
import { ActiveWatchers } from './entities/active-watchers.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Presentity, ActiveWatchers])],
  controllers: [PresenceController],
  providers: [PresenceService],
  exports: [PresenceService],
})
export class PresenceModule {}
