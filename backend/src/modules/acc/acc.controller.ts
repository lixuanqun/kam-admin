import { Controller, Get, Query } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { AccService } from './acc.service';
import { QueryAccDto } from './dto/query-acc.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('计费记录')
@Controller('api/acc')
export class AccController {
  constructor(private readonly accService: AccService) {}

  @Get('cdr')
  @ApiOperation({ summary: '获取 CDR 记录' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAllAcc(@Query() queryDto: QueryAccDto): Promise<ApiResponseDto> {
    const result = await this.accService.findAllAcc(queryDto);
    return ApiResponseDto.success(result);
  }

  @Get('missed-calls')
  @ApiOperation({ summary: '获取未接来电' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAllMissedCalls(@Query() queryDto: QueryAccDto): Promise<ApiResponseDto> {
    const result = await this.accService.findAllMissedCalls(queryDto);
    return ApiResponseDto.success(result);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStats(
    @Query('startTime') startTime?: string,
    @Query('endTime') endTime?: string,
  ): Promise<ApiResponseDto> {
    const stats = await this.accService.getStats(startTime, endTime);
    return ApiResponseDto.success(stats);
  }

  @Get('today')
  @ApiOperation({ summary: '获取今日统计' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getTodayStats(): Promise<ApiResponseDto> {
    const stats = await this.accService.getTodayStats();
    return ApiResponseDto.success(stats);
  }
}
