<template>
  <a-layout class="layout">
    <!-- 侧边栏 -->
    <a-layout-sider
      collapsible
      :collapsed="appStore.menuCollapsed"
      @collapse="appStore.setCollapsed"
      :style="{ overflow: 'auto', height: '100vh' }"
      breakpoint="xl"
      :width="appStore.menuWidth"
    >
      <div class="logo">
        <h1 v-if="!appStore.menuCollapsed" class="logo-text">Kamailio</h1>
        <h1 v-else class="logo-text">K</h1>
      </div>
      <AppMenu />
    </a-layout-sider>

    <!-- 右侧主区域 -->
    <a-layout>
      <!-- 顶部导航栏 -->
      <a-layout-header class="layout-header">
        <div class="header-left">
          <a-button type="text" class="collapse-btn" @click="appStore.toggleCollapsed">
            <template #icon>
              <icon-menu-unfold v-if="appStore.menuCollapsed" />
              <icon-menu-fold v-else />
            </template>
          </a-button>
          <Breadcrumb />
        </div>
        <div class="header-right">
          <a-space :size="16">
            <!-- 暗黑模式切换 -->
            <a-tooltip :content="appStore.theme === 'light' ? '切换暗黑模式' : '切换亮色模式'">
              <a-button type="text" shape="circle" @click="appStore.toggleTheme">
                <template #icon>
                  <icon-moon-fill v-if="appStore.theme === 'light'" />
                  <icon-sun-fill v-else />
                </template>
              </a-button>
            </a-tooltip>
            <!-- 全屏 -->
            <a-tooltip content="全屏">
              <a-button type="text" shape="circle" @click="toggleFullscreen">
                <template #icon><icon-fullscreen /></template>
              </a-button>
            </a-tooltip>
            <!-- 用户信息 -->
            <a-dropdown trigger="click">
              <a-space class="user-area" :size="8">
                <a-avatar :size="28" :style="{ backgroundColor: '#165DFF' }">
                  <icon-user />
                </a-avatar>
                <span class="user-name">{{ userStore.userInfo.name }}</span>
              </a-space>
              <template #content>
                <a-doption @click="handleLogout">
                  <template #icon><icon-export /></template>
                  退出登录
                </a-doption>
              </template>
            </a-dropdown>
          </a-space>
        </div>
      </a-layout-header>

      <!-- 页面标题栏 -->
      <div class="page-header" v-if="currentTitle">
        <h3 class="page-title">{{ currentTitle }}</h3>
      </div>

      <!-- 内容区域 -->
      <a-layout-content class="layout-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore, useAppStore } from '@/store';
import { Breadcrumb } from '@/components';
import AppMenu from '@/components/menu/index.vue';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const appStore = useAppStore();

// 初始化主题
appStore.initTheme();

/** 当前页面标题 */
const currentTitle = computed(() => {
  return (route.meta?.title as string) || '';
});

/** 全屏切换 */
function toggleFullscreen() {
  if (document.fullscreenElement) {
    document.exitFullscreen();
  } else {
    document.documentElement.requestFullscreen();
  }
}

/** 退出登录 */
function handleLogout() {
  userStore.logout();
  router.push({ name: 'login' });
}
</script>

<style scoped lang="less">
.layout {
  width: 100%;
  height: 100vh;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 54px;
  color: var(--color-white);

  &-text {
    margin: 0;
    font-size: 18px;
    font-weight: bold;
    white-space: nowrap;
  }
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 54px;
  padding: 0 16px;
  background: var(--color-bg-2);
  border-bottom: 1px solid var(--color-border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-right {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 18px;
  color: var(--color-text-2);
}

.user-area {
  cursor: pointer;
}

.user-name {
  font-size: 14px;
  color: var(--color-text-1);
}

.page-header {
  padding: 12px 20px 0;
  background: var(--color-fill-2);
}

.page-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-1);
}

.layout-content {
  overflow: auto;
  padding: 0;
  background: var(--color-fill-2);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
