import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('mtree')
export class Mtree {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '树名' })
  @Column({ length: 128 })
  tname: string;

  @ApiProperty({ description: '前缀' })
  @Column({ length: 32 })
  tprefix: string;

  @ApiProperty({ description: '值' })
  @Column({ length: 128 })
  tvalue: string;
}
