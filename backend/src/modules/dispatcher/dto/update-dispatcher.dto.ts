import { IsString, IsOptional, IsInt, Min, MaxLength } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateDispatcherDto {
  @ApiPropertyOptional({ description: '调度组 ID' })
  @IsOptional()
  @IsInt()
  @Min(0)
  setid?: number;

  @ApiPropertyOptional({ description: '目标地址' })
  @IsOptional()
  @IsString()
  @MaxLength(256)
  destination?: string;

  @ApiPropertyOptional({ description: '标志位' })
  @IsOptional()
  @IsInt()
  flags?: number;

  @ApiPropertyOptional({ description: '优先级' })
  @IsOptional()
  @IsInt()
  priority?: number;

  @ApiPropertyOptional({ description: '属性' })
  @IsOptional()
  @IsString()
  @MaxLength(128)
  attrs?: string;

  @ApiPropertyOptional({ description: '描述' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  description?: string;
}
