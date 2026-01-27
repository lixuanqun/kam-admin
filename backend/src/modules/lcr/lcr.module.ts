import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { LcrService } from './lcr.service';
import { LcrController } from './lcr.controller';
import { LcrGw } from './entities/lcr-gw.entity';
import { LcrRule } from './entities/lcr-rule.entity';
import { LcrRuleTarget } from './entities/lcr-rule-target.entity';

@Module({
  imports: [TypeOrmModule.forFeature([LcrGw, LcrRule, LcrRuleTarget])],
  controllers: [LcrController],
  providers: [LcrService],
  exports: [LcrService],
})
export class LcrModule {}
