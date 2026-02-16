<template>
  <div class="page-container">
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="aliases" title="用户别名">
          <a-space style="margin-bottom:16px"><a-input v-model="aliasKeyword" placeholder="搜索别名" :style="{width:'200px'}" @press-enter="loadAliases"><template #suffix><icon-search /></template></a-input><a-button type="primary" @click="aliasModalVisible=true"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadAliases"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="aliasColumns" :data="aliasData" :loading="aliasLoading" :pagination="{total:aliasPagination.total,current:aliasPagination.current,pageSize:aliasPagination.pageSize}" row-key="id">
            <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleAliasDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="groups" title="用户组">
          <a-space style="margin-bottom:16px"><a-input v-model="groupKeyword" placeholder="搜索用户名" :style="{width:'200px'}" @press-enter="loadGroups"><template #suffix><icon-search /></template></a-input><a-button type="primary" @click="groupModalVisible=true"><template #icon><icon-plus /></template>新增</a-button><a-button @click="loadGroups"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="groupColumns" :data="groupData" :loading="groupLoading" :pagination="{total:groupPagination.total,current:groupPagination.current,pageSize:groupPagination.pageSize}" row-key="id">
            <template #action="{record}"><a-popconfirm content="确定删除？" @ok="handleGroupDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template></a-button></a-popconfirm></template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="speedDial" title="快捷拨号">
          <a-space style="margin-bottom:16px"><a-button @click="loadSpeedDials"><template #icon><icon-refresh /></template>刷新</a-button></a-space>
          <a-table :columns="sdColumns" :data="sdData" :loading="sdLoading" :pagination="{total:sdPagination.total,current:sdPagination.current,pageSize:sdPagination.pageSize}" row-key="id" />
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal v-model:visible="aliasModalVisible" title="新增别名" @ok="handleAliasSubmit"><a-form layout="vertical"><a-form-item label="别名用户名"><a-input v-model="aliasForm.aliasUsername" /></a-form-item><a-form-item label="别名域"><a-input v-model="aliasForm.aliasDomain" /></a-form-item><a-form-item label="真实用户名"><a-input v-model="aliasForm.username" /></a-form-item><a-form-item label="真实域"><a-input v-model="aliasForm.domain" /></a-form-item></a-form></a-modal>
    <a-modal v-model:visible="groupModalVisible" title="新增用户组" @ok="handleGroupSubmit"><a-form layout="vertical"><a-form-item label="用户名"><a-input v-model="groupForm.username" /></a-form-item><a-form-item label="域"><a-input v-model="groupForm.domain" /></a-form-item><a-form-item label="组名"><a-input v-model="groupForm.grp" /></a-form-item></a-form></a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getAliases, createAlias, deleteAlias, getGroups, createGroup, deleteGroup, getSpeedDials } from '@/api/kamailio';
const activeTab = ref('aliases');
const aliasLoading = ref(false); const aliasData = ref<any[]>([]); const aliasPagination = reactive({current:1,pageSize:20,total:0}); const aliasKeyword = ref('');
const aliasModalVisible = ref(false); const aliasForm = reactive({aliasUsername:'',aliasDomain:'',username:'',domain:''});
const aliasColumns = [{title:'ID',dataIndex:'id',width:60},{title:'别名用户名',dataIndex:'aliasUsername'},{title:'别名域',dataIndex:'aliasDomain'},{title:'真实用户名',dataIndex:'username'},{title:'真实域',dataIndex:'domain'},{title:'操作',slotName:'action',width:100}];
async function loadAliases(){aliasLoading.value=true;try{const r=await getAliases({page:aliasPagination.current,limit:aliasPagination.pageSize,keyword:aliasKeyword.value});if(r.data?.code===0){aliasData.value=r.data.data.items;aliasPagination.total=r.data.data.total;}}finally{aliasLoading.value=false;}}
async function handleAliasSubmit(){await createAlias(aliasForm);Message.success('创建成功');aliasModalVisible.value=false;loadAliases();}
async function handleAliasDelete(id:number){await deleteAlias(id);Message.success('删除成功');loadAliases();}
const groupLoading = ref(false); const groupData = ref<any[]>([]); const groupPagination = reactive({current:1,pageSize:20,total:0}); const groupKeyword = ref('');
const groupModalVisible = ref(false); const groupForm = reactive({username:'',domain:'',grp:''});
const groupColumns = [{title:'ID',dataIndex:'id',width:60},{title:'用户名',dataIndex:'username'},{title:'域',dataIndex:'domain'},{title:'组名',dataIndex:'grp'},{title:'操作',slotName:'action',width:100}];
async function loadGroups(){groupLoading.value=true;try{const r=await getGroups({page:groupPagination.current,limit:groupPagination.pageSize,keyword:groupKeyword.value});if(r.data?.code===0){groupData.value=r.data.data.items;groupPagination.total=r.data.data.total;}}finally{groupLoading.value=false;}}
async function handleGroupSubmit(){await createGroup(groupForm);Message.success('创建成功');groupModalVisible.value=false;loadGroups();}
async function handleGroupDelete(id:number){await deleteGroup(id);Message.success('删除成功');loadGroups();}
const sdLoading = ref(false); const sdData = ref<any[]>([]); const sdPagination = reactive({current:1,pageSize:20,total:0});
const sdColumns = [{title:'ID',dataIndex:'id',width:60},{title:'用户名',dataIndex:'username'},{title:'域',dataIndex:'domain'},{title:'快捷号码',dataIndex:'sdUsername'},{title:'目标 URI',dataIndex:'newUri'},{title:'描述',dataIndex:'description'}];
async function loadSpeedDials(){sdLoading.value=true;try{const r=await getSpeedDials({page:sdPagination.current,limit:sdPagination.pageSize});if(r.data?.code===0){sdData.value=r.data.data.items;sdPagination.total=r.data.data.total;}}finally{sdLoading.value=false;}}
onMounted(()=>{loadAliases();loadGroups();loadSpeedDials();});
</script>
