<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="8"><a-card><a-statistic title="总记录数" :value="pagination.total" /></a-card></a-col></a-row>
    <a-card title="拓扑隐藏 (Topos)" class="general-card">
      <template #extra><a-space><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button status="danger" @click="handleCleanup"><template #icon><icon-delete /></template>清理 (7天前)</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}" />
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getToposDialogs, cleanupTopos } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0});
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'时间',dataIndex:'rectime',width:180},{title:'A CallID',dataIndex:'a_callid',ellipsis:true},{title:'A UUID',dataIndex:'a_uuid',ellipsis:true},{title:'B UUID',dataIndex:'b_uuid',ellipsis:true},{title:'A Tag',dataIndex:'a_tag',ellipsis:true},{title:'B Tag',dataIndex:'b_tag',ellipsis:true}];
async function loadData(){loading.value=true;try{const r=await getToposDialogs({page:pagination.current,limit:pagination.pageSize});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
async function handleCleanup(){const r=await cleanupTopos(7);Message.success(r.data?.message||'清理成功');loadData();}
onMounted(()=>loadData());
</script>
