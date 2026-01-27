import { Controller, Get, Post, Body, Patch, Param, Delete, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { UserdataService } from './userdata.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('用户数据')
@Controller('api/userdata')
export class UserdataController {
  constructor(private readonly userdataService: UserdataService) {}

  // Aliases
  @Get('aliases')
  @ApiOperation({ summary: '获取别名列表' })
  async findAllAliases(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.userdataService.findAllAliases(dto));
  }

  @Post('aliases')
  async createAlias(@Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.createAlias(data), '创建成功');
  }

  @Patch('aliases/:id')
  async updateAlias(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.updateAlias(id, data), '更新成功');
  }

  @Delete('aliases/:id')
  async removeAlias(@Param('id', ParseIntPipe) id: number) {
    await this.userdataService.removeAlias(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Groups
  @Get('groups')
  @ApiOperation({ summary: '获取用户组列表' })
  async findAllGroups(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.userdataService.findAllGroups(dto));
  }

  @Post('groups')
  async createGroup(@Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.createGroup(data), '创建成功');
  }

  @Patch('groups/:id')
  async updateGroup(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.updateGroup(id, data), '更新成功');
  }

  @Delete('groups/:id')
  async removeGroup(@Param('id', ParseIntPipe) id: number) {
    await this.userdataService.removeGroup(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Speed Dial
  @Get('speed-dial')
  @ApiOperation({ summary: '获取快捷拨号列表' })
  async findAllSpeedDials(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.userdataService.findAllSpeedDials(dto));
  }

  @Post('speed-dial')
  async createSpeedDial(@Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.createSpeedDial(data), '创建成功');
  }

  @Patch('speed-dial/:id')
  async updateSpeedDial(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.userdataService.updateSpeedDial(id, data), '更新成功');
  }

  @Delete('speed-dial/:id')
  async removeSpeedDial(@Param('id', ParseIntPipe) id: number) {
    await this.userdataService.removeSpeedDial(id);
    return ApiResponseDto.success(null, '删除成功');
  }
}
