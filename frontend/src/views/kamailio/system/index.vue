<template>
  <div class="page-container">
    <a-tabs v-model:active-key="activeTab" @change="onTabChange">
      <a-tab-pane key="overview" title="系统概览">
        <a-space style="margin-bottom:16px"><a-button @click="loadOverview" :loading="loading"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
        <a-row :gutter="16" style="margin-bottom:16px">
          <a-col :span="6"><a-card><a-statistic title="进程数" :value="sysStatus?.processCount||0" /></a-card></a-col>
          <a-col :span="6"><a-card><a-statistic title="运行时间" :value="uptime?.uptime||'-'"><template #suffix>秒</template></a-statistic></a-card></a-col>
          <a-col :span="12"><a-card><a-descriptions :column="2" size="small"><a-descriptions-item label="版本">{{version?.version||'-'}}</a-descriptions-item></a-descriptions></a-card></a-col>
        </a-row>
        <a-card title="内存信息" class="general-card" v-if="memory"><pre style="max-height:200px;overflow:auto">{{JSON.stringify(memory,null,2)}}</pre></a-card>
        <a-row :gutter="16" style="margin-top:16px">
          <a-col :span="12"><a-card title="进程列表"><a-table :columns="processColumns" :data="processes" :pagination="false" size="small" row-key="id" :scroll="{y:300}" /></a-card></a-col>
          <a-col :span="12"><a-card title="加载的模块"><a-table :columns="moduleColumns" :data="modules" :pagination="false" size="small" row-key="name" :scroll="{y:300}" /></a-card></a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane key="tls" title="TLS 管理">
        <a-space style="margin-bottom:16px">
          <a-button @click="loadTls"><template #icon><icon-refresh /></template>刷新</a-button>
          <a-button @click="handleReloadTls"><template #icon><icon-sync /></template>重载 TLS</a-button>
        </a-space>
        <a-card title="TLS 连接" class="general-card"><a-table :columns="tlsColumns" :data="tlsList" :pagination="false" size="small" row-key="id" /></a-card>
        <a-card title="TLS 信息" class="general-card" v-if="tlsInfo"><pre>{{JSON.stringify(tlsInfo,null,2)}}</pre></a-card>
      </a-tab-pane>
      <a-tab-pane key="pike" title="Pike 防护">
        <a-space style="margin-bottom:16px"><a-button @click="loadPike"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
        <a-card title="Pike 封禁列表" class="general-card"><a-table :columns="pikeColumns" :data="pikeList" :pagination="false" size="small" row-key="ip" /></a-card>
        <a-card title="Pike Top 20" class="general-card"><a-table :columns="pikeColumns" :data="pikeTop" :pagination="false" size="small" row-key="ip" /></a-card>
      </a-tab-pane>
      <a-tab-pane key="statistics" title="统计信息">
        <a-space style="margin-bottom:16px"><a-button @click="loadStatistics"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
        <a-card><pre style="max-height:500px;overflow:auto">{{JSON.stringify(statistics,null,2)}}</pre></a-card>
      </a-tab-pane>
      <a-tab-pane key="config" title="配置管理">
        <a-space style="margin-bottom:16px"><a-button @click="loadConfig"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
        <a-card><pre style="max-height:500px;overflow:auto">{{JSON.stringify(configList,null,2)}}</pre></a-card>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getSystemInfo, getSystemStatus, getSystemVersion, getSystemUptime, getSystemMemory, getSystemProcesses, getSystemModules, getSystemStatistics, getTlsList, getTlsInfo, reloadTls, getPikeList, getPikeTop, getConfigList } from '@/api/kamailio';

const activeTab = ref('overview');
const loading = ref(false);
const sysStatus = ref<any>(null); const sysInfo = ref<any>(null); const version = ref<any>(null); const uptime = ref<any>(null); const memory = ref<any>(null);
const processes = ref<any[]>([]); const modules = ref<any[]>([]); const tlsList = ref<any[]>([]); const tlsInfo = ref<any>(null);
const pikeList = ref<any[]>([]); const pikeTop = ref<any[]>([]); const statistics = ref<any>(null); const configList = ref<any>(null);
let refreshTimer: any = null;

const processColumns = [{ title: 'ID', dataIndex: 'id', width: 60 },{ title: 'PID', dataIndex: 'pid', width: 80 },{ title: '描述', dataIndex: 'desc' }];
const moduleColumns = [{ title: '模块名', dataIndex: 'name' }];
const tlsColumns = [{ title: 'ID', dataIndex: 'id', width: 60 },{ title: '连接', dataIndex: 'connection', ellipsis: true },{ title: '状态', dataIndex: 'state' }];
const pikeColumns = [{ title: 'IP', dataIndex: 'ip' },{ title: '计数', dataIndex: 'count' },{ title: '状态', dataIndex: 'status' }];

async function loadOverview() { loading.value = true; try { const [a,b,c,d,e] = await Promise.all([getSystemStatus().catch(()=>({data:{data:null}})),getSystemInfo().catch(()=>({data:{data:null}})),getSystemVersion().catch(()=>({data:{data:null}})),getSystemUptime().catch(()=>({data:{data:null}})),getSystemMemory().catch(()=>({data:{data:null}}))]); sysStatus.value=a.data?.data; sysInfo.value=b.data?.data; version.value=c.data?.data; uptime.value=d.data?.data; memory.value=e.data?.data; } finally { loading.value = false; } }
async function loadProcesses() { try { const r = await getSystemProcesses(); processes.value = r.data?.data||[]; } catch {} }
async function loadModules() { try { const r = await getSystemModules(); const d = r.data?.data||[]; modules.value = Array.isArray(d)?d.map((m:string)=>({name:m})):[]; } catch {} }
async function loadTls() { try { const [a,b] = await Promise.all([getTlsList().catch(()=>({data:{data:[]}})),getTlsInfo().catch(()=>({data:{data:null}}))]); tlsList.value=a.data?.data||[]; tlsInfo.value=b.data?.data; } catch {} }
async function handleReloadTls() { await reloadTls(); Message.success('TLS 重载成功'); loadTls(); }
async function loadPike() { try { const [a,b] = await Promise.all([getPikeList().catch(()=>({data:{data:[]}})),getPikeTop(20).catch(()=>({data:{data:[]}}))]); pikeList.value=a.data?.data||[]; pikeTop.value=b.data?.data||[]; } catch {} }
async function loadStatistics() { try { const r = await getSystemStatistics(); statistics.value = r.data?.data; } catch {} }
async function loadConfig() { try { const r = await getConfigList(); configList.value = r.data?.data; } catch {} }
function onTabChange(k: string|number) { if(k==='tls')loadTls(); if(k==='pike')loadPike(); if(k==='statistics')loadStatistics(); if(k==='config')loadConfig(); }
onMounted(() => { loadOverview(); loadProcesses(); loadModules(); refreshTimer = setInterval(loadOverview, 30000); });
onUnmounted(() => { if(refreshTimer)clearInterval(refreshTimer); });
</script>
