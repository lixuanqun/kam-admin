import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('location')
export class Location {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'RU ID' })
  @Column({ default: 0 })
  ruid: string;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64, nullable: true })
  domain: string;

  @ApiProperty({ description: '联系地址' })
  @Column({ length: 512 })
  contact: string;

  @ApiProperty({ description: 'Received' })
  @Column({ length: 128, nullable: true })
  received: string;

  @ApiProperty({ description: '路径' })
  @Column({ length: 512, nullable: true })
  path: string;

  @ApiProperty({ description: '过期时间' })
  @Column({ type: 'datetime' })
  expires: Date;

  @ApiProperty({ description: 'Q 值' })
  @Column({ type: 'float', default: -1 })
  q: number;

  @ApiProperty({ description: 'Call ID' })
  @Column({ length: 255, nullable: true })
  callid: string;

  @ApiProperty({ description: 'CSeq' })
  @Column({ default: 0 })
  cseq: number;

  @ApiProperty({ description: '最后修改时间' })
  @Column({ name: 'last_modified', type: 'datetime', default: () => 'CURRENT_TIMESTAMP' })
  lastModified: Date;

  @ApiProperty({ description: '标志位' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: 'CFlags' })
  @Column({ default: 0 })
  cflags: number;

  @ApiProperty({ description: 'User Agent' })
  @Column({ name: 'user_agent', length: 255, nullable: true })
  userAgent: string;

  @ApiProperty({ description: 'Socket' })
  @Column({ length: 64, nullable: true })
  socket: string;

  @ApiProperty({ description: '方法' })
  @Column({ default: 4294967295 })
  methods: number;

  @ApiProperty({ description: '实例' })
  @Column({ length: 255, nullable: true })
  instance: string;

  @ApiProperty({ description: '注册 ID' })
  @Column({ name: 'reg_id', default: 0 })
  regId: number;

  @ApiProperty({ description: '服务器 ID' })
  @Column({ name: 'server_id', default: 0 })
  serverId: number;

  @ApiProperty({ description: '连接 ID' })
  @Column({ name: 'connection_id', default: 0 })
  connectionId: number;

  @ApiProperty({ description: 'Keepalive' })
  @Column({ default: 0 })
  keepalive: number;

  @ApiProperty({ description: '分区' })
  @Column({ default: 0 })
  partition: number;
}
