<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="6"><a-card><a-statistic title="调度目标总数" :value="stats.total" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="调度组数量" :value="stats.groups.length" /></a-card></a-col>
    </a-row>
    <a-card class="general-card">
      <a-space>
        <a-input v-model="searchKeyword" placeholder="搜索目标地址" :style="{width:'200px'}" @press-enter="handleSearch"><template #prefix><icon-search /></template></a-input>
        <a-button type="primary" @click="handleSearch"><template #icon><icon-search /></template>搜索</a-button>
        <a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button>
        <a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增调度目标</a-button>
        <a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button>
      </a-space>
    </a-card>
    <a-card class="general-card">
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #setid="{record}"><a-tag color="arcoblue">组 {{record.setid}}</a-tag></template>
        <template #action="{record}">
          <a-space>
            <a-button type="text" size="small" @click="handleEdit(record)"><template #icon><icon-edit /></template>编辑</a-button>
            <a-popconfirm content="确定删除？" @ok="handleDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template>删除</a-button></a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" :title="modalTitle" @ok="handleSubmit">
      <a-form ref="formRef" :model="formState" layout="vertical">
        <a-form-item field="setid" label="调度组 ID" :rules="[{required:true,message:'请输入'}]"><a-input-number v-model="formState.setid" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="destination" label="目标地址" :rules="[{required:true,message:'请输入'}]"><a-input v-model="formState.destination" placeholder="sip:192.168.1.100:5060" /></a-form-item>
        <a-form-item field="priority" label="优先级"><a-input-number v-model="formState.priority" style="width:100%" /></a-form-item>
        <a-form-item field="flags" label="标志"><a-input-number v-model="formState.flags" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="attrs" label="属性"><a-input v-model="formState.attrs" /></a-form-item>
        <a-form-item field="description" label="描述"><a-input v-model="formState.description" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getDispatchers, createDispatcher, updateDispatcher, deleteDispatcher, reloadDispatchers, getDispatcherStats, type Dispatcher, type CreateDispatcherDto } from '@/api/kamailio';

const loading = ref(false);
const data = ref<Dispatcher[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const searchKeyword = ref('');
const stats = ref({ total: 0, groups: [] as any[] });
const modalVisible = ref(false);
const modalTitle = ref('新增调度目标');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateDispatcherDto>({ setid: 1, destination: '', flags: 0, priority: 0, attrs: '', description: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '调度组', dataIndex: 'setid', slotName: 'setid', width: 100 },
  { title: '目标地址', dataIndex: 'destination' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '标志', dataIndex: 'flags', width: 80 },
  { title: '描述', dataIndex: 'description' },
  { title: '操作', slotName: 'action', width: 150 },
];

async function loadData() { loading.value = true; try { const res = await getDispatchers({ page: pagination.current, limit: pagination.pageSize, keyword: searchKeyword.value }); if (res.data?.code === 0) { data.value = res.data.data.items; pagination.total = res.data.data.total; } } catch { Message.error('加载失败'); } finally { loading.value = false; } }
async function loadStats() { try { const res = await getDispatcherStats(); if (res.data?.code === 0) stats.value = res.data.data; } catch {} }
function handleSearch() { pagination.current = 1; loadData(); }
function handleAdd() { modalTitle.value = '新增调度目标'; editingId.value = null; Object.assign(formState, { setid: 1, destination: '', flags: 0, priority: 0, attrs: '', description: '' }); modalVisible.value = true; }
function handleEdit(record: Dispatcher) { modalTitle.value = '编辑调度目标'; editingId.value = record.id; Object.assign(formState, record); modalVisible.value = true; }
async function handleDelete(id: number) { try { const res = await deleteDispatcher(id); if (res.data?.code === 0) { Message.success('删除成功'); loadData(); loadStats(); } } catch { Message.error('删除失败'); } }
async function handleReload() { try { const res = await reloadDispatchers(); if (res.data?.code === 0) Message.success('重载成功'); } catch { Message.error('重载失败'); } }
async function handleSubmit() { const errors = await formRef.value?.validate(); if (errors) return; try { if (editingId.value) { await updateDispatcher(editingId.value, formState); Message.success('更新成功'); } else { await createDispatcher(formState); Message.success('创建成功'); } modalVisible.value = false; loadData(); loadStats(); } catch {} }
onMounted(() => { loadData(); loadStats(); });
</script>
