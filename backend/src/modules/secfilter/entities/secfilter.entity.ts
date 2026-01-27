import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('secfilter')
export class Secfilter {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '动作' })
  @Column({ default: 0 })
  action: number;

  @ApiProperty({ description: '类型' })
  @Column({ default: 0 })
  type: number;

  @ApiProperty({ description: '数据' })
  @Column({ length: 64 })
  data: string;
}
