import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('htable')
export class Htable {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '键名' })
  @Column({ name: 'key_name', length: 64 })
  keyName: string;

  @ApiProperty({ description: '键类型' })
  @Column({ name: 'key_type', default: 0 })
  keyType: number;

  @ApiProperty({ description: '值类型' })
  @Column({ name: 'value_type', default: 0 })
  valueType: number;

  @ApiProperty({ description: '键值' })
  @Column({ name: 'key_value', length: 128, default: '' })
  keyValue: string;

  @ApiProperty({ description: '过期时间' })
  @Column({ default: 0 })
  expires: number;
}
