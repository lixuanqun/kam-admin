<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Row, Col, Statistic, Modal, Tag, Select, DatePicker, message } from 'ant-design-vue';
import { ReloadOutlined, SearchOutlined, ClearOutlined, EyeOutlined } from '@ant-design/icons-vue';
import { getSipTraces, getSipTraceByCallId, getSipTraceStats, cleanupSipTrace } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const stats = ref({ total: 0, todayCount: 0, methodStats: [] });

const filters = reactive({ callid: '', tracedUser: '', method: '', fromIp: '', toIp: '' });
const methods = ['INVITE', 'ACK', 'BYE', 'CANCEL', 'REGISTER', 'OPTIONS', 'SUBSCRIBE', 'NOTIFY', 'MESSAGE', 'REFER', 'INFO', 'UPDATE', 'PRACK'];

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '时间', dataIndex: 'timeStamp', width: 180 },
  { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
  { title: '方法', dataIndex: 'method', width: 100 },
  { title: '状态', dataIndex: 'status', width: 120 },
  { title: 'From IP', dataIndex: 'fromIp', width: 140 },
  { title: 'To IP', dataIndex: 'toIp', width: 140 },
  { title: '方向', dataIndex: 'direction', width: 60 },
  { title: '操作', key: 'action', width: 80 },
];

// 详情弹窗
const detailVisible = ref(false);
const callDetails = ref<any[]>([]);
const selectedCallId = ref('');

async function loadData() {
  loading.value = true;
  try {
    const params = { page: pagination.current, limit: pagination.pageSize, ...filters };
    const res = await getSipTraces(params);
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

async function viewCallDetails(callid: string) {
  selectedCallId.value = callid;
  const res = await getSipTraceByCallId(callid);
  if (res.data?.code === 0) {
    callDetails.value = res.data.data;
    detailVisible.value = true;
  }
}

async function loadStats() {
  try {
    const res = await getSipTraceStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

async function handleCleanup() {
  await cleanupSipTrace(30);
  message.success('清理成功');
  loadData();
  loadStats();
}

onMounted(() => {
  loadData();
  loadStats();
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="6"><Card><Statistic title="总记录数" :value="stats.total" /></Card></Col>
      <Col :span="6"><Card><Statistic title="今日记录" :value="stats.todayCount" /></Card></Col>
    </Row>

    <Card title="SIP 跟踪">
      <template #extra>
        <Space>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button danger @click="handleCleanup"><ClearOutlined /> 清理 (30天前)</Button>
        </Space>
      </template>

      <Space class="mb-4" wrap>
        <Input v-model:value="filters.callid" placeholder="Call ID" style="width: 200px" allowClear />
        <Input v-model:value="filters.tracedUser" placeholder="跟踪用户" style="width: 150px" allowClear />
        <Select v-model:value="filters.method" placeholder="方法" style="width: 120px" allowClear>
          <Select.Option v-for="m in methods" :key="m" :value="m">{{ m }}</Select.Option>
        </Select>
        <Input v-model:value="filters.fromIp" placeholder="From IP" style="width: 150px" allowClear />
        <Input v-model:value="filters.toIp" placeholder="To IP" style="width: 150px" allowClear />
        <Button type="primary" @click="handleSearch"><SearchOutlined /> 搜索</Button>
      </Space>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'method'">
            <Tag :color="record.method === 'INVITE' ? 'blue' : record.method === 'BYE' ? 'orange' : 'default'">{{ record.method }}</Tag>
          </template>
          <template v-else-if="column.dataIndex === 'direction'">
            <Tag :color="record.direction === 'in' ? 'green' : 'purple'">{{ record.direction }}</Tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <Button type="link" size="small" @click="viewCallDetails(record.callid)"><EyeOutlined /></Button>
          </template>
        </template>
      </Table>
    </Card>

    <!-- 呼叫详情弹窗 -->
    <Modal v-model:open="detailVisible" :title="`呼叫流程: ${selectedCallId}`" width="900px" :footer="null">
      <Table :dataSource="callDetails" :pagination="false" size="small" rowKey="id">
        <Table.Column title="时间" dataIndex="timeStamp" :width="180" />
        <Table.Column title="方法" dataIndex="method" :width="100" />
        <Table.Column title="状态" dataIndex="status" :width="150" />
        <Table.Column title="方向" dataIndex="direction" :width="60" />
        <Table.Column title="From" dataIndex="fromIp" :width="140" />
        <Table.Column title="To" dataIndex="toIp" :width="140" />
      </Table>
      <Card title="消息内容" class="mt-4" v-if="callDetails.length > 0">
        <pre style="max-height: 300px; overflow: auto; font-size: 12px;">{{ callDetails[0]?.msg }}</pre>
      </Card>
    </Modal>
  </div>
</template>
