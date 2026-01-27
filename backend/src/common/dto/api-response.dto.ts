import { ApiProperty } from '@nestjs/swagger';

export class ApiResponseDto<T = any> {
  @ApiProperty({ description: '状态码' })
  code: number;

  @ApiProperty({ description: '消息' })
  message: string;

  @ApiProperty({ description: '数据' })
  data?: T;

  @ApiProperty({ description: '时间戳' })
  timestamp: string;

  constructor(code: number, message: string, data?: T) {
    this.code = code;
    this.message = message;
    this.data = data;
    this.timestamp = new Date().toISOString();
  }

  static success<T>(data?: T, message = 'Success'): ApiResponseDto<T> {
    return new ApiResponseDto(0, message, data);
  }

  static error(message: string, code = -1): ApiResponseDto {
    return new ApiResponseDto(code, message);
  }
}
