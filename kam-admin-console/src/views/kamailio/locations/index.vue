<template>
  <div class="page-container">
    <a-row :gutter="16">
      <a-col :span="6"><a-card><a-statistic title="在线用户" :value="onlineCount" /></a-card></a-col>
      <a-col :span="6"><a-card><a-statistic title="域数量" :value="stats.byDomain.length" /></a-card></a-col>
    </a-row>
    <a-card class="general-card">
      <a-space>
        <a-input v-model="searchKeyword" placeholder="搜索用户名" :style="{width:'200px'}" @press-enter="handleSearch"><template #prefix><icon-search /></template></a-input>
        <a-button type="primary" @click="handleSearch"><template #icon><icon-search /></template>搜索</a-button>
        <a-button @click="()=>{loadData();loadStats()}"><template #icon><icon-refresh /></template>刷新</a-button>
        <a-popconfirm content="将内存中的注册信息刷入数据库，确定执行？" @ok="handleFlush">
          <a-button><template #icon><icon-sync /></template>刷新到数据库</a-button>
        </a-popconfirm>
      </a-space>
    </a-card>
    <a-card class="general-card">
      <a-table :columns="columns" :data="data" :loading="loading" :pagination="{total:pagination.total,current:pagination.current,pageSize:pagination.pageSize,showTotal:true}" row-key="id" @page-change="(p:number)=>{pagination.current=p;loadData()}">
        <template #username="{record}"><a-tag color="green">{{record.username}}</a-tag></template>
        <template #action="{record}">
          <a-popconfirm content="确定强制下线此用户？" @ok="handleDelete(record)">
            <a-button type="text" status="danger" size="small"><template #icon><icon-delete /></template>下线</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getLocations, getLocationStats, getOnlineCount, deleteUserLocation, flushUsrloc, type Location } from '@/api/kamailio';

const loading = ref(false);
const data = ref<Location[]>([]);
const pagination = reactive({ current: 1, pageSize: 20, total: 0 });
const searchKeyword = ref('');
const onlineCount = ref(0);
const stats = ref({ total: 0, byDomain: [] as any[], byUserAgent: [] as any[] });

const columns = [
  { title: 'ID', dataIndex: 'id', width: 80 },
  { title: '用户名', dataIndex: 'username', slotName: 'username' },
  { title: '域', dataIndex: 'domain' },
  { title: '联系地址', dataIndex: 'contact', ellipsis: true },
  { title: 'User-Agent', dataIndex: 'userAgent', ellipsis: true },
  { title: '过期时间', dataIndex: 'expires' },
  { title: '操作', slotName: 'action', width: 100 },
];

async function loadData() { loading.value = true; try { const res = await getLocations({ page: pagination.current, limit: pagination.pageSize, keyword: searchKeyword.value }); if (res.data?.code === 0) { data.value = res.data.data.items; pagination.total = res.data.data.total; } } catch { Message.error('加载失败'); } finally { loading.value = false; } }
async function loadStats() { try { const [c, s] = await Promise.all([getOnlineCount(), getLocationStats()]); if (c.data?.code === 0) onlineCount.value = c.data.data; if (s.data?.code === 0) stats.value = s.data.data; } catch {} }
function handleSearch() { pagination.current = 1; loadData(); }
async function handleDelete(record: Location) { try { const res = await deleteUserLocation(record.username, record.domain); if (res.data?.code === 0) { Message.success('已强制下线'); loadData(); loadStats(); } } catch { Message.error('操作失败'); } }
async function handleFlush() { try { const res = await flushUsrloc(); if (res.data?.code === 0) { Message.success('已刷新到数据库'); loadData(); loadStats(); } } catch { Message.error('操作失败'); } }
onMounted(() => { loadData(); loadStats(); });
</script>
