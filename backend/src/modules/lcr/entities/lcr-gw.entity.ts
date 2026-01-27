import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('lcr_gw')
export class LcrGw {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'LCR ID' })
  @Column({ name: 'lcr_id', default: 1 })
  lcrId: number;

  @ApiProperty({ description: '网关名称' })
  @Column({ name: 'gw_name', length: 128, nullable: true })
  gwName: string;

  @ApiProperty({ description: 'IP 地址' })
  @Column({ name: 'ip_addr', length: 50, nullable: true })
  ipAddr: string;

  @ApiProperty({ description: '主机名' })
  @Column({ length: 64, nullable: true })
  hostname: string;

  @ApiProperty({ description: '端口' })
  @Column({ default: 0 })
  port: number;

  @ApiProperty({ description: 'URI 方案' })
  @Column({ name: 'uri_scheme', default: 1 })
  uriScheme: number;

  @ApiProperty({ description: '传输协议' })
  @Column({ default: 0 })
  transport: number;

  @ApiProperty({ description: '参数' })
  @Column({ length: 64, nullable: true })
  params: string;

  @ApiProperty({ description: '剥离' })
  @Column({ default: 0 })
  strip: number;

  @ApiProperty({ description: '前缀' })
  @Column({ length: 16, nullable: true })
  prefix: string;

  @ApiProperty({ description: '标签' })
  @Column({ length: 64, nullable: true })
  tag: string;

  @ApiProperty({ description: '标志' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '已废弃' })
  @Column({ default: 0 })
  defunct: number;
}
