import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';

// 配置
import { databaseConfig, kamailioConfig } from './config';

// 公共模块
import { CommonModule } from './common/common.module';

// 业务模块
import { SubscriberModule } from './modules/subscriber/subscriber.module';
import { DomainModule } from './modules/domain/domain.module';
import { DispatcherModule } from './modules/dispatcher/dispatcher.module';
import { LocationModule } from './modules/location/location.module';
import { PermissionsModule } from './modules/permissions/permissions.module';
import { AccModule } from './modules/acc/acc.module';
import { MonitoringModule } from './modules/monitoring/monitoring.module';

@Module({
  imports: [
    // 配置模块
    ConfigModule.forRoot({
      isGlobal: true,
      load: [databaseConfig, kamailioConfig],
      envFilePath: ['.env.local', '.env'],
    }),
    
    // 数据库模块
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        type: 'mysql',
        host: configService.get('database.host'),
        port: configService.get('database.port'),
        username: configService.get('database.username'),
        password: configService.get('database.password'),
        database: configService.get('database.database'),
        entities: [__dirname + '/**/*.entity{.ts,.js}'],
        synchronize: false,
        logging: configService.get('database.logging'),
      }),
      inject: [ConfigService],
    }),
    
    // 公共模块
    CommonModule,
    
    // 业务模块
    SubscriberModule,
    DomainModule,
    DispatcherModule,
    LocationModule,
    PermissionsModule,
    AccModule,
    MonitoringModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
