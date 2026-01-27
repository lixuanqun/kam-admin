import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('missed_calls')
export class MissedCall {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '方法' })
  @Column({ length: 16 })
  method: string;

  @ApiProperty({ description: 'From 标签' })
  @Column({ name: 'from_tag', length: 64 })
  fromTag: string;

  @ApiProperty({ description: 'To 标签' })
  @Column({ name: 'to_tag', length: 64 })
  toTag: string;

  @ApiProperty({ description: 'Call ID' })
  @Column({ name: 'callid', length: 255 })
  callid: string;

  @ApiProperty({ description: 'SIP 响应码' })
  @Column({ name: 'sip_code', length: 3 })
  sipCode: string;

  @ApiProperty({ description: 'SIP 原因' })
  @Column({ name: 'sip_reason', length: 128 })
  sipReason: string;

  @ApiProperty({ description: '时间' })
  @Column({ type: 'datetime' })
  time: Date;

  @ApiProperty({ description: '来源用户' })
  @Column({ name: 'src_user', length: 64, nullable: true })
  srcUser: string;

  @ApiProperty({ description: '来源域' })
  @Column({ name: 'src_domain', length: 128, nullable: true })
  srcDomain: string;

  @ApiProperty({ description: '来源 IP' })
  @Column({ name: 'src_ip', length: 64, nullable: true })
  srcIp: string;

  @ApiProperty({ description: '目标用户' })
  @Column({ name: 'dst_user', length: 64, nullable: true })
  dstUser: string;

  @ApiProperty({ description: '目标域' })
  @Column({ name: 'dst_domain', length: 128, nullable: true })
  dstDomain: string;
}
