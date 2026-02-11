<template>
  <a-breadcrumb>
    <a-breadcrumb-item v-for="item in items" :key="item.name">
      <span v-if="item.name === route.name || !item.path">{{ item.title }}</span>
      <router-link v-else :to="item.path">{{ item.title }}</router-link>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

interface BreadcrumbItem {
  name: string;
  title: string;
  path?: string;
}

const items = computed<BreadcrumbItem[]>(() => {
  const matched = route.matched.filter(
    (r) => r.meta?.title && !r.meta?.hideInMenu
  );
  return matched.map((r) => ({
    name: r.name as string,
    title: (r.meta?.title as string) || '',
    path: r.path,
  }));
});
</script>
