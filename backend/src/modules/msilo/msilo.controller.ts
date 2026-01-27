import { Controller, Get, Post, Delete, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { MsiloService } from './msilo.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('离线消息')
@Controller('api/msilo')
export class MsiloController {
  constructor(private readonly msiloService: MsiloService) {}

  @Get()
  @ApiOperation({ summary: '获取离线消息列表' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.msiloService.findAll(dto));
  }

  @Get('user')
  @ApiOperation({ summary: '获取用户的离线消息' })
  async findByUser(@Query('username') username: string, @Query('domain') domain: string) {
    return ApiResponseDto.success(await this.msiloService.findByUser(username, domain));
  }

  @Delete(':id')
  @ApiOperation({ summary: '删除离线消息' })
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.msiloService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('cleanup')
  @ApiOperation({ summary: '清理过期消息' })
  async cleanExpired() {
    const count = await this.msiloService.cleanExpired();
    return ApiResponseDto.success({ deleted: count }, `清理了 ${count} 条过期消息`);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计' })
  async getStats() {
    return ApiResponseDto.success(await this.msiloService.getStats());
  }

  @Post('dump')
  @ApiOperation({ summary: '导出消息' })
  async dump() {
    await this.msiloService.dump();
    return ApiResponseDto.success(null, '导出成功');
  }
}
