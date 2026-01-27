import { registerAs } from '@nestjs/config';

export default registerAs('database', () => ({
  type: 'mysql',
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT, 10) || 3306,
  username: process.env.DB_USERNAME || 'kamailio',
  password: process.env.DB_PASSWORD || 'kamailiorw',
  database: process.env.DB_DATABASE || 'kamailio',
  entities: [__dirname + '/../**/*.entity{.ts,.js}'],
  synchronize: false, // Kamailio 数据库不需要自动同步
  logging: process.env.DB_LOGGING === 'true',
}));
