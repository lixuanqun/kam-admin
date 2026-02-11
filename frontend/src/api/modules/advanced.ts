import request from '../interceptor';

// ===== 哈希表 =====
export const getHtableRecords = (params: any) => request.get('/htable', { params });
export const getHtableValue = (table: string, key: string) => request.get('/htable/rpc/get', { params: { table, key } });
export const setHtableString = (table: string, key: string, value: string) => request.post('/htable/rpc/sets', { table, key, value });
export const deleteHtableKey = (table: string, key: string) => request.post('/htable/rpc/delete', { table, key });
export const dumpHtable = (table: string) => request.get('/htable/rpc/dump', { params: { table } });
export const reloadHtable = (table: string) => request.post('/htable/rpc/reload', { table });

// ===== 用户数据 =====
export const getAliases = (params: any) => request.get('/userdata/aliases', { params });
export const createAlias = (data: any) => request.post('/userdata/aliases', data);
export const deleteAlias = (id: number) => request.delete(`/userdata/aliases/${id}`);
export const getGroups = (params: any) => request.get('/userdata/groups', { params });
export const createGroup = (data: any) => request.post('/userdata/groups', data);
export const deleteGroup = (id: number) => request.delete(`/userdata/groups/${id}`);
export const getSpeedDials = (params: any) => request.get('/userdata/speed-dial', { params });

// ===== UAC 注册 =====
export const getUacRegistrations = (params: any) => request.get('/uac/registrations', { params });
export const createUacRegistration = (data: any) => request.post('/uac/registrations', data);
export const updateUacRegistration = (id: number, data: any) => request.patch(`/uac/registrations/${id}`, data);
export const deleteUacRegistration = (id: number) => request.delete(`/uac/registrations/${id}`);
export const reloadUac = () => request.post('/uac/reload');
export const getUacDump = () => request.get('/uac/dump');

// ===== 用户偏好 =====
export const getUsrPreferences = (params: any) => request.get('/usrpreferences', { params });
export const getUserPreferences = (username: string, domain: string) => request.get('/usrpreferences/user', { params: { username, domain } });
export const createUsrPreference = (data: any) => request.post('/usrpreferences', data);
export const deleteUsrPreference = (id: number) => request.delete(`/usrpreferences/${id}`);

// ===== 存在服务 =====
export const getPresentities = (params: any) => request.get('/presence/presentities', { params });
export const getWatchers = (params: any) => request.get('/presence/watchers', { params });
export const cleanupPresence = () => request.post('/presence/cleanup');
export const getPresenceStats = () => request.get('/presence/stats');

// ===== 离线消息 =====
export const getMsiloMessages = (params: any) => request.get('/msilo', { params });
export const getMsiloByUser = (username: string, domain: string) => request.get('/msilo/user', { params: { username, domain } });
export const deleteMsiloMessage = (id: number) => request.delete(`/msilo/${id}`);
export const cleanupMsilo = () => request.post('/msilo/cleanup');
export const getMsiloStats = () => request.get('/msilo/stats');
