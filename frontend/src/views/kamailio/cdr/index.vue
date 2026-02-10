<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="6"><a-card><a-statistic title="今日通话" :value="todayStats.totalCalls" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="未接来电" :value="todayStats.missedCalls" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="成功率" :value="todayStats.successRate" :precision="2"><template #suffix>%</template></a-statistic></a-card></a-col>
    </a-row>
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="cdr" title="CDR 记录">
          <a-space wrap style="margin-bottom:16px">
            <a-input v-model="cdrFilters.srcUser" placeholder="来源用户" :style="{width:'150px'}" />
            <a-input v-model="cdrFilters.dstUser" placeholder="目标用户" :style="{width:'150px'}" />
            <a-input v-model="cdrFilters.callid" placeholder="Call ID" :style="{width:'200px'}" />
            <a-input v-model="cdrFilters.sipCode" placeholder="响应码" :style="{width:'100px'}" />
            <a-button type="primary" @click="loadCdrData"><template #icon><icon-search /></template>搜索</a-button>
            <a-button @click="loadCdrData"><template #icon><icon-refresh /></template>刷新</a-button>
          </a-space>
          <a-table :columns="cdrColumns" :data="cdrData" :loading="cdrLoading" :pagination="{total:cdrPagination.total,current:cdrPagination.current,pageSize:cdrPagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{cdrPagination.current=p;loadCdrData()}">
            <template #sipCode="{record}"><a-tag :color="getSipCodeColor(record.sipCode)">{{record.sipCode}}</a-tag></template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="missed" title="未接来电">
          <a-space wrap style="margin-bottom:16px">
            <a-input v-model="missedFilters.srcUser" placeholder="来源用户" :style="{width:'150px'}" />
            <a-input v-model="missedFilters.dstUser" placeholder="目标用户" :style="{width:'150px'}" />
            <a-button type="primary" @click="loadMissedData"><template #icon><icon-search /></template>搜索</a-button>
            <a-button @click="loadMissedData"><template #icon><icon-refresh /></template>刷新</a-button>
          </a-space>
          <a-table :columns="missedColumns" :data="missedData" :loading="missedLoading" :pagination="{total:missedPagination.total,current:missedPagination.current,pageSize:missedPagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{missedPagination.current=p;loadMissedData()}">
            <template #sipCode="{record}"><a-tag :color="getSipCodeColor(record.sipCode)">{{record.sipCode}}</a-tag></template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getCdrRecords, getMissedCalls, getTodayStats, type AccRecord } from '@/api/kamailio';

const activeTab = ref('cdr');
const todayStats = ref({ totalCalls: 0, missedCalls: 0, successRate: 0 });
const cdrLoading = ref(false);
const cdrData = ref<AccRecord[]>([]);
const cdrPagination = reactive({ current: 1, pageSize: 20, total: 0 });
const cdrFilters = reactive({ srcUser: '', dstUser: '', callid: '', sipCode: '' });
const cdrColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 }, { title: '时间', dataIndex: 'time', width: 180 }, { title: '方法', dataIndex: 'method', width: 80 },
  { title: '来源用户', dataIndex: 'srcUser' }, { title: '目标用户', dataIndex: 'dstUser' }, { title: '响应码', dataIndex: 'sipCode', slotName: 'sipCode', width: 100 },
  { title: '原因', dataIndex: 'sipReason' }, { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
];
async function loadCdrData() {
  cdrLoading.value = true;
  try {
    const params: any = { page: cdrPagination.current, limit: cdrPagination.pageSize };
    if (cdrFilters.srcUser) params.srcUser = cdrFilters.srcUser;
    if (cdrFilters.dstUser) params.dstUser = cdrFilters.dstUser;
    if (cdrFilters.callid) params.callid = cdrFilters.callid;
    if (cdrFilters.sipCode) params.sipCode = cdrFilters.sipCode;
    const res = await getCdrRecords(params);
    if (res.data?.code === 0) { cdrData.value = res.data.data.items; cdrPagination.total = res.data.data.total; }
  } catch { Message.error('加载失败'); } finally { cdrLoading.value = false; }
}

const missedLoading = ref(false);
const missedData = ref<AccRecord[]>([]);
const missedPagination = reactive({ current: 1, pageSize: 20, total: 0 });
const missedFilters = reactive({ srcUser: '', dstUser: '' });
const missedColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 }, { title: '时间', dataIndex: 'time', width: 180 }, { title: '方法', dataIndex: 'method', width: 80 },
  { title: '来源用户', dataIndex: 'srcUser' }, { title: '目标用户', dataIndex: 'dstUser' }, { title: '响应码', dataIndex: 'sipCode', slotName: 'sipCode', width: 100 }, { title: '原因', dataIndex: 'sipReason' },
];
async function loadMissedData() {
  missedLoading.value = true;
  try {
    const params: any = { page: missedPagination.current, limit: missedPagination.pageSize };
    if (missedFilters.srcUser) params.srcUser = missedFilters.srcUser;
    if (missedFilters.dstUser) params.dstUser = missedFilters.dstUser;
    const res = await getMissedCalls(params);
    if (res.data?.code === 0) { missedData.value = res.data.data.items; missedPagination.total = res.data.data.total; }
  } catch { Message.error('加载失败'); } finally { missedLoading.value = false; }
}

async function loadTodayStats() { try { const res = await getTodayStats(); if (res.data?.code === 0) todayStats.value = res.data.data; } catch {} }
function getSipCodeColor(code: string) { if (code?.startsWith('2')) return 'green'; if (code?.startsWith('3')) return 'arcoblue'; if (code?.startsWith('4')) return 'orangered'; if (code?.startsWith('5')) return 'red'; return 'gray'; }
onMounted(() => { loadCdrData(); loadMissedData(); loadTodayStats(); });
</script>
