import { IsString, IsNotEmpty, IsOptional, MaxLength } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateDomainDto {
  @ApiProperty({ description: '域名', example: 'sip.example.com' })
  @IsString()
  @IsNotEmpty()
  @MaxLength(64)
  domain: string;

  @ApiPropertyOptional({ description: 'DID' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  did?: string;
}
