import { IsString, IsNotEmpty, IsOptional, IsInt, Min, MaxLength } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateDispatcherDto {
  @ApiProperty({ description: '调度组 ID', example: 1 })
  @IsInt()
  @Min(0)
  setid: number;

  @ApiProperty({ description: '目标地址', example: 'sip:192.168.1.100:5060' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(256)
  destination: string;

  @ApiPropertyOptional({ description: '标志位', default: 0 })
  @IsOptional()
  @IsInt()
  flags?: number = 0;

  @ApiPropertyOptional({ description: '优先级', default: 0 })
  @IsOptional()
  @IsInt()
  priority?: number = 0;

  @ApiPropertyOptional({ description: '属性' })
  @IsOptional()
  @IsString()
  @MaxLength(128)
  attrs?: string = '';

  @ApiPropertyOptional({ description: '描述' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  description?: string = '';
}
