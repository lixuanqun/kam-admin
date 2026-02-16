<template>
  <div class="page-container">
    <a-card title="内存树 (Mtree)" class="general-card">
      <template #extra><a-space><a-input v-model="tnameFilter" placeholder="树名" :style="{width:'150px'}" @change="loadData" /><a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleReload"><template #icon><icon-sync /></template>重载</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" title="新增记录" @ok="handleSubmit">
      <a-form :model="formState" layout="vertical"><a-form-item label="树名"><a-input v-model="formState.tname" /></a-form-item><a-form-item label="前缀"><a-input v-model="formState.tprefix" /></a-form-item><a-form-item label="值"><a-input v-model="formState.tvalue" /></a-form-item></a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getMtreeRecords, createMtreeRecord, deleteMtreeRecord, reloadMtree } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const tnameFilter = ref('');
const modalVisible = ref(false); const formState = reactive({tname:'',tprefix:'',tvalue:''});
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'树名',dataIndex:'tname'},{title:'前缀',dataIndex:'tprefix'},{title:'值',dataIndex:'tvalue'},{title:'操作',slotName:'action',width:100}];
async function loadData(){loading.value=true;try{const p:any={page:pagination.current,limit:pagination.pageSize};if(tnameFilter.value)p.tname=tnameFilter.value;const r=await getMtreeRecords(p);if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleAdd(){Object.assign(formState,{tname:'',tprefix:'',tvalue:''});modalVisible.value=true;}
async function handleSubmit(){await createMtreeRecord(formState);Message.success('创建成功');modalVisible.value=false;loadData();}
async function handleDelete(id:number){await deleteMtreeRecord(id);Message.success('删除成功');loadData();}
async function handleReload(){if(!tnameFilter.value){Message.warning('请输入树名');return;}await reloadMtree(tnameFilter.value);Message.success('重载成功');}
onMounted(()=>loadData());
</script>
