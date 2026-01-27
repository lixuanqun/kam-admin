import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { CarrierrouteService } from './carrierroute.service';
import { CarrierrouteController } from './carrierroute.controller';
import { CarrierName } from './entities/carrier-name.entity';
import { DomainName } from './entities/domain-name.entity';
import { CarrierFailureRoute } from './entities/carrierfailureroute.entity';

@Module({
  imports: [TypeOrmModule.forFeature([CarrierName, DomainName, CarrierFailureRoute])],
  controllers: [CarrierrouteController],
  providers: [CarrierrouteService],
  exports: [CarrierrouteService],
})
export class CarrierrouteModule {}
