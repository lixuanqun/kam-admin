import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsrpreferencesService } from './usrpreferences.service';
import { UsrpreferencesController } from './usrpreferences.controller';
import { UsrPreferences } from './entities/usr-preferences.entity';

@Module({
  imports: [TypeOrmModule.forFeature([UsrPreferences])],
  controllers: [UsrpreferencesController],
  providers: [UsrpreferencesService],
  exports: [UsrpreferencesService],
})
export class UsrpreferencesModule {}
