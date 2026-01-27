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
import { DomainService } from './domain.service';
import { CreateDomainDto } from './dto/create-domain.dto';
import { UpdateDomainDto } from './dto/update-domain.dto';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';
import { Domain } from './entities/domain.entity';

@ApiTags('域管理')
@Controller('api/domains')
export class DomainController {
  constructor(private readonly domainService: DomainService) {}

  @Post()
  @ApiOperation({ summary: '创建域' })
  @ApiResponse({ status: 201, description: '创建成功' })
  async create(@Body() createDto: CreateDomainDto): Promise<ApiResponseDto<Domain>> {
    const domain = await this.domainService.create(createDto);
    return ApiResponseDto.success(domain, '创建成功');
  }

  @Get()
  @ApiOperation({ summary: '获取域列表' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAll(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.domainService.findAll(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('all')
  @ApiOperation({ summary: '获取所有域（不分页）' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAllDomains(): Promise<ApiResponseDto<Domain[]>> {
    const domains = await this.domainService.findAllDomains();
    return ApiResponseDto.success(domains);
  }

  @Get(':id')
  @ApiOperation({ summary: '获取域详情' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findOne(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto<Domain>> {
    const domain = await this.domainService.findOne(id);
    return ApiResponseDto.success(domain);
  }

  @Patch(':id')
  @ApiOperation({ summary: '更新域' })
  @ApiResponse({ status: 200, description: '更新成功' })
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateDto: UpdateDomainDto,
  ): Promise<ApiResponseDto<Domain>> {
    const domain = await this.domainService.update(id, updateDto);
    return ApiResponseDto.success(domain, '更新成功');
  }

  @Delete(':id')
  @ApiOperation({ summary: '删除域' })
  @ApiResponse({ status: 200, description: '删除成功' })
  async remove(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.domainService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载域配置' })
  @ApiResponse({ status: 200, description: '重载成功' })
  async reload(): Promise<ApiResponseDto> {
    await this.domainService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }
}
