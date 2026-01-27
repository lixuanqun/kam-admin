import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('address')
export class Address {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '分组 ID' })
  @Column({ name: 'grp', default: 1 })
  grp: number;

  @ApiProperty({ description: 'IP 地址' })
  @Column({ name: 'ip_addr', length: 50 })
  ipAddr: string;

  @ApiProperty({ description: '掩码' })
  @Column({ default: 32 })
  mask: number;

  @ApiProperty({ description: '端口' })
  @Column({ default: 0 })
  port: number;

  @ApiProperty({ description: '标签' })
  @Column({ length: 64, nullable: true })
  tag: string;
}
