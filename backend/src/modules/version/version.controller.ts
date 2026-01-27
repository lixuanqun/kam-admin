import { Controller, Get, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { VersionService } from './version.service';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('数据库版本')
@Controller('api/version')
export class VersionController {
  constructor(private readonly versionService: VersionService) {}

  @Get()
  @ApiOperation({ summary: '获取所有表版本' })
  async findAll() {
    return ApiResponseDto.success(await this.versionService.findAll());
  }

  @Get('table')
  @ApiOperation({ summary: '获取指定表版本' })
  async getVersion(@Query('name') name: string) {
    return ApiResponseDto.success({ version: await this.versionService.getVersion(name) });
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async getStats() {
    return ApiResponseDto.success(await this.versionService.getStats());
  }
}
