import { registerAs } from '@nestjs/config';

export default registerAs('kamailio', () => ({
  // JSONRPC 配置
  rpc: {
    host: process.env.KAMAILIO_RPC_HOST || 'localhost',
    port: parseInt(process.env.KAMAILIO_RPC_PORT, 10) || 5060,
    path: process.env.KAMAILIO_RPC_PATH || '/RPC',
  },
  // kamctl 命令路径
  kamctlPath: process.env.KAMAILIO_KAMCTL_PATH || '/usr/sbin/kamctl',
  // 默认域
  defaultDomain: process.env.KAMAILIO_DEFAULT_DOMAIN || 'localhost',
}));
