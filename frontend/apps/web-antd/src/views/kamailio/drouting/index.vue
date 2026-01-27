<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Input, Modal, Form, FormItem, InputNumber, message, Popconfirm, Card, Tabs, TabPane, Tag, Row, Col, Statistic } from 'ant-design-vue';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined, SyncOutlined } from '@ant-design/icons-vue';
import { getDrGateways, createDrGateway, updateDrGateway, deleteDrGateway, getDrRules, createDrRule, updateDrRule, deleteDrRule, reloadDrouting, getDrStats } from '#/api/kamailio';

const activeTab = ref('gateways');
const stats = ref({ gateways: 0, rules: 0, groups: 0, carriers: 0 });

// Gateways
const gwLoading = ref(false);
const gwData = ref<any[]>([]);
const gwPagination = reactive({ current: 1, pageSize: 20, total: 0 });
const gwModalVisible = ref(false);
const gwEditingId = ref<number | null>(null);
const gwFormRef = ref();
const gwFormState = reactive({ gwid: '', type: 0, address: '', strip: 0, priPrefix: '', attrs: '', probeMode: 0, state: 0, description: '' });

const gwColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '网关 ID', dataIndex: 'gwid' },
  { title: '地址', dataIndex: 'address' },
  { title: '类型', dataIndex: 'type', width: 60 },
  { title: '状态', dataIndex: 'state', width: 80 },
  { title: '描述', dataIndex: 'description' },
  { title: '操作', key: 'action', width: 150 },
];

async function loadGateways() {
  gwLoading.value = true;
  try {
    const res = await getDrGateways({ page: gwPagination.current, limit: gwPagination.pageSize });
    if (res.data?.code === 0) {
      gwData.value = res.data.data.items;
      gwPagination.total = res.data.data.total;
    }
  } finally { gwLoading.value = false; }
}

function handleGwAdd() {
  gwEditingId.value = null;
  Object.assign(gwFormState, { gwid: '', type: 0, address: '', strip: 0, priPrefix: '', attrs: '', probeMode: 0, state: 0, description: '' });
  gwModalVisible.value = true;
}

function handleGwEdit(record: any) {
  gwEditingId.value = record.id;
  Object.assign(gwFormState, record);
  gwModalVisible.value = true;
}

async function handleGwDelete(id: number) {
  await deleteDrGateway(id);
  message.success('删除成功');
  loadGateways();
  loadStats();
}

async function handleGwSubmit() {
  await gwFormRef.value.validate();
  if (gwEditingId.value) {
    await updateDrGateway(gwEditingId.value, gwFormState);
    message.success('更新成功');
  } else {
    await createDrGateway(gwFormState);
    message.success('创建成功');
  }
  gwModalVisible.value = false;
  loadGateways();
  loadStats();
}

// Rules
const ruleLoading = ref(false);
const ruleData = ref<any[]>([]);
const rulePagination = reactive({ current: 1, pageSize: 20, total: 0 });
const ruleModalVisible = ref(false);
const ruleEditingId = ref<number | null>(null);
const ruleFormRef = ref();
const ruleFormState = reactive({ groupid: '', prefix: '', timerec: '', priority: 0, routeid: '', gwlist: '', attrs: '', description: '' });

const ruleColumns = [
  { title: 'ID', dataIndex: 'ruleid', width: 60 },
  { title: '分组', dataIndex: 'groupid', width: 80 },
  { title: '前缀', dataIndex: 'prefix' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '网关列表', dataIndex: 'gwlist' },
  { title: '描述', dataIndex: 'description' },
  { title: '操作', key: 'action', width: 150 },
];

async function loadRules() {
  ruleLoading.value = true;
  try {
    const res = await getDrRules({ page: rulePagination.current, limit: rulePagination.pageSize });
    if (res.data?.code === 0) {
      ruleData.value = res.data.data.items;
      rulePagination.total = res.data.data.total;
    }
  } finally { ruleLoading.value = false; }
}

function handleRuleAdd() {
  ruleEditingId.value = null;
  Object.assign(ruleFormState, { groupid: '', prefix: '', timerec: '', priority: 0, routeid: '', gwlist: '', attrs: '', description: '' });
  ruleModalVisible.value = true;
}

function handleRuleEdit(record: any) {
  ruleEditingId.value = record.ruleid;
  Object.assign(ruleFormState, record);
  ruleModalVisible.value = true;
}

async function handleRuleDelete(id: number) {
  await deleteDrRule(id);
  message.success('删除成功');
  loadRules();
  loadStats();
}

async function handleRuleSubmit() {
  await ruleFormRef.value.validate();
  if (ruleEditingId.value) {
    await updateDrRule(ruleEditingId.value, ruleFormState);
    message.success('更新成功');
  } else {
    await createDrRule(ruleFormState);
    message.success('创建成功');
  }
  ruleModalVisible.value = false;
  loadRules();
  loadStats();
}

async function handleReload() {
  await reloadDrouting();
  message.success('重载成功');
}

async function loadStats() {
  try {
    const res = await getDrStats();
    if (res.data?.code === 0) stats.value = res.data.data;
  } catch {}
}

onMounted(() => {
  loadGateways();
  loadRules();
  loadStats();
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="6"><Card><Statistic title="网关数" :value="stats.gateways" /></Card></Col>
      <Col :span="6"><Card><Statistic title="规则数" :value="stats.rules" /></Card></Col>
      <Col :span="6"><Card><Statistic title="分组数" :value="stats.groups" /></Card></Col>
      <Col :span="6"><Card><Statistic title="运营商数" :value="stats.carriers" /></Card></Col>
    </Row>

    <Card>
      <Tabs v-model:activeKey="activeTab">
        <TabPane key="gateways" tab="网关">
          <Space class="mb-4">
            <Button type="primary" @click="handleGwAdd"><PlusOutlined /> 新增</Button>
            <Button @click="loadGateways"><ReloadOutlined /> 刷新</Button>
            <Button @click="handleReload"><SyncOutlined /> 重载配置</Button>
          </Space>
          <Table :columns="gwColumns" :dataSource="gwData" :loading="gwLoading" :pagination="gwPagination" rowKey="id" @change="(p) => { gwPagination.current = p.current; loadGateways(); }">
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'state'">
                <Tag :color="record.state === 0 ? 'green' : 'red'">{{ record.state === 0 ? '启用' : '禁用' }}</Tag>
              </template>
              <template v-else-if="column.key === 'action'">
                <Space>
                  <Button type="link" size="small" @click="handleGwEdit(record)"><EditOutlined /></Button>
                  <Popconfirm title="确定删除？" @confirm="handleGwDelete(record.id)">
                    <Button type="link" size="small" danger><DeleteOutlined /></Button>
                  </Popconfirm>
                </Space>
              </template>
            </template>
          </Table>
        </TabPane>

        <TabPane key="rules" tab="规则">
          <Space class="mb-4">
            <Button type="primary" @click="handleRuleAdd"><PlusOutlined /> 新增</Button>
            <Button @click="loadRules"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="ruleColumns" :dataSource="ruleData" :loading="ruleLoading" :pagination="rulePagination" rowKey="ruleid" @change="(p) => { rulePagination.current = p.current; loadRules(); }">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Space>
                  <Button type="link" size="small" @click="handleRuleEdit(record)"><EditOutlined /></Button>
                  <Popconfirm title="确定删除？" @confirm="handleRuleDelete(record.ruleid)">
                    <Button type="link" size="small" danger><DeleteOutlined /></Button>
                  </Popconfirm>
                </Space>
              </template>
            </template>
          </Table>
        </TabPane>
      </Tabs>
    </Card>

    <!-- 网关弹窗 -->
    <Modal v-model:open="gwModalVisible" :title="gwEditingId ? '编辑网关' : '新增网关'" @ok="handleGwSubmit">
      <Form ref="gwFormRef" :model="gwFormState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="网关 ID" name="gwid" :rules="[{ required: true }]"><Input v-model:value="gwFormState.gwid" /></FormItem>
        <FormItem label="地址" name="address" :rules="[{ required: true }]"><Input v-model:value="gwFormState.address" placeholder="sip:ip:port" /></FormItem>
        <FormItem label="类型" name="type"><InputNumber v-model:value="gwFormState.type" :min="0" style="width: 100%" /></FormItem>
        <FormItem label="状态" name="state"><InputNumber v-model:value="gwFormState.state" :min="0" style="width: 100%" /></FormItem>
        <FormItem label="描述" name="description"><Input v-model:value="gwFormState.description" /></FormItem>
      </Form>
    </Modal>

    <!-- 规则弹窗 -->
    <Modal v-model:open="ruleModalVisible" :title="ruleEditingId ? '编辑规则' : '新增规则'" @ok="handleRuleSubmit">
      <Form ref="ruleFormRef" :model="ruleFormState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="分组 ID" name="groupid" :rules="[{ required: true }]"><Input v-model:value="ruleFormState.groupid" /></FormItem>
        <FormItem label="前缀" name="prefix" :rules="[{ required: true }]"><Input v-model:value="ruleFormState.prefix" /></FormItem>
        <FormItem label="网关列表" name="gwlist" :rules="[{ required: true }]"><Input v-model:value="ruleFormState.gwlist" placeholder="gw1,gw2" /></FormItem>
        <FormItem label="优先级" name="priority"><InputNumber v-model:value="ruleFormState.priority" style="width: 100%" /></FormItem>
        <FormItem label="描述" name="description"><Input v-model:value="ruleFormState.description" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
