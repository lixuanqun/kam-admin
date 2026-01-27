import { Controller, Get, Post, Patch, Delete, Body, Query, Param, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { UsrpreferencesService } from './usrpreferences.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('用户偏好设置')
@Controller('api/usrpreferences')
export class UsrpreferencesController {
  constructor(private readonly usrpreferencesService: UsrpreferencesService) {}

  @Get()
  @ApiOperation({ summary: '获取用户偏好列表' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.usrpreferencesService.findAll(dto));
  }

  @Get('user')
  @ApiOperation({ summary: '获取指定用户偏好' })
  async findByUser(@Query('username') username: string, @Query('domain') domain: string) {
    return ApiResponseDto.success(await this.usrpreferencesService.findByUser(username, domain));
  }

  @Post()
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.usrpreferencesService.create(data), '创建成功');
  }

  @Patch(':id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.usrpreferencesService.update(id, data), '更新成功');
  }

  @Delete(':id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.usrpreferencesService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }
}
