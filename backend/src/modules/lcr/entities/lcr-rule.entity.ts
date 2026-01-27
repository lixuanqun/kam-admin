import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('lcr_rule')
export class LcrRule {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'LCR ID' })
  @Column({ name: 'lcr_id', default: 1 })
  lcrId: number;

  @ApiProperty({ description: '前缀' })
  @Column({ length: 16, nullable: true })
  prefix: string;

  @ApiProperty({ description: 'From URI' })
  @Column({ name: 'from_uri', length: 64, nullable: true })
  fromUri: string;

  @ApiProperty({ description: 'Request URI' })
  @Column({ name: 'request_uri', length: 64, nullable: true })
  requestUri: string;

  @ApiProperty({ description: 'MT TValue' })
  @Column({ name: 'mt_tvalue', length: 128, nullable: true })
  mtTvalue: string;

  @ApiProperty({ description: '存储 ID' })
  @Column({ default: 0 })
  stopper: number;

  @ApiProperty({ description: '启用' })
  @Column({ default: 1 })
  enabled: number;
}
