import { Controller, Get, Post, Patch, Delete, Body, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { PdtService } from './pdt.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('前缀域转换')
@Controller('api/pdt')
export class PdtController {
  constructor(private readonly pdtService: PdtService) {}

  @Get()
  @ApiOperation({ summary: '获取 PDT 列表' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.pdtService.findAll(dto));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.pdtService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.pdtService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.pdtService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载 PDT' })
  async reload() {
    await this.pdtService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('list')
  @ApiOperation({ summary: '获取内存列表' })
  async list() {
    return ApiResponseDto.success(await this.pdtService.list());
  }
}
