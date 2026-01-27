import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dialplan')
export class Dialplan {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'DPID' })
  @Column({ default: 0 })
  dpid: number;

  @ApiProperty({ description: '优先级' })
  @Column({ name: 'pr', default: 0 })
  priority: number;

  @ApiProperty({ description: '匹配操作' })
  @Column({ name: 'match_op', default: 1 })
  matchOp: number;

  @ApiProperty({ description: '匹配表达式' })
  @Column({ name: 'match_exp', length: 64 })
  matchExp: string;

  @ApiProperty({ description: '匹配长度' })
  @Column({ name: 'match_len', default: 0 })
  matchLen: number;

  @ApiProperty({ description: '替换操作' })
  @Column({ name: 'subst_exp', length: 64 })
  substExp: string;

  @ApiProperty({ description: '替换结果' })
  @Column({ name: 'repl_exp', length: 256 })
  replExp: string;

  @ApiProperty({ description: '属性' })
  @Column({ length: 64, nullable: true })
  attrs: string;
}
