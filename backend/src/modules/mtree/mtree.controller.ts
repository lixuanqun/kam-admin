import { Controller, Get, Post, Patch, Delete, Body, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { MtreeService } from './mtree.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('内存树')
@Controller('api/mtree')
export class MtreeController {
  constructor(private readonly mtreeService: MtreeService) {}

  @Get()
  @ApiOperation({ summary: '获取 mtree 列表' })
  async findAll(@Query() dto: PaginationDto & { tname?: string }) {
    return ApiResponseDto.success(await this.mtreeService.findAll(dto));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.mtreeService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.mtreeService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.mtreeService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载 mtree' })
  async reload(@Body() body: { tname: string }) {
    await this.mtreeService.reload(body.tname);
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('match')
  @ApiOperation({ summary: '匹配测试' })
  async match(@Query('tname') tname: string, @Query('prefix') prefix: string) {
    return ApiResponseDto.success(await this.mtreeService.match(tname, prefix));
  }

  @Get('summary')
  @ApiOperation({ summary: '获取摘要' })
  async summary(@Query('tname') tname?: string) {
    return ApiResponseDto.success(await this.mtreeService.summary(tname));
  }
}
