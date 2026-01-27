<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { Table, Button, Space, Card, Row, Col, Statistic, Tag, Popconfirm, message } from 'ant-design-vue';
import { ReloadOutlined, PhoneOutlined } from '@ant-design/icons-vue';
import { getDialogs, getActiveDialogsList, getDialogStats, endDialog } from '#/api/kamailio';

const loading = ref(false);
const dbData = ref<any[]>([]);
const activeData = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const stats = ref({ active: 0, dbCount: 0 });

let refreshTimer: any = null;

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
  { title: 'From URI', dataIndex: 'fromUri', ellipsis: true },
  { title: 'To URI', dataIndex: 'toUri', ellipsis: true },
  { title: '状态', dataIndex: 'state', width: 80 },
  { title: '开始时间', dataIndex: 'startTime', width: 120 },
];

const activeColumns = [
  { title: 'Hash', dataIndex: 'h_entry', width: 80 },
  { title: 'ID', dataIndex: 'h_id', width: 80 },
  { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
  { title: 'From', dataIndex: 'from_uri', ellipsis: true },
  { title: 'To', dataIndex: 'to_uri', ellipsis: true },
  { title: '状态', dataIndex: 'state', width: 100 },
  { title: '操作', key: 'action', width: 100 },
];

async function loadDbDialogs() {
  loading.value = true;
  try {
    const res = await getDialogs({ page: pagination.current, limit: pagination.pageSize });
    if (res.data?.code === 0) {
      dbData.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

async function loadActiveDialogs() {
  try {
    const res = await getActiveDialogsList();
    if (res.data?.code === 0) {
      activeData.value = res.data.data || [];
    }
  } catch {}
}

async function loadStats() {
  try {
    const res = await getDialogStats();
    if (res.data?.code === 0) {
      stats.value = res.data.data;
    }
  } catch {}
}

async function handleEndDialog(record: any) {
  try {
    await endDialog(record.h_entry, record.h_id);
    message.success('对话已结束');
    loadActiveDialogs();
    loadStats();
  } catch {
    message.error('操作失败');
  }
}

function loadAll() {
  loadDbDialogs();
  loadActiveDialogs();
  loadStats();
}

onMounted(() => {
  loadAll();
  refreshTimer = setInterval(loadAll, 10000);
});

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer);
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="8">
        <Card>
          <Statistic title="活动对话" :value="stats.active">
            <template #prefix><PhoneOutlined /></template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="8">
        <Card>
          <Statistic title="数据库记录" :value="stats.dbCount" />
        </Card>
      </Col>
    </Row>

    <Card title="活动对话（内存）" class="mb-4">
      <template #extra>
        <Button @click="loadActiveDialogs"><ReloadOutlined /> 刷新</Button>
      </template>
      <Table :columns="activeColumns" :dataSource="activeData" :pagination="false" rowKey="h_id" size="small">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'state'">
            <Tag color="green">{{ record.state }}</Tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <Popconfirm title="确定结束此对话？" @confirm="handleEndDialog(record)">
              <Button type="link" size="small" danger>结束</Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>

    <Card title="对话记录（数据库）">
      <template #extra>
        <Button @click="loadDbDialogs"><ReloadOutlined /> 刷新</Button>
      </template>
      <Table :columns="columns" :dataSource="dbData" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadDbDialogs(); }" />
    </Card>
  </div>
</template>
