import { Controller, Get, Post, Body, Patch, Param, Delete, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { LcrService } from './lcr.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('LCR 路由')
@Controller('api/lcr')
export class LcrController {
  constructor(private readonly lcrService: LcrService) {}

  // Gateway
  @Get('gateways')
  @ApiOperation({ summary: '获取网关列表' })
  async findAllGateways(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.lcrService.findAllGateways(dto));
  }

  @Post('gateways')
  async createGateway(@Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.createGateway(data), '创建成功');
  }

  @Patch('gateways/:id')
  async updateGateway(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.updateGateway(id, data), '更新成功');
  }

  @Delete('gateways/:id')
  async removeGateway(@Param('id', ParseIntPipe) id: number) {
    await this.lcrService.removeGateway(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Rule
  @Get('rules')
  @ApiOperation({ summary: '获取规则列表' })
  async findAllRules(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.lcrService.findAllRules(dto));
  }

  @Post('rules')
  async createRule(@Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.createRule(data), '创建成功');
  }

  @Patch('rules/:id')
  async updateRule(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.updateRule(id, data), '更新成功');
  }

  @Delete('rules/:id')
  async removeRule(@Param('id', ParseIntPipe) id: number) {
    await this.lcrService.removeRule(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Target
  @Get('targets')
  @ApiOperation({ summary: '获取目标列表' })
  async findAllTargets(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.lcrService.findAllTargets(dto));
  }

  @Post('targets')
  async createTarget(@Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.createTarget(data), '创建成功');
  }

  @Patch('targets/:id')
  async updateTarget(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.lcrService.updateTarget(id, data), '更新成功');
  }

  @Delete('targets/:id')
  async removeTarget(@Param('id', ParseIntPipe) id: number) {
    await this.lcrService.removeTarget(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // RPC
  @Post('reload')
  @ApiOperation({ summary: '重载 LCR' })
  async reload() {
    await this.lcrService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('dump-gws')
  @ApiOperation({ summary: '导出网关' })
  async dumpGws() {
    return ApiResponseDto.success(await this.lcrService.dumpGws());
  }

  @Get('dump-rules')
  @ApiOperation({ summary: '导出规则' })
  async dumpRules() {
    return ApiResponseDto.success(await this.lcrService.dumpRules());
  }
}
