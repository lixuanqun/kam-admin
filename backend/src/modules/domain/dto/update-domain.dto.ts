import { IsString, IsOptional, MaxLength } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateDomainDto {
  @ApiPropertyOptional({ description: 'DID' })
  @IsOptional()
  @IsString()
  @MaxLength(64)
  did?: string;
}
