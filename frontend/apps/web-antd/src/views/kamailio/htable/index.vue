<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Input, Form, FormItem, Modal, message, Descriptions, DescriptionsItem } from 'ant-design-vue';
import { ReloadOutlined, SearchOutlined, SyncOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getHtableRecords, getHtableValue, setHtableString, deleteHtableKey, dumpHtable, reloadHtable } from '#/api/kamailio';

const loading = ref(false);
const data = ref<any[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const keyword = ref('');

// RPC 操作
const rpcModalVisible = ref(false);
const rpcAction = ref('get');
const rpcForm = reactive({ table: '', key: '', value: '' });
const rpcResult = ref<any>(null);

// Dump 结果
const dumpModalVisible = ref(false);
const dumpData = ref<any>(null);

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '键名', dataIndex: 'keyName' },
  { title: '键类型', dataIndex: 'keyType', width: 80 },
  { title: '键值', dataIndex: 'keyValue' },
  { title: '值类型', dataIndex: 'valueType', width: 80 },
  { title: '过期时间', dataIndex: 'expires', width: 100 },
];

async function loadData() {
  loading.value = true;
  try {
    const res = await getHtableRecords({ page: pagination.current, limit: pagination.pageSize, keyword: keyword.value });
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

function showRpcModal(action: string) {
  rpcAction.value = action;
  rpcResult.value = null;
  rpcModalVisible.value = true;
}

async function handleRpcSubmit() {
  try {
    switch (rpcAction.value) {
      case 'get':
        const getRes = await getHtableValue(rpcForm.table, rpcForm.key);
        rpcResult.value = getRes.data?.data;
        break;
      case 'set':
        await setHtableString(rpcForm.table, rpcForm.key, rpcForm.value);
        message.success('设置成功');
        rpcModalVisible.value = false;
        break;
      case 'delete':
        await deleteHtableKey(rpcForm.table, rpcForm.key);
        message.success('删除成功');
        rpcModalVisible.value = false;
        break;
      case 'reload':
        await reloadHtable(rpcForm.table);
        message.success('重载成功');
        rpcModalVisible.value = false;
        break;
    }
  } catch (e: any) {
    message.error(e.message || '操作失败');
  }
}

async function handleDump() {
  if (!rpcForm.table) {
    message.warning('请输入表名');
    return;
  }
  try {
    const res = await dumpHtable(rpcForm.table);
    dumpData.value = res.data?.data;
    dumpModalVisible.value = true;
  } catch (e: any) {
    message.error(e.message || '导出失败');
  }
}

onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="p-4">
    <Card title="哈希表管理">
      <template #extra>
        <Space>
          <Input v-model:value="keyword" placeholder="搜索键名" style="width: 200px" @pressEnter="handleSearch">
            <template #suffix><SearchOutlined @click="handleSearch" /></template>
          </Input>
          <Button @click="loadData"><ReloadOutlined /> 刷新</Button>
        </Space>
      </template>

      <Space class="mb-4">
        <Button @click="showRpcModal('get')"><SearchOutlined /> 查询值</Button>
        <Button type="primary" @click="showRpcModal('set')"><PlusOutlined /> 设置值</Button>
        <Button danger @click="showRpcModal('delete')"><DeleteOutlined /> 删除键</Button>
        <Button @click="showRpcModal('reload')"><SyncOutlined /> 重载表</Button>
      </Space>

      <Table :columns="columns" :dataSource="data" :loading="loading" :pagination="pagination" rowKey="id" @change="(p) => { pagination.current = p.current; loadData(); }" />
    </Card>

    <!-- RPC 操作弹窗 -->
    <Modal v-model:open="rpcModalVisible" :title="{ get: '查询值', set: '设置值', delete: '删除键', reload: '重载表' }[rpcAction]" @ok="handleRpcSubmit">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="表名"><Input v-model:value="rpcForm.table" placeholder="htable 表名" /></FormItem>
        <FormItem v-if="['get', 'set', 'delete'].includes(rpcAction)" label="键名"><Input v-model:value="rpcForm.key" /></FormItem>
        <FormItem v-if="rpcAction === 'set'" label="值"><Input v-model:value="rpcForm.value" /></FormItem>
      </Form>
      <div v-if="rpcAction === 'get' && rpcResult !== null" class="mt-4">
        <Descriptions bordered :column="1">
          <DescriptionsItem label="结果">{{ JSON.stringify(rpcResult) }}</DescriptionsItem>
        </Descriptions>
      </div>
      <div class="mt-4" v-if="['get', 'set', 'delete'].includes(rpcAction)">
        <Button block @click="handleDump">导出整个表</Button>
      </div>
    </Modal>

    <!-- Dump 结果弹窗 -->
    <Modal v-model:open="dumpModalVisible" title="表内容" width="800px" :footer="null">
      <pre style="max-height: 400px; overflow: auto;">{{ JSON.stringify(dumpData, null, 2) }}</pre>
    </Modal>
  </div>
</template>
