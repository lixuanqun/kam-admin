<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="6"><a-card><a-statistic title="总记录数" :value="stats.total" /></a-card></a-col><a-col :span="6"><a-card><a-statistic title="今日记录" :value="stats.todayCount" /></a-card></a-col></a-row>
    <a-card title="SIP 跟踪" class="general-card">
      <template #extra><a-space><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button status="danger" @click="handleCleanup"><template #icon><icon-delete /></template>清理 (30天前)</a-button></a-space></template>
      <a-space wrap style="margin-bottom:16px">
        <a-input v-model="filters.callid" placeholder="Call ID" :style="{width:'200px'}" allow-clear />
        <a-input v-model="filters.tracedUser" placeholder="跟踪用户" :style="{width:'150px'}" allow-clear />
        <a-select v-model="filters.method" placeholder="方法" :style="{width:'120px'}" allow-clear><a-option v-for="m in methods" :key="m" :value="m">{{m}}</a-option></a-select>
        <a-input v-model="filters.fromIp" placeholder="From IP" :style="{width:'150px'}" allow-clear />
        <a-input v-model="filters.toIp" placeholder="To IP" :style="{width:'150px'}" allow-clear />
        <a-button type="primary" @click="handleSearch"><template #icon><icon-search /></template>搜索</a-button>
      </a-space>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #method="{record}"><a-tag :color="record.method==='INVITE'?'arcoblue':record.method==='BYE'?'orangered':'gray'">{{record.method}}</a-tag></template>
        <template #direction="{record}"><a-tag :color="record.direction==='in'?'green':'purple'">{{record.direction}}</a-tag></template>
        <template #action="{record}"><a-button type="text" size="small" @click="viewCallDetails(record.callid)"><template #icon><icon-eye /></template></a-button></template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="detailVisible" :title="`呼叫流程: ${selectedCallId}`" :width="900" :footer="false">
      <a-table :data="callDetails" :pagination="false" size="small" row-key="id" :columns="[{title:'时间',dataIndex:'timeStamp',width:180},{title:'方法',dataIndex:'method',width:100},{title:'状态',dataIndex:'status',width:150},{title:'方向',dataIndex:'direction',width:60},{title:'From',dataIndex:'fromIp',width:140},{title:'To',dataIndex:'toIp',width:140}]" />
      <a-card title="消息内容" style="margin-top:16px" v-if="callDetails.length>0"><pre style="max-height:300px;overflow:auto;font-size:12px">{{callDetails[0]?.msg}}</pre></a-card>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getSipTraces, getSipTraceByCallId, getSipTraceStats, cleanupSipTrace } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0});
const stats = ref({total:0,todayCount:0,methodStats:[]}); const filters = reactive({callid:'',tracedUser:'',method:'',fromIp:'',toIp:''});
const methods = ['INVITE','ACK','BYE','CANCEL','REGISTER','OPTIONS','SUBSCRIBE','NOTIFY','MESSAGE','REFER','INFO','UPDATE','PRACK'];
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'时间',dataIndex:'timeStamp',width:180},{title:'Call ID',dataIndex:'callid',ellipsis:true},{title:'方法',dataIndex:'method',slotName:'method',width:100},{title:'状态',dataIndex:'status',width:120},{title:'From IP',dataIndex:'fromIp',width:140},{title:'To IP',dataIndex:'toIp',width:140},{title:'方向',dataIndex:'direction',slotName:'direction',width:60},{title:'操作',slotName:'action',width:80}];
const detailVisible = ref(false); const callDetails = ref<any[]>([]); const selectedCallId = ref('');
async function loadData(){loading.value=true;try{const r=await getSipTraces({page:pagination.current,limit:pagination.pageSize,...filters});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleSearch(){pagination.current=1;loadData();}
async function viewCallDetails(callid:string){selectedCallId.value=callid;const r=await getSipTraceByCallId(callid);if(r.data?.code===0){callDetails.value=r.data.data;detailVisible.value=true;}}
async function loadStats(){try{const r=await getSipTraceStats();if(r.data?.code===0)stats.value=r.data.data;}catch{}}
async function handleCleanup(){await cleanupSipTrace(30);Message.success('清理成功');loadData();loadStats();}
onMounted(()=>{loadData();loadStats();});
</script>
