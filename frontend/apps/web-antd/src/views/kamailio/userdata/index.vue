<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Table, Button, Space, Card, Tabs, TabPane, Input, Modal, Form, FormItem, message, Popconfirm } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined, SearchOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getAliases, createAlias, deleteAlias, getGroups, createGroup, deleteGroup, getSpeedDials } from '#/api/kamailio';

const activeTab = ref('aliases');

// Aliases
const aliasLoading = ref(false);
const aliasData = ref<any[]>([]);
const aliasPagination = reactive({ current: 1, pageSize: 20, total: 0 });
const aliasKeyword = ref('');
const aliasModalVisible = ref(false);
const aliasForm = reactive({ aliasUsername: '', aliasDomain: '', username: '', domain: '' });

const aliasColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '别名用户名', dataIndex: 'aliasUsername' },
  { title: '别名域', dataIndex: 'aliasDomain' },
  { title: '真实用户名', dataIndex: 'username' },
  { title: '真实域', dataIndex: 'domain' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadAliases() {
  aliasLoading.value = true;
  try {
    const res = await getAliases({ page: aliasPagination.current, limit: aliasPagination.pageSize, keyword: aliasKeyword.value });
    if (res.data?.code === 0) {
      aliasData.value = res.data.data.items;
      aliasPagination.total = res.data.data.total;
    }
  } finally { aliasLoading.value = false; }
}

async function handleAliasSubmit() {
  await createAlias(aliasForm);
  message.success('创建成功');
  aliasModalVisible.value = false;
  loadAliases();
}

async function handleAliasDelete(id: number) {
  await deleteAlias(id);
  message.success('删除成功');
  loadAliases();
}

// Groups
const groupLoading = ref(false);
const groupData = ref<any[]>([]);
const groupPagination = reactive({ current: 1, pageSize: 20, total: 0 });
const groupKeyword = ref('');
const groupModalVisible = ref(false);
const groupForm = reactive({ username: '', domain: '', grp: '' });

const groupColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '组名', dataIndex: 'grp' },
  { title: '操作', key: 'action', width: 100 },
];

async function loadGroups() {
  groupLoading.value = true;
  try {
    const res = await getGroups({ page: groupPagination.current, limit: groupPagination.pageSize, keyword: groupKeyword.value });
    if (res.data?.code === 0) {
      groupData.value = res.data.data.items;
      groupPagination.total = res.data.data.total;
    }
  } finally { groupLoading.value = false; }
}

async function handleGroupSubmit() {
  await createGroup(groupForm);
  message.success('创建成功');
  groupModalVisible.value = false;
  loadGroups();
}

async function handleGroupDelete(id: number) {
  await deleteGroup(id);
  message.success('删除成功');
  loadGroups();
}

// Speed Dial
const sdLoading = ref(false);
const sdData = ref<any[]>([]);
const sdPagination = reactive({ current: 1, pageSize: 20, total: 0 });

const sdColumns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '用户名', dataIndex: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '快捷号码', dataIndex: 'sdUsername' },
  { title: '目标 URI', dataIndex: 'newUri' },
  { title: '描述', dataIndex: 'description' },
];

async function loadSpeedDials() {
  sdLoading.value = true;
  try {
    const res = await getSpeedDials({ page: sdPagination.current, limit: sdPagination.pageSize });
    if (res.data?.code === 0) {
      sdData.value = res.data.data.items;
      sdPagination.total = res.data.data.total;
    }
  } finally { sdLoading.value = false; }
}

onMounted(() => {
  loadAliases();
  loadGroups();
  loadSpeedDials();
});
</script>

<template>
  <div class="p-4">
    <Card>
      <Tabs v-model:activeKey="activeTab">
        <TabPane key="aliases" tab="用户别名">
          <Space class="mb-4">
            <Input v-model:value="aliasKeyword" placeholder="搜索别名" style="width: 200px" @pressEnter="loadAliases">
              <template #suffix><SearchOutlined /></template>
            </Input>
            <Button type="primary" @click="aliasModalVisible = true"><PlusOutlined /> 新增</Button>
            <Button @click="loadAliases"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="aliasColumns" :dataSource="aliasData" :loading="aliasLoading" :pagination="aliasPagination" rowKey="id">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Popconfirm title="确定删除？" @confirm="handleAliasDelete(record.id)">
                  <Button type="link" size="small" danger><DeleteOutlined /></Button>
                </Popconfirm>
              </template>
            </template>
          </Table>
        </TabPane>

        <TabPane key="groups" tab="用户组">
          <Space class="mb-4">
            <Input v-model:value="groupKeyword" placeholder="搜索用户名" style="width: 200px" @pressEnter="loadGroups">
              <template #suffix><SearchOutlined /></template>
            </Input>
            <Button type="primary" @click="groupModalVisible = true"><PlusOutlined /> 新增</Button>
            <Button @click="loadGroups"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="groupColumns" :dataSource="groupData" :loading="groupLoading" :pagination="groupPagination" rowKey="id">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <Popconfirm title="确定删除？" @confirm="handleGroupDelete(record.id)">
                  <Button type="link" size="small" danger><DeleteOutlined /></Button>
                </Popconfirm>
              </template>
            </template>
          </Table>
        </TabPane>

        <TabPane key="speedDial" tab="快捷拨号">
          <Space class="mb-4">
            <Button @click="loadSpeedDials"><ReloadOutlined /> 刷新</Button>
          </Space>
          <Table :columns="sdColumns" :dataSource="sdData" :loading="sdLoading" :pagination="sdPagination" rowKey="id" />
        </TabPane>
      </Tabs>
    </Card>

    <!-- 别名弹窗 -->
    <Modal v-model:open="aliasModalVisible" title="新增别名" @ok="handleAliasSubmit">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="别名用户名"><Input v-model:value="aliasForm.aliasUsername" /></FormItem>
        <FormItem label="别名域"><Input v-model:value="aliasForm.aliasDomain" /></FormItem>
        <FormItem label="真实用户名"><Input v-model:value="aliasForm.username" /></FormItem>
        <FormItem label="真实域"><Input v-model:value="aliasForm.domain" /></FormItem>
      </Form>
    </Modal>

    <!-- 用户组弹窗 -->
    <Modal v-model:open="groupModalVisible" title="新增用户组" @ok="handleGroupSubmit">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="用户名"><Input v-model:value="groupForm.username" /></FormItem>
        <FormItem label="域"><Input v-model:value="groupForm.domain" /></FormItem>
        <FormItem label="组名"><Input v-model:value="groupForm.grp" /></FormItem>
      </Form>
    </Modal>
  </div>
</template>
