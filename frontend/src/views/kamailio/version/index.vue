<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="8"><a-card><a-statistic title="数据库表总数" :value="stats.totalTables" /></a-card></a-col></a-row>
    <a-card title="数据库表版本" class="general-card">
      <template #extra><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="false" row-key="tableName" />
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getDbVersions, getDbVersionStats } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const stats = ref({totalTables:0});
const columns = [{title:'表名',dataIndex:'tableName'},{title:'版本号',dataIndex:'tableVersion',width:100}];
async function loadData(){loading.value=true;try{const r=await getDbVersions();if(r.data?.code===0)data.value=r.data.data;}finally{loading.value=false;}}
async function loadStats(){try{const r=await getDbVersionStats();if(r.data?.code===0)stats.value=r.data.data;}catch{}}
onMounted(()=>{loadData();loadStats();});
</script>
