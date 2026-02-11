import {
  Controller,
  Get,
  Delete,
  Query,
  Param,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { LocationService } from './location.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('注册位置管理')
@Controller('api/locations')
export class LocationController {
  constructor(private readonly locationService: LocationService) {}

  @Get()
  @ApiOperation({ summary: '获取注册位置列表' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAll(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.locationService.findAll(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('memory')
  @ApiOperation({ summary: '从内存获取注册位置' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getFromMemory(@Query('table') table?: string): Promise<ApiResponseDto> {
    const result = await this.locationService.getFromMemory(table);
    return ApiResponseDto.success(result);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取注册统计信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStats(): Promise<ApiResponseDto> {
    const stats = await this.locationService.getStats();
    return ApiResponseDto.success(stats);
  }

  @Get('online-count')
  @ApiOperation({ summary: '获取在线用户数量' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getOnlineCount(): Promise<ApiResponseDto<number>> {
    const count = await this.locationService.getOnlineCount();
    return ApiResponseDto.success(count);
  }

  @Get('user/:username')
  @ApiOperation({ summary: '根据用户名查询注册' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findByUsername(@Param('username') username: string): Promise<ApiResponseDto> {
    const locations = await this.locationService.findByUsername(username);
    return ApiResponseDto.success(locations);
  }

  @Delete(':username/:domain')
  @ApiOperation({ summary: '删除用户注册' })
  @ApiResponse({ status: 200, description: '删除成功' })
  async deleteUserLocation(
    @Param('username') username: string,
    @Param('domain') domain: string,
  ): Promise<ApiResponseDto> {
    await this.locationService.deleteUserLocation(username, domain);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Get('lookup')
  @ApiOperation({ summary: '精确查找指定用户注册 (RPC ul.lookup)' })
  async lookupUser(
    @Query('table') table: string,
    @Query('aor') aor: string,
  ): Promise<ApiResponseDto> {
    return ApiResponseDto.success(await this.locationService.lookupUser(table || 'location', aor));
  }

  @Delete('contact')
  @ApiOperation({ summary: '删除指定联系地址 (RPC ul.rm_contact)' })
  async deleteContact(
    @Body() body: { table: string; aor: string; contact: string },
  ): Promise<ApiResponseDto> {
    await this.locationService.deleteContact(body.table || 'location', body.aor, body.contact);
    return ApiResponseDto.success(null, '删除成功');
  }
}
