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

  @Get('detail')
  @ApiOperation({ summary: '获取对话详情 (RPC dlg.dlg_list)' })
  async getDialogDetail(@Query('hashEntry') hashEntry: number, @Query('hashId') hashId: number) {
    return ApiResponseDto.success(await this.dialogService.getDialogDetail(hashEntry, hashId));
  }

  @Post('bridge')
  @ApiOperation({ summary: '桥接/转接对话 (RPC dlg.bridge_dlg)' })
  async bridgeDialog(@Body() body: { from: string; to: string }) {
    await this.dialogService.bridgeDialog(body.from, body.to);
    return ApiResponseDto.success(null, '桥接成功');
  }
}
