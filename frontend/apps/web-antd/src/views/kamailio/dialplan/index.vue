<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, InputNumber, Modal, Form, FormItem, Popconfirm, message } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SyncOutlined, EditOutlined, DeleteOutlined, PlayCircleOutlined } from '@ant-design/icons-vue';
import { getDialplanRules, createDialplanRule, updateDialplanRule, deleteDialplanRule, reloadDialplan, translateDialplan } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const dpidFilter = ref<number | undefined>(undefined);

const modalVisible = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive({
  dpid: 0, priority: 0, matchOp: 1, matchExp: '', matchLen: 0, substExp: '', replExp: '', attrs: ''
});

// 测试翻译
const testVisible = ref(false);
const testDpid = ref(0);
const testInput = ref('');
const testResult = ref<any>(null);

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'DPID', dataIndex: 'dpid', width: 70 },
  { title: '优先级', dataIndex: 'priority', width: 70 },
  { title: '匹配表达式', dataIndex: 'matchExp' },
  { title: '替换表达式', dataIndex: 'substExp' },
  { title: '替换结果', dataIndex: 'replExp' },
  { title: '属性', dataIndex: 'attrs' },
  { title: '操作', key: 'action', width: 120 },
];

async function loadData() {
  loading.value = true;
  try {
    const params: any = { page: pagination.current, limit: pagination.pageSize };
    if (dpidFilter.value !== undefined) params.dpid = dpidFilter.value;
    const res = await getDialplanRules(params);
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

function handleAdd() {
  editingId.value = null;
  Object.assign(formState, { dpid: 0, priority: 0, matchOp: 1, matchExp: '', matchLen: 0, substExp: '', replExp: '', attrs: '' });
  modalVisible.value = true;
}

function handleEdit(record: any) {
  editingId.value = record.id;
  Object.assign(formState, record);
  modalVisible.value = true;
}

async function handleDelete(id: number) {
  await deleteDialplanRule(id);
  message.success('删除成功');
  loadData();
}

async function handleSubmit() {
  await formRef.value.validate();
  if (editingId.value) {
    await updateDialplanRule(editingId.value, formState);
    message.success('更新成功');
  } else {
    await createDialplanRule(formState);
    message.success('创建成功');
  }
  modalVisible.value = false;
  loadData();
}

async function handleReload() {
  await reloadDialplan();
  message.success('重载成功');
}

async function handleTest() {
  try {
    const res = await translateDialplan(testDpid.value, testInput.value);
    testResult.value = res.data?.data;
  } catch (e: any) {
    testResult.value = { error: e.message };
  }
}

onMounted(() => loadData());
</script>

<template>
  <div class="p-4">
    <Card title="拨号计划">
      <template #extra>
        <Space>
          <InputNumber v-model:value="dpidFilter" placeholder="DPID" style="width: 100px" @change="loadData" />
          <Button type="primary" @click="handleAdd"><PlusOutlined /> 新增</Button>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReload"><SyncOutlined /> 重载</Button>
          <Button @click="testVisible = true"><PlayCircleOutlined /> 测试</Button>
        </Space>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <Space>
              <Button type="link" size="small" @click="handleEdit(record)"><EditOutlined /></Button>
              <Popconfirm title="确定删除？" @confirm="handleDelete(record.id)">
                <Button type="link" size="small" danger><DeleteOutlined /></Button>
              </Popconfirm>
            </Space>
          </template>
        </template>
      </Table>
    </Card>

    <Modal v-model:open="modalVisible" :title="editingId ? '编辑规则' : '新增规则'" @ok="handleSubmit">
      <Form ref="formRef" :model="formState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="DPID" name="dpid"><InputNumber v-model:value="formState.dpid" :min="0" style="width: 100%" /></FormItem>
        <FormItem label="优先级" name="priority"><InputNumber v-model:value="formState.priority" style="width: 100%" /></FormItem>
        <FormItem label="匹配表达式" name="matchExp" :rules="[{ required: true }]"><Input v-model:value="formState.matchExp" /></FormItem>
        <FormItem label="替换表达式" name="substExp"><Input v-model:value="formState.substExp" /></FormItem>
        <FormItem label="替换结果" name="replExp"><Input v-model:value="formState.replExp" /></FormItem>
        <FormItem label="属性" name="attrs"><Input v-model:value="formState.attrs" /></FormItem>
      </Form>
    </Modal>

    <Modal v-model:open="testVisible" title="测试拨号计划" @ok="handleTest">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="DPID"><InputNumber v-model:value="testDpid" style="width: 100%" /></FormItem>
        <FormItem label="输入"><Input v-model:value="testInput" placeholder="待翻译的号码" /></FormItem>
      </Form>
      <div v-if="testResult" class="mt-4">
        <pre>{{ JSON.stringify(testResult, null, 2) }}</pre>
      </div>
    </Modal>
  </div>
</template>
