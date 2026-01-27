import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { CarrierName } from './entities/carrier-name.entity';
import { DomainName } from './entities/domain-name.entity';
import { CarrierFailureRoute } from './entities/carrierfailureroute.entity';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class CarrierrouteService {
  constructor(
    @InjectRepository(CarrierName)
    private carrierNameRepository: Repository<CarrierName>,
    @InjectRepository(DomainName)
    private domainNameRepository: Repository<DomainName>,
    @InjectRepository(CarrierFailureRoute)
    private failureRouteRepository: Repository<CarrierFailureRoute>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  // Carrier Names
  async findAllCarriers(): Promise<CarrierName[]> {
    return this.carrierNameRepository.find({ order: { id: 'ASC' } });
  }

  async createCarrier(carrier: string): Promise<CarrierName> {
    return this.carrierNameRepository.save(this.carrierNameRepository.create({ carrier }));
  }

  async deleteCarrier(id: number): Promise<void> {
    await this.carrierNameRepository.delete(id);
  }

  // Domain Names
  async findAllDomains(): Promise<DomainName[]> {
    return this.domainNameRepository.find({ order: { id: 'ASC' } });
  }

  async createDomain(domain: string): Promise<DomainName> {
    return this.domainNameRepository.save(this.domainNameRepository.create({ domain }));
  }

  async deleteDomain(id: number): Promise<void> {
    await this.domainNameRepository.delete(id);
  }

  // Failure Routes
  async findAllFailureRoutes(dto: PaginationDto): Promise<PaginatedResult<CarrierFailureRoute>> {
    const { page, limit } = dto;
    const skip = (page - 1) * limit;
    const [items, total] = await this.failureRouteRepository.findAndCount({
      skip, take: limit, order: { id: 'ASC' },
    });
    return new PaginatedResult(items, total, page, limit);
  }

  async createFailureRoute(data: Partial<CarrierFailureRoute>): Promise<CarrierFailureRoute> {
    return this.failureRouteRepository.save(this.failureRouteRepository.create(data));
  }

  async deleteFailureRoute(id: number): Promise<void> {
    await this.failureRouteRepository.delete(id);
  }

  // RPC
  async reload(): Promise<void> {
    await this.kamailioRpcService.call('cr.reload_routes');
  }

  async dumpRoutes(): Promise<any> {
    return this.kamailioRpcService.call('cr.dump_routes');
  }
}
