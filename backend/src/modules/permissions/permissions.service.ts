import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Address } from './entities/address.entity';
import { Trusted } from './entities/trusted.entity';
import { CreateAddressDto } from './dto/create-address.dto';
import { CreateTrustedDto } from './dto/create-trusted.dto';
import { PaginationDto, PaginatedResult } from '../../common/dto/pagination.dto';
import { KamailioRpcService } from '../../common/services/kamailio-rpc.service';

@Injectable()
export class PermissionsService {
  constructor(
    @InjectRepository(Address)
    private addressRepository: Repository<Address>,
    @InjectRepository(Trusted)
    private trustedRepository: Repository<Trusted>,
    private kamailioRpcService: KamailioRpcService,
  ) {}

  // ========== Address 操作 ==========

  async createAddress(createDto: CreateAddressDto): Promise<Address> {
    const address = this.addressRepository.create(createDto);
    const saved = await this.addressRepository.save(address);

    try {
      await this.kamailioRpcService.reloadPermissions();
    } catch (error) {
      // 忽略 RPC 错误
    }

    return saved;
  }

  async findAllAddresses(paginationDto: PaginationDto): Promise<PaginatedResult<Address>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {};
    if (keyword) {
      where.ipAddr = Like(`%${keyword}%`);
    }

    const [items, total] = await this.addressRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { grp: 'ASC', id: 'ASC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  async findOneAddress(id: number): Promise<Address> {
    const address = await this.addressRepository.findOne({ where: { id } });
    if (!address) {
      throw new NotFoundException('地址不存在');
    }
    return address;
  }

  async updateAddress(id: number, updateDto: Partial<CreateAddressDto>): Promise<Address> {
    const address = await this.findOneAddress(id);
    Object.assign(address, updateDto);
    const saved = await this.addressRepository.save(address);

    try {
      await this.kamailioRpcService.reloadPermissions();
    } catch (error) {
      // 忽略 RPC 错误
    }

    return saved;
  }

  async removeAddress(id: number): Promise<void> {
    const address = await this.findOneAddress(id);
    await this.addressRepository.remove(address);

    try {
      await this.kamailioRpcService.reloadPermissions();
    } catch (error) {
      // 忽略 RPC 错误
    }
  }

  // ========== Trusted 操作 ==========

  async createTrusted(createDto: CreateTrustedDto): Promise<Trusted> {
    const trusted = this.trustedRepository.create(createDto);
    return this.trustedRepository.save(trusted);
  }

  async findAllTrusted(paginationDto: PaginationDto): Promise<PaginatedResult<Trusted>> {
    const { page, limit, keyword } = paginationDto;
    const skip = (page - 1) * limit;

    const where: any = {};
    if (keyword) {
      where.srcIp = Like(`%${keyword}%`);
    }

    const [items, total] = await this.trustedRepository.findAndCount({
      where,
      skip,
      take: limit,
      order: { priority: 'DESC', id: 'ASC' },
    });

    return new PaginatedResult(items, total, page, limit);
  }

  async findOneTrusted(id: number): Promise<Trusted> {
    const trusted = await this.trustedRepository.findOne({ where: { id } });
    if (!trusted) {
      throw new NotFoundException('信任地址不存在');
    }
    return trusted;
  }

  async updateTrusted(id: number, updateDto: Partial<CreateTrustedDto>): Promise<Trusted> {
    const trusted = await this.findOneTrusted(id);
    Object.assign(trusted, updateDto);
    return this.trustedRepository.save(trusted);
  }

  async removeTrusted(id: number): Promise<void> {
    const trusted = await this.findOneTrusted(id);
    await this.trustedRepository.remove(trusted);
  }

  // ========== 通用操作 ==========

  async reload(): Promise<void> {
    await this.kamailioRpcService.reloadPermissions();
  }

  async getStats(): Promise<{
    addressCount: number;
    trustedCount: number;
    addressGroups: { grp: number; count: number }[];
  }> {
    const addressCount = await this.addressRepository.count();
    const trustedCount = await this.trustedRepository.count();

    const addressGroups = await this.addressRepository
      .createQueryBuilder('a')
      .select('a.grp', 'grp')
      .addSelect('COUNT(*)', 'count')
      .groupBy('a.grp')
      .orderBy('a.grp', 'ASC')
      .getRawMany();

    return { addressCount, trustedCount, addressGroups };
  }
}
