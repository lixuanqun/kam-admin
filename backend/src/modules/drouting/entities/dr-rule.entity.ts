import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dr_rules')
export class DrRule {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  ruleid: number;

  @ApiProperty({ description: '分组 ID' })
  @Column({ length: 255 })
  groupid: string;

  @ApiProperty({ description: '前缀' })
  @Column({ length: 64 })
  prefix: string;

  @ApiProperty({ description: '时间规则' })
  @Column({ length: 255, nullable: true })
  timerec: string;

  @ApiProperty({ description: '优先级' })
  @Column({ default: 0 })
  priority: number;

  @ApiProperty({ description: '路由 ID' })
  @Column({ length: 255, nullable: true })
  routeid: string;

  @ApiProperty({ description: '网关列表' })
  @Column({ length: 255 })
  gwlist: string;

  @ApiProperty({ description: '排序算法' })
  @Column({ default: 0 })
  sort_alg: number;

  @ApiProperty({ description: '排序配置' })
  @Column({ length: 255, nullable: true })
  sort_profile: string;

  @ApiProperty({ description: '属性' })
  @Column({ length: 255, nullable: true })
  attrs: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 128, nullable: true })
  description: string;
}
