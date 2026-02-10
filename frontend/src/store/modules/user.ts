import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface UserInfo {
  name: string;
  avatar?: string;
  role: string;
}

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '');
  const userInfo = ref<UserInfo>({
    name: 'Admin',
    role: 'admin',
  });
  const isLoggedIn = ref(!!localStorage.getItem('token'));

  function setToken(val: string) {
    token.value = val;
    localStorage.setItem('token', val);
    isLoggedIn.value = !!val;
  }

  function setUserInfo(info: UserInfo) {
    userInfo.value = info;
  }

  function logout() {
    token.value = '';
    localStorage.removeItem('token');
    isLoggedIn.value = false;
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    setToken,
    setUserInfo,
    logout,
  };
});
