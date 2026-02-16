<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="6"><a-card><a-statistic title="总消息数" :value="stats.total" /></a-card></a-col><a-col :span="6"><a-card><a-statistic title="活动消息" :value="stats.active" /></a-card></a-col><a-col :span="6"><a-card><a-statistic title="过期消息" :value="stats.expired" /></a-card></a-col></a-row>
    <a-card title="离线消息管理" class="general-card">
      <template #extra><a-space><a-input v-model="keyword" placeholder="搜索用户名" :style="{width:'200px'}" @press-enter="handleSearch"><template #suffix><icon-search @click="handleSearch" style="cursor:pointer" /></template></a-input><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button status="danger" @click="handleCleanup"><template #icon><icon-delete /></template>清理过期</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
      </a-table>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getMsiloMessages, deleteMsiloMessage, cleanupMsilo, getMsiloStats } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const keyword = ref('');
const stats = ref({total:0,expired:0,active:0});
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'源地址',dataIndex:'srcAddr',ellipsis:true},{title:'目标地址',dataIndex:'dstAddr',ellipsis:true},{title:'用户名',dataIndex:'username'},{title:'域',dataIndex:'domain'},{title:'存储时间',dataIndex:'incTime'},{title:'过期时间',dataIndex:'expTime'},{title:'内容类型',dataIndex:'contentType'},{title:'操作',slotName:'action',width:100}];
async function loadData(){loading.value=true;try{const r=await getMsiloMessages({page:pagination.current,limit:pagination.pageSize,keyword:keyword.value});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleSearch(){pagination.current=1;loadData();}
async function handleDelete(id:number){await deleteMsiloMessage(id);Message.success('删除成功');loadData();loadStats();}
async function handleCleanup(){const r=await cleanupMsilo();Message.success(r.data?.message||'清理成功');loadData();loadStats();}
async function loadStats(){try{const r=await getMsiloStats();if(r.data?.code===0)stats.value=r.data.data;}catch{}}
onMounted(()=>{loadData();loadStats();});
</script>
