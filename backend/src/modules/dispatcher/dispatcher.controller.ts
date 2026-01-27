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
import { DispatcherService } from './dispatcher.service';
import { CreateDispatcherDto } from './dto/create-dispatcher.dto';
import { UpdateDispatcherDto } from './dto/update-dispatcher.dto';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';
import { Dispatcher } from './entities/dispatcher.entity';

@ApiTags('调度器管理')
@Controller('api/dispatchers')
export class DispatcherController {
  constructor(private readonly dispatcherService: DispatcherService) {}

  @Post()
  @ApiOperation({ summary: '创建调度目标' })
  @ApiResponse({ status: 201, description: '创建成功' })
  async create(@Body() createDto: CreateDispatcherDto): Promise<ApiResponseDto<Dispatcher>> {
    const dispatcher = await this.dispatcherService.create(createDto);
    return ApiResponseDto.success(dispatcher, '创建成功');
  }

  @Get()
  @ApiOperation({ summary: '获取调度目标列表' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAll(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.dispatcherService.findAll(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('setids')
  @ApiOperation({ summary: '获取所有调度组 ID' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getSetIds(): Promise<ApiResponseDto<number[]>> {
    const setIds = await this.dispatcherService.getSetIds();
    return ApiResponseDto.success(setIds);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取调度器统计信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStats(): Promise<ApiResponseDto> {
    const stats = await this.dispatcherService.getStats();
    return ApiResponseDto.success(stats);
  }

  @Get('status')
  @ApiOperation({ summary: '获取调度器实时状态' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStatus(): Promise<ApiResponseDto> {
    const status = await this.dispatcherService.getStatus();
    return ApiResponseDto.success(status);
  }

  @Get('setid/:setid')
  @ApiOperation({ summary: '根据调度组获取调度目标' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findBySetId(
    @Param('setid', ParseIntPipe) setid: number,
  ): Promise<ApiResponseDto<Dispatcher[]>> {
    const dispatchers = await this.dispatcherService.findBySetId(setid);
    return ApiResponseDto.success(dispatchers);
  }

  @Get(':id')
  @ApiOperation({ summary: '获取调度目标详情' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findOne(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto<Dispatcher>> {
    const dispatcher = await this.dispatcherService.findOne(id);
    return ApiResponseDto.success(dispatcher);
  }

  @Patch(':id')
  @ApiOperation({ summary: '更新调度目标' })
  @ApiResponse({ status: 200, description: '更新成功' })
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateDto: UpdateDispatcherDto,
  ): Promise<ApiResponseDto<Dispatcher>> {
    const dispatcher = await this.dispatcherService.update(id, updateDto);
    return ApiResponseDto.success(dispatcher, '更新成功');
  }

  @Delete(':id')
  @ApiOperation({ summary: '删除调度目标' })
  @ApiResponse({ status: 200, description: '删除成功' })
  async remove(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.dispatcherService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载调度器配置' })
  @ApiResponse({ status: 200, description: '重载成功' })
  async reload(): Promise<ApiResponseDto> {
    await this.dispatcherService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }
}
