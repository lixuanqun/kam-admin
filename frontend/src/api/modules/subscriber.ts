import request from '../interceptor';

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

export const getSubscribers = (params: { page?: number; limit?: number; keyword?: string }) =>
  request.get('/subscribers', { params });
export const getSubscriber = (id: number) => request.get(`/subscribers/${id}`);
export const createSubscriber = (data: CreateSubscriberDto) => request.post('/subscribers', data);
export const updateSubscriber = (id: number, data: UpdateSubscriberDto) => request.patch(`/subscribers/${id}`, data);
export const deleteSubscriber = (id: number) => request.delete(`/subscribers/${id}`);
export const getSubscriberStats = () => request.get('/subscribers/stats');
