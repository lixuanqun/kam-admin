import request from '../interceptor';

export interface Location { id: number; username: string; domain: string; contact: string; expires: string; userAgent: string; socket: string; lastModified: string; }

export const getLocations = (params: { page?: number; limit?: number; keyword?: string }) => request.get('/locations', { params });
export const getLocationStats = () => request.get('/locations/stats');
export const getOnlineCount = () => request.get('/locations/online-count');
export const deleteUserLocation = (username: string, domain: string) => request.delete(`/locations/${username}/${domain}`);
