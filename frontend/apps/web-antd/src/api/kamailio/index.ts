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
