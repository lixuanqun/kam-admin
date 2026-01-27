import { requestClient } from '#/api/request';

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
  return requestClient.get('/subscribers', { params });
}

export function getSubscriber(id: number) {
  return requestClient.get(`/subscribers/${id}`);
}

export function createSubscriber(data: CreateSubscriberDto) {
  return requestClient.post('/subscribers', data);
}

export function updateSubscriber(id: number, data: UpdateSubscriberDto) {
  return requestClient.patch(`/subscribers/${id}`, data);
}

export function deleteSubscriber(id: number) {
  return requestClient.delete(`/subscribers/${id}`);
}

export function getSubscriberStats() {
  return requestClient.get('/subscribers/stats');
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
  return requestClient.get('/domains', { params });
}

export function getAllDomains() {
  return requestClient.get('/domains/all');
}

export function getDomain(id: number) {
  return requestClient.get(`/domains/${id}`);
}

export function createDomain(data: CreateDomainDto) {
  return requestClient.post('/domains', data);
}

export function updateDomain(id: number, data: Partial<CreateDomainDto>) {
  return requestClient.patch(`/domains/${id}`, data);
}

export function deleteDomain(id: number) {
  return requestClient.delete(`/domains/${id}`);
}

export function reloadDomains() {
  return requestClient.post('/domains/reload');
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
  return requestClient.get('/dispatchers', { params });
}

export function getDispatcher(id: number) {
  return requestClient.get(`/dispatchers/${id}`);
}

export function createDispatcher(data: CreateDispatcherDto) {
  return requestClient.post('/dispatchers', data);
}

export function updateDispatcher(id: number, data: Partial<CreateDispatcherDto>) {
  return requestClient.patch(`/dispatchers/${id}`, data);
}

export function deleteDispatcher(id: number) {
  return requestClient.delete(`/dispatchers/${id}`);
}

export function reloadDispatchers() {
  return requestClient.post('/dispatchers/reload');
}

export function getDispatcherStats() {
  return requestClient.get('/dispatchers/stats');
}

export function getDispatcherStatus() {
  return requestClient.get('/dispatchers/status');
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
  return requestClient.get('/locations', { params });
}

export function getLocationStats() {
  return requestClient.get('/locations/stats');
}

export function getOnlineCount() {
  return requestClient.get('/locations/online-count');
}

export function deleteUserLocation(username: string, domain: string) {
  return requestClient.delete(`/locations/${username}/${domain}`);
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
  return requestClient.get('/permissions/address', { params });
}

export function createAddress(data: Partial<Address>) {
  return requestClient.post('/permissions/address', data);
}

export function updateAddress(id: number, data: Partial<Address>) {
  return requestClient.patch(`/permissions/address/${id}`, data);
}

export function deleteAddress(id: number) {
  return requestClient.delete(`/permissions/address/${id}`);
}

export function getTrusted(params: { page?: number; limit?: number; keyword?: string }) {
  return requestClient.get('/permissions/trusted', { params });
}

export function createTrusted(data: Partial<Trusted>) {
  return requestClient.post('/permissions/trusted', data);
}

export function updateTrusted(id: number, data: Partial<Trusted>) {
  return requestClient.patch(`/permissions/trusted/${id}`, data);
}

export function deleteTrusted(id: number) {
  return requestClient.delete(`/permissions/trusted/${id}`);
}

export function reloadPermissions() {
  return requestClient.post('/permissions/reload');
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

export function getCdrRecords(params: {
  page?: number;
  limit?: number;
  srcUser?: string;
  dstUser?: string;
  callid?: string;
  startTime?: string;
  endTime?: string;
  sipCode?: string;
}) {
  return requestClient.get('/acc/cdr', { params });
}

export function getMissedCalls(params: {
  page?: number;
  limit?: number;
  srcUser?: string;
  dstUser?: string;
  startTime?: string;
  endTime?: string;
}) {
  return requestClient.get('/acc/missed-calls', { params });
}

export function getAccStats(params?: { startTime?: string; endTime?: string }) {
  return requestClient.get('/acc/stats', { params });
}

export function getTodayStats() {
  return requestClient.get('/acc/today');
}

// ========== 监控统计 ==========
export function checkHealth() {
  return requestClient.get('/monitoring/health');
}

export function getDashboardData() {
  return requestClient.get('/monitoring/dashboard');
}

export function getStatistics(group?: string) {
  return requestClient.get('/monitoring/statistics', { params: { group } });
}

export function getCoreInfo() {
  return requestClient.get('/monitoring/core-info');
}

export function getActiveDialogs() {
  return requestClient.get('/monitoring/dialogs');
}

export function getSystemOverview() {
  return requestClient.get('/monitoring/overview');
}

// ========== 动态路由 ==========
export function getDrGateways(params: any) {
  return requestClient.get('/drouting/gateways', { params });
}

export function createDrGateway(data: any) {
  return requestClient.post('/drouting/gateways', data);
}

export function updateDrGateway(id: number, data: any) {
  return requestClient.patch(`/drouting/gateways/${id}`, data);
}

export function deleteDrGateway(id: number) {
  return requestClient.delete(`/drouting/gateways/${id}`);
}

export function getDrRules(params: any) {
  return requestClient.get('/drouting/rules', { params });
}

export function createDrRule(data: any) {
  return requestClient.post('/drouting/rules', data);
}

export function updateDrRule(id: number, data: any) {
  return requestClient.patch(`/drouting/rules/${id}`, data);
}

export function deleteDrRule(id: number) {
  return requestClient.delete(`/drouting/rules/${id}`);
}

export function getDrGroups(params: any) {
  return requestClient.get('/drouting/groups', { params });
}

export function getDrCarriers(params: any) {
  return requestClient.get('/drouting/carriers', { params });
}

export function reloadDrouting() {
  return requestClient.post('/drouting/reload');
}

export function getDrStats() {
  return requestClient.get('/drouting/stats');
}

// ========== LCR 路由 ==========
export function getLcrGateways(params: any) {
  return requestClient.get('/lcr/gateways', { params });
}

export function getLcrRules(params: any) {
  return requestClient.get('/lcr/rules', { params });
}

export function getLcrTargets(params: any) {
  return requestClient.get('/lcr/targets', { params });
}

export function reloadLcr() {
  return requestClient.post('/lcr/reload');
}

// ========== 对话管理 ==========
export function getDialogs(params: any) {
  return requestClient.get('/dialogs', { params });
}

export function getActiveDialogsList() {
  return requestClient.get('/dialogs/active');
}

export function getDialogStats() {
  return requestClient.get('/dialogs/stats');
}

export function endDialog(hashEntry: number, hashId: number) {
  return requestClient.post('/dialogs/end', { hashEntry, hashId });
}

// ========== 哈希表 ==========
export function getHtableRecords(params: any) {
  return requestClient.get('/htable', { params });
}

export function getHtableValue(table: string, key: string) {
  return requestClient.get('/htable/rpc/get', { params: { table, key } });
}

export function setHtableString(table: string, key: string, value: string) {
  return requestClient.post('/htable/rpc/sets', { table, key, value });
}

export function deleteHtableKey(table: string, key: string) {
  return requestClient.post('/htable/rpc/delete', { table, key });
}

export function dumpHtable(table: string) {
  return requestClient.get('/htable/rpc/dump', { params: { table } });
}

export function reloadHtable(table: string) {
  return requestClient.post('/htable/rpc/reload', { table });
}

// ========== 用户数据 ==========
export function getAliases(params: any) {
  return requestClient.get('/userdata/aliases', { params });
}

export function createAlias(data: any) {
  return requestClient.post('/userdata/aliases', data);
}

export function deleteAlias(id: number) {
  return requestClient.delete(`/userdata/aliases/${id}`);
}

export function getGroups(params: any) {
  return requestClient.get('/userdata/groups', { params });
}

export function createGroup(data: any) {
  return requestClient.post('/userdata/groups', data);
}

export function deleteGroup(id: number) {
  return requestClient.delete(`/userdata/groups/${id}`);
}

export function getSpeedDials(params: any) {
  return requestClient.get('/userdata/speed-dial', { params });
}

// ========== UAC 注册 ==========
export function getUacRegistrations(params: any) {
  return requestClient.get('/uac/registrations', { params });
}

export function createUacRegistration(data: any) {
  return requestClient.post('/uac/registrations', data);
}

export function updateUacRegistration(id: number, data: any) {
  return requestClient.patch(`/uac/registrations/${id}`, data);
}

export function deleteUacRegistration(id: number) {
  return requestClient.delete(`/uac/registrations/${id}`);
}

export function reloadUac() {
  return requestClient.post('/uac/reload');
}

export function getUacDump() {
  return requestClient.get('/uac/dump');
}

// ========== RTPEngine ==========
export function getRtpengineStatus() {
  return requestClient.get('/rtpengine/status');
}

export function showRtpengine() {
  return requestClient.get('/rtpengine/show');
}

export function reloadRtpengine() {
  return requestClient.post('/rtpengine/reload');
}

export function enableRtpengine(url: string, flag: number) {
  return requestClient.post('/rtpengine/enable', { url, flag });
}

// ========== 系统管理 ==========
export function getSystemInfo() {
  return requestClient.get('/system/info');
}

export function getSystemStatus() {
  return requestClient.get('/system/status');
}

export function getSystemVersion() {
  return requestClient.get('/system/version');
}

export function getSystemUptime() {
  return requestClient.get('/system/uptime');
}

export function getSystemMemory() {
  return requestClient.get('/system/memory');
}

export function getSystemProcesses() {
  return requestClient.get('/system/processes');
}

export function getSystemModules() {
  return requestClient.get('/system/modules');
}

export function getSystemStatistics(group?: string) {
  return requestClient.get('/system/statistics', { params: { group } });
}

export function getTlsList() {
  return requestClient.get('/system/tls/list');
}

export function getTlsInfo() {
  return requestClient.get('/system/tls/info');
}

export function reloadTls() {
  return requestClient.post('/system/tls/reload');
}

export function getPikeList() {
  return requestClient.get('/system/pike/list');
}

export function getPikeTop(limit?: number) {
  return requestClient.get('/system/pike/top', { params: { limit } });
}

export function getConfigList() {
  return requestClient.get('/system/config/list');
}

export function getConfig(group: string, name: string) {
  return requestClient.get('/system/config', { params: { group, name } });
}

export function setConfig(group: string, name: string, value: any) {
  return requestClient.post('/system/config', { group, name, value });
}

// ========== 存在服务 ==========
export function getPresentities(params: any) {
  return requestClient.get('/presence/presentities', { params });
}

export function getWatchers(params: any) {
  return requestClient.get('/presence/watchers', { params });
}

export function cleanupPresence() {
  return requestClient.post('/presence/cleanup');
}

export function getPresenceStats() {
  return requestClient.get('/presence/stats');
}

// ========== 离线消息 ==========
export function getMsiloMessages(params: any) {
  return requestClient.get('/msilo', { params });
}

export function getMsiloByUser(username: string, domain: string) {
  return requestClient.get('/msilo/user', { params: { username, domain } });
}

export function deleteMsiloMessage(id: number) {
  return requestClient.delete(`/msilo/${id}`);
}

export function cleanupMsilo() {
  return requestClient.post('/msilo/cleanup');
}

export function getMsiloStats() {
  return requestClient.get('/msilo/stats');
}

// ========== SIP 跟踪 ==========
export function getSipTraces(params: any) {
  return requestClient.get('/siptrace', { params });
}

export function getSipTraceByCallId(callid: string) {
  return requestClient.get('/siptrace/call', { params: { callid } });
}

export function getSipTraceStats() {
  return requestClient.get('/siptrace/stats');
}

export function cleanupSipTrace(days: number) {
  return requestClient.post('/siptrace/cleanup', { days });
}

// ========== 运营商路由 ==========
export function getCarrierNames() {
  return requestClient.get('/carrierroute/carriers');
}

export function createCarrierName(carrier: string) {
  return requestClient.post('/carrierroute/carriers', { carrier });
}

export function deleteCarrierName(id: number) {
  return requestClient.delete(`/carrierroute/carriers/${id}`);
}

export function getCarrierDomains() {
  return requestClient.get('/carrierroute/domains');
}

export function createCarrierDomain(domain: string) {
  return requestClient.post('/carrierroute/domains', { domain });
}

export function deleteCarrierDomain(id: number) {
  return requestClient.delete(`/carrierroute/domains/${id}`);
}

export function getCarrierFailureRoutes(params: any) {
  return requestClient.get('/carrierroute/failure-routes', { params });
}

export function reloadCarrierroute() {
  return requestClient.post('/carrierroute/reload');
}

export function dumpCarrierRoutes() {
  return requestClient.get('/carrierroute/dump');
}

// ========== 拨号计划 ==========
export function getDialplanRules(params: any) {
  return requestClient.get('/dialplan', { params });
}

export function createDialplanRule(data: any) {
  return requestClient.post('/dialplan', data);
}

export function updateDialplanRule(id: number, data: any) {
  return requestClient.patch(`/dialplan/${id}`, data);
}

export function deleteDialplanRule(id: number) {
  return requestClient.delete(`/dialplan/${id}`);
}

export function reloadDialplan() {
  return requestClient.post('/dialplan/reload');
}

export function translateDialplan(dpid: number, input: string) {
  return requestClient.get('/dialplan/translate', { params: { dpid, input } });
}

// ========== Mtree ==========
export function getMtreeRecords(params: any) {
  return requestClient.get('/mtree', { params });
}

export function createMtreeRecord(data: any) {
  return requestClient.post('/mtree', data);
}

export function deleteMtreeRecord(id: number) {
  return requestClient.delete(`/mtree/${id}`);
}

export function reloadMtree(tname: string) {
  return requestClient.post('/mtree/reload', { tname });
}

// ========== PDT ==========
export function getPdtRecords(params: any) {
  return requestClient.get('/pdt', { params });
}

export function createPdtRecord(data: any) {
  return requestClient.post('/pdt', data);
}

export function deletePdtRecord(id: number) {
  return requestClient.delete(`/pdt/${id}`);
}

export function reloadPdt() {
  return requestClient.post('/pdt/reload');
}

// ========== Topos ==========
export function getToposDialogs(params: any) {
  return requestClient.get('/topos/dialogs', { params });
}

export function cleanupTopos(days: number) {
  return requestClient.post('/topos/cleanup', { days });
}

// ========== 安全过滤 ==========
export function getSecfilterRules(params: any) {
  return requestClient.get('/secfilter', { params });
}

export function createSecfilterRule(data: any) {
  return requestClient.post('/secfilter', data);
}

export function deleteSecfilterRule(id: number) {
  return requestClient.delete(`/secfilter/${id}`);
}

export function reloadSecfilter() {
  return requestClient.post('/secfilter/reload');
}

export function getSecfilterStats() {
  return requestClient.get('/secfilter/stats');
}

export function addSecfilterBlacklist(type: number, data: string) {
  return requestClient.post('/secfilter/add-bl', { type, data });
}

export function addSecfilterWhitelist(type: number, data: string) {
  return requestClient.post('/secfilter/add-wl', { type, data });
}

// ========== 用户偏好 ==========
export function getUsrPreferences(params: any) {
  return requestClient.get('/usrpreferences', { params });
}

export function getUserPreferences(username: string, domain: string) {
  return requestClient.get('/usrpreferences/user', { params: { username, domain } });
}

export function createUsrPreference(data: any) {
  return requestClient.post('/usrpreferences', data);
}

export function deleteUsrPreference(id: number) {
  return requestClient.delete(`/usrpreferences/${id}`);
}

// ========== 数据库版本 ==========
export function getDbVersions() {
  return requestClient.get('/version');
}

export function getDbVersionStats() {
  return requestClient.get('/version/stats');
}
