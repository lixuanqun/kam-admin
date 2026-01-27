import { IsString, IsNotEmpty, IsOptional, IsInt, MaxLength } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateTrustedDto {
  @ApiProperty({ description: '源 IP', example: '192.168.1.100' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(50)
  srcIp: string;

  @ApiPropertyOptional({ description: '协议', example: 'any' })
  @IsOptional()
  @IsString()
  @MaxLength(4)
  proto?: string;

  @ApiPropertyOptional({ description: 'From 匹配模式' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  fromPattern?: string;

  @ApiPropertyOptional({ description: 'RU ID 匹配模式' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  ruri_pattern?: string;

  @ApiPropertyOptional({ description: '标签' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  tag?: string;

  @ApiPropertyOptional({ description: '优先级', default: 0 })
  @IsOptional()
  @IsInt()
  priority?: number = 0;
}
