import { Controller, Get, Post, Patch, Delete, Body, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { DialplanService } from './dialplan.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('拨号计划')
@Controller('api/dialplan')
export class DialplanController {
  constructor(private readonly dialplanService: DialplanService) {}

  @Get()
  @ApiOperation({ summary: '获取拨号规则列表' })
  async findAll(@Query() dto: PaginationDto & { dpid?: number }) {
    return ApiResponseDto.success(await this.dialplanService.findAll(dto));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.dialplanService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.dialplanService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.dialplanService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载拨号计划' })
  async reload() {
    await this.dialplanService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('translate')
  @ApiOperation({ summary: '测试翻译' })
  async translate(@Query('dpid') dpid: number, @Query('input') input: string) {
    return ApiResponseDto.success(await this.dialplanService.translate(dpid, input));
  }

  @Get('dump')
  @ApiOperation({ summary: '导出规则' })
  async dump(@Query('dpid') dpid?: number) {
    return ApiResponseDto.success(await this.dialplanService.dump(dpid));
  }
}
