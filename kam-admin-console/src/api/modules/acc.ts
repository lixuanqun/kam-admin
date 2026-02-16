import request from '../interceptor';

export interface AccRecord { id: number; method: string; callid: string; sipCode: string; sipReason: string; time: string; srcUser: string; srcDomain: string; srcIp: string; dstUser: string; dstDomain: string; }

export const getCdrRecords = (params: any) => request.get('/acc/cdr', { params });
export const getMissedCalls = (params: any) => request.get('/acc/missed-calls', { params });
export const getAccStats = (params?: any) => request.get('/acc/stats', { params });
export const getTodayStats = () => request.get('/acc/today');
