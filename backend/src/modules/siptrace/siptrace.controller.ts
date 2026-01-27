import { Controller, Get, Post, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { SiptraceService, SipTraceQueryDto } from './siptrace.service';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('SIP 跟踪')
@Controller('api/siptrace')
export class SiptraceController {
  constructor(private readonly siptraceService: SiptraceService) {}

  @Get()
  @ApiOperation({ summary: '获取 SIP 跟踪记录' })
  async findAll(@Query() dto: SipTraceQueryDto) {
    return ApiResponseDto.success(await this.siptraceService.findAll(dto));
  }

  @Get('call')
  @ApiOperation({ summary: '获取呼叫的完整跟踪' })
  async getByCallId(@Query('callid') callid: string) {
    return ApiResponseDto.success(await this.siptraceService.getByCallId(callid));
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async getStats() {
    return ApiResponseDto.success(await this.siptraceService.getStats());
  }

  @Post('cleanup')
  @ApiOperation({ summary: '清理旧记录' })
  async cleanup(@Body() body: { days: number }) {
    const beforeDate = new Date();
    beforeDate.setDate(beforeDate.getDate() - (body.days || 30));
    const count = await this.siptraceService.cleanup(beforeDate);
    return ApiResponseDto.success({ deleted: count }, `清理了 ${count} 条记录`);
  }
}
