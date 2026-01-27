import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dr_groups')
export class DrGroup {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 128, default: '' })
  domain: string;

  @ApiProperty({ description: '分组 ID' })
  @Column({ default: 0 })
  groupid: number;

  @ApiProperty({ description: '描述' })
  @Column({ length: 128, nullable: true })
  description: string;
}
