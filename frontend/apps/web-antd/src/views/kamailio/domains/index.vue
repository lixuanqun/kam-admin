<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {
  Table,
  Button,
  Space,
  Input,
  Modal,
  Form,
  FormItem,
  message,
  Popconfirm,
  Card,
} from 'ant-design-vue';
import {
  PlusOutlined,
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  DeleteOutlined,
  SyncOutlined,
} from '@ant-design/icons-vue';
import {
  getDomains,
  createDomain,
  updateDomain,
  deleteDomain,
  reloadDomains,
  type Domain,
  type CreateDomainDto,
} from '#/api/kamailio';

// 状态
const loading = ref(false);
const data = ref<Domain[]>([]);
const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const searchKeyword = ref('');

// 弹窗
const modalVisible = ref(false);
const modalTitle = ref('新增域');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateDomainDto>({
  domain: '',
  did: '',
});

// 表格列
const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '域名', dataIndex: 'domain' },
  { title: 'DID', dataIndex: 'did' },
  { title: '最后修改', dataIndex: 'lastModified' },
  { title: '操作', key: 'action', width: 200 },
];

// 加载数据
async function loadData() {
  loading.value = true;
  try {
    const res = await getDomains({
      page: pagination.current,
      limit: pagination.pageSize,
      keyword: searchKeyword.value,
    });
    if (res.data?.code === 0) {
      data.value = res.data.data.items;
      pagination.total = res.data.data.total;
    }
  } catch (error) {
    message.error('加载数据失败');
  } finally {
    loading.value = false;
  }
}

// 搜索
function handleSearch() {
  pagination.current = 1;
  loadData();
}

// 表格变化
function handleTableChange(pag: any) {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  loadData();
}

// 新增
function handleAdd() {
  modalTitle.value = '新增域';
  editingId.value = null;
  Object.assign(formState, { domain: '', did: '' });
  modalVisible.value = true;
}

// 编辑
function handleEdit(record: Domain) {
  modalTitle.value = '编辑域';
  editingId.value = record.id;
  Object.assign(formState, {
    domain: record.domain,
    did: record.did || '',
  });
  modalVisible.value = true;
}

// 删除
async function handleDelete(id: number) {
  try {
    const res = await deleteDomain(id);
    if (res.data?.code === 0) {
      message.success('删除成功');
      loadData();
    } else {
      message.error(res.data?.message || '删除失败');
    }
  } catch (error) {
    message.error('删除失败');
  }
}

// 重载配置
async function handleReload() {
  try {
    const res = await reloadDomains();
    if (res.data?.code === 0) {
      message.success('重载成功');
    } else {
      message.error(res.data?.message || '重载失败');
    }
  } catch (error) {
    message.error('重载失败');
  }
}

// 提交
async function handleSubmit() {
  try {
    await formRef.value.validate();
    
    if (editingId.value) {
      const res = await updateDomain(editingId.value, { did: formState.did });
      if (res.data?.code === 0) {
        message.success('更新成功');
        modalVisible.value = false;
        loadData();
      } else {
        message.error(res.data?.message || '更新失败');
      }
    } else {
      const res = await createDomain(formState);
      if (res.data?.code === 0) {
        message.success('创建成功');
        modalVisible.value = false;
        loadData();
      } else {
        message.error(res.data?.message || '创建失败');
      }
    }
  } catch (error) {
    console.error(error);
  }
}

onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="p-4">
    <!-- 搜索和操作栏 -->
    <Card class="mb-4">
      <Space>
        <Input
          v-model:value="searchKeyword"
          placeholder="搜索域名"
          style="width: 200px"
          @pressEnter="handleSearch"
        >
          <template #prefix>
            <SearchOutlined />
          </template>
        </Input>
        <Button type="primary" @click="handleSearch">
          <SearchOutlined /> 搜索
        </Button>
        <Button @click="loadData">
          <ReloadOutlined /> 刷新
        </Button>
        <Button type="primary" @click="handleAdd">
          <PlusOutlined /> 新增域
        </Button>
        <Button @click="handleReload">
          <SyncOutlined /> 重载配置
        </Button>
      </Space>
    </Card>

    <!-- 数据表格 -->
    <Card>
      <Table
        :columns="columns"
        :dataSource="data"
        :loading="loading"
        :pagination="pagination"
        rowKey="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <Space>
              <Button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined /> 编辑
              </Button>
              <Popconfirm
                title="确定删除此域？"
                @confirm="handleDelete(record.id)"
              >
                <Button type="link" size="small" danger>
                  <DeleteOutlined /> 删除
                </Button>
              </Popconfirm>
            </Space>
          </template>
        </template>
      </Table>
    </Card>

    <!-- 编辑弹窗 -->
    <Modal
      v-model:open="modalVisible"
      :title="modalTitle"
      @ok="handleSubmit"
    >
      <Form
        ref="formRef"
        :model="formState"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 16 }"
      >
        <FormItem
          label="域名"
          name="domain"
          :rules="[{ required: !editingId, message: '请输入域名' }]"
        >
          <Input v-model:value="formState.domain" :disabled="!!editingId" />
        </FormItem>
        <FormItem label="DID" name="did">
          <Input v-model:value="formState.did" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
