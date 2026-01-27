<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Modal, Form, FormItem, InputNumber, message, Popconfirm, Tag } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SearchOutlined, EditOutlined, DeleteOutlined, SyncOutlined } from '@ant-design/icons-vue';
import { getUacRegistrations, createUacRegistration, updateUacRegistration, deleteUacRegistration, reloadUac, getUacDump } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');
const modalVisible = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive({
  lUuid: '', lUsername: '', lDomain: '', rUsername: '', rDomain: '',
  realm: '', authUsername: '', authPassword: '', authProxy: '',
  expires: 3600, flags: 0, regDelay: 0, contactAddr: '', socket: ''
});

const dumpVisible = ref(false);
const dumpData = ref<any>(null);

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: 'L UUID', dataIndex: 'lUuid', ellipsis: true },
  { title: 'L 用户名', dataIndex: 'lUsername' },
  { title: 'R 用户名', dataIndex: 'rUsername' },
  { title: 'R 域', dataIndex: 'rDomain' },
  { title: '过期', dataIndex: 'expires', width: 80 },
  { title: '标志', dataIndex: 'flags', width: 60 },
  { title: '操作', key: 'action', width: 150 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getUacRegistrations({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
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
  editingId.value = null;
  Object.assign(formState, {
    lUuid: '', lUsername: '', lDomain: '', rUsername: '', rDomain: '',
    realm: '', authUsername: '', authPassword: '', authProxy: '',
    expires: 3600, flags: 0, regDelay: 0, contactAddr: '', socket: ''
  });
  modalVisible.value = true;
}

function handleEdit(record: any) {
  editingId.value = record.id;
  Object.assign(formState, record);
  modalVisible.value = true;
}

async function handleDelete(id: number) {
  await deleteUacRegistration(id);
  message.success('删除成功');
  loadData();
}

async function handleSubmit() {
  await formRef.value.validate();
  if (editingId.value) {
    await updateUacRegistration(editingId.value, formState);
    message.success('更新成功');
  } else {
    await createUacRegistration(formState);
    message.success('创建成功');
  }
  modalVisible.value = false;
  loadData();
}

async function handleReload() {
  await reloadUac();
  message.success('重载成功');
}

async function handleDump() {
  const res = await getUacDump();
  dumpData.value = res.data?.data;
  dumpVisible.value = true;
}

onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="p-4">
    <Card title="UAC 注册管理">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索用户名" style="width: 200px" @pressEnter="handleSearch">
            <template #suffix><SearchOutlined @click="handleSearch" /></template>
          </Input>
          <Button type="primary" @click="handleAdd"><PlusOutlined /> 新增</Button>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReload"><SyncOutlined /> 重载</Button>
          <Button @click="handleDump">导出内存</Button>
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

    <Modal v-model:open="modalVisible" :title="editingId ? '编辑注册' : '新增注册'" width="600px" @ok="handleSubmit">
      <Form ref="formRef" :model="formState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="L UUID" name="lUuid" :rules="[{ required: true }]"><Input v-model:value="formState.lUuid" /></FormItem>
        <FormItem label="L 用户名" name="lUsername" :rules="[{ required: true }]"><Input v-model:value="formState.lUsername" /></FormItem>
        <FormItem label="L 域" name="lDomain" :rules="[{ required: true }]"><Input v-model:value="formState.lDomain" /></FormItem>
        <FormItem label="R 用户名" name="rUsername" :rules="[{ required: true }]"><Input v-model:value="formState.rUsername" /></FormItem>
        <FormItem label="R 域" name="rDomain" :rules="[{ required: true }]"><Input v-model:value="formState.rDomain" /></FormItem>
        <FormItem label="认证用户名" name="authUsername" :rules="[{ required: true }]"><Input v-model:value="formState.authUsername" /></FormItem>
        <FormItem label="认证密码" name="authPassword" :rules="[{ required: true }]"><Input.Password v-model:value="formState.authPassword" /></FormItem>
        <FormItem label="认证代理" name="authProxy"><Input v-model:value="formState.authProxy" placeholder="sip:ip:port" /></FormItem>
        <FormItem label="过期时间" name="expires"><InputNumber v-model:value="formState.expires" :min="0" style="width: 100%" /></FormItem>
        <FormItem label="标志" name="flags"><InputNumber v-model:value="formState.flags" :min="0" style="width: 100%" /></FormItem>
      </Form>
    </Modal>

    <Modal v-model:open="dumpVisible" title="内存注册状态" width="800px" :footer="null">
      <pre style="max-height: 400px; overflow: auto;">{{ JSON.stringify(dumpData, null, 2) }}</pre>
    </Modal>
  </div>
</template>
