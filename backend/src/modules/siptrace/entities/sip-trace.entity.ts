import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('sip_trace')
export class SipTrace {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '时间戳' })
  @Column({ name: 'time_stamp', type: 'datetime' })
  timeStamp: Date;

  @ApiProperty({ description: '时间微秒' })
  @Column({ name: 'time_us', default: 0 })
  timeUs: number;

  @ApiProperty({ description: 'Call ID' })
  @Column({ length: 255 })
  callid: string;

  @ApiProperty({ description: '跟踪关联 ID' })
  @Column({ name: 'traced_user', length: 255, nullable: true })
  tracedUser: string;

  @ApiProperty({ description: '消息' })
  @Column({ type: 'mediumtext' })
  msg: string;

  @ApiProperty({ description: '方法' })
  @Column({ length: 50 })
  method: string;

  @ApiProperty({ description: '状态' })
  @Column({ length: 255, nullable: true })
  status: string;

  @ApiProperty({ description: 'From IP' })
  @Column({ name: 'fromip', length: 64 })
  fromIp: string;

  @ApiProperty({ description: 'To IP' })
  @Column({ name: 'toip', length: 64 })
  toIp: string;

  @ApiProperty({ description: 'From Tag' })
  @Column({ name: 'fromtag', length: 128 })
  fromTag: string;

  @ApiProperty({ description: 'To Tag' })
  @Column({ name: 'totag', length: 128, nullable: true })
  toTag: string;

  @ApiProperty({ description: '方向' })
  @Column({ length: 4 })
  direction: string;
}
