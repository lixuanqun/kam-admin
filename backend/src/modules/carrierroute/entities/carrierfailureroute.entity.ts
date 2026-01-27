import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('carrierfailureroute')
export class CarrierFailureRoute {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '运营商' })
  @Column()
  carrier: number;

  @ApiProperty({ description: '域' })
  @Column()
  domain: number;

  @ApiProperty({ description: '扫描前缀' })
  @Column({ name: 'scan_prefix', length: 64 })
  scanPrefix: string;

  @ApiProperty({ description: '主机名' })
  @Column({ length: 255 })
  host_name: string;

  @ApiProperty({ description: '回复码' })
  @Column({ name: 'reply_code', length: 3 })
  replyCode: string;

  @ApiProperty({ description: '标志' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '掩码' })
  @Column({ default: 0 })
  mask: number;

  @ApiProperty({ description: '下一个域' })
  @Column({ name: 'next_domain', length: 64 })
  nextDomain: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 255, nullable: true })
  description: string;
}
