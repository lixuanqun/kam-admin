<template>
  <div class="page-container">
    <a-card title="安全过滤" class="general-card">
      <template #extra><a-space><a-input v-model="keyword" placeholder="搜索" :style="{width:'150px'}" @press-enter="loadData" /><a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增</a-button><a-button @click="quickAddVisible=true">快速添加</a-button><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleReload"><template #icon><icon-sync /></template>重载</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #actionCol="{record}"><a-tag :color="record.action===0?'red':'green'">{{record.action===0?'拒绝':'接受'}}</a-tag></template>
        <template #typeCol="{record}">{{typeLabels[record.type]||record.type}}</template>
        <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" title="新增规则" @ok="handleSubmit">
      <a-form :model="formState" layout="vertical">
        <a-form-item label="动作"><a-select v-model="formState.action"><a-option :value="0">拒绝 (Blacklist)</a-option><a-option :value="1">接受 (Whitelist)</a-option></a-select></a-form-item>
        <a-form-item label="类型"><a-select v-model="formState.type"><a-option :value="0">User-Agent</a-option><a-option :value="1">Country</a-option><a-option :value="2">Domain</a-option><a-option :value="3">IP</a-option><a-option :value="4">User</a-option></a-select></a-form-item>
        <a-form-item label="数据"><a-input v-model="formState.data" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:visible="quickAddVisible" title="快速添加到内存" @ok="handleQuickAdd">
      <a-form :model="quickAddForm" layout="vertical">
        <a-form-item label="列表类型"><a-select v-model="quickAddForm.listType"><a-option value="bl">黑名单</a-option><a-option value="wl">白名单</a-option></a-select></a-form-item>
        <a-form-item label="类型"><a-select v-model="quickAddForm.type"><a-option :value="0">User-Agent</a-option><a-option :value="1">Country</a-option><a-option :value="2">Domain</a-option><a-option :value="3">IP</a-option><a-option :value="4">User</a-option></a-select></a-form-item>
        <a-form-item label="数据"><a-input v-model="quickAddForm.data" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getSecfilterRules, createSecfilterRule, deleteSecfilterRule, reloadSecfilter, getSecfilterStats, addSecfilterBlacklist, addSecfilterWhitelist } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const keyword = ref('');
const modalVisible = ref(false); const formState = reactive({action:0,type:0,data:''});
const quickAddVisible = ref(false); const quickAddForm = reactive({listType:'bl',type:0,data:''});
const typeLabels: Record<number,string> = {0:'User-Agent',1:'Country',2:'Domain',3:'IP',4:'User'};
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'动作',dataIndex:'action',slotName:'actionCol',width:80},{title:'类型',dataIndex:'type',slotName:'typeCol',width:100},{title:'数据',dataIndex:'data'},{title:'操作',slotName:'action',width:100}];
async function loadData(){loading.value=true;try{const r=await getSecfilterRules({page:pagination.current,limit:pagination.pageSize,keyword:keyword.value});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleAdd(){Object.assign(formState,{action:0,type:0,data:''});modalVisible.value=true;}
async function handleSubmit(){await createSecfilterRule(formState);Message.success('创建成功');modalVisible.value=false;loadData();}
async function handleDelete(id:number){await deleteSecfilterRule(id);Message.success('删除成功');loadData();}
async function handleReload(){await reloadSecfilter();Message.success('重载成功');}
async function handleQuickAdd(){if(quickAddForm.listType==='bl')await addSecfilterBlacklist(quickAddForm.type,quickAddForm.data);else await addSecfilterWhitelist(quickAddForm.type,quickAddForm.data);Message.success('添加成功');quickAddVisible.value=false;}
onMounted(()=>loadData());
</script>
