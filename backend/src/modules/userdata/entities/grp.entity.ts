import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('grp')
export class Grp {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;

  @ApiProperty({ description: '组名' })
  @Column({ name: 'grp', length: 64 })
  grp: string;

  @ApiProperty({ description: '最后修改时间' })
  @Column({ name: 'last_modified', type: 'datetime', default: () => 'CURRENT_TIMESTAMP' })
  lastModified: Date;
}
