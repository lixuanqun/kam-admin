import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('presentity')
export class Presentity {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;

  @ApiProperty({ description: '事件' })
  @Column({ length: 64 })
  event: string;

  @ApiProperty({ description: 'ETag' })
  @Column({ length: 128 })
  etag: string;

  @ApiProperty({ description: '过期时间' })
  @Column()
  expires: number;

  @ApiProperty({ description: '接收时间' })
  @Column({ name: 'received_time' })
  receivedTime: number;

  @ApiProperty({ description: 'Body' })
  @Column({ type: 'blob', nullable: true })
  body: Buffer;

  @ApiProperty({ description: '发送者' })
  @Column({ length: 255, nullable: true })
  sender: string;

  @ApiProperty({ description: '优先级' })
  @Column({ default: 0 })
  priority: number;
}
