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

// 路由模块
import { DroutingModule } from './modules/drouting/drouting.module';
import { LcrModule } from './modules/lcr/lcr.module';
import { CarrierrouteModule } from './modules/carrierroute/carrierroute.module';

// 对话与缓存
import { DialogModule } from './modules/dialog/dialog.module';
import { HtableModule } from './modules/htable/htable.module';

// 用户数据
import { UserdataModule } from './modules/userdata/userdata.module';
import { UacModule } from './modules/uac/uac.module';

// 存在服务与消息
import { PresenceModule } from './modules/presence/presence.module';
import { MsiloModule } from './modules/msilo/msilo.module';

// 跟踪与调试
import { SiptraceModule } from './modules/siptrace/siptrace.module';

// 媒体与系统
import { RtpengineModule } from './modules/rtpengine/rtpengine.module';
import { SystemModule } from './modules/system/system.module';

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
    
    // 核心业务模块
    SubscriberModule,
    DomainModule,
    DispatcherModule,
    LocationModule,
    PermissionsModule,
    AccModule,
    MonitoringModule,
    
    // 路由模块
    DroutingModule,
    LcrModule,
    CarrierrouteModule,
    
    // 对话与缓存
    DialogModule,
    HtableModule,
    
    // 用户数据
    UserdataModule,
    UacModule,
    
    // 存在服务与消息
    PresenceModule,
    MsiloModule,
    
    // 跟踪与调试
    SiptraceModule,
    
    // 媒体与系统
    RtpengineModule,
    SystemModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
