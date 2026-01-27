import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dr_carriers')
export class DrCarrier {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '运营商 ID' })
  @Column({ length: 64 })
  carrierid: string;

  @ApiProperty({ description: '网关列表' })
  @Column({ length: 255 })
  gwlist: string;

  @ApiProperty({ description: '标志' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '排序算法' })
  @Column({ default: 0 })
  sort_alg: number;

  @ApiProperty({ description: '状态' })
  @Column({ default: 0 })
  state: number;

  @ApiProperty({ description: '属性' })
  @Column({ length: 255, nullable: true })
  attrs: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 128, nullable: true })
  description: string;
}
