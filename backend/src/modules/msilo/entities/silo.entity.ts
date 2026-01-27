import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('silo')
export class Silo {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '源地址' })
  @Column({ name: 'src_addr', length: 255 })
  srcAddr: string;

  @ApiProperty({ description: '目标地址' })
  @Column({ name: 'dst_addr', length: 255 })
  dstAddr: string;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;

  @ApiProperty({ description: '存储时间' })
  @Column({ name: 'inc_time' })
  incTime: number;

  @ApiProperty({ description: '过期时间' })
  @Column({ name: 'exp_time' })
  expTime: number;

  @ApiProperty({ description: 'SIP 方法' })
  @Column({ name: 'snd_time' })
  sndTime: number;

  @ApiProperty({ description: 'Call ID' })
  @Column({ length: 255, nullable: true })
  callid: string;

  @ApiProperty({ description: '消息体' })
  @Column({ type: 'blob', nullable: true })
  body: Buffer;

  @ApiProperty({ description: '额外头部' })
  @Column({ name: 'extra_hdrs', type: 'text', nullable: true })
  extraHdrs: string;

  @ApiProperty({ description: '内容类型' })
  @Column({ name: 'content_type', length: 128, nullable: true })
  contentType: string;
}
