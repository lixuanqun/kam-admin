/**
 * 格式化运行时间（秒 -> 可读字符串）
 */
export function formatUptime(uptime: any): string {
  if (!uptime) return '-';
  if (typeof uptime === 'string') return uptime;
  const seconds = uptime.uptime ?? uptime;
  if (typeof seconds !== 'number') return '-';
  const days = Math.floor(seconds / 86400);
  const hours = Math.floor((seconds % 86400) / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  return `${days}天 ${hours}小时 ${minutes}分钟`;
}

/**
 * SIP 响应码颜色
 */
export function getSipCodeColor(code: string): string {
  if (code?.startsWith('2')) return 'green';
  if (code?.startsWith('3')) return 'arcoblue';
  if (code?.startsWith('4')) return 'orangered';
  if (code?.startsWith('5')) return 'red';
  return 'gray';
}
