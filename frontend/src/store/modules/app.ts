import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const theme = ref<'light' | 'dark'>(
    (localStorage.getItem('arco-theme') as 'light' | 'dark') || 'light'
  );
  const menuCollapsed = ref(false);
  const menuWidth = ref(220);

  function toggleTheme() {
    theme.value = theme.value === 'light' ? 'dark' : 'light';
    localStorage.setItem('arco-theme', theme.value);
    if (theme.value === 'dark') {
      document.body.setAttribute('arco-theme', 'dark');
    } else {
      document.body.removeAttribute('arco-theme');
    }
  }

  /** 初始化主题（应在 app 启动时调用） */
  function initTheme() {
    if (theme.value === 'dark') {
      document.body.setAttribute('arco-theme', 'dark');
    }
  }

  function setCollapsed(val: boolean) {
    menuCollapsed.value = val;
  }

  function toggleCollapsed() {
    menuCollapsed.value = !menuCollapsed.value;
  }

  return {
    theme,
    menuCollapsed,
    menuWidth,
    toggleTheme,
    initTheme,
    setCollapsed,
    toggleCollapsed,
  };
});
