<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <a-row :gutter="16">
      <a-col :span="6">
        <a-card><a-statistic title="用户总数" :value="stats.total" /></a-card>
      </a-col>
      <a-col :span="6">
        <a-card><a-statistic title="域数量" :value="stats.domains.length" /></a-card>
      </a-col>
    </a-row>

    <!-- 搜索和操作栏 -->
    <a-card class="general-card">
      <a-space>
        <a-input v-model="searchKeyword" placeholder="搜索用户名" :style="{ width: '200px' }" @press-enter="handleSearch">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button type="primary" @click="handleSearch">
          <template #icon><icon-search /></template>搜索
        </a-button>
        <a-button @click="loadData">
          <template #icon><icon-refresh /></template>刷新
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><icon-plus /></template>新增用户
        </a-button>
      </a-space>
    </a-card>

    <!-- 数据表格 -->
    <a-card class="general-card">
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="paginationProps" row-key="id" @page-change="onPageChange">
        <template #action="{ record }">
          <a-space>
            <a-button type="text" size="small" @click="handleEdit(record)">
              <template #icon><icon-edit /></template>编辑
            </a-button>
            <a-popconfirm content="确定删除此用户？" @ok="handleDelete(record.id)">
              <a-button type="text" status="danger" size="small">
                <template #icon><icon-delete /></template>删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 编辑弹窗 -->
    <a-modal v-model:visible="modalVisible" :title="modalTitle" @ok="handleSubmit" @cancel="modalVisible = false">
      <a-form ref="formRef" :model="formState" layout="vertical">
        <a-form-item field="username" label="用户名" :rules="[{ required: !editingId, message: '请输入用户名' }]">
          <a-input v-model="formState.username" :disabled="!!editingId" />
        </a-form-item>
        <a-form-item field="domain" label="域" :rules="[{ required: !editingId, message: '请选择域' }]">
          <a-input v-model="formState.domain" :disabled="!!editingId" />
        </a-form-item>
        <a-form-item field="password" label="密码" :rules="[{ required: !editingId, message: '请输入密码' }]">
          <a-input-password v-model="formState.password" :placeholder="editingId ? '留空则不修改' : ''" />
        </a-form-item>
        <a-form-item field="emailAddress" label="邮箱">
          <a-input v-model="formState.emailAddress" />
        </a-form-item>
        <a-form-item field="rpid" label="RP ID">
          <a-input v-model="formState.rpid" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getSubscribers, createSubscriber, updateSubscriber, deleteSubscriber, getSubscriberStats, getAllDomains, type Subscriber, type CreateSubscriberDto } from '@/api/kamailio';

const loading = ref(false);
const data = ref<Subscriber[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const searchKeyword = ref('');
const stats = ref({ total: 0, domains: [] as { domain: string; count: number }[] });

const modalVisible = ref(false);
const modalTitle = ref('新增用户');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateSubscriberDto>({ username: '', domain: '', password: '', emailAddress: '', rpid: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '邮箱', dataIndex: 'emailAddress' },
  { title: 'RP ID', dataIndex: 'rpid' },
  { title: '操作', slotName: 'action', width: 150 },
];

const paginationProps = computed(() => ({
  total: pagination.total,
  current: pagination.current,
  pageSize: pagination.pageSize,
  showTotal: true,
}));

async function loadData() {
  loading.value = true;
  try {
    const res = await getSubscribers({ page: pagination.current, limit: pagination.pageSize, keyword: searchKeyword.value });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } catch { Message.error('加载数据失败'); }
  finally { loading.value = false; }
}

async function loadStats() {
  try {
    const res = await getSubscriberStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

function handleSearch() { pagination.current = 1; loadData(); }
function onPageChange(page: number) { pagination.current = page; loadData(); }

function handleAdd() {
  modalTitle.value = '新增用户';
  editingId.value = null;
  Object.assign(formState, { username: '', domain: '', password: '', emailAddress: '', rpid: '' });
  modalVisible.value = true;
}

function handleEdit(record: Subscriber) {
  modalTitle.value = '编辑用户';
  editingId.value = record.id;
  Object.assign(formState, { username: record.username, domain: record.domain, password: '', emailAddress: record.emailAddress || '', rpid: record.rpid || '' });
  modalVisible.value = true;
}

async function handleDelete(id: number) {
  try {
    const res = await deleteSubscriber(id);
    if (res.data?.code === 0) { Message.success('删除成功'); loadData(); loadStats(); }
    else Message.error(res.data?.message || '删除失败');
  } catch { Message.error('删除失败'); }
}

async function handleSubmit() {
  try {
    const errors = await formRef.value?.validate();
    if (errors) return;
    if (editingId.value) {
      const res = await updateSubscriber(editingId.value, { password: formState.password || undefined, emailAddress: formState.emailAddress, rpid: formState.rpid });
      if (res.data?.code === 0) { Message.success('更新成功'); modalVisible.value = false; loadData(); }
      else Message.error(res.data?.message || '更新失败');
    } else {
      const res = await createSubscriber(formState);
      if (res.data?.code === 0) { Message.success('创建成功'); modalVisible.value = false; loadData(); loadStats(); }
      else Message.error(res.data?.message || '创建失败');
    }
  } catch {}
}

onMounted(() => { loadData(); loadStats(); });
</script>
