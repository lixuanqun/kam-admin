import { Controller, Get, Query } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { MonitoringService } from './monitoring.service';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('监控统计')
@Controller('api/monitoring')
export class MonitoringController {
  constructor(private readonly monitoringService: MonitoringService) {}

  @Get('health')
  @ApiOperation({ summary: '检查 Kamailio 服务状态' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async checkHealth(): Promise<ApiResponseDto> {
    const health = await this.monitoringService.checkHealth();
    return ApiResponseDto.success(health);
  }

  @Get('dashboard')
  @ApiOperation({ summary: '获取仪表盘数据' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getDashboardData(): Promise<ApiResponseDto> {
    const data = await this.monitoringService.getDashboardData();
    return ApiResponseDto.success(data);
  }

  @Get('statistics')
  @ApiOperation({ summary: '获取 Kamailio 统计信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStatistics(@Query('group') group?: string): Promise<ApiResponseDto> {
    const stats = await this.monitoringService.getStatistics(group);
    return ApiResponseDto.success(stats);
  }

  @Get('core-info')
  @ApiOperation({ summary: '获取核心信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getCoreInfo(): Promise<ApiResponseDto> {
    const info = await this.monitoringService.getCoreInfo();
    return ApiResponseDto.success(info);
  }

  @Get('dialogs')
  @ApiOperation({ summary: '获取活动对话' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getActiveDialogs(): Promise<ApiResponseDto> {
    const dialogs = await this.monitoringService.getActiveDialogs();
    return ApiResponseDto.success(dialogs);
  }

  @Get('overview')
  @ApiOperation({ summary: '获取系统概览' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getSystemOverview(): Promise<ApiResponseDto> {
    const overview = await this.monitoringService.getSystemOverview();
    return ApiResponseDto.success(overview);
  }
}
