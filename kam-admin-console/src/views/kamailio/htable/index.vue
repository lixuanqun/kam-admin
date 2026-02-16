<template>
  <div class="page-container">
    <a-card title="哈希表管理" class="general-card">
      <template #extra><a-space><a-input v-model="keyword" placeholder="搜索键名" :style="{width:'200px'}" @press-enter="handleSearch"><template #suffix><icon-search @click="handleSearch" style="cursor:pointer" /></template></a-input><a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button></a-space></template>
      <a-space style="margin-bottom:16px">
        <a-button @click="showRpcModal('get')"><template #icon><icon-search /></template>查询值</a-button>
        <a-button type="primary" @click="showRpcModal('set')"><template #icon><icon-plus /></template>设置值</a-button>
        <a-button status="danger" @click="showRpcModal('delete')"><template #icon><icon-delete /></template>删除键</a-button>
        <a-button @click="showRpcModal('reload')"><template #icon><icon-sync /></template>重载表</a-button>
      </a-space>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}" />
    </a-card>
    <a-modal v-model:visible="rpcModalVisible" :title="rpcTitles[rpcAction]" @ok="handleRpcSubmit">
      <a-form layout="vertical">
        <a-form-item label="表名"><a-input v-model="rpcForm.table" placeholder="htable 表名" /></a-form-item>
        <a-form-item v-if="['get','set','delete'].includes(rpcAction)" label="键名"><a-input v-model="rpcForm.key" /></a-form-item>
        <a-form-item v-if="rpcAction==='set'" label="值"><a-input v-model="rpcForm.value" /></a-form-item>
      </a-form>
      <div v-if="rpcAction==='get'&&rpcResult!==null" style="margin-top:16px"><a-descriptions bordered :column="1"><a-descriptions-item label="结果">{{JSON.stringify(rpcResult)}}</a-descriptions-item></a-descriptions></div>
      <div style="margin-top:16px" v-if="['get','set','delete'].includes(rpcAction)"><a-button long @click="handleDump">导出整个表</a-button></div>
    </a-modal>
    <a-modal v-model:visible="dumpModalVisible" title="表内容" :width="800" :footer="false"><pre style="max-height:400px;overflow:auto">{{JSON.stringify(dumpData,null,2)}}</pre></a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getHtableRecords, getHtableValue, setHtableString, deleteHtableKey, dumpHtable, reloadHtable } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const keyword = ref('');
const rpcModalVisible = ref(false); const rpcAction = ref('get'); const rpcForm = reactive({table:'',key:'',value:''}); const rpcResult = ref<any>(null);
const dumpModalVisible = ref(false); const dumpData = ref<any>(null);
const rpcTitles: Record<string,string> = {get:'查询值',set:'设置值',delete:'删除键',reload:'重载表'};
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'键名',dataIndex:'keyName'},{title:'键类型',dataIndex:'keyType',width:80},{title:'键值',dataIndex:'keyValue'},{title:'值类型',dataIndex:'valueType',width:80},{title:'过期时间',dataIndex:'expires',width:100}];
async function loadData(){loading.value=true;try{const r=await getHtableRecords({page:pagination.current,limit:pagination.pageSize,keyword:keyword.value});if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleSearch(){pagination.current=1;loadData();}
function showRpcModal(action:string){rpcAction.value=action;rpcResult.value=null;rpcModalVisible.value=true;}
async function handleRpcSubmit(){try{switch(rpcAction.value){case 'get':const gr=await getHtableValue(rpcForm.table,rpcForm.key);rpcResult.value=gr.data?.data;break;case 'set':await setHtableString(rpcForm.table,rpcForm.key,rpcForm.value);Message.success('设置成功');rpcModalVisible.value=false;break;case 'delete':await deleteHtableKey(rpcForm.table,rpcForm.key);Message.success('删除成功');rpcModalVisible.value=false;break;case 'reload':await reloadHtable(rpcForm.table);Message.success('重载成功');rpcModalVisible.value=false;break;}}catch(e:any){Message.error(e.message||'操作失败');}}
async function handleDump(){if(!rpcForm.table){Message.warning('请输入表名');return;}try{const r=await dumpHtable(rpcForm.table);dumpData.value=r.data?.data;dumpModalVisible.value=true;}catch(e:any){Message.error(e.message||'导出失败');}}
onMounted(()=>loadData());
</script>
