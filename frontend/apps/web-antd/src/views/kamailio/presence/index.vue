<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Tabs, TabPane, Row, Col, Statistic, message } from 'ant-design-vue';
import { ReloadOutlined, ClearOutlined } from '@ant-design/icons-vue';
import { getPresentities, getWatchers, cleanupPresence, getPresenceStats } from '#/api/kamailio';

const activeTab = ref('presentities');
const stats = ref({ presentities: 0, watchers: 0 });

// Presentities
const presLoading = ref(false);
const presData = ref<any[]>([]);
const presPagination = reactive({ current: 1, pageSize: 20, total: 0 });

const presColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '事件', dataIndex: 'event' },
  { title: 'ETag', dataIndex: 'etag', ellipsis: true },
  { title: '过期时间', dataIndex: 'expires' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
];

async function loadPresentities() {
  presLoading.value = true;
  try {
    const res = await getPresentities({ page: presPagination.current, limit: presPagination.pageSize });
    if (res.data?.code === 0) {
      presData.value = res.data.data.items;
      presPagination.total = res.data.data.total;
    }
  } finally { presLoading.value = false; }
}

// Watchers
const watchLoading = ref(false);
const watchData = ref<any[]>([]);
const watchPagination = reactive({ current: 1, pageSize: 20, total: 0 });

const watchColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'Presentity URI', dataIndex: 'presentityUri', ellipsis: true },
  { title: 'Watcher', dataIndex: 'watcherUsername' },
  { title: 'To User', dataIndex: 'toUser' },
  { title: '事件', dataIndex: 'event' },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '过期时间', dataIndex: 'expires' },
];

async function loadWatchers() {
  watchLoading.value = true;
  try {
    const res = await getWatchers({ page: watchPagination.current, limit: watchPagination.pageSize });
    if (res.data?.code === 0) {
      watchData.value = res.data.data.items;
      watchPagination.total = res.data.data.total;
    }
  } finally { watchLoading.value = false; }
}

async function handleCleanup() {
  await cleanupPresence();
  message.success('清理成功');
  loadStats();
  loadPresentities();
  loadWatchers();
}

async function loadStats() {
  try {
    const res = await getPresenceStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

onMounted(() => {
  loadPresentities();
  loadWatchers();
  loadStats();
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="8"><Card><Statistic title="Presentities" :value="stats.presentities" /></Card></Col>
      <Col :span="8"><Card><Statistic title="活动 Watchers" :value="stats.watchers" /></Card></Col>
    </Row>

    <Card>
      <Tabs v-model:activeKey="activeTab">
        <TabPane key="presentities" tab="Presentities">
          <Space class="mb-4">
            <Button @click="loadPresentities"><ReloadOutlined /> 刷新</Button>
            <Button @click="handleCleanup"><ClearOutlined /> 清理过期</Button>
          </Space>
          <Table :columns="presColumns" :dataSource="presData" :loading="presLoading" :pagination="presPagination" rowKey="id" @change="(p) => { presPagination.current = p.current; loadPresentities(); }" />
        </TabPane>
        <TabPane key="watchers" tab="Watchers">
          <Space class="mb-4">
            <Button @click="loadWatchers"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="watchColumns" :dataSource="watchData" :loading="watchLoading" :pagination="watchPagination" rowKey="id" @change="(p) => { watchPagination.current = p.current; loadWatchers(); }" />
        </TabPane>
      </Tabs>
    </Card>
  </div>
</template>
