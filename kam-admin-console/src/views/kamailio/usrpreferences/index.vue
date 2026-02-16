<template>
  <div class="page-container">
    <a-card title="用户偏好设置" class="general-card">
      <template #extra><a-space><a-input v-model="keyword" placeholder="搜索用户名" :style="{width:'200px'}" @press-enter="handleSearch"><template #suffix><icon-search @click="handleSearch" style="cursor:pointer" /></template></a-input><a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" title="新增偏好" @ok="handleSubmit">
      <a-form :model="formState" layout="vertical">
        <a-form-item label="UUID"><a-input v-model="formState.uuid" /></a-form-item>
        <a-form-item label="用户名"><a-input v-model="formState.username" /></a-form-item>
        <a-form-item label="域"><a-input v-model="formState.domain" /></a-form-item>
        <a-form-item label="属性"><a-input v-model="formState.attribute" /></a-form-item>
        <a-form-item label="类型"><a-input-number v-model="formState.type" :min="0" style="width:100%" /></a-form-item>
        <a-form-item label="值"><a-input v-model="formState.value" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getUsrPreferences, createUsrPreference, deleteUsrPreference } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const keyword = ref('');
const modalVisible = ref(false); const formState = reactive({uuid:'',username:'',domain:'',attribute:'',type:0,value:''});
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'UUID',dataIndex:'uuid',ellipsis:true},{title:'用户名',dataIndex:'username'},{title:'域',dataIndex:'domain'},{title:'属性',dataIndex:'attribute'},{title:'类型',dataIndex:'type',width:60},{title:'值',dataIndex:'value'},{title:'操作',slotName:'action',width:100}];
async function loadData(){loading.value=true;try{const r=await getUsrPreferences({page:pagination.current,limit:pagination.pageSize,keyword:keyword.value});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleSearch(){pagination.current=1;loadData();}
function handleAdd(){Object.assign(formState,{uuid:'',username:'',domain:'',attribute:'',type:0,value:''});modalVisible.value=true;}
async function handleSubmit(){await createUsrPreference(formState);Message.success('创建成功');modalVisible.value=false;loadData();}
async function handleDelete(id:number){await deleteUsrPreference(id);Message.success('删除成功');loadData();}
onMounted(()=>loadData());
</script>
