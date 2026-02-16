<template>
  <div class="page-container">
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="gateways" title="网关">
          <a-space style="margin-bottom:16px"><a-button @click="loadGateways"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button></a-space>
          <a-table :columns="gwColumns" :data="gwData" :loading="gwLoading" :pagination="{total:gwPagination.total,current:gwPagination.current,pageSize:gwPagination.pageSize,showTotal:true}" row-key="id" />
        </a-tab-pane>
        <a-tab-pane key="rules" title="规则">
          <a-space style="margin-bottom:16px"><a-button @click="loadRules"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="ruleColumns" :data="ruleData" :loading="ruleLoading" :pagination="{total:rulePagination.total,current:rulePagination.current,pageSize:rulePagination.pageSize,showTotal:true}" row-key="id" />
        </a-tab-pane>
        <a-tab-pane key="targets" title="目标">
          <a-space style="margin-bottom:16px"><a-button @click="loadTargets"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="targetColumns" :data="targetData" :loading="targetLoading" :pagination="{total:targetPagination.total,current:targetPagination.current,pageSize:targetPagination.pageSize,showTotal:true}" row-key="id" />
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getLcrGateways, getLcrRules, getLcrTargets, reloadLcr } from '@/api/kamailio';
const activeTab = ref('gateways');
const gwLoading = ref(false); const gwData = ref<any[]>([]); const gwPagination = reactive({current:1,pageSize:20,total:0});
const gwColumns = [{title:'ID',dataIndex:'id',width:60},{title:'LCR ID',dataIndex:'lcrId',width:80},{title:'网关名',dataIndex:'gwName'},{title:'IP',dataIndex:'ipAddr'},{title:'端口',dataIndex:'port',width:80},{title:'标签',dataIndex:'tag'}];
async function loadGateways(){gwLoading.value=true;try{const r=await getLcrGateways({page:gwPagination.current,limit:gwPagination.pageSize});if(r.data?.code===0){gwData.value=r.data.data.items;gwPagination.total=r.data.data.total;}}finally{gwLoading.value=false;}}
const ruleLoading = ref(false); const ruleData = ref<any[]>([]); const rulePagination = reactive({current:1,pageSize:20,total:0});
const ruleColumns = [{title:'ID',dataIndex:'id',width:60},{title:'LCR ID',dataIndex:'lcrId',width:80},{title:'前缀',dataIndex:'prefix'},{title:'From URI',dataIndex:'fromUri'},{title:'启用',dataIndex:'enabled',width:80}];
async function loadRules(){ruleLoading.value=true;try{const r=await getLcrRules({page:rulePagination.current,limit:rulePagination.pageSize});if(r.data?.code===0){ruleData.value=r.data.data.items;rulePagination.total=r.data.data.total;}}finally{ruleLoading.value=false;}}
const targetLoading = ref(false); const targetData = ref<any[]>([]); const targetPagination = reactive({current:1,pageSize:20,total:0});
const targetColumns = [{title:'ID',dataIndex:'id',width:60},{title:'LCR ID',dataIndex:'lcrId',width:80},{title:'规则 ID',dataIndex:'ruleId',width:80},{title:'网关 ID',dataIndex:'gwId',width:80},{title:'优先级',dataIndex:'priority',width:80},{title:'权重',dataIndex:'weight',width:80}];
async function loadTargets(){targetLoading.value=true;try{const r=await getLcrTargets({page:targetPagination.current,limit:targetPagination.pageSize});if(r.data?.code===0){targetData.value=r.data.data.items;targetPagination.total=r.data.data.total;}}finally{targetLoading.value=false;}}
async function handleReload(){await reloadLcr();Message.success('重载成功');}
onMounted(()=>{loadGateways();loadRules();loadTargets();});
</script>
