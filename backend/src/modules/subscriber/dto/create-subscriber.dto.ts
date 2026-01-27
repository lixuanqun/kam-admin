import { IsString, IsNotEmpty, IsOptional, IsEmail, MinLength, MaxLength } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateSubscriberDto {
  @ApiProperty({ description: '用户名', example: 'user001' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(64)
  username: string;

  @ApiProperty({ description: '域', example: 'sip.example.com' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(64)
  domain: string;

  @ApiProperty({ description: '密码', example: 'password123' })
  @IsString()
  @IsNotEmpty()
  @MinLength(6)
  @MaxLength(64)
  password: string;

  @ApiPropertyOptional({ description: '邮箱地址', example: 'user@example.com' })
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
