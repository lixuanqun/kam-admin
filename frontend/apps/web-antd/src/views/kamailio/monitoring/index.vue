<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import {
  Card,
  Row,
  Col,
  Statistic,
  Button,
  Tag,
  Descriptions,
  DescriptionsItem,
  Table,
  Spin,
  message,
} from 'ant-design-vue';
import {
  ReloadOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  UserOutlined,
  PhoneOutlined,
} from '@ant-design/icons-vue';
import {
  checkHealth,
  getDashboardData,
  getStatistics,
  getCoreInfo,
  getActiveDialogs,
  getSystemOverview,
} from '#/api/kamailio';

// 状态
const loading = ref(false);
const health = ref({
  status: 'offline' as 'online' | 'offline',
  uptime: null as any,
  version: '',
});
const dashboard = ref({
  subscriberCount: 0,
  onlineCount: 0,
  todayCalls: 0,
  todayMissedCalls: 0,
  successRate: 0,
  kamailioStatus: 'offline' as 'online' | 'offline',
});
const coreInfo = ref<any>(null);
const dialogs = ref({ count: 0, dialogs: [] as any[] });
const statistics = ref<any>(null);

// 自动刷新定时器
let refreshTimer: any = null;

// 加载健康状态
async function loadHealth() {
  try {
    const res = await checkHealth();
    if (res.data?.code === 0) {
      health.value = res.data.data;
    }
  } catch (error) {
    health.value.status = 'offline';
  }
}

// 加载仪表盘数据
async function loadDashboard() {
  try {
    const res = await getDashboardData();
    if (res.data?.code === 0) {
      dashboard.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 加载核心信息
async function loadCoreInfo() {
  try {
    const res = await getCoreInfo();
    if (res.data?.code === 0) {
      coreInfo.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 加载活动对话
async function loadDialogs() {
  try {
    const res = await getActiveDialogs();
    if (res.data?.code === 0) {
      dialogs.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 加载统计信息
async function loadStatistics() {
  try {
    const res = await getStatistics();
    if (res.data?.code === 0) {
      statistics.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 加载所有数据
async function loadAllData() {
  loading.value = true;
  try {
    await Promise.all([
      loadHealth(),
      loadDashboard(),
      loadCoreInfo(),
      loadDialogs(),
      loadStatistics(),
    ]);
  } finally {
    loading.value = false;
  }
}

// 手动刷新
function handleRefresh() {
  loadAllData();
  message.success('数据已刷新');
}

// 格式化运行时间
function formatUptime(uptime: any) {
  if (!uptime) return '-';
  if (typeof uptime === 'string') return uptime;
  if (uptime.uptime) {
    const seconds = uptime.uptime;
    const days = Math.floor(seconds / 86400);
    const hours = Math.floor((seconds % 86400) / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    return `${days}天 ${hours}小时 ${minutes}分钟`;
  }
  return '-';
}

onMounted(() => {
  loadAllData();
  // 每 30 秒自动刷新
  refreshTimer = setInterval(loadAllData, 30000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
});
</script>

<template>
  <div class="p-4">
    <Spin :spinning="loading">
      <!-- 操作栏 -->
      <Card class="mb-4">
        <Row justify="space-between" align="middle">
          <Col>
            <h2 class="m-0">系统监控</h2>
          </Col>
          <Col>
            <Button type="primary" @click="handleRefresh">
              <ReloadOutlined /> 刷新
            </Button>
          </Col>
        </Row>
      </Card>

      <!-- 状态概览 -->
      <Row :gutter="16" class="mb-4">
        <Col :span="6">
          <Card>
            <Statistic title="Kamailio 状态">
              <template #value>
                <Tag :color="health.status === 'online' ? 'green' : 'red'" style="font-size: 16px;">
                  <template v-if="health.status === 'online'">
                    <CheckCircleOutlined /> 在线
                  </template>
                  <template v-else>
                    <CloseCircleOutlined /> 离线
                  </template>
                </Tag>
              </template>
            </Statistic>
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="注册用户" :value="dashboard.subscriberCount">
              <template #prefix>
                <UserOutlined />
              </template>
            </Statistic>
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="在线用户" :value="dashboard.onlineCount">
              <template #prefix>
                <UserOutlined style="color: #52c41a;" />
              </template>
            </Statistic>
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="今日通话" :value="dashboard.todayCalls">
              <template #prefix>
                <PhoneOutlined />
              </template>
            </Statistic>
          </Card>
        </Col>
      </Row>

      <Row :gutter="16" class="mb-4">
        <Col :span="6">
          <Card>
            <Statistic title="今日未接" :value="dashboard.todayMissedCalls" />
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="成功率" :value="dashboard.successRate" suffix="%" :precision="2" />
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="活动对话" :value="dialogs.count" />
          </Card>
        </Col>
        <Col :span="6">
          <Card>
            <Statistic title="运行时间">
              <template #value>
                {{ formatUptime(health.uptime) }}
              </template>
            </Statistic>
          </Card>
        </Col>
      </Row>

      <!-- 核心信息 -->
      <Card title="Kamailio 信息" class="mb-4" v-if="coreInfo">
        <Descriptions :column="2" bordered size="small">
          <DescriptionsItem label="版本">{{ health.version || coreInfo?.version || '-' }}</DescriptionsItem>
          <DescriptionsItem label="编译时间">{{ coreInfo?.compile_time || '-' }}</DescriptionsItem>
          <DescriptionsItem label="编译器">{{ coreInfo?.compiler || '-' }}</DescriptionsItem>
          <DescriptionsItem label="架构">{{ coreInfo?.arch || '-' }}</DescriptionsItem>
        </Descriptions>
      </Card>

      <!-- 统计信息 -->
      <Card title="运行统计" v-if="statistics">
        <pre style="max-height: 400px; overflow: auto; background: #f5f5f5; padding: 16px; border-radius: 4px;">{{ JSON.stringify(statistics, null, 2) }}</pre>
      </Card>
    </Spin>
  </div>
</template>
