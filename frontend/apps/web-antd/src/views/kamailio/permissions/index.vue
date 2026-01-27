<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {
  Table,
  Button,
  Space,
  Input,
  InputNumber,
  Modal,
  Form,
  FormItem,
  message,
  Popconfirm,
  Card,
  Tabs,
  TabPane,
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
  getAddresses,
  createAddress,
  updateAddress,
  deleteAddress,
  getTrusted,
  createTrusted,
  updateTrusted,
  deleteTrusted,
  reloadPermissions,
  type Address,
  type Trusted,
} from '#/api/kamailio';

// 当前标签
const activeTab = ref('address');

// ========== 地址白名单 ==========
const addressLoading = ref(false);
const addressData = ref<Address[]>([]);
const addressPagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const addressKeyword = ref('');
const addressModalVisible = ref(false);
const addressEditingId = ref<number | null>(null);
const addressFormRef = ref();
const addressFormState = reactive({
  grp: 1,
  ipAddr: '',
  mask: 32,
  port: 0,
  tag: '',
});

const addressColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '分组', dataIndex: 'grp', width: 80 },
  { title: 'IP 地址', dataIndex: 'ipAddr' },
  { title: '掩码', dataIndex: 'mask', width: 80 },
  { title: '端口', dataIndex: 'port', width: 80 },
  { title: '标签', dataIndex: 'tag' },
  { title: '操作', key: 'action', width: 150 },
];

async function loadAddressData() {
  addressLoading.value = true;
  try {
    const res = await getAddresses({
      page: addressPagination.current,
      limit: addressPagination.pageSize,
      keyword: addressKeyword.value,
    });
    if (res.data?.code === 0) {
      addressData.value = res.data.data.items;
      addressPagination.total = res.data.data.total;
    }
  } catch (error) {
    message.error('加载数据失败');
  } finally {
    addressLoading.value = false;
  }
}

function handleAddressAdd() {
  addressEditingId.value = null;
  Object.assign(addressFormState, { grp: 1, ipAddr: '', mask: 32, port: 0, tag: '' });
  addressModalVisible.value = true;
}

function handleAddressEdit(record: Address) {
  addressEditingId.value = record.id;
  Object.assign(addressFormState, record);
  addressModalVisible.value = true;
}

async function handleAddressDelete(id: number) {
  try {
    const res = await deleteAddress(id);
    if (res.data?.code === 0) {
      message.success('删除成功');
      loadAddressData();
    }
  } catch (error) {
    message.error('删除失败');
  }
}

async function handleAddressSubmit() {
  try {
    await addressFormRef.value.validate();
    if (addressEditingId.value) {
      await updateAddress(addressEditingId.value, addressFormState);
      message.success('更新成功');
    } else {
      await createAddress(addressFormState);
      message.success('创建成功');
    }
    addressModalVisible.value = false;
    loadAddressData();
  } catch (error) {
    console.error(error);
  }
}

// ========== 信任地址 ==========
const trustedLoading = ref(false);
const trustedData = ref<Trusted[]>([]);
const trustedPagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
});
const trustedKeyword = ref('');
const trustedModalVisible = ref(false);
const trustedEditingId = ref<number | null>(null);
const trustedFormRef = ref();
const trustedFormState = reactive({
  srcIp: '',
  proto: 'any',
  fromPattern: '',
  tag: '',
  priority: 0,
});

const trustedColumns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '源 IP', dataIndex: 'srcIp' },
  { title: '协议', dataIndex: 'proto', width: 80 },
  { title: 'From 模式', dataIndex: 'fromPattern' },
  { title: '标签', dataIndex: 'tag' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '操作', key: 'action', width: 150 },
];

async function loadTrustedData() {
  trustedLoading.value = true;
  try {
    const res = await getTrusted({
      page: trustedPagination.current,
      limit: trustedPagination.pageSize,
      keyword: trustedKeyword.value,
    });
    if (res.data?.code === 0) {
      trustedData.value = res.data.data.items;
      trustedPagination.total = res.data.data.total;
    }
  } catch (error) {
    message.error('加载数据失败');
  } finally {
    trustedLoading.value = false;
  }
}

function handleTrustedAdd() {
  trustedEditingId.value = null;
  Object.assign(trustedFormState, { srcIp: '', proto: 'any', fromPattern: '', tag: '', priority: 0 });
  trustedModalVisible.value = true;
}

function handleTrustedEdit(record: Trusted) {
  trustedEditingId.value = record.id;
  Object.assign(trustedFormState, record);
  trustedModalVisible.value = true;
}

async function handleTrustedDelete(id: number) {
  try {
    const res = await deleteTrusted(id);
    if (res.data?.code === 0) {
      message.success('删除成功');
      loadTrustedData();
    }
  } catch (error) {
    message.error('删除失败');
  }
}

async function handleTrustedSubmit() {
  try {
    await trustedFormRef.value.validate();
    if (trustedEditingId.value) {
      await updateTrusted(trustedEditingId.value, trustedFormState);
      message.success('更新成功');
    } else {
      await createTrusted(trustedFormState);
      message.success('创建成功');
    }
    trustedModalVisible.value = false;
    loadTrustedData();
  } catch (error) {
    console.error(error);
  }
}

// 重载配置
async function handleReload() {
  try {
    const res = await reloadPermissions();
    if (res.data?.code === 0) {
      message.success('重载成功');
    }
  } catch (error) {
    message.error('重载失败');
  }
}

onMounted(() => {
  loadAddressData();
  loadTrustedData();
});
</script>

<template>
  <div class="p-4">
    <Card>
      <Tabs v-model:activeKey="activeTab">
        <!-- 地址白名单 -->
        <TabPane key="address" tab="地址白名单">
          <Space class="mb-4">
            <Input
              v-model:value="addressKeyword"
              placeholder="搜索 IP 地址"
              style="width: 200px"
              @pressEnter="loadAddressData"
            >
              <template #prefix>
                <SearchOutlined />
              </template>
            </Input>
            <Button type="primary" @click="loadAddressData">
              <SearchOutlined /> 搜索
            </Button>
            <Button @click="loadAddressData">
              <ReloadOutlined /> 刷新
            </Button>
            <Button type="primary" @click="handleAddressAdd">
              <PlusOutlined /> 新增
            </Button>
            <Button @click="handleReload">
              <SyncOutlined /> 重载配置
            </Button>
          </Space>

          <Table
            :columns="addressColumns"
            :dataSource="addressData"
            :loading="addressLoading"
            :pagination="addressPagination"
            rowKey="id"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Space>
                  <Button type="link" size="small" @click="handleAddressEdit(record)">
                    <EditOutlined /> 编辑
                  </Button>
                  <Popconfirm title="确定删除？" @confirm="handleAddressDelete(record.id)">
                    <Button type="link" size="small" danger>
                      <DeleteOutlined /> 删除
                    </Button>
                  </Popconfirm>
                </Space>
              </template>
            </template>
          </Table>
        </TabPane>

        <!-- 信任地址 -->
        <TabPane key="trusted" tab="信任地址">
          <Space class="mb-4">
            <Input
              v-model:value="trustedKeyword"
              placeholder="搜索源 IP"
              style="width: 200px"
              @pressEnter="loadTrustedData"
            >
              <template #prefix>
                <SearchOutlined />
              </template>
            </Input>
            <Button type="primary" @click="loadTrustedData">
              <SearchOutlined /> 搜索
            </Button>
            <Button @click="loadTrustedData">
              <ReloadOutlined /> 刷新
            </Button>
            <Button type="primary" @click="handleTrustedAdd">
              <PlusOutlined /> 新增
            </Button>
          </Space>

          <Table
            :columns="trustedColumns"
            :dataSource="trustedData"
            :loading="trustedLoading"
            :pagination="trustedPagination"
            rowKey="id"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Space>
                  <Button type="link" size="small" @click="handleTrustedEdit(record)">
                    <EditOutlined /> 编辑
                  </Button>
                  <Popconfirm title="确定删除？" @confirm="handleTrustedDelete(record.id)">
                    <Button type="link" size="small" danger>
                      <DeleteOutlined /> 删除
                    </Button>
                  </Popconfirm>
                </Space>
              </template>
            </template>
          </Table>
        </TabPane>
      </Tabs>
    </Card>

    <!-- 地址弹窗 -->
    <Modal v-model:open="addressModalVisible" :title="addressEditingId ? '编辑地址' : '新增地址'" @ok="handleAddressSubmit">
      <Form ref="addressFormRef" :model="addressFormState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="分组" name="grp">
          <InputNumber v-model:value="addressFormState.grp" :min="0" style="width: 100%" />
        </FormItem>
        <FormItem label="IP 地址" name="ipAddr" :rules="[{ required: true, message: '请输入 IP 地址' }]">
          <Input v-model:value="addressFormState.ipAddr" />
        </FormItem>
        <FormItem label="掩码" name="mask">
          <InputNumber v-model:value="addressFormState.mask" :min="0" :max="128" style="width: 100%" />
        </FormItem>
        <FormItem label="端口" name="port">
          <InputNumber v-model:value="addressFormState.port" :min="0" :max="65535" style="width: 100%" />
        </FormItem>
        <FormItem label="标签" name="tag">
          <Input v-model:value="addressFormState.tag" />
        </FormItem>
      </Form>
    </Modal>

    <!-- 信任地址弹窗 -->
    <Modal v-model:open="trustedModalVisible" :title="trustedEditingId ? '编辑信任地址' : '新增信任地址'" @ok="handleTrustedSubmit">
      <Form ref="trustedFormRef" :model="trustedFormState" :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="源 IP" name="srcIp" :rules="[{ required: true, message: '请输入源 IP' }]">
          <Input v-model:value="trustedFormState.srcIp" />
        </FormItem>
        <FormItem label="协议" name="proto">
          <Input v-model:value="trustedFormState.proto" />
        </FormItem>
        <FormItem label="From 模式" name="fromPattern">
          <Input v-model:value="trustedFormState.fromPattern" />
        </FormItem>
        <FormItem label="标签" name="tag">
          <Input v-model:value="trustedFormState.tag" />
        </FormItem>
        <FormItem label="优先级" name="priority">
          <InputNumber v-model:value="trustedFormState.priority" style="width: 100%" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
