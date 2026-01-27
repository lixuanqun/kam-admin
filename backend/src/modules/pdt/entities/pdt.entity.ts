import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('pdt')
export class Pdt {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'SD 域' })
  @Column({ length: 255 })
  sdomain: string;

  @ApiProperty({ description: '前缀' })
  @Column({ length: 32 })
  prefix: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 255 })
  domain: string;
}
