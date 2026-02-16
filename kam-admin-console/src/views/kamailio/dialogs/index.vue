<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="8"><a-card><a-statistic title="活动对话" :value="stats.active" /></a-card></a-col>
      <a-col :span="8"><a-card><a-statistic title="数据库记录" :value="stats.dbCount" /></a-card></a-col>
    </a-row>
    <a-card title="活动对话（内存）" class="general-card">
      <template #extra><a-button @click="loadActiveDialogs"><template #icon><icon-refresh /></template>刷新</a-button></template>
      <a-table :columns="activeColumns" :data="activeData" :pagination="false" row-key="h_id" size="small">
        <template #state="{record}"><a-tag color="green">{{record.state}}</a-tag></template>
        <template #action="{record}">
          <a-popconfirm content="确定结束此对话？" @ok="handleEndDialog(record)">
            <a-button type="text" status="danger" size="small">结束</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
    <a-card title="对话记录（数据库）" class="general-card">
      <template #extra><a-button @click="loadDbDialogs"><template #icon><icon-refresh /></template>刷新</a-button></template>
      <a-table :columns="dbColumns" :data="dbData" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadDbDialogs()}" />
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getDialogs, getActiveDialogsList, getDialogStats, endDialog } from '@/api/kamailio';

const loading = ref(false);
const dbData = ref<any[]>([]);
const activeData = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const stats = ref({ active: 0, dbCount: 0 });
let refreshTimer: any = null;

const dbColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 }, { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
  { title: 'From URI', dataIndex: 'fromUri', ellipsis: true }, { title: 'To URI', dataIndex: 'toUri', ellipsis: true },
  { title: '状态', dataIndex: 'state', width: 80 }, { title: '开始时间', dataIndex: 'startTime', width: 120 },
];
const activeColumns = [
  { title: 'Hash', dataIndex: 'h_entry', width: 80 }, { title: 'ID', dataIndex: 'h_id', width: 80 },
  { title: 'Call ID', dataIndex: 'callid', ellipsis: true }, { title: 'From', dataIndex: 'from_uri', ellipsis: true },
  { title: 'To', dataIndex: 'to_uri', ellipsis: true }, { title: '状态', dataIndex: 'state', slotName: 'state', width: 100 },
  { title: '操作', slotName: 'action', width: 100 },
];

async function loadDbDialogs() { loading.value = true; try { const res = await getDialogs({ page: pagination.current, limit: pagination.pageSize }); if (res.data?.code === 0) { dbData.value = res.data.data.items; pagination.total = res.data.data.total; } } finally { loading.value = false; } }
async function loadActiveDialogs() { try { const res = await getActiveDialogsList(); if (res.data?.code === 0) activeData.value = res.data.data || []; } catch {} }
async function loadStats() { try { const res = await getDialogStats(); if (res.data?.code === 0) stats.value = res.data.data; } catch {} }
async function handleEndDialog(record: any) { try { await endDialog(record.h_entry, record.h_id); Message.success('对话已结束'); loadActiveDialogs(); loadStats(); } catch { Message.error('操作失败'); } }
function loadAll() { loadDbDialogs(); loadActiveDialogs(); loadStats(); }
onMounted(() => { loadAll(); refreshTimer = setInterval(loadAll, 10000); });
onUnmounted(() => { if (refreshTimer) clearInterval(refreshTimer); });
</script>
