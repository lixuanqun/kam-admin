import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dr_gateways')
export class DrGateway {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '网关 ID' })
  @Column({ length: 64 })
  gwid: string;

  @ApiProperty({ description: '类型' })
  @Column({ default: 0 })
  type: number;

  @ApiProperty({ description: '地址' })
  @Column({ length: 128 })
  address: string;

  @ApiProperty({ description: '剥离位数' })
  @Column({ default: 0 })
  strip: number;

  @ApiProperty({ description: '前缀' })
  @Column({ name: 'pri_prefix', length: 16, nullable: true })
  priPrefix: string;

  @ApiProperty({ description: '属性' })
  @Column({ length: 255, nullable: true })
  attrs: string;

  @ApiProperty({ description: '探测模式' })
  @Column({ name: 'probe_mode', default: 0 })
  probeMode: number;

  @ApiProperty({ description: '状态' })
  @Column({ default: 0 })
  state: number;

  @ApiProperty({ description: '套接字' })
  @Column({ length: 128, nullable: true })
  socket: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 128, nullable: true })
  description: string;
}
