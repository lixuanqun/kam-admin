import { Controller, Get, Post, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { PresenceService } from './presence.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('存在服务')
@Controller('api/presence')
export class PresenceController {
  constructor(private readonly presenceService: PresenceService) {}

  @Get('presentities')
  @ApiOperation({ summary: '获取 Presentity 列表' })
  async findAllPresentities(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.presenceService.findAllPresentities(dto));
  }

  @Get('watchers')
  @ApiOperation({ summary: '获取活动 Watcher 列表' })
  async findAllWatchers(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.presenceService.findAllWatchers(dto));
  }

  @Post('cleanup')
  @ApiOperation({ summary: '清理过期记录' })
  async cleanExpired() {
    await this.presenceService.cleanExpired();
    return ApiResponseDto.success(null, '清理成功');
  }

  @Post('refresh-watchers')
  @ApiOperation({ summary: '刷新 Watchers' })
  async refreshWatchers(@Body() body: { presentityUri: string; event: string }) {
    await this.presenceService.refreshWatchers(body.presentityUri, body.event);
    return ApiResponseDto.success(null, '刷新成功');
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async getStats() {
    return ApiResponseDto.success(await this.presenceService.getStats());
  }
}
