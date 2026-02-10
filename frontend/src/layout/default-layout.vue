<template>
  <a-layout class="layout" :style="{ height: '100vh' }">
    <a-layout-sider
      collapsible
      :collapsed="collapsed"
      @collapse="onCollapse"
      :style="{ overflow: 'auto', height: '100vh' }"
      breakpoint="xl"
    >
      <div class="logo">
        <h1 v-if="!collapsed">Kamailio</h1>
        <h1 v-else>K</h1>
      </div>
      <a-menu
        :selected-keys="selectedKeys"
        :open-keys="openKeys"
        :style="{ width: '100%' }"
        @menu-item-click="onMenuItemClick"
        @sub-menu-click="onSubMenuClick"
        auto-open-selected
      >
        <a-menu-item key="monitoring">
          <template #icon><icon-dashboard /></template>
          监控面板
        </a-menu-item>

        <a-sub-menu key="user-mgmt">
          <template #icon><icon-user-group /></template>
          <template #title>用户管理</template>
          <a-menu-item key="subscribers">用户管理</a-menu-item>
          <a-menu-item key="domains">域管理</a-menu-item>
          <a-menu-item key="userdata">用户数据</a-menu-item>
          <a-menu-item key="usrpreferences">用户偏好</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="routing-mgmt">
          <template #icon><icon-relation /></template>
          <template #title>路由管理</template>
          <a-menu-item key="dispatchers">调度器</a-menu-item>
          <a-menu-item key="drouting">动态路由</a-menu-item>
          <a-menu-item key="lcr">LCR 路由</a-menu-item>
          <a-menu-item key="carrierroute">运营商路由</a-menu-item>
          <a-menu-item key="dialplan">拨号计划</a-menu-item>
          <a-menu-item key="mtree">内存树</a-menu-item>
          <a-menu-item key="pdt">前缀域转换</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="monitor-mgmt">
          <template #icon><icon-eye /></template>
          <template #title>实时监控</template>
          <a-menu-item key="locations">注册监控</a-menu-item>
          <a-menu-item key="dialogs">对话管理</a-menu-item>
          <a-menu-item key="siptrace">SIP 跟踪</a-menu-item>
          <a-menu-item key="topos">拓扑隐藏</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="security-mgmt">
          <template #icon><icon-safe /></template>
          <template #title>安全管理</template>
          <a-menu-item key="permissions">权限管理</a-menu-item>
          <a-menu-item key="secfilter">安全过滤</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="billing-mgmt">
          <template #icon><icon-file /></template>
          <template #title>计费跟踪</template>
          <a-menu-item key="cdr">CDR 记录</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="advanced-mgmt">
          <template #icon><icon-apps /></template>
          <template #title>高级功能</template>
          <a-menu-item key="uac">UAC 注册</a-menu-item>
          <a-menu-item key="htable">哈希表</a-menu-item>
          <a-menu-item key="presence">存在服务</a-menu-item>
          <a-menu-item key="msilo">离线消息</a-menu-item>
        </a-sub-menu>

        <a-sub-menu key="system-mgmt">
          <template #icon><icon-settings /></template>
          <template #title>系统管理</template>
          <a-menu-item key="system">系统管理</a-menu-item>
          <a-menu-item key="rtpengine">RTPEngine</a-menu-item>
          <a-menu-item key="version">数据库版本</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header :style="{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 20px', background: 'var(--color-bg-2)', borderBottom: '1px solid var(--color-border)' }">
        <div :style="{ fontSize: '16px', fontWeight: 'bold' }">
          Kamailio Dashboard
        </div>
        <a-space>
          <a-tooltip content="系统设置">
            <a-button type="text" shape="circle">
              <template #icon><icon-settings /></template>
            </a-button>
          </a-tooltip>
          <a-dropdown trigger="click">
            <a-avatar :size="32" :style="{ cursor: 'pointer', backgroundColor: '#165DFF' }">
              <icon-user />
            </a-avatar>
            <template #content>
              <a-doption @click="handleLogout">
                <template #icon><icon-export /></template>
                退出登录
              </a-doption>
            </template>
          </a-dropdown>
        </a-space>
      </a-layout-header>
      <a-layout-content :style="{ overflow: 'auto', padding: '0', background: 'var(--color-fill-2)' }">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/store';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const collapsed = ref(false);
const openKeys = ref<string[]>(['user-mgmt']);

const selectedKeys = computed(() => {
  const name = route.name as string;
  if (name?.startsWith('Kamailio')) {
    return [name.replace('Kamailio', '').charAt(0).toLowerCase() + name.replace('Kamailio', '').slice(1)];
  }
  return [name || 'monitoring'];
});

watch(route, () => {
  const name = route.name as string;
  if (!name) return;
  const key = name.startsWith('Kamailio')
    ? name.replace('Kamailio', '').charAt(0).toLowerCase() + name.replace('Kamailio', '').slice(1)
    : name;

  // Auto open parent submenu
  const menuMap: Record<string, string> = {
    subscribers: 'user-mgmt',
    domains: 'user-mgmt',
    userdata: 'user-mgmt',
    usrpreferences: 'user-mgmt',
    dispatchers: 'routing-mgmt',
    drouting: 'routing-mgmt',
    lcr: 'routing-mgmt',
    carrierroute: 'routing-mgmt',
    dialplan: 'routing-mgmt',
    mtree: 'routing-mgmt',
    pdt: 'routing-mgmt',
    locations: 'monitor-mgmt',
    dialogs: 'monitor-mgmt',
    siptrace: 'monitor-mgmt',
    topos: 'monitor-mgmt',
    permissions: 'security-mgmt',
    secfilter: 'security-mgmt',
    cdr: 'billing-mgmt',
    uac: 'advanced-mgmt',
    htable: 'advanced-mgmt',
    presence: 'advanced-mgmt',
    msilo: 'advanced-mgmt',
    system: 'system-mgmt',
    rtpengine: 'system-mgmt',
    version: 'system-mgmt',
  };
  const parent = menuMap[key];
  if (parent && !openKeys.value.includes(parent)) {
    openKeys.value.push(parent);
  }
}, { immediate: true });

function onCollapse(val: boolean) {
  collapsed.value = val;
}

function onMenuItemClick(key: string) {
  const routeMap: Record<string, string> = {
    monitoring: 'KamailioMonitoring',
    subscribers: 'KamailioSubscribers',
    domains: 'KamailioDomains',
    userdata: 'KamailioUserdata',
    usrpreferences: 'KamailioUsrpreferences',
    dispatchers: 'KamailioDispatchers',
    drouting: 'KamailioDrouting',
    lcr: 'KamailioLcr',
    carrierroute: 'KamailioCarrierroute',
    dialplan: 'KamailioDialplan',
    mtree: 'KamailioMtree',
    pdt: 'KamailioPdt',
    locations: 'KamailioLocations',
    dialogs: 'KamailioDialogs',
    siptrace: 'KamailioSiptrace',
    topos: 'KamailioTopos',
    permissions: 'KamailioPermissions',
    secfilter: 'KamailioSecfilter',
    cdr: 'KamailioCdr',
    uac: 'KamailioUac',
    htable: 'KamailioHtable',
    presence: 'KamailioPresence',
    msilo: 'KamailioMsilo',
    rtpengine: 'KamailioRtpengine',
    system: 'KamailioSystem',
    version: 'KamailioVersion',
  };
  const routeName = routeMap[key];
  if (routeName) {
    router.push({ name: routeName });
  }
}

function onSubMenuClick(key: string, openKeysArr: string[]) {
  openKeys.value = openKeysArr;
}

function handleLogout() {
  userStore.logout();
  router.push({ name: 'login' });
}
</script>

<style scoped lang="less">
.layout {
  width: 100%;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 54px;
  color: var(--color-white);

  h1 {
    margin: 0;
    font-size: 18px;
    font-weight: bold;
    white-space: nowrap;
  }
}
</style>
