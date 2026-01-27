<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Table, Button, Card, Row, Col, Statistic } from 'ant-design-vue';
import { ReloadOutlined, DatabaseOutlined } from '@ant-design/icons-vue';
import { getDbVersions, getDbVersionStats } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const stats = ref({ totalTables: 0 });

const columns = [
  { title: '表名', dataIndex: 'tableName' },
  { title: '版本号', dataIndex: 'tableVersion', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getDbVersions();
    if (res.data?.code === 0) {
      data.value = res.data.data;
    }
  } finally { loading.value = false; }
}

async function loadStats() {
  try {
    const res = await getDbVersionStats();
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
      <Col :span="8">
        <Card>
          <Statistic title="数据库表总数" :value="stats.totalTables">
            <template #prefix><DatabaseOutlined /></template>
          </Statistic>
        </Card>
      </Col>
    </Row>

    <Card title="数据库表版本">
      <template #extra>
        <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="false" rowKey="tableName" />
    </Card>
  </div>
</template>
