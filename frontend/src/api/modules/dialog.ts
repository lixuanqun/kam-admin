import request from '../interceptor';

export const getDialogs = (params: any) => request.get('/dialogs', { params });
export const getActiveDialogsList = () => request.get('/dialogs/active');
export const getDialogStats = () => request.get('/dialogs/stats');
export const endDialog = (hashEntry: number, hashId: number) => request.post('/dialogs/end', { hashEntry, hashId });
