import request from '../interceptor';

// ===== 系统管理 =====
export const getSystemInfo = () => request.get('/system/info');
export const getSystemStatus = () => request.get('/system/status');
export const getSystemVersion = () => request.get('/system/version');
export const getSystemUptime = () => request.get('/system/uptime');
export const getSystemMemory = () => request.get('/system/memory');
export const getSystemProcesses = () => request.get('/system/processes');
export const getSystemModules = () => request.get('/system/modules');
export const getSystemStatistics = (group?: string) => request.get('/system/statistics', { params: { group } });
export const getTlsList = () => request.get('/system/tls/list');
export const getTlsInfo = () => request.get('/system/tls/info');
export const reloadTls = () => request.post('/system/tls/reload');
export const getPikeList = () => request.get('/system/pike/list');
export const getPikeTop = (limit?: number) => request.get('/system/pike/top', { params: { limit } });
export const getConfigList = () => request.get('/system/config/list');
export const getConfig = (group: string, name: string) => request.get('/system/config', { params: { group, name } });
export const setConfig = (group: string, name: string, value: any) => request.post('/system/config', { group, name, value });

// ===== RTPEngine =====
export const getRtpengineStatus = () => request.get('/rtpengine/status');
export const showRtpengine = () => request.get('/rtpengine/show');
export const reloadRtpengine = () => request.post('/rtpengine/reload');
export const enableRtpengine = (url: string, flag: number) => request.post('/rtpengine/enable', { url, flag });

// ===== 安全过滤 =====
export const getSecfilterRules = (params: any) => request.get('/secfilter', { params });
export const createSecfilterRule = (data: any) => request.post('/secfilter', data);
export const deleteSecfilterRule = (id: number) => request.delete(`/secfilter/${id}`);
export const reloadSecfilter = () => request.post('/secfilter/reload');
export const getSecfilterStats = () => request.get('/secfilter/stats');
export const addSecfilterBlacklist = (type: number, data: string) => request.post('/secfilter/add-bl', { type, data });
export const addSecfilterWhitelist = (type: number, data: string) => request.post('/secfilter/add-wl', { type, data });

// ===== SIP 跟踪 =====
export const getSipTraces = (params: any) => request.get('/siptrace', { params });
export const getSipTraceByCallId = (callid: string) => request.get('/siptrace/call', { params: { callid } });
export const getSipTraceStats = () => request.get('/siptrace/stats');
export const cleanupSipTrace = (days: number) => request.post('/siptrace/cleanup', { days });

// ===== 拓扑隐藏 =====
export const getToposDialogs = (params: any) => request.get('/topos/dialogs', { params });
export const cleanupTopos = (days: number) => request.post('/topos/cleanup', { days });

// ===== 数据库版本 =====
export const getDbVersions = () => request.get('/version');
export const getDbVersionStats = () => request.get('/version/stats');
