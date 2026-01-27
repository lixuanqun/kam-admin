import { Controller, Get, Post, Patch, Delete, Body, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { SecfilterService } from './secfilter.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('安全过滤')
@Controller('api/secfilter')
export class SecfilterController {
  constructor(private readonly secfilterService: SecfilterService) {}

  @Get()
  @ApiOperation({ summary: '获取安全过滤规则列表' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.secfilterService.findAll(dto));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.secfilterService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.secfilterService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.secfilterService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载安全过滤' })
  async reload() {
    await this.secfilterService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('print')
  @ApiOperation({ summary: '打印规则' })
  async print() {
    return ApiResponseDto.success(await this.secfilterService.print());
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async stats() {
    return ApiResponseDto.success(await this.secfilterService.stats());
  }

  @Post('stats/reset')
  @ApiOperation({ summary: '重置统计' })
  async statsReset() {
    await this.secfilterService.statsReset();
    return ApiResponseDto.success(null, '重置成功');
  }

  @Post('add-bl')
  @ApiOperation({ summary: '添加黑名单' })
  async addBlacklist(@Body() body: { type: number; data: string }) {
    await this.secfilterService.addBlacklist(body.type, body.data);
    return ApiResponseDto.success(null, '添加成功');
  }

  @Post('add-wl')
  @ApiOperation({ summary: '添加白名单' })
  async addWhitelist(@Body() body: { type: number; data: string }) {
    await this.secfilterService.addWhitelist(body.type, body.data);
    return ApiResponseDto.success(null, '添加成功');
  }
}
