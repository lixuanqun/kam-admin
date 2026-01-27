import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dbaliases')
export class DbAliases {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: '别名用户名' })
  @Column({ name: 'alias_username', length: 64 })
  aliasUsername: string;

  @ApiProperty({ description: '别名域' })
  @Column({ name: 'alias_domain', length: 64 })
  aliasDomain: string;

  @ApiProperty({ description: '用户名' })
  @Column({ length: 64 })
  username: string;

  @ApiProperty({ description: '域' })
  @Column({ length: 64 })
  domain: string;
}
