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
  Row,
  Col,
  Statistic,
} from 'ant-design-vue';
import {
  PlusOutlined,
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons-vue';
import {
  getSubscribers,
  createSubscriber,
  updateSubscriber,
  deleteSubscriber,
  getSubscriberStats,
  getAllDomains,
  type Subscriber,
  type CreateSubscriberDto,
} from '#/api/kamailio';

// 状态
const loading = ref(false);
const data = ref<Subscriber[]>([]);
const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const searchKeyword = ref('');
const stats = ref({ total: 0, domains: [] as { domain: string; count: number }[] });
const domains = ref<{ domain: string }[]>([]);

// 弹窗
const modalVisible = ref(false);
const modalTitle = ref('新增用户');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateSubscriberDto>({
  username: '',
  domain: '',
  password: '',
  emailAddress: '',
  rpid: '',
});

// 表格列
const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '邮箱', dataIndex: 'emailAddress' },
  { title: 'RP ID', dataIndex: 'rpid' },
  { title: '操作', key: 'action', width: 150 },
];

// 加载数据
async function loadData() {
  loading.value = true;
  try {
    const res = await getSubscribers({
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

// 加载统计
async function loadStats() {
  try {
    const res = await getSubscriberStats();
    if (res.data?.code === 0) {
      stats.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 加载域列表
async function loadDomains() {
  try {
    const res = await getAllDomains();
    if (res.data?.code === 0) {
      domains.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
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
  modalTitle.value = '新增用户';
  editingId.value = null;
  Object.assign(formState, {
    username: '',
    domain: domains.value[0]?.domain || '',
    password: '',
    emailAddress: '',
    rpid: '',
  });
  modalVisible.value = true;
}

// 编辑
function handleEdit(record: Subscriber) {
  modalTitle.value = '编辑用户';
  editingId.value = record.id;
  Object.assign(formState, {
    username: record.username,
    domain: record.domain,
    password: '',
    emailAddress: record.emailAddress || '',
    rpid: record.rpid || '',
  });
  modalVisible.value = true;
}

// 删除
async function handleDelete(id: number) {
  try {
    const res = await deleteSubscriber(id);
    if (res.data?.code === 0) {
      message.success('删除成功');
      loadData();
      loadStats();
    } else {
      message.error(res.data?.message || '删除失败');
    }
  } catch (error) {
    message.error('删除失败');
  }
}

// 提交
async function handleSubmit() {
  try {
    await formRef.value.validate();
    
    if (editingId.value) {
      const res = await updateSubscriber(editingId.value, {
        password: formState.password || undefined,
        emailAddress: formState.emailAddress,
        rpid: formState.rpid,
      });
      if (res.data?.code === 0) {
        message.success('更新成功');
        modalVisible.value = false;
        loadData();
      } else {
        message.error(res.data?.message || '更新失败');
      }
    } else {
      const res = await createSubscriber(formState);
      if (res.data?.code === 0) {
        message.success('创建成功');
        modalVisible.value = false;
        loadData();
        loadStats();
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
  loadStats();
  loadDomains();
});
</script>

<template>
  <div class="p-4">
    <!-- 统计卡片 -->
    <Row :gutter="16" class="mb-4">
      <Col :span="6">
        <Card>
          <Statistic title="用户总数" :value="stats.total" />
        </Card>
      </Col>
      <Col :span="6">
        <Card>
          <Statistic title="域数量" :value="stats.domains.length" />
        </Card>
      </Col>
    </Row>

    <!-- 搜索和操作栏 -->
    <Card class="mb-4">
      <Space>
        <Input
          v-model:value="searchKeyword"
          placeholder="搜索用户名"
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
          <PlusOutlined /> 新增用户
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
                title="确定删除此用户？"
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
          label="用户名"
          name="username"
          :rules="[{ required: !editingId, message: '请输入用户名' }]"
        >
          <Input v-model:value="formState.username" :disabled="!!editingId" />
        </FormItem>
        <FormItem
          label="域"
          name="domain"
          :rules="[{ required: !editingId, message: '请选择域' }]"
        >
          <Input v-model:value="formState.domain" :disabled="!!editingId" />
        </FormItem>
        <FormItem
          label="密码"
          name="password"
          :rules="[{ required: !editingId, message: '请输入密码' }]"
        >
          <Input.Password v-model:value="formState.password" :placeholder="editingId ? '留空则不修改' : ''" />
        </FormItem>
        <FormItem label="邮箱" name="emailAddress">
          <Input v-model:value="formState.emailAddress" />
        </FormItem>
        <FormItem label="RP ID" name="rpid">
          <Input v-model:value="formState.rpid" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
