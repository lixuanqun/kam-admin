import request from '../interceptor';

export interface Dispatcher { id: number; setid: number; destination: string; flags: number; priority: number; attrs: string; description: string; }
export interface CreateDispatcherDto { setid: number; destination: string; flags?: number; priority?: number; attrs?: string; description?: string; }

export const getDispatchers = (params: { page?: number; limit?: number; keyword?: string }) => request.get('/dispatchers', { params });
export const getDispatcher = (id: number) => request.get(`/dispatchers/${id}`);
export const createDispatcher = (data: CreateDispatcherDto) => request.post('/dispatchers', data);
export const updateDispatcher = (id: number, data: Partial<CreateDispatcherDto>) => request.patch(`/dispatchers/${id}`, data);
export const deleteDispatcher = (id: number) => request.delete(`/dispatchers/${id}`);
export const reloadDispatchers = () => request.post('/dispatchers/reload');
export const getDispatcherStats = () => request.get('/dispatchers/stats');
export const getDispatcherStatus = () => request.get('/dispatchers/status');
export const setDispatcherState = (state: string, group: number, address: string) => request.post('/dispatchers/set-state', { state, group, address });
