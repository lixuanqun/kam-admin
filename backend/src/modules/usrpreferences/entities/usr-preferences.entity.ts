import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('usr_preferences')
export class UsrPreferences {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'UUID' })
  @Column({ length: 64, default: '' })
  uuid: string;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 255, default: '0' })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64, default: '' })
  domain: string;

  @ApiProperty({ description: '属性' })
  @Column({ length: 64, default: '' })
  attribute: string;

  @ApiProperty({ description: '类型' })
  @Column({ default: 0 })
  type: number;

  @ApiProperty({ description: '值' })
  @Column({ length: 255, default: '' })
  value: string;

  @ApiProperty({ description: '最后修改时间' })
  @Column({ name: 'last_modified', type: 'datetime', default: () => 'CURRENT_TIMESTAMP' })
  lastModified: Date;
}
