<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="6"><a-card><a-statistic title="网关数" :value="stats.gateways" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="规则数" :value="stats.rules" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="分组数" :value="stats.groups" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="运营商数" :value="stats.carriers" /></a-card></a-col>
    </a-row>
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="gateways" title="网关">
          <a-space style="margin-bottom:16px">
            <a-button type="primary" @click="handleGwAdd"><template #icon><icon-plus /></template>新增</a-button>
            <a-button @click="loadGateways"><template #icon><icon-refresh /></template>刷新</a-button>
            <a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button>
          </a-space>
          <a-table :columns="gwColumns" :data="gwData" :loading="gwLoading" :pagination="{total:gwPagination.total,current:gwPagination.current,pageSize:gwPagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{gwPagination.current=p;loadGateways()}">
            <template #state="{record}"><a-tag :color="record.state===0?'green':'red'">{{record.state===0?'启用':'禁用'}}</a-tag></template>
            <template #action="{record}">
              <a-space><a-button type="text" size="small" @click="handleGwEdit(record)"><template #icon><icon-edit /></template></a-button>
              <a-popconfirm content="确定删除？" @ok="handleGwDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></a-space>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="rules" title="规则">
          <a-space style="margin-bottom:16px">
            <a-button type="primary" @click="handleRuleAdd"><template #icon><icon-plus /></template>新增</a-button>
            <a-button @click="loadRules"><template #icon><icon-refresh /></template>刷新</a-button>
          </a-space>
          <a-table :columns="ruleColumns" :data="ruleData" :loading="ruleLoading" :pagination="{total:rulePagination.total,current:rulePagination.current,pageSize:rulePagination.pageSize,showTotal:true}" row-key="ruleid" @page-change="(p:number)=>{rulePagination.current=p;loadRules()}">
            <template #action="{record}">
              <a-space><a-button type="text" size="small" @click="handleRuleEdit(record)"><template #icon><icon-edit /></template></a-button>
              <a-popconfirm content="确定删除？" @ok="handleRuleDelete(record.ruleid)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></a-space>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal v-model:visible="gwModalVisible" :title="gwEditingId?'编辑网关':'新增网关'" @ok="handleGwSubmit">
      <a-form ref="gwFormRef" :model="gwFormState" layout="vertical">
        <a-form-item field="gwid" label="网关 ID" :rules="[{required:true}]"><a-input v-model="gwFormState.gwid" /></a-form-item>
        <a-form-item field="address" label="地址" :rules="[{required:true}]"><a-input v-model="gwFormState.address" placeholder="sip:ip:port" /></a-form-item>
        <a-form-item field="type" label="类型"><a-input-number v-model="gwFormState.type" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="state" label="状态"><a-input-number v-model="gwFormState.state" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="description" label="描述"><a-input v-model="gwFormState.description" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:visible="ruleModalVisible" :title="ruleEditingId?'编辑规则':'新增规则'" @ok="handleRuleSubmit">
      <a-form ref="ruleFormRef" :model="ruleFormState" layout="vertical">
        <a-form-item field="groupid" label="分组 ID" :rules="[{required:true}]"><a-input v-model="ruleFormState.groupid" /></a-form-item>
        <a-form-item field="prefix" label="前缀" :rules="[{required:true}]"><a-input v-model="ruleFormState.prefix" /></a-form-item>
        <a-form-item field="gwlist" label="网关列表" :rules="[{required:true}]"><a-input v-model="ruleFormState.gwlist" placeholder="gw1,gw2" /></a-form-item>
        <a-form-item field="priority" label="优先级"><a-input-number v-model="ruleFormState.priority" style="width:100%" /></a-form-item>
        <a-form-item field="description" label="描述"><a-input v-model="ruleFormState.description" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getDrGateways, createDrGateway, updateDrGateway, deleteDrGateway, getDrRules, createDrRule, updateDrRule, deleteDrRule, reloadDrouting, getDrStats } from '@/api/kamailio';

const activeTab = ref('gateways');
const stats = ref({ gateways: 0, rules: 0, groups: 0, carriers: 0 });
const gwLoading = ref(false); const gwData = ref<any[]>([]); const gwPagination = reactive({ current:1, pageSize:20, total:0 });
const gwModalVisible = ref(false); const gwEditingId = ref<number|null>(null); const gwFormRef = ref();
const gwFormState = reactive({ gwid:'', type:0, address:'', strip:0, priPrefix:'', attrs:'', probeMode:0, state:0, description:'' });
const gwColumns = [{ title:'ID',dataIndex:'id',width:60 },{ title:'网关 ID',dataIndex:'gwid' },{ title:'地址',dataIndex:'address' },{ title:'类型',dataIndex:'type',width:60 },{ title:'状态',dataIndex:'state',slotName:'state',width:80 },{ title:'描述',dataIndex:'description' },{ title:'操作',slotName:'action',width:150 }];

async function loadGateways() { gwLoading.value=true; try { const r=await getDrGateways({page:gwPagination.current,limit:gwPagination.pageSize}); if(r.data?.code===0){gwData.value=r.data.data.items;gwPagination.total=r.data.data.total;} } finally { gwLoading.value=false; } }
function handleGwAdd() { gwEditingId.value=null; Object.assign(gwFormState,{gwid:'',type:0,address:'',strip:0,priPrefix:'',attrs:'',probeMode:0,state:0,description:''}); gwModalVisible.value=true; }
function handleGwEdit(r:any) { gwEditingId.value=r.id; Object.assign(gwFormState,r); gwModalVisible.value=true; }
async function handleGwDelete(id:number) { await deleteDrGateway(id); Message.success('删除成功'); loadGateways(); loadStats(); }
async function handleGwSubmit() { const e=await gwFormRef.value?.validate(); if(e)return; if(gwEditingId.value){await updateDrGateway(gwEditingId.value,gwFormState);Message.success('更新成功');}else{await createDrGateway(gwFormState);Message.success('创建成功');} gwModalVisible.value=false; loadGateways(); loadStats(); }

const ruleLoading = ref(false); const ruleData = ref<any[]>([]); const rulePagination = reactive({ current:1, pageSize:20, total:0 });
const ruleModalVisible = ref(false); const ruleEditingId = ref<number|null>(null); const ruleFormRef = ref();
const ruleFormState = reactive({ groupid:'', prefix:'', timerec:'', priority:0, routeid:'', gwlist:'', attrs:'', description:'' });
const ruleColumns = [{ title:'ID',dataIndex:'ruleid',width:60 },{ title:'分组',dataIndex:'groupid',width:80 },{ title:'前缀',dataIndex:'prefix' },{ title:'优先级',dataIndex:'priority',width:80 },{ title:'网关列表',dataIndex:'gwlist' },{ title:'描述',dataIndex:'description' },{ title:'操作',slotName:'action',width:150 }];

async function loadRules() { ruleLoading.value=true; try { const r=await getDrRules({page:rulePagination.current,limit:rulePagination.pageSize}); if(r.data?.code===0){ruleData.value=r.data.data.items;rulePagination.total=r.data.data.total;} } finally { ruleLoading.value=false; } }
function handleRuleAdd() { ruleEditingId.value=null; Object.assign(ruleFormState,{groupid:'',prefix:'',timerec:'',priority:0,routeid:'',gwlist:'',attrs:'',description:''}); ruleModalVisible.value=true; }
function handleRuleEdit(r:any) { ruleEditingId.value=r.ruleid; Object.assign(ruleFormState,r); ruleModalVisible.value=true; }
async function handleRuleDelete(id:number) { await deleteDrRule(id); Message.success('删除成功'); loadRules(); loadStats(); }
async function handleRuleSubmit() { const e=await ruleFormRef.value?.validate(); if(e)return; if(ruleEditingId.value){await updateDrRule(ruleEditingId.value,ruleFormState);Message.success('更新成功');}else{await createDrRule(ruleFormState);Message.success('创建成功');} ruleModalVisible.value=false; loadRules(); loadStats(); }

async function handleReload() { await reloadDrouting(); Message.success('重载成功'); }
async function loadStats() { try { const r=await getDrStats(); if(r.data?.code===0) stats.value=r.data.data; } catch {} }
onMounted(() => { loadGateways(); loadRules(); loadStats(); });
</script>
