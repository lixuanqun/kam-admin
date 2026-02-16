import axios from 'axios';
import type { AxiosInstance } from 'axios';
import { Message } from '@arco-design/web-vue';
import router from '@/router';

const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
});

// Request interceptor - 从 store 获取 token
request.interceptors.request.use(
  (config) => {
    // 使用动态 import 避免循环依赖
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
request.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error;
    if (response) {
      const { status, data } = response;
      if (status === 401) {
        // 清除 token 并通过 router 跳转到登录页
        localStorage.removeItem('token');
        Message.error('登录已过期，请重新登录');
        router.push({
          name: 'login',
          query: { redirect: router.currentRoute.value.fullPath },
        });
        return Promise.reject(error);
      }
      const msg = data?.error || data?.message || `请求失败 (${status})`;
      Message.error(msg);
    } else {
      Message.error('网络连接异常');
    }
    return Promise.reject(error);
  }
);

export default request;
