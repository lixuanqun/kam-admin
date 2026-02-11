import request from '../interceptor';

export interface Domain { id: number; domain: string; did?: string; lastModified: string; }
export interface CreateDomainDto { domain: string; did?: string; }

export const getDomains = (params: { page?: number; limit?: number; keyword?: string }) => request.get('/domains', { params });
export const getAllDomains = () => request.get('/domains/all');
export const getDomain = (id: number) => request.get(`/domains/${id}`);
export const createDomain = (data: CreateDomainDto) => request.post('/domains', data);
export const updateDomain = (id: number, data: Partial<CreateDomainDto>) => request.patch(`/domains/${id}`, data);
export const deleteDomain = (id: number) => request.delete(`/domains/${id}`);
export const reloadDomains = () => request.post('/domains/reload');
