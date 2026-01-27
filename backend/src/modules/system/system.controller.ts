import { Controller, Get, Post, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { SystemService } from './system.service';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('系统管理')
@Controller('api/system')
export class SystemController {
  constructor(private readonly systemService: SystemService) {}

  // 核心信息
  @Get('info')
  @ApiOperation({ summary: '获取核心信息' })
  async getCoreInfo() {
    return ApiResponseDto.success(await this.systemService.getCoreInfo());
  }

  @Get('version')
  @ApiOperation({ summary: '获取版本' })
  async getVersion() {
    return ApiResponseDto.success(await this.systemService.getVersion());
  }

  @Get('uptime')
  @ApiOperation({ summary: '获取运行时间' })
  async getUptime() {
    return ApiResponseDto.success(await this.systemService.getUptime());
  }

  @Get('memory')
  @ApiOperation({ summary: '获取内存信息' })
  async getSharedMemory() {
    return ApiResponseDto.success(await this.systemService.getSharedMemory());
  }

  @Get('processes')
  @ApiOperation({ summary: '获取进程列表' })
  async getProcessList() {
    return ApiResponseDto.success(await this.systemService.getProcessList());
  }

  @Get('status')
  @ApiOperation({ summary: '获取系统状态' })
  async getSystemStatus() {
    return ApiResponseDto.success(await this.systemService.getSystemStatus());
  }

  @Get('modules')
  @ApiOperation({ summary: '获取模块列表' })
  async getModulesList() {
    return ApiResponseDto.success(await this.systemService.getModulesList());
  }

  // 配置管理
  @Get('config')
  @ApiOperation({ summary: '获取配置值' })
  async getConfig(@Query('group') group: string, @Query('name') name: string) {
    return ApiResponseDto.success(await this.systemService.getConfig(group, name));
  }

  @Post('config')
  @ApiOperation({ summary: '设置配置值' })
  async setConfig(@Body() body: { group: string; name: string; value: any }) {
    await this.systemService.setConfigNow(body.group, body.name, body.value);
    return ApiResponseDto.success(null, '设置成功');
  }

  @Get('config/list')
  @ApiOperation({ summary: '列出所有配置' })
  async listConfig() {
    return ApiResponseDto.success(await this.systemService.listConfig());
  }

  @Get('config/diff')
  @ApiOperation({ summary: '配置差异' })
  async diffConfig() {
    return ApiResponseDto.success(await this.systemService.diffConfig());
  }

  // TLS
  @Get('tls/list')
  @ApiOperation({ summary: '获取 TLS 连接列表' })
  async getTlsList() {
    return ApiResponseDto.success(await this.systemService.getTlsList());
  }

  @Get('tls/info')
  @ApiOperation({ summary: '获取 TLS 信息' })
  async getTlsInfo() {
    return ApiResponseDto.success(await this.systemService.getTlsInfo());
  }

  @Post('tls/reload')
  @ApiOperation({ summary: '重载 TLS' })
  async reloadTls() {
    await this.systemService.reloadTls();
    return ApiResponseDto.success(null, '重载成功');
  }

  // Pike
  @Get('pike/list')
  @ApiOperation({ summary: '获取 Pike 封禁列表' })
  async getPikeList() {
    return ApiResponseDto.success(await this.systemService.getPikeList());
  }

  @Get('pike/top')
  @ApiOperation({ summary: '获取 Pike 排行' })
  async getPikeTop(@Query('limit') limit?: number) {
    return ApiResponseDto.success(await this.systemService.getPikeTop(limit || 10));
  }

  // 统计
  @Get('statistics')
  @ApiOperation({ summary: '获取统计信息' })
  async getStatistics(@Query('group') group?: string) {
    return ApiResponseDto.success(await this.systemService.getStatistics(group));
  }

  @Post('statistics/reset')
  @ApiOperation({ summary: '重置统计' })
  async resetStatistics(@Body() body: { name: string }) {
    await this.systemService.resetStatistics(body.name);
    return ApiResponseDto.success(null, '重置成功');
  }
}
