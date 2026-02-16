<template>
  <div class="page-container">
    <a-card title="拨号计划" class="general-card">
      <template #extra>
        <a-space>
          <a-input-number v-model="dpidFilter" placeholder="DPID" :style="{width:'100px'}" @change="loadData" />
          <a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增</a-button>
          <a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button>
          <a-button @click="handleReload"><template #icon><icon-sync /></template>重载</a-button>
          <a-button @click="testVisible=true"><template #icon><icon-play-arrow /></template>测试</a-button>
        </a-space>
      </template>
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #action="{record}">
          <a-space><a-button type="text" size="small" @click="handleEdit(record)"><template #icon><icon-edit /></template></a-button>
          <a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></a-space>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" :title="editingId?'编辑规则':'新增规则'" @ok="handleSubmit">
      <a-form ref="formRef" :model="formState" layout="vertical">
        <a-form-item field="dpid" label="DPID"><a-input-number v-model="formState.dpid" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="priority" label="优先级"><a-input-number v-model="formState.priority" style="width:100%" /></a-form-item>
        <a-form-item field="matchExp" label="匹配表达式" :rules="[{required:true}]"><a-input v-model="formState.matchExp" /></a-form-item>
        <a-form-item field="substExp" label="替换表达式"><a-input v-model="formState.substExp" /></a-form-item>
        <a-form-item field="replExp" label="替换结果"><a-input v-model="formState.replExp" /></a-form-item>
        <a-form-item field="attrs" label="属性"><a-input v-model="formState.attrs" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:visible="testVisible" title="测试拨号计划" @ok="handleTest">
      <a-form layout="vertical">
        <a-form-item label="DPID"><a-input-number v-model="testDpid" style="width:100%" /></a-form-item>
        <a-form-item label="输入"><a-input v-model="testInput" placeholder="待翻译的号码" /></a-form-item>
      </a-form>
      <div v-if="testResult" style="margin-top:16px"><pre>{{JSON.stringify(testResult,null,2)}}</pre></div>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getDialplanRules, createDialplanRule, updateDialplanRule, deleteDialplanRule, reloadDialplan, translateDialplan } from '@/api/kamailio';
const loading = ref(false); const data = ref<any[]>([]); const pagination = reactive({current:1,pageSize:20,total:0}); const dpidFilter = ref<number|undefined>(undefined);
const modalVisible = ref(false); const editingId = ref<number|null>(null); const formRef = ref();
const formState = reactive({dpid:0,priority:0,matchOp:1,matchExp:'',matchLen:0,substExp:'',replExp:'',attrs:''});
const testVisible = ref(false); const testDpid = ref(0); const testInput = ref(''); const testResult = ref<any>(null);
const columns = [{title:'ID',dataIndex:'id',width:60},{title:'DPID',dataIndex:'dpid',width:70},{title:'优先级',dataIndex:'priority',width:70},{title:'匹配表达式',dataIndex:'matchExp'},{title:'替换表达式',dataIndex:'substExp'},{title:'替换结果',dataIndex:'replExp'},{title:'属性',dataIndex:'attrs'},{title:'操作',slotName:'action',width:120}];
async function loadData(){loading.value=true;try{const p:any={page:pagination.current,limit:pagination.pageSize};if(dpidFilter.value!==undefined)p.dpid=dpidFilter.value;const r=await getDialplanRules(p);if(r.data?.code===0){data.value=r.data.data.items;pagination.total=r.data.data.total;}}finally{loading.value=false;}}
function handleAdd(){editingId.value=null;Object.assign(formState,{dpid:0,priority:0,matchOp:1,matchExp:'',matchLen:0,substExp:'',replExp:'',attrs:''});modalVisible.value=true;}
function handleEdit(r:any){editingId.value=r.id;Object.assign(formState,r);modalVisible.value=true;}
async function handleDelete(id:number){await deleteDialplanRule(id);Message.success('删除成功');loadData();}
async function handleSubmit(){const e=await formRef.value?.validate();if(e)return;if(editingId.value){await updateDialplanRule(editingId.value,formState);Message.success('更新成功');}else{await createDialplanRule(formState);Message.success('创建成功');}modalVisible.value=false;loadData();}
async function handleReload(){await reloadDialplan();Message.success('重载成功');}
async function handleTest(){try{const r=await translateDialplan(testDpid.value,testInput.value);testResult.value=r.data?.data;}catch(e:any){testResult.value={error:e.message};}}
onMounted(()=>loadData());
</script>
