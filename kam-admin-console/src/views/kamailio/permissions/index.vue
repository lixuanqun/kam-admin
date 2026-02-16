<template>
  <div class="page-container">
    <a-card class="general-card">
      <a-tabs v-model:active-key="activeTab">
        <a-tab-pane key="address" title="地址白名单">
          <a-space style="margin-bottom:16px">
            <a-input v-model="addressKeyword" placeholder="搜索 IP" :style="{width:'200px'}" @press-enter="loadAddressData"><template #prefix><icon-search /></template></a-input>
            <a-button type="primary" @click="loadAddressData"><template #icon><icon-search /></template>搜索</a-button>
            <a-button @click="loadAddressData"><template #icon><icon-refresh /></template>刷新</a-button>
            <a-button type="primary" @click="handleAddressAdd"><template #icon><icon-plus /></template>新增</a-button>
            <a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button>
          </a-space>
          <a-table :columns="addressColumns" :data="addressData" :loading="addressLoading" :pagination="{total:addressPagination.total,current:addressPagination.current,pageSize:addressPagination.pageSize}" row-key="id">
            <template #action="{record}">
              <a-space>
                <a-button type="text" size="small" @click="handleAddressEdit(record)"><template #icon><icon-edit /></template>编辑</a-button>
                <a-popconfirm content="确定删除？" @ok="handleAddressDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template>删除</a-button></a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="trusted" title="信任地址">
          <a-space style="margin-bottom:16px">
            <a-input v-model="trustedKeyword" placeholder="搜索源 IP" :style="{width:'200px'}" @press-enter="loadTrustedData"><template #prefix><icon-search /></template></a-input>
            <a-button type="primary" @click="loadTrustedData"><template #icon><icon-search /></template>搜索</a-button>
            <a-button @click="loadTrustedData"><template #icon><icon-refresh /></template>刷新</a-button>
            <a-button type="primary" @click="handleTrustedAdd"><template #icon><icon-plus /></template>新增</a-button>
          </a-space>
          <a-table :columns="trustedColumns" :data="trustedData" :loading="trustedLoading" :pagination="{total:trustedPagination.total,current:trustedPagination.current,pageSize:trustedPagination.pageSize}" row-key="id">
            <template #action="{record}">
              <a-space>
                <a-button type="text" size="small" @click="handleTrustedEdit(record)"><template #icon><icon-edit /></template>编辑</a-button>
                <a-popconfirm content="确定删除？" @ok="handleTrustedDelete(record.id)"><a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template>删除</a-button></a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal v-model:visible="addressModalVisible" :title="addressEditingId?'编辑地址':'新增地址'" @ok="handleAddressSubmit">
      <a-form ref="addressFormRef" :model="addressFormState" layout="vertical">
        <a-form-item field="grp" label="分组"><a-input-number v-model="addressFormState.grp" :min="0" style="width:100%" /></a-form-item>
        <a-form-item field="ipAddr" label="IP 地址" :rules="[{required:true,message:'请输入'}]"><a-input v-model="addressFormState.ipAddr" /></a-form-item>
        <a-form-item field="mask" label="掩码"><a-input-number v-model="addressFormState.mask" :min="0" :max="128" style="width:100%" /></a-form-item>
        <a-form-item field="port" label="端口"><a-input-number v-model="addressFormState.port" :min="0" :max="65535" style="width:100%" /></a-form-item>
        <a-form-item field="tag" label="标签"><a-input v-model="addressFormState.tag" /></a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model:visible="trustedModalVisible" :title="trustedEditingId?'编辑信任地址':'新增信任地址'" @ok="handleTrustedSubmit">
      <a-form ref="trustedFormRef" :model="trustedFormState" layout="vertical">
        <a-form-item field="srcIp" label="源 IP" :rules="[{required:true,message:'请输入'}]"><a-input v-model="trustedFormState.srcIp" /></a-form-item>
        <a-form-item field="proto" label="协议"><a-input v-model="trustedFormState.proto" /></a-form-item>
        <a-form-item field="fromPattern" label="From 模式"><a-input v-model="trustedFormState.fromPattern" /></a-form-item>
        <a-form-item field="tag" label="标签"><a-input v-model="trustedFormState.tag" /></a-form-item>
        <a-form-item field="priority" label="优先级"><a-input-number v-model="trustedFormState.priority" style="width:100%" /></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getAddresses, createAddress, updateAddress, deleteAddress, getTrusted, createTrusted, updateTrusted, deleteTrusted, reloadPermissions, type Address, type Trusted } from '@/api/kamailio';

const activeTab = ref('address');
const addressLoading = ref(false); const addressData = ref<Address[]>([]); const addressPagination = reactive({ current: 1, pageSize: 20, total: 0 }); const addressKeyword = ref('');
const addressModalVisible = ref(false); const addressEditingId = ref<number|null>(null); const addressFormRef = ref();
const addressFormState = reactive({ grp: 1, ipAddr: '', mask: 32, port: 0, tag: '' });
const addressColumns = [ { title: 'ID', dataIndex: 'id', width: 80 }, { title: '分组', dataIndex: 'grp', width: 80 }, { title: 'IP 地址', dataIndex: 'ipAddr' }, { title: '掩码', dataIndex: 'mask', width: 80 }, { title: '端口', dataIndex: 'port', width: 80 }, { title: '标签', dataIndex: 'tag' }, { title: '操作', slotName: 'action', width: 150 } ];

async function loadAddressData() { addressLoading.value = true; try { const res = await getAddresses({ page: addressPagination.current, limit: addressPagination.pageSize, keyword: addressKeyword.value }); if (res.data?.code === 0) { addressData.value = res.data.data.items; addressPagination.total = res.data.data.total; } } catch { Message.error('加载失败'); } finally { addressLoading.value = false; } }
function handleAddressAdd() { addressEditingId.value = null; Object.assign(addressFormState, { grp: 1, ipAddr: '', mask: 32, port: 0, tag: '' }); addressModalVisible.value = true; }
function handleAddressEdit(r: Address) { addressEditingId.value = r.id; Object.assign(addressFormState, r); addressModalVisible.value = true; }
async function handleAddressDelete(id: number) { try { await deleteAddress(id); Message.success('删除成功'); loadAddressData(); } catch { Message.error('删除失败'); } }
async function handleAddressSubmit() { const errors = await addressFormRef.value?.validate(); if (errors) return; try { if (addressEditingId.value) { await updateAddress(addressEditingId.value, addressFormState); Message.success('更新成功'); } else { await createAddress(addressFormState); Message.success('创建成功'); } addressModalVisible.value = false; loadAddressData(); } catch {} }

const trustedLoading = ref(false); const trustedData = ref<Trusted[]>([]); const trustedPagination = reactive({ current: 1, pageSize: 20, total: 0 }); const trustedKeyword = ref('');
const trustedModalVisible = ref(false); const trustedEditingId = ref<number|null>(null); const trustedFormRef = ref();
const trustedFormState = reactive({ srcIp: '', proto: 'any', fromPattern: '', tag: '', priority: 0 });
const trustedColumns = [ { title: 'ID', dataIndex: 'id', width: 80 }, { title: '源 IP', dataIndex: 'srcIp' }, { title: '协议', dataIndex: 'proto', width: 80 }, { title: 'From 模式', dataIndex: 'fromPattern' }, { title: '标签', dataIndex: 'tag' }, { title: '优先级', dataIndex: 'priority', width: 80 }, { title: '操作', slotName: 'action', width: 150 } ];

async function loadTrustedData() { trustedLoading.value = true; try { const res = await getTrusted({ page: trustedPagination.current, limit: trustedPagination.pageSize, keyword: trustedKeyword.value }); if (res.data?.code === 0) { trustedData.value = res.data.data.items; trustedPagination.total = res.data.data.total; } } catch { Message.error('加载失败'); } finally { trustedLoading.value = false; } }
function handleTrustedAdd() { trustedEditingId.value = null; Object.assign(trustedFormState, { srcIp: '', proto: 'any', fromPattern: '', tag: '', priority: 0 }); trustedModalVisible.value = true; }
function handleTrustedEdit(r: Trusted) { trustedEditingId.value = r.id; Object.assign(trustedFormState, r); trustedModalVisible.value = true; }
async function handleTrustedDelete(id: number) { try { await deleteTrusted(id); Message.success('删除成功'); loadTrustedData(); } catch { Message.error('删除失败'); } }
async function handleTrustedSubmit() { const errors = await trustedFormRef.value?.validate(); if (errors) return; try { if (trustedEditingId.value) { await updateTrusted(trustedEditingId.value, trustedFormState); Message.success('更新成功'); } else { await createTrusted(trustedFormState); Message.success('创建成功'); } trustedModalVisible.value = false; loadTrustedData(); } catch {} }
async function handleReload() { try { await reloadPermissions(); Message.success('重载成功'); } catch { Message.error('重载失败'); } }
onMounted(() => { loadAddressData(); loadTrustedData(); });
</script>
