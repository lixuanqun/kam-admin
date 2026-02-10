<template>
  <div class="page-container">
    <a-card class="general-card">
      <a-space>
        <a-input v-model="searchKeyword" placeholder="搜索域名" :style="{ width: '200px' }" @press-enter="handleSearch">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button type="primary" @click="handleSearch"><template #icon><icon-search /></template>搜索</a-button>
        <a-button @click="loadData"><template #icon><icon-refresh /></template>刷新</a-button>
        <a-button type="primary" @click="handleAdd"><template #icon><icon-plus /></template>新增域</a-button>
        <a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button>
      </a-space>
    </a-card>
    <a-card class="general-card">
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{ total: pagination.total, current: pagination.current, pageSize: pagination.pageSize, showTotal: true }" row-key="id" @page-change="(p: number) => { pagination.current = p; loadData(); }">
        <template #action="{ record }">
          <a-space>
            <a-button type="text" size="small" @click="handleEdit(record)"><template #icon><icon-edit /></template>编辑</a-button>
            <a-popconfirm content="确定删除此域？" @ok="handleDelete(record.id)">
              <a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:visible="modalVisible" :title="modalTitle" @ok="handleSubmit">
      <a-form ref="formRef" :model="formState" layout="vertical">
        <a-form-item field="domain" label="域名" :rules="[{ required: !editingId, message: '请输入域名' }]">
          <a-input v-model="formState.domain" :disabled="!!editingId" />
        </a-form-item>
        <a-form-item field="did" label="DID"><a-input v-model="formState.did" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getDomains, createDomain, updateDomain, deleteDomain, reloadDomains, type Domain, type CreateDomainDto } from '@/api/kamailio';

const loading = ref(false);
const data = ref<Domain[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const searchKeyword = ref('');
const modalVisible = ref(false);
const modalTitle = ref('新增域');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateDomainDto>({ domain: '', did: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '域名', dataIndex: 'domain' },
  { title: 'DID', dataIndex: 'did' },
  { title: '最后修改', dataIndex: 'lastModified' },
  { title: '操作', slotName: 'action', width: 200 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getDomains({ page: pagination.current, limit: pagination.pageSize, keyword: searchKeyword.value });
    if (res.data?.code === 0) { data.value = res.data.data.items; pagination.total = res.data.data.total; }
  } catch { Message.error('加载数据失败'); }
  finally { loading.value = false; }
}
function handleSearch() { pagination.current = 1; loadData(); }
function handleAdd() { modalTitle.value = '新增域'; editingId.value = null; Object.assign(formState, { domain: '', did: '' }); modalVisible.value = true; }
function handleEdit(record: Domain) { modalTitle.value = '编辑域'; editingId.value = record.id; Object.assign(formState, { domain: record.domain, did: record.did || '' }); modalVisible.value = true; }
async function handleDelete(id: number) { try { const res = await deleteDomain(id); if (res.data?.code === 0) { Message.success('删除成功'); loadData(); } else Message.error(res.data?.message || '删除失败'); } catch { Message.error('删除失败'); } }
async function handleReload() { try { const res = await reloadDomains(); if (res.data?.code === 0) Message.success('重载成功'); else Message.error(res.data?.message || '重载失败'); } catch { Message.error('重载失败'); } }
async function handleSubmit() {
  try {
    const errors = await formRef.value?.validate();
    if (errors) return;
    if (editingId.value) { const res = await updateDomain(editingId.value, { did: formState.did }); if (res.data?.code === 0) { Message.success('更新成功'); modalVisible.value = false; loadData(); } else Message.error(res.data?.message || '更新失败'); }
    else { const res = await createDomain(formState); if (res.data?.code === 0) { Message.success('创建成功'); modalVisible.value = false; loadData(); } else Message.error(res.data?.message || '创建失败'); }
  } catch {}
}
onMounted(() => loadData());
</script>
