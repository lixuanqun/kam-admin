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
import { SubscriberService } from './subscriber.service';
import { CreateSubscriberDto } from './dto/create-subscriber.dto';
import { UpdateSubscriberDto } from './dto/update-subscriber.dto';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';
import { Subscriber } from './entities/subscriber.entity';

@ApiTags('用户管理')
@Controller('api/subscribers')
export class SubscriberController {
  constructor(private readonly subscriberService: SubscriberService) {}

  @Post()
  @ApiOperation({ summary: '创建用户' })
  @ApiResponse({ status: 201, description: '创建成功' })
  async create(@Body() createDto: CreateSubscriberDto): Promise<ApiResponseDto<Subscriber>> {
    const subscriber = await this.subscriberService.create(createDto);
    return ApiResponseDto.success(subscriber, '创建成功');
  }

  @Get()
  @ApiOperation({ summary: '获取用户列表' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findAll(@Query() paginationDto: PaginationDto): Promise<ApiResponseDto> {
    const result = await this.subscriberService.findAll(paginationDto);
    return ApiResponseDto.success(result);
  }

  @Get('stats')
  @ApiOperation({ summary: '获取用户统计信息' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async getStats(): Promise<ApiResponseDto> {
    const stats = await this.subscriberService.getStats();
    return ApiResponseDto.success(stats);
  }

  @Get(':id')
  @ApiOperation({ summary: '获取用户详情' })
  @ApiResponse({ status: 200, description: '获取成功' })
  async findOne(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto<Subscriber>> {
    const subscriber = await this.subscriberService.findOne(id);
    return ApiResponseDto.success(subscriber);
  }

  @Patch(':id')
  @ApiOperation({ summary: '更新用户' })
  @ApiResponse({ status: 200, description: '更新成功' })
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateDto: UpdateSubscriberDto,
  ): Promise<ApiResponseDto<Subscriber>> {
    const subscriber = await this.subscriberService.update(id, updateDto);
    return ApiResponseDto.success(subscriber, '更新成功');
  }

  @Delete(':id')
  @ApiOperation({ summary: '删除用户' })
  @ApiResponse({ status: 200, description: '删除成功' })
  async remove(@Param('id', ParseIntPipe) id: number): Promise<ApiResponseDto> {
    await this.subscriberService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('batch-delete')
  @ApiOperation({ summary: '批量删除用户' })
  @ApiResponse({ status: 200, description: '删除成功' })
  async batchRemove(@Body('ids') ids: number[]): Promise<ApiResponseDto> {
    await this.subscriberService.batchRemove(ids);
    return ApiResponseDto.success(null, '删除成功');
  }
}
