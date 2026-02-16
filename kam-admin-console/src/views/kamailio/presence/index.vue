<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="8"><a-card><a-statistic title="Presentities" :value="stats.presentities" /></a-card></a-col><a-col :span="8"><a-card><a-statistic title="活动 Watchers" :value="stats.watchers" /></a-card></a-col></a-row>
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="presentities" title="Presentities">
          <a-space style="margin-bottom:16px"><a-button @click="loadPresentities"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleCleanup"><template #icon><icon-delete /></template>清理过期</a-button></a-space>
          <a-table :columns="presColumns" :data="presData" :loading="presLoading" :pagination="{total:presPagination.total,current:presPagination.current,pageSize:presPagination.pageSize}" row-key="id" @page-change="(p:number)=>{presPagination.current=p;loadPresentities()}" />
        </a-tab-pane>
        <a-tab-pane key="watchers" title="Watchers">
          <a-space style="margin-bottom:16px"><a-button @click="loadWatchers"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="watchColumns" :data="watchData" :loading="watchLoading" :pagination="{total:watchPagination.total,current:watchPagination.current,pageSize:watchPagination.pageSize}" row-key="id" @page-change="(p:number)=>{watchPagination.current=p;loadWatchers()}" />
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getPresentities, getWatchers, cleanupPresence, getPresenceStats } from '@/api/kamailio';
const activeTab = ref('presentities'); const stats = ref({presentities:0,watchers:0});
const presLoading = ref(false); const presData = ref<any[]>([]); const presPagination = reactive({current:1,pageSize:20,total:0});
const presColumns = [{title:'ID',dataIndex:'id',width:60},{title:'用户名',dataIndex:'username'},{title:'域',dataIndex:'domain'},{title:'事件',dataIndex:'event'},{title:'ETag',dataIndex:'etag',ellipsis:true},{title:'过期时间',dataIndex:'expires'},{title:'优先级',dataIndex:'priority',width:80}];
async function loadPresentities(){presLoading.value=true;try{const r=await getPresentities({page:presPagination.current,limit:presPagination.pageSize});if(r.data?.code===0){presData.value=r.data.data.items;presPagination.total=r.data.data.total;}}finally{presLoading.value=false;}}
const watchLoading = ref(false); const watchData = ref<any[]>([]); const watchPagination = reactive({current:1,pageSize:20,total:0});
const watchColumns = [{title:'ID',dataIndex:'id',width:60},{title:'Presentity URI',dataIndex:'presentityUri',ellipsis:true},{title:'Watcher',dataIndex:'watcherUsername'},{title:'To User',dataIndex:'toUser'},{title:'事件',dataIndex:'event'},{title:'状态',dataIndex:'status',width:80},{title:'过期时间',dataIndex:'expires'}];
async function loadWatchers(){watchLoading.value=true;try{const r=await getWatchers({page:watchPagination.current,limit:watchPagination.pageSize});if(r.data?.code===0){watchData.value=r.data.data.items;watchPagination.total=r.data.data.total;}}finally{watchLoading.value=false;}}
async function handleCleanup(){await cleanupPresence();Message.success('清理成功');loadStats();loadPresentities();loadWatchers();}
async function loadStats(){try{const r=await getPresenceStats();if(r.data?.code===0)stats.value=r.data.data;}catch{}}
onMounted(()=>{loadPresentities();loadWatchers();loadStats();});
</script>
