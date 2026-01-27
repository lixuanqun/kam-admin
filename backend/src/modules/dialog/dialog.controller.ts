import { Controller, Get, Post, Body, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { DialogService } from './dialog.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('对话管理')
@Controller('api/dialogs')
export class DialogController {
  constructor(private readonly dialogService: DialogService) {}

  @Get()
  @ApiOperation({ summary: '获取对话列表（数据库）' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.dialogService.findAll(dto));
  }

  @Get('active')
  @ApiOperation({ summary: '获取活动对话（内存）' })
  async getActiveDialogs() {
    return ApiResponseDto.success(await this.dialogService.getActiveDialogs());
  }

  @Get('stats')
  @ApiOperation({ summary: '获取对话统计' })
  async getStats() {
    return ApiResponseDto.success(await this.dialogService.getStats());
  }

  @Post('end')
  @ApiOperation({ summary: '结束对话' })
  async endDialog(@Body() body: { hashEntry: number; hashId: number }) {
    await this.dialogService.endDialog(body.hashEntry, body.hashId);
    return ApiResponseDto.success(null, '对话已结束');
  }
}
