import { Controller, Get, Post, Body, Patch, Param, Delete, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { DroutingService } from './drouting.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('动态路由')
@Controller('api/drouting')
export class DroutingController {
  constructor(private readonly droutingService: DroutingService) {}

  // ========== Gateway ==========
  @Get('gateways')
  @ApiOperation({ summary: '获取网关列表' })
  async findAllGateways(@Query() dto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.droutingService.findAllGateways(dto);
    return ApiResponseDto.success(result);
  }

  @Post('gateways')
  @ApiOperation({ summary: '创建网关' })
  async createGateway(@Body() data: any): Promise<ApiResponseDto> {
    const gateway = await this.droutingService.createGateway(data);
    return ApiResponseDto.success(gateway, '创建成功');
  }

  @Patch('gateways/:id')
  @ApiOperation({ summary: '更新网关' })
  async updateGateway(@Param('id', ParseIntPipe) id: number, @Body() data: any): Promise<ApiResponseDto> {
    const gateway = await this.droutingService.updateGateway(id, data);
    return ApiResponseDto.success(gateway, '更新成功');
  }

  @Delete('gateways/:id')
  @ApiOperation({ summary: '删除网关' })
  async removeGateway(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.droutingService.removeGateway(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== Rule ==========
  @Get('rules')
  @ApiOperation({ summary: '获取规则列表' })
  async findAllRules(@Query() dto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.droutingService.findAllRules(dto);
    return ApiResponseDto.success(result);
  }

  @Post('rules')
  @ApiOperation({ summary: '创建规则' })
  async createRule(@Body() data: any): Promise<ApiResponseDto> {
    const rule = await this.droutingService.createRule(data);
    return ApiResponseDto.success(rule, '创建成功');
  }

  @Patch('rules/:id')
  @ApiOperation({ summary: '更新规则' })
  async updateRule(@Param('id', ParseIntPipe) id: number, @Body() data: any): Promise<ApiResponseDto> {
    const rule = await this.droutingService.updateRule(id, data);
    return ApiResponseDto.success(rule, '更新成功');
  }

  @Delete('rules/:id')
  @ApiOperation({ summary: '删除规则' })
  async removeRule(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.droutingService.removeRule(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== Group ==========
  @Get('groups')
  @ApiOperation({ summary: '获取分组列表' })
  async findAllGroups(@Query() dto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.droutingService.findAllGroups(dto);
    return ApiResponseDto.success(result);
  }

  @Post('groups')
  @ApiOperation({ summary: '创建分组' })
  async createGroup(@Body() data: any): Promise<ApiResponseDto> {
    const group = await this.droutingService.createGroup(data);
    return ApiResponseDto.success(group, '创建成功');
  }

  @Patch('groups/:id')
  @ApiOperation({ summary: '更新分组' })
  async updateGroup(@Param('id', ParseIntPipe) id: number, @Body() data: any): Promise<ApiResponseDto> {
    const group = await this.droutingService.updateGroup(id, data);
    return ApiResponseDto.success(group, '更新成功');
  }

  @Delete('groups/:id')
  @ApiOperation({ summary: '删除分组' })
  async removeGroup(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.droutingService.removeGroup(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== Carrier ==========
  @Get('carriers')
  @ApiOperation({ summary: '获取运营商列表' })
  async findAllCarriers(@Query() dto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.droutingService.findAllCarriers(dto);
    return ApiResponseDto.success(result);
  }

  @Post('carriers')
  @ApiOperation({ summary: '创建运营商' })
  async createCarrier(@Body() data: any): Promise<ApiResponseDto> {
    const carrier = await this.droutingService.createCarrier(data);
    return ApiResponseDto.success(carrier, '创建成功');
  }

  @Patch('carriers/:id')
  @ApiOperation({ summary: '更新运营商' })
  async updateCarrier(@Param('id', ParseIntPipe) id: number, @Body() data: any): Promise<ApiResponseDto> {
    const carrier = await this.droutingService.updateCarrier(id, data);
    return ApiResponseDto.success(carrier, '更新成功');
  }

  @Delete('carriers/:id')
  @ApiOperation({ summary: '删除运营商' })
  async removeCarrier(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.droutingService.removeCarrier(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // ========== RPC ==========
  @Post('reload')
  @ApiOperation({ summary: '重载动态路由' })
  async reload(): Promise<ApiResponseDto> {
    await this.droutingService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('stats')
  @ApiOperation({ summary: '获取统计信息' })
  async getStats(): Promise<ApiResponseDto> {
    const stats = await this.droutingService.getStats();
    return ApiResponseDto.success(stats);
  }

  @Get('gw-status')
  @ApiOperation({ summary: '获取网关实时状态' })
  async getGwStatus(): Promise<ApiResponseDto> {
    return ApiResponseDto.success(await this.droutingService.getGwStatus());
  }

  @Get('carrier-status')
  @ApiOperation({ summary: '获取运营商实时状态' })
  async getCarrierStatus(): Promise<ApiResponseDto> {
    return ApiResponseDto.success(await this.droutingService.getCarrierStatus());
  }
}
