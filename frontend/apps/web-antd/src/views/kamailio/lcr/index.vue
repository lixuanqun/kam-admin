<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Tabs, TabPane, message } from 'ant-design-vue';
import { ReloadOutlined, SyncOutlined } from '@ant-design/icons-vue';
import { getLcrGateways, getLcrRules, getLcrTargets, reloadLcr } from '#/api/kamailio';

const activeTab = ref('gateways');

// Gateways
const gwLoading = ref(false);
const gwData = ref<any[]>([]);
const gwPagination = reactive({ current: 1, pageSize: 20, total: 0 });

const gwColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'LCR ID', dataIndex: 'lcrId', width: 80 },
  { title: '网关名', dataIndex: 'gwName' },
  { title: 'IP 地址', dataIndex: 'ipAddr' },
  { title: '端口', dataIndex: 'port', width: 80 },
  { title: '标签', dataIndex: 'tag' },
];

async function loadGateways() {
  gwLoading.value = true;
  try {
    const res = await getLcrGateways({ page: gwPagination.current, limit: gwPagination.pageSize });
    if (res.data?.code === 0) {
      gwData.value = res.data.data.items;
      gwPagination.total = res.data.data.total;
    }
  } finally { gwLoading.value = false; }
}

// Rules
const ruleLoading = ref(false);
const ruleData = ref<any[]>([]);
const rulePagination = reactive({ current: 1, pageSize: 20, total: 0 });

const ruleColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'LCR ID', dataIndex: 'lcrId', width: 80 },
  { title: '前缀', dataIndex: 'prefix' },
  { title: 'From URI', dataIndex: 'fromUri' },
  { title: '启用', dataIndex: 'enabled', width: 80 },
];

async function loadRules() {
  ruleLoading.value = true;
  try {
    const res = await getLcrRules({ page: rulePagination.current, limit: rulePagination.pageSize });
    if (res.data?.code === 0) {
      ruleData.value = res.data.data.items;
      rulePagination.total = res.data.data.total;
    }
  } finally { ruleLoading.value = false; }
}

// Targets
const targetLoading = ref(false);
const targetData = ref<any[]>([]);
const targetPagination = reactive({ current: 1, pageSize: 20, total: 0 });

const targetColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'LCR ID', dataIndex: 'lcrId', width: 80 },
  { title: '规则 ID', dataIndex: 'ruleId', width: 80 },
  { title: '网关 ID', dataIndex: 'gwId', width: 80 },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '权重', dataIndex: 'weight', width: 80 },
];

async function loadTargets() {
  targetLoading.value = true;
  try {
    const res = await getLcrTargets({ page: targetPagination.current, limit: targetPagination.pageSize });
    if (res.data?.code === 0) {
      targetData.value = res.data.data.items;
      targetPagination.total = res.data.data.total;
    }
  } finally { targetLoading.value = false; }
}

async function handleReload() {
  await reloadLcr();
  message.success('重载成功');
}

onMounted(() => {
  loadGateways();
  loadRules();
  loadTargets();
});
</script>

<template>
  <div class="p-4">
    <Card>
      <Tabs v-model:activeKey="activeTab">
        <TabPane key="gateways" tab="网关">
          <Space class="mb-4">
            <Button @click="loadGateways"><ReloadOutlined /> 刷新</Button>
            <Button @click="handleReload"><SyncOutlined /> 重载配置</Button>
          </Space>
          <Table :columns="gwColumns" :dataSource="gwData" :loading="gwLoading" :pagination="gwPagination" rowKey="id" />
        </TabPane>
        <TabPane key="rules" tab="规则">
          <Space class="mb-4">
            <Button @click="loadRules"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="ruleColumns" :dataSource="ruleData" :loading="ruleLoading" :pagination="rulePagination" rowKey="id" />
        </TabPane>
        <TabPane key="targets" tab="目标">
          <Space class="mb-4">
            <Button @click="loadTargets"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="targetColumns" :dataSource="targetData" :loading="targetLoading" :pagination="targetPagination" rowKey="id" />
        </TabPane>
      </Tabs>
    </Card>
  </div>
</template>
