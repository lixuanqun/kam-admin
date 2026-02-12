# Arco Design Vue SKILL

> 基于 [Arco Design Vue 官方文档](https://arco.design/vue/docs/start) 总结的开发技能与最佳实践

## 一、快速开始

### 1.1 安装

```bash
# npm
npm install @arco-design/web-vue

# yarn
yarn add @arco-design/web-vue

# pnpm
pnpm add @arco-design/web-vue
```

### 1.2 全局引入

```typescript
// main.ts
import { createApp } from 'vue';
import ArcoVue from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';  // 图标按需引入
import '@arco-design/web-vue/dist/arco.css';

import App from './App.vue';

const app = createApp(App);
app.use(ArcoVue);
app.use(ArcoVueIcon);  // 必须单独引入图标组件
app.mount('#app');
```

### 1.3 按需引入（可选）

若需减小打包体积，可配合 `unplugin-vue-components` 按需引入，仅打包使用到的组件。

---

## 二、核心特性

| 特性 | 说明 |
|------|------|
| **组件数量** | 60+ 开箱即用组件 |
| **主题定制** | 支持 Less 变量 / Design Lab 在线配置 |
| **TypeScript** | 全组件 TS 编写，类型友好 |
| **Vue 3** | 基于 Vue 3 Composition API |

---

## 三、常用组件与用法

### 3.1 布局组件

```vue
<!-- 布局结构 -->
<a-layout>
  <a-layout-sider collapsible>侧边栏</a-layout-sider>
  <a-layout>
    <a-layout-header>顶部</a-layout-header>
    <a-layout-content>内容区</a-layout-content>
  </a-layout>
</a-layout>

<!-- 栅格 -->
<a-row :gutter="16">
  <a-col :span="6"><a-card>...</a-card></a-col>
  <a-col :span="6"><a-card>...</a-card></a-col>
</a-row>
```

### 3.2 表单组件

```vue
<!-- 表单 -->
<a-form ref="formRef" :model="formState" layout="vertical">
  <a-form-item field="username" label="用户名" :rules="[{ required: true, message: '必填' }]">
    <a-input v-model="formState.username" />
  </a-form-item>
  <a-form-item field="password" label="密码">
    <a-input-password v-model="formState.password" />
  </a-form-item>
</a-form>

<!-- 弹窗表单 -->
<a-modal v-model:visible="modalVisible" title="编辑" @ok="handleSubmit" @cancel="modalVisible = false">
  <a-form>...</a-form>
</a-modal>
```

### 3.3 表格组件

```vue
<a-table
  :columns="columns"
  :data="data"
  :loading="loading"
  :pagination="paginationProps"
  row-key="id"
  @page-change="onPageChange"
>
  <!-- 插槽定制列 -->
  <template #action="{ record }">
    <a-space>
      <a-button type="text" size="small" @click="handleEdit(record)">编辑</a-button>
      <a-popconfirm content="确定删除？" @ok="handleDelete(record.id)">
        <a-button type="text" status="danger" size="small">删除</a-button>
      </a-popconfirm>
    </a-space>
  </template>
</a-table>
```

### 3.4 消息反馈

```typescript
import { Message } from '@arco-design/web-vue';

Message.success('操作成功');
Message.error('操作失败');
Message.warning('请注意');
```

### 3.5 图标使用

```vue
<!-- 使用 icon- 前缀的组件 -->
<template #icon><icon-search /></template>
<template #icon><icon-plus /></template>
<template #icon><icon-edit /></template>
<template #icon><icon-delete /></template>
<template #icon><icon-refresh /></template>
```

### 3.6 菜单

```vue
<a-menu
  :selected-keys="selectedKeys"
  :open-keys="openKeys"
  @menu-item-click="onMenuItemClick"
  @sub-menu-click="onSubMenuClick"
  auto-open-selected
>
  <a-sub-menu key="sub" v-if="hasChildren">
    <template #icon><component :is="icon" /></template>
    <template #title>子菜单</template>
    <a-menu-item v-for="child in children" :key="child.name">
      {{ child.title }}
    </a-menu-item>
  </a-sub-menu>
  <a-menu-item key="item" v-else>
    <template #icon><component :is="icon" /></template>
    菜单项
  </a-menu-item>
</a-menu>
```

### 3.7 卡片与统计

```vue
<a-card class="general-card">
  <a-statistic title="标题" :value="count" />
</a-card>
```

### 3.8 按钮与操作区

```vue
<a-space>
  <a-input v-model="keyword" placeholder="搜索" @press-enter="handleSearch">
    <template #prefix><icon-search /></template>
  </a-input>
  <a-button type="primary" @click="handleSearch">搜索</a-button>
  <a-button @click="loadData">
    <template #icon><icon-refresh /></template>刷新
  </a-button>
  <a-button type="primary" @click="handleAdd">
    <template #icon><icon-plus /></template>新增
  </a-button>
</a-space>
```

---

## 四、主题定制

### 4.1 Vite + Less 变量

```typescript
// vite.config.ts
export default defineConfig({
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          'arcoblue-6': '#165DFF',  // 主色
        },
        javascriptEnabled: true,
      },
    },
  },
});
```

### 4.2 引入自定义样式

```less
// global.less
@import '@arco-design/web-vue/dist/arco.css';
// 自定义覆盖
```

### 4.3 暗黑模式

参考官方文档: https://arco.design/vue/docs/dark

---

## 五、常见 API 模式

### 5.1 表格分页

```typescript
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const paginationProps = computed(() => ({
  current: pagination.current,
  pageSize: pagination.pageSize,
  total: pagination.total,
  showTotal: true,
  showPageSize: true,
}));

const onPageChange = (page: number, pageSize: number) => {
  pagination.current = page;
  pagination.pageSize = pageSize;
  loadData();
};
```

### 5.2 表单校验与提交

```typescript
const formRef = ref();
const handleSubmit = async () => {
  try {
    await formRef.value?.validate();
    // 提交逻辑
    modalVisible.value = false;
    Message.success('保存成功');
    loadData();
  } catch (e) {
    // 校验失败
  }
};
```

### 5.3 确认弹窗

```vue
<a-popconfirm content="确定删除？" @ok="handleDelete(id)">
  <a-button type="text" status="danger">删除</a-button>
</a-popconfirm>
```

---

## 六、生态与扩展

| 资源 | 链接 |
|------|------|
| 官网文档 | https://arco.design/vue/docs/start |
| 主题配置 | https://arco.design/themes |
| 暗黑模式 | https://arco.design/vue/docs/dark |
| Figma 设计 | [Figma 组件库](https://www.figma.com/file/...) |
| Arco Pro | https://arco.design/pro/ |

---

## 七、本项目约定

- **布局**: 使用 `a-layout` + `a-layout-sider` + `a-layout-header` + `a-layout-content`
- **页面结构**: `a-row` / `a-col` 统计卡片 + `a-card` 搜索栏 + `a-card` 数据表格
- **表单**: `a-modal` 包裹 `a-form`，`layout="vertical"`
- **表格**: 使用 `#action` 等插槽实现操作列
- **消息**: 统一使用 `Message` 做成功/失败提示
- **样式**: 通过 `vite.config.ts` 的 `modifyVars` 定制主色

---

## 八、注意事项

1. **图标**：必须 `app.use(ArcoVueIcon)` 才能使用 `<icon-xxx />` 组件
2. **Form 校验**：`a-form-item` 的 `field` 需与 `formState` 字段一致
3. **Modal**：使用 `v-model:visible` 控制显示
4. **Table**：必须设置 `row-key`，建议用唯一 id
5. **TypeScript**：组件均提供类型，可充分利用 IDE 提示
