<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Modal, Form, FormItem, Popconfirm, message } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SyncOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getMtreeRecords, createMtreeRecord, deleteMtreeRecord, reloadMtree } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const tnameFilter = ref('');

const modalVisible = ref(false);
const formState = reactive({ tname: '', tprefix: '', tvalue: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '树名', dataIndex: 'tname' },
  { title: '前缀', dataIndex: 'tprefix' },
  { title: '值', dataIndex: 'tvalue' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const params: any = { page: pagination.current, limit: pagination.pageSize };
    if (tnameFilter.value) params.tname = tnameFilter.value;
    const res = await getMtreeRecords(params);
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

async function handleAdd() {
  Object.assign(formState, { tname: '', tprefix: '', tvalue: '' });
  modalVisible.value = true;
}

async function handleSubmit() {
  await createMtreeRecord(formState);
  message.success('创建成功');
  modalVisible.value = false;
  loadData();
}

async function handleDelete(id: number) {
  await deleteMtreeRecord(id);
  message.success('删除成功');
  loadData();
}

async function handleReload() {
  if (!tnameFilter.value) {
    message.warning('请输入树名');
    return;
  }
  await reloadMtree(tnameFilter.value);
  message.success('重载成功');
}

onMounted(() => loadData());
</script>

<template>
  <div class="p-4">
    <Card title="内存树 (Mtree)">
      <template #extra>
        <Space>
          <Input v-model:value="tnameFilter" placeholder="树名" style="width: 150px" @change="loadData" />
          <Button type="primary" @click="handleAdd"><PlusOutlined /> 新增</Button>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReload"><SyncOutlined /> 重载</Button>
        </Space>
      </template>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <Popconfirm title="确定删除？" @confirm="handleDelete(record.id)">
              <Button type="link" size="small" danger><DeleteOutlined /></Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>

    <Modal v-model:open="modalVisible" title="新增记录" @ok="handleSubmit">
      <Form :model="formState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="树名"><Input v-model:value="formState.tname" /></FormItem>
        <FormItem label="前缀"><Input v-model:value="formState.tprefix" /></FormItem>
        <FormItem label="值"><Input v-model:value="formState.tvalue" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
