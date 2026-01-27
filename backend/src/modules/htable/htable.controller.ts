import { Controller, Get, Post, Body, Patch, Param, Delete, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { HtableService } from './htable.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('哈希表')
@Controller('api/htable')
export class HtableController {
  constructor(private readonly htableService: HtableService) {}

  @Get()
  @ApiOperation({ summary: '获取哈希表记录（数据库）' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.htableService.findAll(dto));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.htableService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.htableService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.htableService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // RPC 操作
  @Get('rpc/get')
  @ApiOperation({ summary: '获取内存值' })
  async getValue(@Query('table') table: string, @Query('key') key: string) {
    return ApiResponseDto.success(await this.htableService.getValue(table, key));
  }

  @Post('rpc/sets')
  @ApiOperation({ summary: '设置字符串值' })
  async setString(@Body() body: { table: string; key: string; value: string }) {
    await this.htableService.setString(body.table, body.key, body.value);
    return ApiResponseDto.success(null, '设置成功');
  }

  @Post('rpc/seti')
  @ApiOperation({ summary: '设置整数值' })
  async setInt(@Body() body: { table: string; key: string; value: number }) {
    await this.htableService.setInt(body.table, body.key, body.value);
    return ApiResponseDto.success(null, '设置成功');
  }

  @Post('rpc/delete')
  @ApiOperation({ summary: '删除键' })
  async deleteKey(@Body() body: { table: string; key: string }) {
    await this.htableService.deleteKey(body.table, body.key);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Get('rpc/dump')
  @ApiOperation({ summary: '导出表' })
  async dump(@Query('table') table: string) {
    return ApiResponseDto.success(await this.htableService.dump(table));
  }

  @Post('rpc/reload')
  @ApiOperation({ summary: '重载表' })
  async reload(@Body() body: { table: string }) {
    await this.htableService.reload(body.table);
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('rpc/tables')
  @ApiOperation({ summary: '列出所有表' })
  async listTables() {
    return ApiResponseDto.success(await this.htableService.listTables());
  }
}
