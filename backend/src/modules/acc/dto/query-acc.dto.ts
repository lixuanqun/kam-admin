import { IsOptional, IsString, IsDateString } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';
import { PaginationDto } from '../../../common/dto/pagination.dto';

export class QueryAccDto extends PaginationDto {
  @ApiPropertyOptional({ description: '来源用户' })
  @IsOptional()
  @IsString()
  srcUser?: string;

  @ApiPropertyOptional({ description: '目标用户' })
  @IsOptional()
  @IsString()
  dstUser?: string;

  @ApiPropertyOptional({ description: 'Call ID' })
  @IsOptional()
  @IsString()
  callid?: string;

  @ApiPropertyOptional({ description: '开始时间' })
  @IsOptional()
  @IsDateString()
  startTime?: string;

  @ApiPropertyOptional({ description: '结束时间' })
  @IsOptional()
  @IsDateString()
  endTime?: string;

  @ApiPropertyOptional({ description: 'SIP 响应码' })
  @IsOptional()
  @IsString()
  sipCode?: string;
}
