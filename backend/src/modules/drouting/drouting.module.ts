import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DroutingService } from './drouting.service';
import { DroutingController } from './drouting.controller';
import { DrGateway } from './entities/dr-gateway.entity';
import { DrRule } from './entities/dr-rule.entity';
import { DrGroup } from './entities/dr-group.entity';
import { DrCarrier } from './entities/dr-carrier.entity';

@Module({
  imports: [TypeOrmModule.forFeature([DrGateway, DrRule, DrGroup, DrCarrier])],
  controllers: [DroutingController],
  providers: [DroutingService],
  exports: [DroutingService],
})
export class DroutingModule {}
