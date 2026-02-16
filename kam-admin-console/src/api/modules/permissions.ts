import request from '../interceptor';

export interface Address { id: number; grp: number; ipAddr: string; mask: number; port: number; tag?: string; }
export interface Trusted { id: number; srcIp: string; proto?: string; fromPattern?: string; tag?: string; priority: number; }

export const getAddresses = (params: any) => request.get('/permissions/address', { params });
export const createAddress = (data: Partial<Address>) => request.post('/permissions/address', data);
export const updateAddress = (id: number, data: Partial<Address>) => request.patch(`/permissions/address/${id}`, data);
export const deleteAddress = (id: number) => request.delete(`/permissions/address/${id}`);
export const getTrusted = (params: any) => request.get('/permissions/trusted', { params });
export const createTrusted = (data: Partial<Trusted>) => request.post('/permissions/trusted', data);
export const updateTrusted = (id: number, data: Partial<Trusted>) => request.patch(`/permissions/trusted/${id}`, data);
export const deleteTrusted = (id: number) => request.delete(`/permissions/trusted/${id}`);
export const reloadPermissions = () => request.post('/permissions/reload');
export const reloadTrustedPermissions = () => request.post('/permissions/reload-trusted');
export const addressDump = () => request.get('/permissions/address-dump');
export const subnetDump = () => request.get('/permissions/subnet-dump');
