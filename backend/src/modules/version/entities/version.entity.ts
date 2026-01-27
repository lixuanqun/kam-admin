import { Entity, Column, PrimaryColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('version')
export class Version {
  @ApiProperty({ description: '表名' })
  @PrimaryColumn({ name: 'table_name', length: 32 })
  tableName: string;

  @ApiProperty({ description: '版本号' })
  @Column({ name: 'table_version', default: 0 })
  tableVersion: number;
}
