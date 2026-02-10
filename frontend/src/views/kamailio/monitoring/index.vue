<template>
  <div class="page-container">
    <a-spin :loading="loading" style="width: 100%">
      <!-- 操作栏 -->
      <a-card class="general-card">
        <a-row justify="space-between" align="center">
          <a-col><h3 style="margin:0">系统监控</h3></a-col>
          <a-col>
            <a-button type="primary" @click="handleRefresh">
              <template #icon><icon-refresh /></template>
              刷新
            </a-button>
          </a-col>
        </a-row>
      </a-card>

      <!-- 状态概览 -->
      <a-row :gutter="16" style="margin-top: 16px">
        <a-col :span="6">
          <a-card>
            <a-statistic title="Kamailio 状态">
              <template #value>
                <a-tag :color="health.status === 'online' ? 'green' : 'red'" size="large">
                  {{ health.status === 'online' ? '在线' : '离线' }}
                </a-tag>
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="注册用户" :value="dashboard.subscriberCount" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="在线用户" :value="dashboard.onlineCount" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="今日通话" :value="dashboard.todayCalls" />
          </a-card>
        </a-col>
      </a-row>

      <a-row :gutter="16" style="margin-top: 16px">
        <a-col :span="6">
          <a-card>
            <a-statistic title="今日未接" :value="dashboard.todayMissedCalls" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="成功率" :value="dashboard.successRate" :precision="2">
              <template #suffix>%</template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="活动对话" :value="dialogs.count" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="运行时间" :value="formatUptime(health.uptime)" />
          </a-card>
        </a-col>
      </a-row>

      <!-- 核心信息 -->
      <a-card title="Kamailio 信息" class="general-card" v-if="coreInfo">
        <a-descriptions :column="2" bordered size="small">
          <a-descriptions-item label="版本">{{ health.version || coreInfo?.version || '-' }}</a-descriptions-item>
          <a-descriptions-item label="编译时间">{{ coreInfo?.compile_time || '-' }}</a-descriptions-item>
          <a-descriptions-item label="编译器">{{ coreInfo?.compiler || '-' }}</a-descriptions-item>
          <a-descriptions-item label="架构">{{ coreInfo?.arch || '-' }}</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 统计信息 -->
      <a-card title="运行统计" class="general-card" v-if="statistics">
        <pre style="max-height: 400px; overflow: auto; background: var(--color-fill-2); padding: 16px; border-radius: 4px;">{{ JSON.stringify(statistics, null, 2) }}</pre>
      </a-card>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { checkHealth, getDashboardData, getStatistics, getCoreInfo, getActiveDialogs } from '@/api/kamailio';

const loading = ref(false);
const health = ref({ status: 'offline' as 'online' | 'offline', uptime: null as any, version: '' });
const dashboard = ref({ subscriberCount: 0, onlineCount: 0, todayCalls: 0, todayMissedCalls: 0, successRate: 0, kamailioStatus: 'offline' });
const coreInfo = ref<any>(null);
const dialogs = ref({ count: 0, dialogs: [] as any[] });
const statistics = ref<any>(null);
let refreshTimer: any = null;

async function loadAllData() {
  loading.value = true;
  try {
    await Promise.all([
      checkHealth().then(res => { if (res.data?.code === 0) health.value = res.data.data; }).catch(() => { health.value.status = 'offline'; }),
      getDashboardData().then(res => { if (res.data?.code === 0) dashboard.value = res.data.data; }).catch(() => {}),
      getCoreInfo().then(res => { if (res.data?.code === 0) coreInfo.value = res.data.data; }).catch(() => {}),
      getActiveDialogs().then(res => { if (res.data?.code === 0) dialogs.value = res.data.data; }).catch(() => {}),
      getStatistics().then(res => { if (res.data?.code === 0) statistics.value = res.data.data; }).catch(() => {}),
    ]);
  } finally {
    loading.value = false;
  }
}

function handleRefresh() {
  loadAllData();
  Message.success('数据已刷新');
}

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
  refreshTimer = setInterval(loadAllData, 30000);
});

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer);
});
</script>
