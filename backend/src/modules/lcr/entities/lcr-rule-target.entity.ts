import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('lcr_rule_target')
export class LcrRuleTarget {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'LCR ID' })
  @Column({ name: 'lcr_id', default: 1 })
  lcrId: number;

  @ApiProperty({ description: '规则 ID' })
  @Column({ name: 'rule_id' })
  ruleId: number;

  @ApiProperty({ description: '网关 ID' })
  @Column({ name: 'gw_id' })
  gwId: number;

  @ApiProperty({ description: '优先级' })
  @Column({ default: 1 })
  priority: number;

  @ApiProperty({ description: '权重' })
  @Column({ default: 1 })
  weight: number;
}
