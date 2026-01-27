import { Entity, Column, PrimaryGeneratedColumn, CreateDateColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('subscriber')
export class Subscriber {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;

  @ApiProperty({ description: '密码（HA1 格式）' })
  @Column({ length: 64, select: false })
  password: string;

  @ApiProperty({ description: 'HA1 哈希' })
  @Column({ length: 128, select: false })
  ha1: string;

  @ApiProperty({ description: 'HA1B 哈希' })
  @Column({ length: 128, select: false })
  ha1b: string;

  @ApiProperty({ description: '邮箱地址' })
  @Column({ length: 64, nullable: true, name: 'email_address' })
  emailAddress: string;

  @ApiProperty({ description: 'RP ID' })
  @Column({ length: 64, nullable: true, name: 'rpid' })
  rpid: string;
}
