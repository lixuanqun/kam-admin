import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dispatcher')
export class Dispatcher {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '调度组 ID' })
  @Column({ name: 'setid', default: 0 })
  setid: number;

  @ApiProperty({ description: '目标地址' })
  @Column({ length: 256 })
  destination: string;

  @ApiProperty({ description: '标志位' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '优先级' })
  @Column({ default: 0 })
  priority: number;

  @ApiProperty({ description: '属性' })
  @Column({ length: 128, default: '' })
  attrs: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 64, default: '' })
  description: string;
}
