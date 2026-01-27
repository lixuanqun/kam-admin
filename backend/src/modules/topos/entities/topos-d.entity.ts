import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('topos_d')
export class ToposD {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty({ description: 'Rectime' })
  @Column({ type: 'datetime' })
  rectime: Date;

  @ApiProperty({ description: 'X 上下文' })
  @Column({ length: 64 })
  x_context: string;

  @ApiProperty({ description: 'S 方法' })
  @Column({ length: 64 })
  s_method: string;

  @ApiProperty({ description: 'S CSeq' })
  @Column({ length: 64 })
  s_cseq: string;

  @ApiProperty({ description: 'A Callid' })
  @Column({ length: 255 })
  a_callid: string;

  @ApiProperty({ description: 'A UUID' })
  @Column({ length: 255 })
  a_uuid: string;

  @ApiProperty({ description: 'B UUID' })
  @Column({ length: 255 })
  b_uuid: string;

  @ApiProperty({ description: 'A Contact' })
  @Column({ length: 512 })
  a_contact: string;

  @ApiProperty({ description: 'B Contact' })
  @Column({ length: 512 })
  b_contact: string;

  @ApiProperty({ description: 'As Contact' })
  @Column({ length: 512 })
  as_contact: string;

  @ApiProperty({ description: 'Bs Contact' })
  @Column({ length: 512 })
  bs_contact: string;

  @ApiProperty({ description: 'A Tag' })
  @Column({ length: 255 })
  a_tag: string;

  @ApiProperty({ description: 'B Tag' })
  @Column({ length: 255 })
  b_tag: string;

  @ApiProperty({ description: 'A RR' })
  @Column({ type: 'mediumtext', nullable: true })
  a_rr: string;

  @ApiProperty({ description: 'B RR' })
  @Column({ type: 'mediumtext', nullable: true })
  b_rr: string;

  @ApiProperty({ description: 'S RR' })
  @Column({ type: 'mediumtext', nullable: true })
  s_rr: string;

  @ApiProperty({ description: 'I User' })
  @Column({ length: 255, nullable: true })
  iflags: string;

  @ApiProperty({ description: 'A Socket' })
  @Column({ length: 128, nullable: true })
  a_socket: string;

  @ApiProperty({ description: 'B Socket' })
  @Column({ length: 128, nullable: true })
  b_socket: string;
}
