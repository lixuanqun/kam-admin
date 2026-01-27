<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {
  Table,
  Button,
  Space,
  Input,
  message,
  Popconfirm,
  Card,
  Tag,
  Row,
  Col,
  Statistic,
} from 'ant-design-vue';
import {
  SearchOutlined,
  ReloadOutlined,
  DeleteOutlined,
  UserOutlined,
} from '@ant-design/icons-vue';
import {
  getLocations,
  getLocationStats,
  getOnlineCount,
  deleteUserLocation,
  type Location,
} from '#/api/kamailio';

// 状态
const loading = ref(false);
const data = ref<Location[]>([]);
const pagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const searchKeyword = ref('');
const onlineCount = ref(0);
const stats = ref({
  total: 0,
  byDomain: [] as { domain: string; count: number }[],
  byUserAgent: [] as { userAgent: string; count: number }[],
});

// 表格列
const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '联系地址', dataIndex: 'contact', ellipsis: true },
  { title: 'User-Agent', dataIndex: 'userAgent', ellipsis: true },
  { title: '过期时间', dataIndex: 'expires' },
  { title: '操作', key: 'action', width: 100 },
];

// 加载数据
async function loadData() {
  loading.value = true;
  try {
    const res = await getLocations({
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
    const [countRes, statsRes] = await Promise.all([
      getOnlineCount(),
      getLocationStats(),
    ]);
    if (countRes.data?.code === 0) {
      onlineCount.value = countRes.data.data;
    }
    if (statsRes.data?.code === 0) {
      stats.value = statsRes.data.data;
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

// 强制下线
async function handleDelete(record: Location) {
  try {
    const res = await deleteUserLocation(record.username, record.domain);
    if (res.data?.code === 0) {
      message.success('已强制下线');
      loadData();
      loadStats();
    } else {
      message.error(res.data?.message || '操作失败');
    }
  } catch (error) {
    message.error('操作失败');
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
          <Statistic title="在线用户" :value="onlineCount">
            <template #prefix>
              <UserOutlined />
            </template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="6">
        <Card>
          <Statistic title="域数量" :value="stats.byDomain.length" />
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
        <Button @click="() => { loadData(); loadStats(); }">
          <ReloadOutlined /> 刷新
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
          <template v-if="column.dataIndex === 'username'">
            <Tag color="green">{{ record.username }}</Tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <Popconfirm
              title="确定强制下线此用户？"
              @confirm="handleDelete(record)"
            >
              <Button type="link" size="small" danger>
                <DeleteOutlined /> 下线
              </Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>
  </div>
</template>
