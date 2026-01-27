import { Controller, Get, Post, Body, Patch, Param, Delete, Query, ParseIntPipe } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { UacService } from './uac.service';
import { PaginationDto } from '../../common/dto/pagination.dto';
import { ApiResponseDto } from '../../common/dto/api-response.dto';

@ApiTags('UAC 注册')
@Controller('api/uac')
export class UacController {
  constructor(private readonly uacService: UacService) {}

  @Get('registrations')
  @ApiOperation({ summary: '获取 UAC 注册列表' })
  async findAll(@Query() dto: PaginationDto) {
    return ApiResponseDto.success(await this.uacService.findAll(dto));
  }

  @Post('registrations')
  async create(@Body() data: any) {
    return ApiResponseDto.success(await this.uacService.create(data), '创建成功');
  }

  @Patch('registrations/:id')
  async update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return ApiResponseDto.success(await this.uacService.update(id, data), '更新成功');
  }

  @Delete('registrations/:id')
  async remove(@Param('id', ParseIntPipe) id: number) {
    await this.uacService.remove(id);
    return ApiResponseDto.success(null, '删除成功');
  }

  @Post('reload')
  @ApiOperation({ summary: '重载 UAC 注册' })
  async reload() {
    await this.uacService.reload();
    return ApiResponseDto.success(null, '重载成功');
  }

  @Get('info/:lUuid')
  @ApiOperation({ summary: '获取注册信息' })
  async getInfo(@Param('lUuid') lUuid: string) {
    return ApiResponseDto.success(await this.uacService.getInfo(lUuid));
  }

  @Post('refresh/:lUuid')
  @ApiOperation({ summary: '刷新注册' })
  async refresh(@Param('lUuid') lUuid: string) {
    await this.uacService.refresh(lUuid);
    return ApiResponseDto.success(null, '刷新成功');
  }

  @Get('dump')
  @ApiOperation({ summary: '导出注册列表' })
  async dumpList() {
    return ApiResponseDto.success(await this.uacService.dumpList());
  }
}
