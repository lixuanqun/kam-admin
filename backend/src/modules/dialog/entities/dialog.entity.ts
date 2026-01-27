import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('dialog')
export class Dialog {
  @ApiProperty({ description: 'ID' })
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ name: 'hash_entry' })
  hashEntry: number;

  @Column({ name: 'hash_id' })
  hashId: number;

  @Column({ length: 255 })
  callid: string;

  @Column({ name: 'from_uri', length: 255 })
  fromUri: string;

  @Column({ name: 'from_tag', length: 128 })
  fromTag: string;

  @Column({ name: 'to_uri', length: 255 })
  toUri: string;

  @Column({ name: 'to_tag', length: 128 })
  toTag: string;

  @Column({ name: 'caller_cseq', length: 20 })
  callerCseq: string;

  @Column({ name: 'callee_cseq', length: 20 })
  calleeCseq: string;

  @Column({ name: 'caller_contact', length: 255 })
  callerContact: string;

  @Column({ name: 'callee_contact', length: 255 })
  calleeContact: string;

  @Column({ name: 'caller_sock', length: 64 })
  callerSock: string;

  @Column({ name: 'callee_sock', length: 64 })
  calleeSock: string;

  @Column()
  state: number;

  @Column({ name: 'start_time' })
  startTime: number;

  @Column({ default: 0 })
  timeout: number;

  @Column({ default: 0 })
  sflags: number;

  @Column({ name: 'toroute_name', length: 32, nullable: true })
  torouteName: string;

  @Column({ name: 'req_uri', length: 255 })
  reqUri: string;
}
