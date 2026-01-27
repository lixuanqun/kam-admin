import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UacService } from './uac.service';
import { UacController } from './uac.controller';
import { UacReg } from './entities/uacreg.entity';

@Module({
  imports: [TypeOrmModule.forFeature([UacReg])],
  controllers: [UacController],
  providers: [UacService],
  exports: [UacService],
})
export class UacModule {}
