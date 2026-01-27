<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Modal, Form, FormItem, InputNumber, Popconfirm, message } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SearchOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getUsrPreferences, createUsrPreference, deleteUsrPreference } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');

const modalVisible = ref(false);
const formState = reactive({ uuid: '', username: '', domain: '', attribute: '', type: 0, value: '' });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'UUID', dataIndex: 'uuid', ellipsis: true },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '属性', dataIndex: 'attribute' },
  { title: '类型', dataIndex: 'type', width: 60 },
  { title: '值', dataIndex: 'value' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getUsrPreferences({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } finally { loading.value = false; }
}

function handleSearch() {
  pagination.current = 1;
  loadData();
}

function handleAdd() {
  Object.assign(formState, { uuid: '', username: '', domain: '', attribute: '', type: 0, value: '' });
  modalVisible.value = true;
}

async function handleSubmit() {
  await createUsrPreference(formState);
  message.success('创建成功');
  modalVisible.value = false;
  loadData();
}

async function handleDelete(id: number) {
  await deleteUsrPreference(id);
  message.success('删除成功');
  loadData();
}

onMounted(() => loadData());
</script>

<template>
  <div class="p-4">
    <Card title="用户偏好设置">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索用户名" style="width: 200px" @pressEnter="handleSearch">
            <template #suffix><SearchOutlined @click="handleSearch" /></template>
          </Input>
          <Button type="primary" @click="handleAdd"><PlusOutlined /> 新增</Button>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
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

    <Modal v-model:open="modalVisible" title="新增偏好" @ok="handleSubmit">
      <Form :model="formState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="UUID"><Input v-model:value="formState.uuid" /></FormItem>
        <FormItem label="用户名"><Input v-model:value="formState.username" /></FormItem>
        <FormItem label="域"><Input v-model:value="formState.domain" /></FormItem>
        <FormItem label="属性"><Input v-model:value="formState.attribute" /></FormItem>
        <FormItem label="类型"><InputNumber v-model:value="formState.type" :min="0" style="width: 100%" /></FormItem>
        <FormItem label="值"><Input v-model:value="formState.value" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
