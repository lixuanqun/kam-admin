import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('uacreg')
export class UacReg {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'L UUID' })
  @Column({ name: 'l_uuid', length: 64 })
  lUuid: string;

  @ApiProperty({ description: 'L 用户名' })
  @Column({ name: 'l_username', length: 64 })
  lUsername: string;

  @ApiProperty({ description: 'L 域' })
  @Column({ name: 'l_domain', length: 64 })
  lDomain: string;

  @ApiProperty({ description: 'R 用户名' })
  @Column({ name: 'r_username', length: 64 })
  rUsername: string;

  @ApiProperty({ description: 'R 域' })
  @Column({ name: 'r_domain', length: 64 })
  rDomain: string;

  @ApiProperty({ description: 'Realm' })
  @Column({ length: 64, nullable: true })
  realm: string;

  @ApiProperty({ description: '认证用户名' })
  @Column({ name: 'auth_username', length: 64 })
  authUsername: string;

  @ApiProperty({ description: '认证密码' })
  @Column({ name: 'auth_password', length: 64 })
  authPassword: string;

  @ApiProperty({ description: '认证代理' })
  @Column({ name: 'auth_proxy', length: 255, nullable: true })
  authProxy: string;

  @ApiProperty({ description: '过期时间' })
  @Column({ default: 0 })
  expires: number;

  @ApiProperty({ description: '标志' })
  @Column({ default: 0 })
  flags: number;

  @ApiProperty({ description: '注册延迟' })
  @Column({ name: 'reg_delay', default: 0 })
  regDelay: number;

  @ApiProperty({ description: '联系地址' })
  @Column({ name: 'contact_addr', length: 255, nullable: true })
  contactAddr: string;

  @ApiProperty({ description: '套接字' })
  @Column({ length: 128, nullable: true })
  socket: string;
}
