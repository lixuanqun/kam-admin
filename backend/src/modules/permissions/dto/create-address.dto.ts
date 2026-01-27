import { IsString, IsNotEmpty, IsOptional, IsInt, Min, Max, MaxLength, IsIP } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateAddressDto {
  @ApiPropertyOptional({ description: '分组 ID', default: 1 })
  @IsOptional()
  @IsInt()
  @Min(0)
  grp?: number = 1;

  @ApiProperty({ description: 'IP 地址', example: '192.168.1.100' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(50)
  ipAddr: string;

  @ApiPropertyOptional({ description: '掩码', default: 32 })
  @IsOptional()
  @IsInt()
  @Min(0)
  @Max(128)
  mask?: number = 32;

  @ApiPropertyOptional({ description: '端口', default: 0 })
  @IsOptional()
  @IsInt()
  @Min(0)
  @Max(65535)
  port?: number = 0;

  @ApiPropertyOptional({ description: '标签' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  tag?: string;
}
