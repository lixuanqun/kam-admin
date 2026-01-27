import { Controller, Get, Post, Query, Body } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { ToposService } from './topos.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('Topos 拓扑隐藏')
@Controller('api/topos')
export class ToposController {
  constructor(private readonly toposService: ToposService) {}

  @Get('dialogs')
  @ApiOperation({ summary: '获取 Topos 对话列表' })
  async findAllDialogs(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.toposService.findAllDialogs(dto));
  }

  @Post('cleanup')
  @ApiOperation({ summary: '清理旧记录' })
  async cleanup(@Body() body: { days: number }) {
    const beforeDate = new Date();
    beforeDate.setDate(beforeDate.getDate() - (body.days || 7));
    const count = await this.toposService.cleanup(beforeDate);
    return ApiResponseDto.success({ deleted: count }, `清理了 ${count} 条记录`);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async getStats() {
    return ApiResponseDto.success(await this.toposService.getStats());
  }
}
