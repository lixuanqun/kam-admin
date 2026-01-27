import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('trusted')
export class Trusted {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '源 IP' })
  @Column({ name: 'src_ip', length: 50 })
  srcIp: string;

  @ApiProperty({ description: '协议' })
  @Column({ length: 4, nullable: true })
  proto: string;

  @ApiProperty({ description: 'From 匹配模式' })
  @Column({ name: 'from_pattern', length: 64, nullable: true })
  fromPattern: string;

  @ApiProperty({ description: 'RU ID' })
  @Column({ length: 64, nullable: true })
  ruri_pattern: string;

  @ApiProperty({ description: '标签' })
  @Column({ length: 64, nullable: true })
  tag: string;

  @ApiProperty({ description: '优先级' })
  @Column({ default: 0 })
  priority: number;
}
