<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { Card, Row, Col, Statistic, Button, Space, Descriptions, DescriptionsItem, Table, Tabs, TabPane, Tag, message } from 'ant-design-vue';
import { ReloadOutlined, SyncOutlined, InfoCircleOutlined, SafetyCertificateOutlined, ThunderboltOutlined, SettingOutlined } from '@ant-design/icons-vue';
import {
  getSystemInfo, getSystemStatus, getSystemVersion, getSystemUptime, getSystemMemory,
  getSystemProcesses, getSystemModules, getSystemStatistics, getTlsList, getTlsInfo,
  reloadTls, getPikeList, getPikeTop, getConfigList
} from '#/api/kamailio';

const activeTab = ref('overview');
const loading = ref(false);

// 系统概览
const sysStatus = ref<any>(null);
const sysInfo = ref<any>(null);
const version = ref<any>(null);
const uptime = ref<any>(null);
const memory = ref<any>(null);

// 进程列表
const processes = ref<any[]>([]);
const processColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'PID', dataIndex: 'pid', width: 80 },
  { title: '描述', dataIndex: 'desc' },
];

// 模块列表
const modules = ref<any[]>([]);
const moduleColumns = [
  { title: '模块名', dataIndex: 'name' },
];

// TLS
const tlsList = ref<any[]>([]);
const tlsInfo = ref<any>(null);
const tlsColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '连接', dataIndex: 'connection', ellipsis: true },
  { title: '状态', dataIndex: 'state' },
];

// Pike
const pikeList = ref<any[]>([]);
const pikeTop = ref<any[]>([]);
const pikeColumns = [
  { title: 'IP', dataIndex: 'ip' },
  { title: '计数', dataIndex: 'count' },
  { title: '状态', dataIndex: 'status' },
];

// 统计
const statistics = ref<any>(null);

// 配置
const configList = ref<any>(null);

let refreshTimer: any = null;

async function loadOverview() {
  loading.value = true;
  try {
    const [statusRes, infoRes, versionRes, uptimeRes, memoryRes] = await Promise.all([
      getSystemStatus().catch(() => ({ data: { data: null } })),
      getSystemInfo().catch(() => ({ data: { data: null } })),
      getSystemVersion().catch(() => ({ data: { data: null } })),
      getSystemUptime().catch(() => ({ data: { data: null } })),
      getSystemMemory().catch(() => ({ data: { data: null } })),
    ]);
    sysStatus.value = statusRes.data?.data;
    sysInfo.value = infoRes.data?.data;
    version.value = versionRes.data?.data;
    uptime.value = uptimeRes.data?.data;
    memory.value = memoryRes.data?.data;
  } finally { loading.value = false; }
}

async function loadProcesses() {
  try {
    const res = await getSystemProcesses();
    processes.value = res.data?.data || [];
  } catch {}
}

async function loadModules() {
  try {
    const res = await getSystemModules();
    const data = res.data?.data || [];
    modules.value = Array.isArray(data) ? data.map((m: string) => ({ name: m })) : [];
  } catch {}
}

async function loadTls() {
  try {
    const [listRes, infoRes] = await Promise.all([
      getTlsList().catch(() => ({ data: { data: [] } })),
      getTlsInfo().catch(() => ({ data: { data: null } })),
    ]);
    tlsList.value = listRes.data?.data || [];
    tlsInfo.value = infoRes.data?.data;
  } catch {}
}

async function handleReloadTls() {
  await reloadTls();
  message.success('TLS 重载成功');
  loadTls();
}

async function loadPike() {
  try {
    const [listRes, topRes] = await Promise.all([
      getPikeList().catch(() => ({ data: { data: [] } })),
      getPikeTop(20).catch(() => ({ data: { data: [] } })),
    ]);
    pikeList.value = listRes.data?.data || [];
    pikeTop.value = topRes.data?.data || [];
  } catch {}
}

async function loadStatistics() {
  try {
    const res = await getSystemStatistics();
    statistics.value = res.data?.data;
  } catch {}
}

async function loadConfig() {
  try {
    const res = await getConfigList();
    configList.value = res.data?.data;
  } catch {}
}

function loadAll() {
  loadOverview();
  loadProcesses();
  loadModules();
}

onMounted(() => {
  loadAll();
  refreshTimer = setInterval(loadOverview, 30000);
});

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer);
});
</script>

<template>
  <div class="p-4">
    <Tabs v-model:activeKey="activeTab" @change="(k) => {
      if (k === 'tls') loadTls();
      if (k === 'pike') loadPike();
      if (k === 'statistics') loadStatistics();
      if (k === 'config') loadConfig();
    }">
      <TabPane key="overview" tab="系统概览">
        <Space class="mb-4">
          <Button @click="loadOverview" :loading="loading"><ReloadOutlined /> 刷新</Button>
        </Space>
        <Row :gutter="16" class="mb-4">
          <Col :span="6">
            <Card>
              <Statistic title="进程数" :value="sysStatus?.processCount || 0">
                <template #prefix><InfoCircleOutlined /></template>
              </Statistic>
            </Card>
          </Col>
          <Col :span="6">
            <Card>
              <Statistic title="运行时间" :value="uptime?.uptime || '-'" suffix="秒" />
            </Card>
          </Col>
          <Col :span="12">
            <Card>
              <Descriptions :column="2" size="small">
                <DescriptionsItem label="版本">{{ version?.version || '-' }}</DescriptionsItem>
              </Descriptions>
            </Card>
          </Col>
        </Row>
        <Card title="内存信息" class="mb-4" v-if="memory">
          <pre style="max-height: 200px; overflow: auto;">{{ JSON.stringify(memory, null, 2) }}</pre>
        </Card>
        <Row :gutter="16">
          <Col :span="12">
            <Card title="进程列表">
              <Table :columns="processColumns" :dataSource="processes" :pagination="false" size="small" rowKey="id" :scroll="{ y: 300 }" />
            </Card>
          </Col>
          <Col :span="12">
            <Card title="加载的模块">
              <Table :columns="moduleColumns" :dataSource="modules" :pagination="false" size="small" rowKey="name" :scroll="{ y: 300 }" />
            </Card>
          </Col>
        </Row>
      </TabPane>

      <TabPane key="tls" tab="TLS 管理">
        <Space class="mb-4">
          <Button @click="loadTls"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReloadTls"><SyncOutlined /> 重载 TLS</Button>
        </Space>
        <Card title="TLS 连接" class="mb-4">
          <Table :columns="tlsColumns" :dataSource="tlsList" :pagination="false" size="small" rowKey="id" />
        </Card>
        <Card title="TLS 信息" v-if="tlsInfo">
          <pre>{{ JSON.stringify(tlsInfo, null, 2) }}</pre>
        </Card>
      </TabPane>

      <TabPane key="pike" tab="Pike 防护">
        <Space class="mb-4">
          <Button @click="loadPike"><ReloadOutlined /> 刷新</Button>
        </Space>
        <Card title="Pike 封禁列表" class="mb-4">
          <Table :columns="pikeColumns" :dataSource="pikeList" :pagination="false" size="small" rowKey="ip" />
        </Card>
        <Card title="Pike Top 20">
          <Table :columns="pikeColumns" :dataSource="pikeTop" :pagination="false" size="small" rowKey="ip" />
        </Card>
      </TabPane>

      <TabPane key="statistics" tab="统计信息">
        <Space class="mb-4">
          <Button @click="loadStatistics"><ReloadOutlined /> 刷新</Button>
        </Space>
        <Card>
          <pre style="max-height: 500px; overflow: auto;">{{ JSON.stringify(statistics, null, 2) }}</pre>
        </Card>
      </TabPane>

      <TabPane key="config" tab="配置管理">
        <Space class="mb-4">
          <Button @click="loadConfig"><ReloadOutlined /> 刷新</Button>
        </Space>
        <Card>
          <pre style="max-height: 500px; overflow: auto;">{{ JSON.stringify(configList, null, 2) }}</pre>
        </Card>
      </TabPane>
    </Tabs>
  </div>
</template>
