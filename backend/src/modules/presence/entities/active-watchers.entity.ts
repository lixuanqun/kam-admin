import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('active_watchers')
export class ActiveWatchers {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'Presentity URI' })
  @Column({ name: 'presentity_uri', length: 255 })
  presentityUri: string;

  @ApiProperty({ description: 'Watcher 用户名' })
  @Column({ name: 'watcher_username', length: 64 })
  watcherUsername: string;

  @ApiProperty({ description: 'Watcher 域' })
  @Column({ name: 'watcher_domain', length: 64 })
  watcherDomain: string;

  @ApiProperty({ description: 'To User' })
  @Column({ name: 'to_user', length: 64 })
  toUser: string;

  @ApiProperty({ description: 'To Domain' })
  @Column({ name: 'to_domain', length: 64 })
  toDomain: string;

  @ApiProperty({ description: '事件' })
  @Column({ length: 64 })
  event: string;

  @ApiProperty({ description: '事件 ID' })
  @Column({ name: 'event_id', length: 64, nullable: true })
  eventId: string;

  @ApiProperty({ description: 'To Tag' })
  @Column({ name: 'to_tag', length: 128 })
  toTag: string;

  @ApiProperty({ description: 'From Tag' })
  @Column({ name: 'from_tag', length: 128 })
  fromTag: string;

  @ApiProperty({ description: 'Call ID' })
  @Column({ length: 255 })
  callid: string;

  @ApiProperty({ description: '本地 CSeq' })
  @Column({ name: 'local_cseq' })
  localCseq: number;

  @ApiProperty({ description: '远程 CSeq' })
  @Column({ name: 'remote_cseq' })
  remoteCseq: number;

  @ApiProperty({ description: '联系地址' })
  @Column({ length: 255 })
  contact: string;

  @ApiProperty({ description: 'Record Route' })
  @Column({ name: 'record_route', type: 'text', nullable: true })
  recordRoute: string;

  @ApiProperty({ description: '过期时间' })
  @Column()
  expires: number;

  @ApiProperty({ description: '状态' })
  @Column({ default: 0 })
  status: number;

  @ApiProperty({ description: '原因' })
  @Column({ length: 64, nullable: true })
  reason: string;

  @ApiProperty({ description: '版本' })
  @Column({ default: 0 })
  version: number;

  @ApiProperty({ description: '套接字信息' })
  @Column({ name: 'socket_info', length: 64 })
  socketInfo: string;

  @ApiProperty({ description: '本地联系地址' })
  @Column({ name: 'local_contact', length: 255 })
  localContact: string;

  @ApiProperty({ description: 'From User' })
  @Column({ name: 'from_user', length: 64 })
  fromUser: string;

  @ApiProperty({ description: 'From Domain' })
  @Column({ name: 'from_domain', length: 64 })
  fromDomain: string;

  @ApiProperty({ description: '更新时间' })
  @Column()
  updated: number;

  @ApiProperty({ description: '更新 Watcher' })
  @Column({ name: 'updated_winfo' })
  updatedWinfo: number;

  @ApiProperty({ description: '标志' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '用户代理' })
  @Column({ name: 'user_agent', length: 255, nullable: true })
  userAgent: string;
}
