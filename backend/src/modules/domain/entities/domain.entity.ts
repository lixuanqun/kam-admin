import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('domain')
export class Domain {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '域名' })
  @Column({ length: 64, unique: true })
  domain: string;

  @ApiProperty({ description: '是否禁用' })
  @Column({ name: 'did', length: 64, nullable: true })
  did: string;

  @ApiProperty({ description: '最后修改时间' })
  @Column({ name: 'last_modified', type: 'datetime', default: () => 'CURRENT_TIMESTAMP' })
  lastModified: Date;
}
