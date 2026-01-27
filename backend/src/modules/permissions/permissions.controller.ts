import {
  Controller,
  Get,
  Post,
  Body,
  Patch,
  Param,
  Delete,
  Query,
  ParseIntPipe,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { PermissionsService } from './permissions.service';
import { CreateAddressDto } from './dto/create-address.dto';
import { CreateTrustedDto } from './dto/create-trusted.dto';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('权限管理')
@Controller('api/permissions')
export class PermissionsController {
  constructor(private readonly permissionsService: PermissionsService) {}

  // ========== Address 接口 ==========

  @Post('address')
  @ApiOperation({ summary: '创建地址白名单' })
  async createAddress(@Body() createDto: CreateAddressDto): Promise<ApiResponseDto> {
    const address = await this.permissionsService.createAddress(createDto);
    return ApiResponseDto.success(address, '创建成功');
  }

  @Get('address')
  @ApiOperation({ summary: '获取地址白名单列表' })
  async findAllAddresses(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.permissionsService.findAllAddresses(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('address/:id')
  @ApiOperation({ summary: '获取地址详情' })
  async findOneAddress(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    const address = await this.permissionsService.findOneAddress(id);
    return ApiResponseDto.success(address);
  }

  @Patch('address/:id')
  @ApiOperation({ summary: '更新地址' })
  async updateAddress(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateDto: Partial<CreateAddressDto>,
  ): Promise<ApiResponseDto> {
    const address = await this.permissionsService.updateAddress(id, updateDto);
    return ApiResponseDto.success(address, '更新成功');
  }

  @Delete('address/:id')
  @ApiOperation({ summary: '删除地址' })
  async removeAddress(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.permissionsService.removeAddress(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== Trusted 接口 ==========

  @Post('trusted')
  @ApiOperation({ summary: '创建信任地址' })
  async createTrusted(@Body() createDto: CreateTrustedDto): Promise<ApiResponseDto> {
    const trusted = await this.permissionsService.createTrusted(createDto);
    return ApiResponseDto.success(trusted, '创建成功');
  }

  @Get('trusted')
  @ApiOperation({ summary: '获取信任地址列表' })
  async findAllTrusted(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.permissionsService.findAllTrusted(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('trusted/:id')
  @ApiOperation({ summary: '获取信任地址详情' })
  async findOneTrusted(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    const trusted = await this.permissionsService.findOneTrusted(id);
    return ApiResponseDto.success(trusted);
  }

  @Patch('trusted/:id')
  @ApiOperation({ summary: '更新信任地址' })
  async updateTrusted(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateDto: Partial<CreateTrustedDto>,
  ): Promise<ApiResponseDto> {
    const trusted = await this.permissionsService.updateTrusted(id, updateDto);
    return ApiResponseDto.success(trusted, '更新成功');
  }

  @Delete('trusted/:id')
  @ApiOperation({ summary: '删除信任地址' })
  async removeTrusted(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.permissionsService.removeTrusted(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== 通用接口 ==========

  @Post('reload')
  @ApiOperation({ summary: '重载权限配置' })
  async reload(): Promise<ApiResponseDto> {
    await this.permissionsService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('stats')
  @ApiOperation({ summary: '获取权限统计信息' })
  async getStats(): Promise<ApiResponseDto> {
    const stats = await this.permissionsService.getStats();
    return ApiResponseDto.success(stats);
  }
}
