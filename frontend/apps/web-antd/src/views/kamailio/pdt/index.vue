<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Modal, Form, FormItem, Popconfirm, message } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SyncOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getPdtRecords, createPdtRecord, deletePdtRecord, reloadPdt } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');

const modalVisible = ref(false);
const formState = reactive({ sdomain: '', prefix: '', domain: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'SD 域', dataIndex: 'sdomain' },
  { title: '前缀', dataIndex: 'prefix' },
  { title: '域', dataIndex: 'domain' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getPdtRecords({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

async function handleAdd() {
  Object.assign(formState, { sdomain: '', prefix: '', domain: '' });
  modalVisible.value = true;
}

async function handleSubmit() {
  await createPdtRecord(formState);
  message.success('创建成功');
  modalVisible.value = false;
  loadData();
}

async function handleDelete(id: number) {
  await deletePdtRecord(id);
  message.success('删除成功');
  loadData();
}

async function handleReload() {
  await reloadPdt();
  message.success('重载成功');
}

onMounted(() => loadData());
</script>

<template>
  <div class="p-4">
    <Card title="前缀域转换 (PDT)">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索前缀" style="width: 150px" @pressEnter="loadData" />
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
        <FormItem label="SD 域"><Input v-model:value="formState.sdomain" /></FormItem>
        <FormItem label="前缀"><Input v-model:value="formState.prefix" /></FormItem>
        <FormItem label="域"><Input v-model:value="formState.domain" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
