<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Row, Col, Statistic, message } from 'ant-design-vue';
import { ReloadOutlined, ClearOutlined } from '@ant-design/icons-vue';
import { getToposDialogs, cleanupTopos } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '时间', dataIndex: 'rectime', width: 180 },
  { title: 'A CallID', dataIndex: 'a_callid', ellipsis: true },
  { title: 'A UUID', dataIndex: 'a_uuid', ellipsis: true },
  { title: 'B UUID', dataIndex: 'b_uuid', ellipsis: true },
  { title: 'A Tag', dataIndex: 'a_tag', ellipsis: true },
  { title: 'B Tag', dataIndex: 'b_tag', ellipsis: true },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getToposDialogs({ page: pagination.current, limit: pagination.pageSize });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

async function handleCleanup() {
  const res = await cleanupTopos(7);
  message.success(res.data?.message || '清理成功');
  loadData();
}

onMounted(() => loadData());
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="8"><Card><Statistic title="总记录数" :value="pagination.total" /></Card></Col>
    </Row>

    <Card title="拓扑隐藏 (Topos)">
      <template #extra>
        <Space>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button danger @click="handleCleanup"><ClearOutlined /> 清理 (7天前)</Button>
        </Space>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }" />
    </Card>
  </div>
</template>
