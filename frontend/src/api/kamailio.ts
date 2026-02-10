import request from './interceptor';

// ========== 用户管理 ==========
export interface Subscriber {
  id: number;
  username: string;
  domain: string;
  emailAddress?: string;
  rpid?: string;
}

export interface CreateSubscriberDto {
  username: string;
  domain: string;
  password: string;
  emailAddress?: string;
  rpid?: string;
}

export interface UpdateSubscriberDto {
  password?: string;
  emailAddress?: string;
  rpid?: string;
}

export function getSubscribers(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/subscribers', { params });
}

export function getSubscriber(id: number) {
  return request.get(`/subscribers/${id}`);
}

export function createSubscriber(data: CreateSubscriberDto) {
  return request.post('/subscribers', data);
}

export function updateSubscriber(id: number, data: UpdateSubscriberDto) {
  return request.patch(`/subscribers/${id}`, data);
}

export function deleteSubscriber(id: number) {
  return request.delete(`/subscribers/${id}`);
}

export function getSubscriberStats() {
  return request.get('/subscribers/stats');
}

// ========== 域管理 ==========
export interface Domain {
  id: number;
  domain: string;
  did?: string;
  lastModified: string;
}

export interface CreateDomainDto {
  domain: string;
  did?: string;
}

export function getDomains(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/domains', { params });
}

export function getAllDomains() {
  return request.get('/domains/all');
}

export function getDomain(id: number) {
  return request.get(`/domains/${id}`);
}

export function createDomain(data: CreateDomainDto) {
  return request.post('/domains', data);
}

export function updateDomain(id: number, data: Partial<CreateDomainDto>) {
  return request.patch(`/domains/${id}`, data);
}

export function deleteDomain(id: number) {
  return request.delete(`/domains/${id}`);
}

export function reloadDomains() {
  return request.post('/domains/reload');
}

// ========== 调度器管理 ==========
export interface Dispatcher {
  id: number;
  setid: number;
  destination: string;
  flags: number;
  priority: number;
  attrs: string;
  description: string;
}

export interface CreateDispatcherDto {
  setid: number;
  destination: string;
  flags?: number;
  priority?: number;
  attrs?: string;
  description?: string;
}

export function getDispatchers(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/dispatchers', { params });
}

export function getDispatcher(id: number) {
  return request.get(`/dispatchers/${id}`);
}

export function createDispatcher(data: CreateDispatcherDto) {
  return request.post('/dispatchers', data);
}

export function updateDispatcher(id: number, data: Partial<CreateDispatcherDto>) {
  return request.patch(`/dispatchers/${id}`, data);
}

export function deleteDispatcher(id: number) {
  return request.delete(`/dispatchers/${id}`);
}

export function reloadDispatchers() {
  return request.post('/dispatchers/reload');
}

export function getDispatcherStats() {
  return request.get('/dispatchers/stats');
}

export function getDispatcherStatus() {
  return request.get('/dispatchers/status');
}

// ========== 注册位置 ==========
export interface Location {
  id: number;
  username: string;
  domain: string;
  contact: string;
  expires: string;
  userAgent: string;
  socket: string;
  lastModified: string;
}

export function getLocations(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/locations', { params });
}

export function getLocationStats() {
  return request.get('/locations/stats');
}

export function getOnlineCount() {
  return request.get('/locations/online-count');
}

export function deleteUserLocation(username: string, domain: string) {
  return request.delete(`/locations/${username}/${domain}`);
}

// ========== 权限管理 ==========
export interface Address {
  id: number;
  grp: number;
  ipAddr: string;
  mask: number;
  port: number;
  tag?: string;
}

export interface Trusted {
  id: number;
  srcIp: string;
  proto?: string;
  fromPattern?: string;
  tag?: string;
  priority: number;
}

export function getAddresses(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/permissions/address', { params });
}

export function createAddress(data: Partial<Address>) {
  return request.post('/permissions/address', data);
}

export function updateAddress(id: number, data: Partial<Address>) {
  return request.patch(`/permissions/address/${id}`, data);
}

export function deleteAddress(id: number) {
  return request.delete(`/permissions/address/${id}`);
}

export function getTrusted(params: { page?: number; limit?: number; keyword?: string }) {
  return request.get('/permissions/trusted', { params });
}

export function createTrusted(data: Partial<Trusted>) {
  return request.post('/permissions/trusted', data);
}

export function updateTrusted(id: number, data: Partial<Trusted>) {
  return request.patch(`/permissions/trusted/${id}`, data);
}

export function deleteTrusted(id: number) {
  return request.delete(`/permissions/trusted/${id}`);
}

export function reloadPermissions() {
  return request.post('/permissions/reload');
}

// ========== CDR 记录 ==========
export interface AccRecord {
  id: number;
  method: string;
  callid: string;
  sipCode: string;
  sipReason: string;
  time: string;
  srcUser: string;
  srcDomain: string;
  srcIp: string;
  dstUser: string;
  dstDomain: string;
}

export function getCdrRecords(params: any) {
  return request.get('/acc/cdr', { params });
}

export function getMissedCalls(params: any) {
  return request.get('/acc/missed-calls', { params });
}

export function getAccStats(params?: { startTime?: string; endTime?: string }) {
  return request.get('/acc/stats', { params });
}

export function getTodayStats() {
  return request.get('/acc/today');
}

// ========== 监控统计 ==========
export function checkHealth() {
  return request.get('/monitoring/health');
}

export function getDashboardData() {
  return request.get('/monitoring/dashboard');
}

export function getStatistics(group?: string) {
  return request.get('/monitoring/statistics', { params: { group } });
}

export function getCoreInfo() {
  return request.get('/monitoring/core-info');
}

export function getActiveDialogs() {
  return request.get('/monitoring/dialogs');
}

export function getSystemOverview() {
  return request.get('/monitoring/overview');
}

// ========== 动态路由 ==========
export function getDrGateways(params: any) {
  return request.get('/drouting/gateways', { params });
}

export function createDrGateway(data: any) {
  return request.post('/drouting/gateways', data);
}

export function updateDrGateway(id: number, data: any) {
  return request.patch(`/drouting/gateways/${id}`, data);
}

export function deleteDrGateway(id: number) {
  return request.delete(`/drouting/gateways/${id}`);
}

export function getDrRules(params: any) {
  return request.get('/drouting/rules', { params });
}

export function createDrRule(data: any) {
  return request.post('/drouting/rules', data);
}

export function updateDrRule(id: number, data: any) {
  return request.patch(`/drouting/rules/${id}`, data);
}

export function deleteDrRule(id: number) {
  return request.delete(`/drouting/rules/${id}`);
}

export function getDrGroups(params: any) {
  return request.get('/drouting/groups', { params });
}

export function getDrCarriers(params: any) {
  return request.get('/drouting/carriers', { params });
}

export function reloadDrouting() {
  return request.post('/drouting/reload');
}

export function getDrStats() {
  return request.get('/drouting/stats');
}

// ========== LCR 路由 ==========
export function getLcrGateways(params: any) {
  return request.get('/lcr/gateways', { params });
}

export function getLcrRules(params: any) {
  return request.get('/lcr/rules', { params });
}

export function getLcrTargets(params: any) {
  return request.get('/lcr/targets', { params });
}

export function reloadLcr() {
  return request.post('/lcr/reload');
}

// ========== 对话管理 ==========
export function getDialogs(params: any) {
  return request.get('/dialogs', { params });
}

export function getActiveDialogsList() {
  return request.get('/dialogs/active');
}

export function getDialogStats() {
  return request.get('/dialogs/stats');
}

export function endDialog(hashEntry: number, hashId: number) {
  return request.post('/dialogs/end', { hashEntry, hashId });
}

// ========== 哈希表 ==========
export function getHtableRecords(params: any) {
  return request.get('/htable', { params });
}

export function getHtableValue(table: string, key: string) {
  return request.get('/htable/rpc/get', { params: { table, key } });
}

export function setHtableString(table: string, key: string, value: string) {
  return request.post('/htable/rpc/sets', { table, key, value });
}

export function deleteHtableKey(table: string, key: string) {
  return request.post('/htable/rpc/delete', { table, key });
}

export function dumpHtable(table: string) {
  return request.get('/htable/rpc/dump', { params: { table } });
}

export function reloadHtable(table: string) {
  return request.post('/htable/rpc/reload', { table });
}

// ========== 用户数据 ==========
export function getAliases(params: any) {
  return request.get('/userdata/aliases', { params });
}

export function createAlias(data: any) {
  return request.post('/userdata/aliases', data);
}

export function deleteAlias(id: number) {
  return request.delete(`/userdata/aliases/${id}`);
}

export function getGroups(params: any) {
  return request.get('/userdata/groups', { params });
}

export function createGroup(data: any) {
  return request.post('/userdata/groups', data);
}

export function deleteGroup(id: number) {
  return request.delete(`/userdata/groups/${id}`);
}

export function getSpeedDials(params: any) {
  return request.get('/userdata/speed-dial', { params });
}

// ========== UAC 注册 ==========
export function getUacRegistrations(params: any) {
  return request.get('/uac/registrations', { params });
}

export function createUacRegistration(data: any) {
  return request.post('/uac/registrations', data);
}

export function updateUacRegistration(id: number, data: any) {
  return request.patch(`/uac/registrations/${id}`, data);
}

export function deleteUacRegistration(id: number) {
  return request.delete(`/uac/registrations/${id}`);
}

export function reloadUac() {
  return request.post('/uac/reload');
}

export function getUacDump() {
  return request.get('/uac/dump');
}

// ========== RTPEngine ==========
export function getRtpengineStatus() {
  return request.get('/rtpengine/status');
}

export function showRtpengine() {
  return request.get('/rtpengine/show');
}

export function reloadRtpengine() {
  return request.post('/rtpengine/reload');
}

export function enableRtpengine(url: string, flag: number) {
  return request.post('/rtpengine/enable', { url, flag });
}

// ========== 系统管理 ==========
export function getSystemInfo() {
  return request.get('/system/info');
}

export function getSystemStatus() {
  return request.get('/system/status');
}

export function getSystemVersion() {
  return request.get('/system/version');
}

export function getSystemUptime() {
  return request.get('/system/uptime');
}

export function getSystemMemory() {
  return request.get('/system/memory');
}

export function getSystemProcesses() {
  return request.get('/system/processes');
}

export function getSystemModules() {
  return request.get('/system/modules');
}

export function getSystemStatistics(group?: string) {
  return request.get('/system/statistics', { params: { group } });
}

export function getTlsList() {
  return request.get('/system/tls/list');
}

export function getTlsInfo() {
  return request.get('/system/tls/info');
}

export function reloadTls() {
  return request.post('/system/tls/reload');
}

export function getPikeList() {
  return request.get('/system/pike/list');
}

export function getPikeTop(limit?: number) {
  return request.get('/system/pike/top', { params: { limit } });
}

export function getConfigList() {
  return request.get('/system/config/list');
}

export function getConfig(group: string, name: string) {
  return request.get('/system/config', { params: { group, name } });
}

export function setConfig(group: string, name: string, value: any) {
  return request.post('/system/config', { group, name, value });
}

// ========== 存在服务 ==========
export function getPresentities(params: any) {
  return request.get('/presence/presentities', { params });
}

export function getWatchers(params: any) {
  return request.get('/presence/watchers', { params });
}

export function cleanupPresence() {
  return request.post('/presence/cleanup');
}

export function getPresenceStats() {
  return request.get('/presence/stats');
}

// ========== 离线消息 ==========
export function getMsiloMessages(params: any) {
  return request.get('/msilo', { params });
}

export function getMsiloByUser(username: string, domain: string) {
  return request.get('/msilo/user', { params: { username, domain } });
}

export function deleteMsiloMessage(id: number) {
  return request.delete(`/msilo/${id}`);
}

export function cleanupMsilo() {
  return request.post('/msilo/cleanup');
}

export function getMsiloStats() {
  return request.get('/msilo/stats');
}

// ========== SIP 跟踪 ==========
export function getSipTraces(params: any) {
  return request.get('/siptrace', { params });
}

export function getSipTraceByCallId(callid: string) {
  return request.get('/siptrace/call', { params: { callid } });
}

export function getSipTraceStats() {
  return request.get('/siptrace/stats');
}

export function cleanupSipTrace(days: number) {
  return request.post('/siptrace/cleanup', { days });
}

// ========== 运营商路由 ==========
export function getCarrierNames() {
  return request.get('/carrierroute/carriers');
}

export function createCarrierName(carrier: string) {
  return request.post('/carrierroute/carriers', { carrier });
}

export function deleteCarrierName(id: number) {
  return request.delete(`/carrierroute/carriers/${id}`);
}

export function getCarrierDomains() {
  return request.get('/carrierroute/domains');
}

export function createCarrierDomain(domain: string) {
  return request.post('/carrierroute/domains', { domain });
}

export function deleteCarrierDomain(id: number) {
  return request.delete(`/carrierroute/domains/${id}`);
}

export function getCarrierFailureRoutes(params: any) {
  return request.get('/carrierroute/failure-routes', { params });
}

export function reloadCarrierroute() {
  return request.post('/carrierroute/reload');
}

export function dumpCarrierRoutes() {
  return request.get('/carrierroute/dump');
}

// ========== 拨号计划 ==========
export function getDialplanRules(params: any) {
  return request.get('/dialplan', { params });
}

export function createDialplanRule(data: any) {
  return request.post('/dialplan', data);
}

export function updateDialplanRule(id: number, data: any) {
  return request.patch(`/dialplan/${id}`, data);
}

export function deleteDialplanRule(id: number) {
  return request.delete(`/dialplan/${id}`);
}

export function reloadDialplan() {
  return request.post('/dialplan/reload');
}

export function translateDialplan(dpid: number, input: string) {
  return request.get('/dialplan/translate', { params: { dpid, input } });
}

// ========== Mtree ==========
export function getMtreeRecords(params: any) {
  return request.get('/mtree', { params });
}

export function createMtreeRecord(data: any) {
  return request.post('/mtree', data);
}

export function deleteMtreeRecord(id: number) {
  return request.delete(`/mtree/${id}`);
}

export function reloadMtree(tname: string) {
  return request.post('/mtree/reload', { tname });
}

// ========== PDT ==========
export function getPdtRecords(params: any) {
  return request.get('/pdt', { params });
}

export function createPdtRecord(data: any) {
  return request.post('/pdt', data);
}

export function deletePdtRecord(id: number) {
  return request.delete(`/pdt/${id}`);
}

export function reloadPdt() {
  return request.post('/pdt/reload');
}

// ========== Topos ==========
export function getToposDialogs(params: any) {
  return request.get('/topos/dialogs', { params });
}

export function cleanupTopos(days: number) {
  return request.post('/topos/cleanup', { days });
}

// ========== 安全过滤 ==========
export function getSecfilterRules(params: any) {
  return request.get('/secfilter', { params });
}

export function createSecfilterRule(data: any) {
  return request.post('/secfilter', data);
}

export function deleteSecfilterRule(id: number) {
  return request.delete(`/secfilter/${id}`);
}

export function reloadSecfilter() {
  return request.post('/secfilter/reload');
}

export function getSecfilterStats() {
  return request.get('/secfilter/stats');
}

export function addSecfilterBlacklist(type: number, data: string) {
  return request.post('/secfilter/add-bl', { type, data });
}

export function addSecfilterWhitelist(type: number, data: string) {
  return request.post('/secfilter/add-wl', { type, data });
}

// ========== 用户偏好 ==========
export function getUsrPreferences(params: any) {
  return request.get('/usrpreferences', { params });
}

export function getUserPreferences(username: string, domain: string) {
  return request.get('/usrpreferences/user', { params: { username, domain } });
}

export function createUsrPreference(data: any) {
  return request.post('/usrpreferences', data);
}

export function deleteUsrPreference(id: number) {
  return request.delete(`/usrpreferences/${id}`);
}

// ========== 数据库版本 ==========
export function getDbVersions() {
  return request.get('/version');
}

export function getDbVersionStats() {
  return request.get('/version/stats');
}
