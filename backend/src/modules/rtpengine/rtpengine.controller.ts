import { Controller, Get, Post, Body } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { RtpengineService } from './rtpengine.service';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('RTPEngine/NAT')
@Controller('api/rtpengine')
export class RtpengineController {
  constructor(private readonly rtpengineService: RtpengineService) {}

  @Get('show')
  @ApiOperation({ summary: '显示 RTPEngine 状态' })
  async showAll() {
    return ApiResponseDto.success(await this.rtpengineService.showAll());
  }

  @Post('reload')
  @ApiOperation({ summary: '重载 RTPEngine' })
  async reload() {
    await this.rtpengineService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Post('enable')
  @ApiOperation({ summary: '启用/禁用 RTPEngine' })
  async enable(@Body() body: { url: string; flag: number }) {
    await this.rtpengineService.enable(body.url, body.flag);
    return ApiResponseDto.success(null, '操作成功');
  }

  @Post('ping')
  @ApiOperation({ summary: 'Ping RTPEngine' })
  async ping(@Body() body: { url: string }) {
    return ApiResponseDto.success(await this.rtpengineService.ping(body.url));
  }

  @Post('nat-ping')
  @ApiOperation({ summary: '启用/禁用 NAT Ping' })
  async enableNatPing(@Body() body: { flag: number }) {
    await this.rtpengineService.enableNatPing(body.flag);
    return ApiResponseDto.success(null, '操作成功');
  }

  @Get('status')
  @ApiOperation({ summary: '获取状态' })
  async getStatus() {
    return ApiResponseDto.success(await this.rtpengineService.getStatus());
  }
}
