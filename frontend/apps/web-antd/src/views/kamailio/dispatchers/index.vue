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
  InputNumber,
  message,
  Popconfirm,
  Card,
  Tag,
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
  SyncOutlined,
} from '@ant-design/icons-vue';
import {
  getDispatchers,
  createDispatcher,
  updateDispatcher,
  deleteDispatcher,
  reloadDispatchers,
  getDispatcherStats,
  type Dispatcher,
  type CreateDispatcherDto,
} from '#/api/kamailio';

// 状态
const loading = ref(false);
const data = ref<Dispatcher[]>([]);
const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const searchKeyword = ref('');
const stats = ref({ total: 0, groups: [] as { setid: number; count: number }[] });

// 弹窗
const modalVisible = ref(false);
const modalTitle = ref('新增调度目标');
const editingId = ref<number | null>(null);
const formRef = ref();
const formState = reactive<CreateDispatcherDto>({
  setid: 1,
  destination: '',
  flags: 0,
  priority: 0,
  attrs: '',
  description: '',
});

// 表格列
const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '调度组', dataIndex: 'setid', width: 100 },
  { title: '目标地址', dataIndex: 'destination' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '标志', dataIndex: 'flags', width: 80 },
  { title: '描述', dataIndex: 'description' },
  { title: '操作', key: 'action', width: 150 },
];

// 加载数据
async function loadData() {
  loading.value = true;
  try {
    const res = await getDispatchers({
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
    const res = await getDispatcherStats();
    if (res.data?.code === 0) {
      stats.value = res.data.data;
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
  modalTitle.value = '新增调度目标';
  editingId.value = null;
  Object.assign(formState, {
    setid: 1,
    destination: '',
    flags: 0,
    priority: 0,
    attrs: '',
    description: '',
  });
  modalVisible.value = true;
}

// 编辑
function handleEdit(record: Dispatcher) {
  modalTitle.value = '编辑调度目标';
  editingId.value = record.id;
  Object.assign(formState, {
    setid: record.setid,
    destination: record.destination,
    flags: record.flags,
    priority: record.priority,
    attrs: record.attrs,
    description: record.description,
  });
  modalVisible.value = true;
}

// 删除
async function handleDelete(id: number) {
  try {
    const res = await deleteDispatcher(id);
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

// 重载配置
async function handleReload() {
  try {
    const res = await reloadDispatchers();
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
      const res = await updateDispatcher(editingId.value, formState);
      if (res.data?.code === 0) {
        message.success('更新成功');
        modalVisible.value = false;
        loadData();
      } else {
        message.error(res.data?.message || '更新失败');
      }
    } else {
      const res = await createDispatcher(formState);
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
});
</script>

<template>
  <div class="p-4">
    <!-- 统计卡片 -->
    <Row :gutter="16" class="mb-4">
      <Col :span="6">
        <Card>
          <Statistic title="调度目标总数" :value="stats.total" />
        </Card>
      </Col>
      <Col :span="6">
        <Card>
          <Statistic title="调度组数量" :value="stats.groups.length" />
        </Card>
      </Col>
    </Row>

    <!-- 搜索和操作栏 -->
    <Card class="mb-4">
      <Space>
        <Input
          v-model:value="searchKeyword"
          placeholder="搜索目标地址"
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
          <PlusOutlined /> 新增调度目标
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
          <template v-if="column.dataIndex === 'setid'">
            <Tag color="blue">组 {{ record.setid }}</Tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <Space>
              <Button type="link" size="small" @click="handleEdit(record)">
                <EditOutlined /> 编辑
              </Button>
              <Popconfirm
                title="确定删除此调度目标？"
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
          label="调度组 ID"
          name="setid"
          :rules="[{ required: true, message: '请输入调度组 ID' }]"
        >
          <InputNumber v-model:value="formState.setid" :min="0" style="width: 100%" />
        </FormItem>
        <FormItem
          label="目标地址"
          name="destination"
          :rules="[{ required: true, message: '请输入目标地址' }]"
        >
          <Input v-model:value="formState.destination" placeholder="sip:192.168.1.100:5060" />
        </FormItem>
        <FormItem label="优先级" name="priority">
          <InputNumber v-model:value="formState.priority" style="width: 100%" />
        </FormItem>
        <FormItem label="标志" name="flags">
          <InputNumber v-model:value="formState.flags" :min="0" style="width: 100%" />
        </FormItem>
        <FormItem label="属性" name="attrs">
          <Input v-model:value="formState.attrs" />
        </FormItem>
        <FormItem label="描述" name="description">
          <Input v-model:value="formState.description" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
