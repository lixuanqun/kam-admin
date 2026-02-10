<template>
  <div class="page-container">
    <a-row :gutter="16"><a-col :span="8"><a-card><a-statistic title="RTPEngine 状态"><template #value><a-tag :color="status?.status==='ok'?'green':'red'" size="large">{{status?.status==='ok'?'正常':'异常'}}</a-tag></template></a-statistic></a-card></a-col></a-row>
    <a-card title="RTPEngine 管理" class="general-card">
      <template #extra><a-space><a-button @click="loadAll"><template #icon><icon-refresh /></template>刷新</a-button><a-button @click="handleReload"><template #icon><icon-sync /></template>重载配置</a-button><a-button type="primary" @click="showEnableModal">启用/禁用</a-button></a-space></template>
      <a-descriptions title="RTPEngine 信息" bordered :column="2" v-if="showData"><a-descriptions-item label="原始数据" :span="2"><pre style="max-height:300px;overflow:auto;margin:0">{{JSON.stringify(showData,null,2)}}</pre></a-descriptions-item></a-descriptions>
      <div v-else style="text-align:center;padding:32px;color:var(--color-text-3)">暂无 RTPEngine 数据，请确保 Kamailio 已加载 rtpengine 模块</div>
    </a-card>
    <a-card title="NAT Helper" class="general-card"><p>NAT Helper 模块用于处理 NAT 穿透问题，提供 ping 机制保持 NAT 绑定。</p></a-card>
    <a-modal v-model:visible="enableModalVisible" title="启用/禁用 RTPEngine" @ok="handleEnable">
      <a-form layout="vertical"><a-form-item label="URL"><a-input v-model="enableForm.url" placeholder="rtpengine URL" /></a-form-item><a-form-item label="标志"><a-input-number v-model="enableForm.flag" :min="0" :max="1" style="width:100%" /><span style="color:var(--color-text-3)">0 = 禁用, 1 = 启用</span></a-form-item></a-form>
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Message } from '@arco-design/web-vue';
import { getRtpengineStatus, showRtpengine, reloadRtpengine, enableRtpengine } from '@/api/kamailio';
const status = ref<any>(null); const showData = ref<any>(null); const enableModalVisible = ref(false); const enableForm = ref({url:'',flag:1});
let refreshTimer: any = null;
async function loadStatus(){try{const r=await getRtpengineStatus();if(r.data?.code===0)status.value=r.data.data;}catch{status.value={status:'error'};}}
async function loadShowData(){try{const r=await showRtpengine();if(r.data?.code===0)showData.value=r.data.data;}catch{}}
async function handleReload(){await reloadRtpengine();Message.success('重载成功');loadStatus();loadShowData();}
function showEnableModal(){enableForm.value={url:'',flag:1};enableModalVisible.value=true;}
async function handleEnable(){await enableRtpengine(enableForm.value.url,enableForm.value.flag);Message.success('操作成功');enableModalVisible.value=false;loadStatus();}
function loadAll(){loadStatus();loadShowData();}
onMounted(()=>{loadAll();refreshTimer=setInterval(loadAll,30000);});
onUnmounted(()=>{if(refreshTimer)clearInterval(refreshTimer);});
</script>
