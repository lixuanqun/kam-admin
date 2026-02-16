<template>
  <div class="page-container">
    <a-card class="general-card">
      <template #extra><a-space><a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button><a-button @click="handleDump">导出路由</a-button></a-space></template>
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="carriers" title="运营商">
          <a-space style="margin-bottom:16px"><a-button type="primary" @click="carrierModalVisible=true"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadCarriers"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="carrierColumns" :data="carriers" :pagination="false" row-key="id">
            <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDeleteCarrier(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="domains" title="域">
          <a-space style="margin-bottom:16px"><a-button type="primary" @click="domainModalVisible=true"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadDomains"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="domainColumns" :data="domains" :pagination="false" row-key="id">
            <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleDeleteDomain(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="failureRoutes" title="失败路由">
          <a-space style="margin-bottom:16px"><a-button @click="loadFailureRoutes"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="failureColumns" :data="failureRoutes" :loading="failureLoading" :pagination="{total:failurePagination.total,current:failurePagination.current,pageSize:failurePagination.pageSize}" row-key="id" />
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal v-model:visible="carrierModalVisible" title="新增运营商" @ok="handleAddCarrier"><a-form layout="vertical"><a-form-item label="运营商名称"><a-input v-model="newCarrier" /></a-form-item></a-form></a-modal>
    <a-modal v-model:visible="domainModalVisible" title="新增域" @ok="handleAddDomain"><a-form layout="vertical"><a-form-item label="域名"><a-input v-model="newDomain" /></a-form-item></a-form></a-modal>
    <a-modal v-model:visible="dumpVisible" title="路由配置" :width="800" :footer="false"><pre style="max-height:500px;overflow:auto">{{JSON.stringify(dumpData,null,2)}}</pre></a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getCarrierNames, createCarrierName, deleteCarrierName, getCarrierDomains, createCarrierDomain, deleteCarrierDomain, getCarrierFailureRoutes, reloadCarrierroute, dumpCarrierRoutes } from '@/api/kamailio';
const activeTab = ref('carriers');
const carriers = ref<any[]>([]); const carrierModalVisible = ref(false); const newCarrier = ref('');
const carrierColumns = [{title:'ID',dataIndex:'id',width:60},{title:'运营商名称',dataIndex:'carrier'},{title:'操作',slotName:'action',width:100}];
async function loadCarriers(){const r=await getCarrierNames();if(r.data?.code===0)carriers.value=r.data.data;}
async function handleAddCarrier(){if(!newCarrier.value)return;await createCarrierName(newCarrier.value);Message.success('创建成功');carrierModalVisible.value=false;newCarrier.value='';loadCarriers();}
async function handleDeleteCarrier(id:number){await deleteCarrierName(id);Message.success('删除成功');loadCarriers();}
const domains = ref<any[]>([]); const domainModalVisible = ref(false); const newDomain = ref('');
const domainColumns = [{title:'ID',dataIndex:'id',width:60},{title:'域名',dataIndex:'domain'},{title:'操作',slotName:'action',width:100}];
async function loadDomains(){const r=await getCarrierDomains();if(r.data?.code===0)domains.value=r.data.data;}
async function handleAddDomain(){if(!newDomain.value)return;await createCarrierDomain(newDomain.value);Message.success('创建成功');domainModalVisible.value=false;newDomain.value='';loadDomains();}
async function handleDeleteDomain(id:number){await deleteCarrierDomain(id);Message.success('删除成功');loadDomains();}
const failureRoutes = ref<any[]>([]); const failurePagination = reactive({current:1,pageSize:20,total:0}); const failureLoading = ref(false);
const failureColumns = [{title:'ID',dataIndex:'id',width:60},{title:'运营商',dataIndex:'carrier',width:80},{title:'域',dataIndex:'domain',width:80},{title:'扫描前缀',dataIndex:'scanPrefix'},{title:'主机名',dataIndex:'host_name'},{title:'回复码',dataIndex:'replyCode',width:80},{title:'下一个域',dataIndex:'nextDomain'},{title:'描述',dataIndex:'description'}];
async function loadFailureRoutes(){failureLoading.value=true;try{const r=await getCarrierFailureRoutes({page:failurePagination.current,limit:failurePagination.pageSize});if(r.data?.code===0){failureRoutes.value=r.data.data.items;failurePagination.total=r.data.data.total;}}finally{failureLoading.value=false;}}
const dumpVisible = ref(false); const dumpData = ref<any>(null);
async function handleReload(){await reloadCarrierroute();Message.success('重载成功');}
async function handleDump(){const r=await dumpCarrierRoutes();dumpData.value=r.data?.data;dumpVisible.value=true;}
onMounted(()=>{loadCarriers();loadDomains();loadFailureRoutes();});
</script>
