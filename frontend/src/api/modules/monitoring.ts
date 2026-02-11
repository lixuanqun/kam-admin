import request from '../interceptor';

export const checkHealth = () => request.get('/monitoring/health');
export const getDashboardData = () => request.get('/monitoring/dashboard');
export const getStatistics = (group?: string) => request.get('/monitoring/statistics', { params: { group } });
export const getCoreInfo = () => request.get('/monitoring/core-info');
export const getActiveDialogs = () => request.get('/monitoring/dialogs');
export const getSystemOverview = () => request.get('/monitoring/overview');
