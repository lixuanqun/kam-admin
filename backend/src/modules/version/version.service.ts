import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Version } from './entities/version.entity';

@Injectable()
export class VersionService {
  constructor(
    @InjectRepository(Version)
    private versionRepository: Repository<Version>,
  ) {}

  async findAll(): Promise<Version[]> {
    return this.versionRepository.find({ order: { tableName: 'ASC' } });
  }

  async getVersion(tableName: string): Promise<number | null> {
    const item = await this.versionRepository.findOne({ where: { tableName } });
    return item?.tableVersion ?? null;
  }

  async getStats(): Promise<any> {
    const tables = await this.versionRepository.find();
    return {
      totalTables: tables.length,
      tables: tables.map(t => ({ name: t.tableName, version: t.tableVersion })),
    };
  }
}
