import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('speed_dial')
export class SpeedDial {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;

  @ApiProperty({ description: '快捷号码' })
  @Column({ name: 'sd_username', length: 64 })
  sdUsername: string;

  @ApiProperty({ description: '快捷域' })
  @Column({ name: 'sd_domain', length: 64 })
  sdDomain: string;

  @ApiProperty({ description: '新 URI' })
  @Column({ name: 'new_uri', length: 255 })
  newUri: string;

  @ApiProperty({ description: '描述' })
  @Column({ length: 64, nullable: true })
  description: string;
}
