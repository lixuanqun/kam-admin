<template>
  <a-menu
    :selected-keys="selectedKeys"
    :open-keys="openKeys"
    :style="{ width: '100%' }"
    @menu-item-click="onMenuItemClick"
    @sub-menu-click="onSubMenuClick"
    auto-open-selected
  >
    <template v-for="item in menuTree" :key="item.name">
      <!-- 有子菜单 -->
      <a-sub-menu v-if="item.children?.length" :key="item.name">
        <template #icon>
          <component :is="item.meta?.icon" />
        </template>
        <template #title>{{ item.meta?.title }}</template>
        <a-menu-item
          v-for="child in item.children"
          :key="child.name"
        >
          {{ child.meta?.title }}
        </a-menu-item>
      </a-sub-menu>
      <!-- 无子菜单（顶级页面） -->
      <a-menu-item v-else :key="item.name">
        <template #icon>
          <component :is="item.meta?.icon" />
        </template>
        {{ item.meta?.title }}
      </a-menu-item>
    </template>
  </a-menu>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useRouter, useRoute, type RouteRecordRaw } from 'vue-router';

const router = useRouter();
const route = useRoute();

/** 从路由配置中提取菜单树 */
const menuTree = computed(() => {
  const rootRoute = router.options.routes.find((r) => r.name === 'root');
  if (!rootRoute?.children) return [];

  return rootRoute.children
    .filter((r) => !r.meta?.hideInMenu)
    .sort((a, b) => (a.meta?.order ?? 99) - (b.meta?.order ?? 99))
    .map((r) => ({
      ...r,
      children: r.children
        ?.filter((c) => !c.meta?.hideInMenu)
        .sort((a, b) => (a.meta?.order ?? 99) - (b.meta?.order ?? 99)),
    }));
});

/** 当前选中项：匹配 route.name */
const selectedKeys = computed(() => {
  return [route.name as string];
});

/** 当前展开的子菜单 */
const openKeys = ref<string[]>([]);

/** 路由变化时自动展开父菜单 */
watch(
  () => route.name,
  () => {
    for (const item of menuTree.value) {
      if (item.children?.some((c: RouteRecordRaw) => c.name === route.name)) {
        if (!openKeys.value.includes(item.name as string)) {
          openKeys.value.push(item.name as string);
        }
        break;
      }
    }
  },
  { immediate: true }
);

function onMenuItemClick(key: string) {
  router.push({ name: key });
}

function onSubMenuClick(_key: string, keys: string[]) {
  openKeys.value = keys;
}
</script>
