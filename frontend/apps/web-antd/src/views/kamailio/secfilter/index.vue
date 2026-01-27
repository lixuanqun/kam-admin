<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, InputNumber, Modal, Form, FormItem, Popconfirm, Tag, Row, Col, Statistic, message, Select } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SyncOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getSecfilterRules, createSecfilterRule, deleteSecfilterRule, reloadSecfilter, getSecfilterStats, addSecfilterBlacklist, addSecfilterWhitelist } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');
const stats = ref<any>(null);

const modalVisible = ref(false);
const formState = reactive({ action: 0, type: 0, data: '' });

const quickAddVisible = ref(false);
const quickAddForm = reactive({ listType: 'bl', type: 0, data: '' });

const actionLabels = { 0: '拒绝', 1: '接受' };
const typeLabels = { 0: 'User-Agent', 1: 'Country', 2: 'Domain', 3: 'IP', 4: 'User' };

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '动作', dataIndex: 'action', width: 80 },
  { title: '类型', dataIndex: 'type', width: 100 },
  { title: '数据', dataIndex: 'data' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getSecfilterRules({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

async function loadStats() {
  try {
    const res = await getSecfilterStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

async function handleAdd() {
  Object.assign(formState, { action: 0, type: 0, data: '' });
  modalVisible.value = true;
}

async function handleSubmit() {
  await createSecfilterRule(formState);
  message.success('创建成功');
  modalVisible.value = false;
  loadData();
}

async function handleDelete(id: number) {
  await deleteSecfilterRule(id);
  message.success('删除成功');
  loadData();
}

async function handleReload() {
  await reloadSecfilter();
  message.success('重载成功');
  loadStats();
}

async function handleQuickAdd() {
  if (quickAddForm.listType === 'bl') {
    await addSecfilterBlacklist(quickAddForm.type, quickAddForm.data);
  } else {
    await addSecfilterWhitelist(quickAddForm.type, quickAddForm.data);
  }
  message.success('添加成功');
  quickAddVisible.value = false;
  loadStats();
}

onMounted(() => {
  loadData();
  loadStats();
});
</script>

<template>
  <div class="p-4">
    <Card title="安全过滤">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索" style="width: 150px" @pressEnter="loadData" />
          <Button type="primary" @click="handleAdd"><PlusOutlined /> 新增</Button>
          <Button @click="quickAddVisible = true">快速添加</Button>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReload"><SyncOutlined /> 重载</Button>
        </Space>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'action'">
            <Tag :color="record.action === 0 ? 'red' : 'green'">{{ actionLabels[record.action] }}</Tag>
          </template>
          <template v-else-if="column.dataIndex === 'type'">
            {{ typeLabels[record.type] || record.type }}
          </template>
          <template v-else-if="column.key === 'action'">
            <Popconfirm title="确定删除？" @confirm="handleDelete(record.id)">
              <Button type="link" size="small" danger><DeleteOutlined /></Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>

    <Modal v-model:open="modalVisible" title="新增规则" @ok="handleSubmit">
      <Form :model="formState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="动作">
          <Select v-model:value="formState.action">
            <Select.Option :value="0">拒绝 (Blacklist)</Select.Option>
            <Select.Option :value="1">接受 (Whitelist)</Select.Option>
          </Select>
        </FormItem>
        <FormItem label="类型">
          <Select v-model:value="formState.type">
            <Select.Option :value="0">User-Agent</Select.Option>
            <Select.Option :value="1">Country</Select.Option>
            <Select.Option :value="2">Domain</Select.Option>
            <Select.Option :value="3">IP</Select.Option>
            <Select.Option :value="4">User</Select.Option>
          </Select>
        </FormItem>
        <FormItem label="数据"><Input v-model:value="formState.data" /></FormItem>
      </Form>
    </Modal>

    <Modal v-model:open="quickAddVisible" title="快速添加到内存" @ok="handleQuickAdd">
      <Form :model="quickAddForm" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="列表类型">
          <Select v-model:value="quickAddForm.listType">
            <Select.Option value="bl">黑名单</Select.Option>
            <Select.Option value="wl">白名单</Select.Option>
          </Select>
        </FormItem>
        <FormItem label="类型">
          <Select v-model:value="quickAddForm.type">
            <Select.Option :value="0">User-Agent</Select.Option>
            <Select.Option :value="1">Country</Select.Option>
            <Select.Option :value="2">Domain</Select.Option>
            <Select.Option :value="3">IP</Select.Option>
            <Select.Option :value="4">User</Select.Option>
          </Select>
        </FormItem>
        <FormItem label="数据"><Input v-model:value="quickAddForm.data" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
