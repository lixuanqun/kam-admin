import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PermissionsService } from './permissions.service';
import { PermissionsController } from './permissions.controller';
import { Address } from './entities/address.entity';
import { Trusted } from './entities/trusted.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Address, Trusted])],
  controllers: [PermissionsController],
  providers: [PermissionsService],
  exports: [PermissionsService],
})
export class PermissionsModule {}
