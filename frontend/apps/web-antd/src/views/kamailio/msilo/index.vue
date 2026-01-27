<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Row, Col, Statistic, Popconfirm, message, Input } from 'ant-design-vue';
import { ReloadOutlined, SearchOutlined, ClearOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getMsiloMessages, deleteMsiloMessage, cleanupMsilo, getMsiloStats } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');
const stats = ref({ total: 0, expired: 0, active: 0 });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '源地址', dataIndex: 'srcAddr', ellipsis: true },
  { title: '目标地址', dataIndex: 'dstAddr', ellipsis: true },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '存储时间', dataIndex: 'incTime' },
  { title: '过期时间', dataIndex: 'expTime' },
  { title: '内容类型', dataIndex: 'contentType' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getMsiloMessages({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

function handleSearch() {
  pagination.current = 1;
  loadData();
}

async function handleDelete(id: number) {
  await deleteMsiloMessage(id);
  message.success('删除成功');
  loadData();
  loadStats();
}

async function handleCleanup() {
  const res = await cleanupMsilo();
  message.success(res.data?.message || '清理成功');
  loadData();
  loadStats();
}

async function loadStats() {
  try {
    const res = await getMsiloStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

onMounted(() => {
  loadData();
  loadStats();
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="6"><Card><Statistic title="总消息数" :value="stats.total" /></Card></Col>
      <Col :span="6"><Card><Statistic title="活动消息" :value="stats.active" /></Card></Col>
      <Col :span="6"><Card><Statistic title="过期消息" :value="stats.expired" /></Card></Col>
    </Row>

    <Card title="离线消息管理">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索用户名" style="width: 200px" @pressEnter="handleSearch">
            <template #suffix><SearchOutlined @click="handleSearch" /></template>
          </Input>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button danger @click="handleCleanup"><ClearOutlined /> 清理过期</Button>
        </Space>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <Popconfirm title="确定删除？" @confirm="handleDelete(record.id)">
              <Button type="link" size="small" danger><DeleteOutlined /></Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>
  </div>
</template>
