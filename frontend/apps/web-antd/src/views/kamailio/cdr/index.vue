<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {
  Table,
  Button,
  Space,
  Input,
  DatePicker,
  Card,
  Tabs,
  TabPane,
  Tag,
  Row,
  Col,
  Statistic,
  message,
} from 'ant-design-vue';
import {
  SearchOutlined,
  ReloadOutlined,
  PhoneOutlined,
  PhoneMissed,
} from '@ant-design/icons-vue';
import {
  getCdrRecords,
  getMissedCalls,
  getAccStats,
  getTodayStats,
  type AccRecord,
} from '#/api/kamailio';
import dayjs from 'dayjs';

const { RangePicker } = DatePicker;

// 当前标签
const activeTab = ref('cdr');

// 统计数据
const todayStats = ref({
  totalCalls: 0,
  missedCalls: 0,
  successRate: 0,
});

// ========== CDR 记录 ==========
const cdrLoading = ref(false);
const cdrData = ref<AccRecord[]>([]);
const cdrPagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const cdrFilters = reactive({
  srcUser: '',
  dstUser: '',
  callid: '',
  dateRange: [] as any[],
  sipCode: '',
});

const cdrColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '时间', dataIndex: 'time', width: 180 },
  { title: '方法', dataIndex: 'method', width: 80 },
  { title: '来源用户', dataIndex: 'srcUser' },
  { title: '目标用户', dataIndex: 'dstUser' },
  { title: '响应码', dataIndex: 'sipCode', width: 100 },
  { title: '原因', dataIndex: 'sipReason' },
  { title: 'Call ID', dataIndex: 'callid', ellipsis: true },
];

async function loadCdrData() {
  cdrLoading.value = true;
  try {
    const params: any = {
      page: cdrPagination.current,
      limit: cdrPagination.pageSize,
    };
    if (cdrFilters.srcUser) params.srcUser = cdrFilters.srcUser;
    if (cdrFilters.dstUser) params.dstUser = cdrFilters.dstUser;
    if (cdrFilters.callid) params.callid = cdrFilters.callid;
    if (cdrFilters.sipCode) params.sipCode = cdrFilters.sipCode;
    if (cdrFilters.dateRange?.length === 2) {
      params.startTime = dayjs(cdrFilters.dateRange[0]).format('YYYY-MM-DD HH:mm:ss');
      params.endTime = dayjs(cdrFilters.dateRange[1]).format('YYYY-MM-DD HH:mm:ss');
    }

    const res = await getCdrRecords(params);
    if (res.data?.code === 0) {
      cdrData.value = res.data.data.items;
      cdrPagination.total = res.data.data.total;
    }
  } catch (error) {
    message.error('加载数据失败');
  } finally {
    cdrLoading.value = false;
  }
}

// ========== 未接来电 ==========
const missedLoading = ref(false);
const missedData = ref<AccRecord[]>([]);
const missedPagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const missedFilters = reactive({
  srcUser: '',
  dstUser: '',
  dateRange: [] as any[],
});

const missedColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '时间', dataIndex: 'time', width: 180 },
  { title: '方法', dataIndex: 'method', width: 80 },
  { title: '来源用户', dataIndex: 'srcUser' },
  { title: '目标用户', dataIndex: 'dstUser' },
  { title: '响应码', dataIndex: 'sipCode', width: 100 },
  { title: '原因', dataIndex: 'sipReason' },
];

async function loadMissedData() {
  missedLoading.value = true;
  try {
    const params: any = {
      page: missedPagination.current,
      limit: missedPagination.pageSize,
    };
    if (missedFilters.srcUser) params.srcUser = missedFilters.srcUser;
    if (missedFilters.dstUser) params.dstUser = missedFilters.dstUser;
    if (missedFilters.dateRange?.length === 2) {
      params.startTime = dayjs(missedFilters.dateRange[0]).format('YYYY-MM-DD HH:mm:ss');
      params.endTime = dayjs(missedFilters.dateRange[1]).format('YYYY-MM-DD HH:mm:ss');
    }

    const res = await getMissedCalls(params);
    if (res.data?.code === 0) {
      missedData.value = res.data.data.items;
      missedPagination.total = res.data.data.total;
    }
  } catch (error) {
    message.error('加载数据失败');
  } finally {
    missedLoading.value = false;
  }
}

// 加载今日统计
async function loadTodayStats() {
  try {
    const res = await getTodayStats();
    if (res.data?.code === 0) {
      todayStats.value = res.data.data;
    }
  } catch (error) {
    console.error(error);
  }
}

// 获取响应码颜色
function getSipCodeColor(code: string) {
  if (code.startsWith('2')) return 'green';
  if (code.startsWith('3')) return 'blue';
  if (code.startsWith('4')) return 'orange';
  if (code.startsWith('5')) return 'red';
  return 'default';
}

onMounted(() => {
  loadCdrData();
  loadMissedData();
  loadTodayStats();
});
</script>

<template>
  <div class="p-4">
    <!-- 统计卡片 -->
    <Row :gutter="16" class="mb-4">
      <Col :span="6">
        <Card>
          <Statistic title="今日通话" :value="todayStats.totalCalls">
            <template #prefix>
              <PhoneOutlined />
            </template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="6">
        <Card>
          <Statistic title="未接来电" :value="todayStats.missedCalls">
            <template #prefix>
              <PhoneMissed />
            </template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="6">
        <Card>
          <Statistic title="成功率" :value="todayStats.successRate" suffix="%" :precision="2" />
        </Card>
      </Col>
    </Row>

    <Card>
      <Tabs v-model:activeKey="activeTab">
        <!-- CDR 记录 -->
        <TabPane key="cdr" tab="CDR 记录">
          <Space class="mb-4" wrap>
            <Input
              v-model:value="cdrFilters.srcUser"
              placeholder="来源用户"
              style="width: 150px"
            />
            <Input
              v-model:value="cdrFilters.dstUser"
              placeholder="目标用户"
              style="width: 150px"
            />
            <Input
              v-model:value="cdrFilters.callid"
              placeholder="Call ID"
              style="width: 200px"
            />
            <Input
              v-model:value="cdrFilters.sipCode"
              placeholder="响应码"
              style="width: 100px"
            />
            <RangePicker
              v-model:value="cdrFilters.dateRange"
              showTime
              format="YYYY-MM-DD HH:mm:ss"
            />
            <Button type="primary" @click="loadCdrData">
              <SearchOutlined /> 搜索
            </Button>
            <Button @click="loadCdrData">
              <ReloadOutlined /> 刷新
            </Button>
          </Space>

          <Table
            :columns="cdrColumns"
            :dataSource="cdrData"
            :loading="cdrLoading"
            :pagination="cdrPagination"
            rowKey="id"
            @change="(pag) => { cdrPagination.current = pag.current; loadCdrData(); }"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'sipCode'">
                <Tag :color="getSipCodeColor(record.sipCode)">{{ record.sipCode }}</Tag>
              </template>
            </template>
          </Table>
        </TabPane>

        <!-- 未接来电 -->
        <TabPane key="missed" tab="未接来电">
          <Space class="mb-4" wrap>
            <Input
              v-model:value="missedFilters.srcUser"
              placeholder="来源用户"
              style="width: 150px"
            />
            <Input
              v-model:value="missedFilters.dstUser"
              placeholder="目标用户"
              style="width: 150px"
            />
            <RangePicker
              v-model:value="missedFilters.dateRange"
              showTime
              format="YYYY-MM-DD HH:mm:ss"
            />
            <Button type="primary" @click="loadMissedData">
              <SearchOutlined /> 搜索
            </Button>
            <Button @click="loadMissedData">
              <ReloadOutlined /> 刷新
            </Button>
          </Space>

          <Table
            :columns="missedColumns"
            :dataSource="missedData"
            :loading="missedLoading"
            :pagination="missedPagination"
            rowKey="id"
            @change="(pag) => { missedPagination.current = pag.current; loadMissedData(); }"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'sipCode'">
                <Tag :color="getSipCodeColor(record.sipCode)">{{ record.sipCode }}</Tag>
              </template>
            </template>
          </Table>
        </TabPane>
      </Tabs>
    </Card>
  </div>
</template>
