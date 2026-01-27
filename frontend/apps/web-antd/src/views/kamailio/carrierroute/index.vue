<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Tabs, TabPane, Input, Modal, Form, FormItem, Popconfirm, message } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SyncOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getCarrierNames, createCarrierName, deleteCarrierName, getCarrierDomains, createCarrierDomain, deleteCarrierDomain, getCarrierFailureRoutes, reloadCarrierroute, dumpCarrierRoutes } from '#/api/kamailio';

const activeTab = ref('carriers');

// Carriers
const carriers = ref<any[]>([]);
const carrierModalVisible = ref(false);
const newCarrier = ref('');

const carrierColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '运营商名称', dataIndex: 'carrier' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadCarriers() {
  const res = await getCarrierNames();
  if (res.data?.code === 0) carriers.value = res.data.data;
}

async function handleAddCarrier() {
  if (!newCarrier.value) return;
  await createCarrierName(newCarrier.value);
  message.success('创建成功');
  carrierModalVisible.value = false;
  newCarrier.value = '';
  loadCarriers();
}

async function handleDeleteCarrier(id: number) {
  await deleteCarrierName(id);
  message.success('删除成功');
  loadCarriers();
}

// Domains
const domains = ref<any[]>([]);
const domainModalVisible = ref(false);
const newDomain = ref('');

const domainColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '域名', dataIndex: 'domain' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadDomains() {
  const res = await getCarrierDomains();
  if (res.data?.code === 0) domains.value = res.data.data;
}

async function handleAddDomain() {
  if (!newDomain.value) return;
  await createCarrierDomain(newDomain.value);
  message.success('创建成功');
  domainModalVisible.value = false;
  newDomain.value = '';
  loadDomains();
}

async function handleDeleteDomain(id: number) {
  await deleteCarrierDomain(id);
  message.success('删除成功');
  loadDomains();
}

// Failure Routes
const failureRoutes = ref<any[]>([]);
const failurePagination = reactive({ current: 1, pageSize: 20, total: 0 });
const failureLoading = ref(false);

const failureColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '运营商', dataIndex: 'carrier', width: 80 },
  { title: '域', dataIndex: 'domain', width: 80 },
  { title: '扫描前缀', dataIndex: 'scanPrefix' },
  { title: '主机名', dataIndex: 'host_name' },
  { title: '回复码', dataIndex: 'replyCode', width: 80 },
  { title: '下一个域', dataIndex: 'nextDomain' },
  { title: '描述', dataIndex: 'description' },
];

async function loadFailureRoutes() {
  failureLoading.value = true;
  try {
    const res = await getCarrierFailureRoutes({ page: failurePagination.current, limit: failurePagination.pageSize });
    if (res.data?.code === 0) {
      failureRoutes.value = res.data.data.items;
      failurePagination.total = res.data.data.total;
    }
  } finally { failureLoading.value = false; }
}

// Dump
const dumpVisible = ref(false);
const dumpData = ref<any>(null);

async function handleReload() {
  await reloadCarrierroute();
  message.success('重载成功');
}

async function handleDump() {
  const res = await dumpCarrierRoutes();
  dumpData.value = res.data?.data;
  dumpVisible.value = true;
}

onMounted(() => {
  loadCarriers();
  loadDomains();
  loadFailureRoutes();
});
</script>

<template>
  <div class="p-4">
    <Card>
      <template #extra>
        <Space>
          <Button @click="handleReload"><SyncOutlined /> 重载配置</Button>
          <Button @click="handleDump">导出路由</Button>
        </Space>
      </template>

      <Tabs v-model:activeKey="activeTab">
        <TabPane key="carriers" tab="运营商">
          <Space class="mb-4">
            <Button type="primary" @click="carrierModalVisible = true"><PlusOutlined /> 新增</Button>
            <Button @click="loadCarriers"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="carrierColumns" :dataSource="carriers" :pagination="false" rowKey="id">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Popconfirm title="确定删除？" @confirm="handleDeleteCarrier(record.id)">
                  <Button type="link" size="small" danger><DeleteOutlined /></Button>
                </Popconfirm>
              </template>
            </template>
          </Table>
        </TabPane>

        <TabPane key="domains" tab="域">
          <Space class="mb-4">
            <Button type="primary" @click="domainModalVisible = true"><PlusOutlined /> 新增</Button>
            <Button @click="loadDomains"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="domainColumns" :dataSource="domains" :pagination="false" rowKey="id">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Popconfirm title="确定删除？" @confirm="handleDeleteDomain(record.id)">
                  <Button type="link" size="small" danger><DeleteOutlined /></Button>
                </Popconfirm>
              </template>
            </template>
          </Table>
        </TabPane>

        <TabPane key="failureRoutes" tab="失败路由">
          <Space class="mb-4">
            <Button @click="loadFailureRoutes"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="failureColumns" :dataSource="failureRoutes" :loading="failureLoading" :pagination="failurePagination" rowKey="id" />
        </TabPane>
      </Tabs>
    </Card>

    <!-- 新增运营商弹窗 -->
    <Modal v-model:open="carrierModalVisible" title="新增运营商" @ok="handleAddCarrier">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="运营商名称"><Input v-model:value="newCarrier" /></FormItem>
      </Form>
    </Modal>

    <!-- 新增域弹窗 -->
    <Modal v-model:open="domainModalVisible" title="新增域" @ok="handleAddDomain">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="域名"><Input v-model:value="newDomain" /></FormItem>
      </Form>
    </Modal>

    <!-- 导出弹窗 -->
    <Modal v-model:open="dumpVisible" title="路由配置" width="800px" :footer="null">
      <pre style="max-height: 500px; overflow: auto;">{{ JSON.stringify(dumpData, null, 2) }}</pre>
    </Modal>
  </div>
</template>
