<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Card, Row, Col, Statistic, Button, Space, Descriptions, DescriptionsItem, Tag, message, Input, Modal, Form, FormItem, InputNumber } from 'ant-design-vue';
import { ReloadOutlined, SyncOutlined, CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';
import { getRtpengineStatus, showRtpengine, reloadRtpengine, enableRtpengine } from '#/api/kamailio';

const loading = ref(false);
const status = ref<any>(null);
const showData = ref<any>(null);

const enableModalVisible = ref(false);
const enableForm = ref({ url: '', flag: 1 });

let refreshTimer: any = null;

async function loadStatus() {
  loading.value = true;
  try {
    const res = await getRtpengineStatus();
    if (res.data?.code === 0) {
      status.value = res.data.data;
    }
  } catch {
    status.value = { status: 'error' };
  } finally { loading.value = false; }
}

async function loadShowData() {
  try {
    const res = await showRtpengine();
    if (res.data?.code === 0) {
      showData.value = res.data.data;
    }
  } catch {}
}

async function handleReload() {
  await reloadRtpengine();
  message.success('重载成功');
  loadStatus();
  loadShowData();
}

function showEnableModal() {
  enableForm.value = { url: '', flag: 1 };
  enableModalVisible.value = true;
}

async function handleEnable() {
  await enableRtpengine(enableForm.value.url, enableForm.value.flag);
  message.success('操作成功');
  enableModalVisible.value = false;
  loadStatus();
}

function loadAll() {
  loadStatus();
  loadShowData();
}

onMounted(() => {
  loadAll();
  refreshTimer = setInterval(loadAll, 30000);
});

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer);
});
</script>

<template>
  <div class="p-4">
    <Row :gutter="16" class="mb-4">
      <Col :span="8">
        <Card>
          <Statistic title="RTPEngine 状态">
            <template #prefix>
              <CheckCircleOutlined v-if="status?.status === 'ok'" style="color: #52c41a" />
              <CloseCircleOutlined v-else style="color: #ff4d4f" />
            </template>
            <template #value>
              <Tag :color="status?.status === 'ok' ? 'success' : 'error'">
                {{ status?.status === 'ok' ? '正常' : '异常' }}
              </Tag>
            </template>
          </Statistic>
        </Card>
      </Col>
    </Row>

    <Card title="RTPEngine 管理">
      <template #extra>
        <Space>
          <Button @click="loadAll"><ReloadOutlined /> 刷新</Button>
          <Button @click="handleReload"><SyncOutlined /> 重载配置</Button>
          <Button type="primary" @click="showEnableModal">启用/禁用</Button>
        </Space>
      </template>

      <Descriptions title="RTPEngine 信息" bordered :column="2" v-if="showData">
        <DescriptionsItem label="原始数据" :span="2">
          <pre style="max-height: 300px; overflow: auto; margin: 0;">{{ JSON.stringify(showData, null, 2) }}</pre>
        </DescriptionsItem>
      </Descriptions>
      <div v-else class="text-center py-8 text-gray-500">
        暂无 RTPEngine 数据，请确保 Kamailio 已加载 rtpengine 模块
      </div>
    </Card>

    <Card title="NAT Helper" class="mt-4">
      <p>NAT Helper 模块用于处理 NAT 穿透问题，提供 ping 机制保持 NAT 绑定。</p>
      <ul>
        <li>nathelper.enable_ping - 启用/禁用 NAT ping</li>
        <li>通过 RPC 可以动态控制 NAT 行为</li>
      </ul>
    </Card>

    <!-- 启用/禁用弹窗 -->
    <Modal v-model:open="enableModalVisible" title="启用/禁用 RTPEngine" @ok="handleEnable">
      <Form :labelCol="{ span: 6 }" :wrapperCol="{ span: 16 }">
        <FormItem label="URL"><Input v-model:value="enableForm.url" placeholder="rtpengine URL" /></FormItem>
        <FormItem label="标志">
          <InputNumber v-model:value="enableForm.flag" :min="0" :max="1" style="width: 100%" />
          <span class="text-gray-500">0 = 禁用, 1 = 启用</span>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
