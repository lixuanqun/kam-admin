import { Controller, Get, Post, Body, Delete, Param, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { CarrierrouteService } from './carrierroute.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('运营商路由')
@Controller('api/carrierroute')
export class CarrierrouteController {
  constructor(private readonly carrierrouteService: CarrierrouteService) {}

  // Carriers
  @Get('carriers')
  @ApiOperation({ summary: '获取运营商列表' })
  async findAllCarriers() {
    return ApiResponseDto.success(await this.carrierrouteService.findAllCarriers());
  }

  @Post('carriers')
  async createCarrier(@Body() body: { carrier: string }) {
    return ApiResponseDto.success(await this.carrierrouteService.createCarrier(body.carrier), '创建成功');
  }

  @Delete('carriers/:id')
  async deleteCarrier(@Param('id', ParseIntPipe) id: number) {
    await this.carrierrouteService.deleteCarrier(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Domains
  @Get('domains')
  @ApiOperation({ summary: '获取域列表' })
  async findAllDomains() {
    return ApiResponseDto.success(await this.carrierrouteService.findAllDomains());
  }

  @Post('domains')
  async createDomain(@Body() body: { domain: string }) {
    return ApiResponseDto.success(await this.carrierrouteService.createDomain(body.domain), '创建成功');
  }

  @Delete('domains/:id')
  async deleteDomain(@Param('id', ParseIntPipe) id: number) {
    await this.carrierrouteService.deleteDomain(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // Failure Routes
  @Get('failure-routes')
  @ApiOperation({ summary: '获取失败路由列表' })
  async findAllFailureRoutes(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.carrierrouteService.findAllFailureRoutes(dto));
  }

  @Post('failure-routes')
  async createFailureRoute(@Body() data: any) {
    return ApiResponseDto.success(await this.carrierrouteService.createFailureRoute(data), '创建成功');
  }

  @Delete('failure-routes/:id')
  async deleteFailureRoute(@Param('id', ParseIntPipe) id: number) {
    await this.carrierrouteService.deleteFailureRoute(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  // RPC
  @Post('reload')
  @ApiOperation({ summary: '重载路由' })
  async reload() {
    await this.carrierrouteService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('dump')
  @ApiOperation({ summary: '导出路由' })
  async dumpRoutes() {
    return ApiResponseDto.success(await this.carrierrouteService.dumpRoutes());
  }
}
