<template>
  <div class="page-container">
    <a-card title="UAC 注册管理" class="general-card">
      <template #extra><a-space><a-input v-model="keyword" placeholder="搜索用户名" :style="{width:'200px'}" @press-enter="handleSearch"><template #suffix><icon-search @click="handleSearch" style="cursor:pointer" /></template></a-input><a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleReload"><template #icon><icon-sync /></template>重载</a-button><a-button @click="handleDump">导出内存</a-button></a-space></template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #action="{record}"><a-space><a-button type="text" size="small" @click="handleEdit(record)"><template #icon><icon-edit /></template></a-button><a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></a-space></template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" :title="editingId?'编辑注册':'新增注册'" :width="600" @ok="handleSubmit">
      <a-form ref="formRef" :model="formState" layout="vertical">
        <a-form-item field="lUuid" label="L UUID" :rules="[{required:true}]"><a-input v-model="formState.lUuid" /></a-form-item>
        <a-form-item field="lUsername" label="L 用户名" :rules="[{required:true}]"><a-input v-model="formState.lUsername" /></a-form-item>
        <a-form-item field="lDomain" label="L 域" :rules="[{required:true}]"><a-input v-model="formState.lDomain" /></a-form-item>
        <a-form-item field="rUsername" label="R 用户名" :rules="[{required:true}]"><a-input v-model="formState.rUsername" /></a-form-item>
        <a-form-item field="rDomain" label="R 域" :rules="[{required:true}]"><a-input v-model="formState.rDomain" /></a-form-item>
        <a-form-item field="authUsername" label="认证用户名" :rules="[{required:true}]"><a-input v-model="formState.authUsername" /></a-form-item>
        <a-form-item field="authPassword" label="认证密码" :rules="[{required:true}]"><a-input-password v-model="formState.authPassword" /></a-form-item>
        <a-form-item field="authProxy" label="认证代理"><a-input v-model="formState.authProxy" placeholder="sip:ip:port" /></a-form-item>
        <a-form-item field="expires" label="过期时间"><a-input-number v-model="formState.expires" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="flags" label="标志"><a-input-number v-model="formState.flags" :min="0" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:visible="dumpVisible" title="内存注册状态" :width="800" :footer="false"><pre style="max-height:400px;overflow:auto">{{JSON.stringify(dumpData,null,2)}}</pre></a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getUacRegistrations, createUacRegistration, updateUacRegistration, deleteUacRegistration, reloadUac, getUacDump } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const keyword = ref('');
const modalVisible = ref(false); const editingId = ref<number|null>(null); const formRef = ref();
const formState = reactive({lUuid:'',lUsername:'',lDomain:'',rUsername:'',rDomain:'',realm:'',authUsername:'',authPassword:'',authProxy:'',expires:3600,flags:0,regDelay:0,contactAddr:'',socket:''});
const dumpVisible = ref(false); const dumpData = ref<any>(null);
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'L UUID',dataIndex:'lUuid',ellipsis:true},{title:'L 用户名',dataIndex:'lUsername'},{title:'R 用户名',dataIndex:'rUsername'},{title:'R 域',dataIndex:'rDomain'},{title:'过期',dataIndex:'expires',width:80},{title:'标志',dataIndex:'flags',width:60},{title:'操作',slotName:'action',width:150}];
async function loadData(){loading.value=true;try{const r=await getUacRegistrations({page:pagination.current,limit:pagination.pageSize,keyword:keyword.value});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleSearch(){pagination.current=1;loadData();}
function handleAdd(){editingId.value=null;Object.assign(formState,{lUuid:'',lUsername:'',lDomain:'',rUsername:'',rDomain:'',realm:'',authUsername:'',authPassword:'',authProxy:'',expires:3600,flags:0,regDelay:0,contactAddr:'',socket:''});modalVisible.value=true;}
function handleEdit(r:any){editingId.value=r.id;Object.assign(formState,r);modalVisible.value=true;}
async function handleDelete(id:number){await deleteUacRegistration(id);Message.success('删除成功');loadData();}
async function handleSubmit(){const e=await formRef.value?.validate();if(e)return;if(editingId.value){await updateUacRegistration(editingId.value,formState);Message.success('更新成功');}else{await createUacRegistration(formState);Message.success('创建成功');}modalVisible.value=false;loadData();}
async function handleReload(){await reloadUac();Message.success('重载成功');}
async function handleDump(){const r=await getUacDump();dumpData.value=r.data?.data;dumpVisible.value=true;}
onMounted(()=>loadData());
</script>
