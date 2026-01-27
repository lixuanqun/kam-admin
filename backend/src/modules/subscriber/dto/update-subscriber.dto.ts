import { IsString, IsOptional, IsEmail, MinLength, MaxLength } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateSubscriberDto {
  @ApiPropertyOptional({ description: '密码' })
  @IsOptional()
  @IsString()
  @MinLength(6)
  @MaxLength(64)
  password?: string;

  @ApiPropertyOptional({ description: '邮箱地址' })
  @IsOptional()
  @IsEmail()
  @MaxLength(64)
  emailAddress?: string;

  @ApiPropertyOptional({ description: 'RP ID' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  rpid?: string;
}
