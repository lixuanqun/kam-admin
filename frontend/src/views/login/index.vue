<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>Kamailio Dashboard</h1>
        <p>基于 Arco Design Pro 的 Kamailio 管理后台</p>
      </div>
      <a-form
        ref="formRef"
        :model="formState"
        layout="vertical"
        @submit-success="handleSubmit"
      >
        <a-form-item field="username" label="用户名" :rules="[{ required: true, message: '请输入用户名' }]">
          <a-input v-model="formState.username" placeholder="请输入用户名" size="large">
            <template #prefix><icon-user /></template>
          </a-input>
        </a-form-item>
        <a-form-item field="password" label="密码" :rules="[{ required: true, message: '请输入密码' }]">
          <a-input-password v-model="formState.password" placeholder="请输入密码" size="large">
            <template #prefix><icon-lock /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" long size="large">
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Message } from '@arco-design/web-vue';
import { useUserStore } from '@/store';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const loading = ref(false);
const formState = reactive({
  username: '',
  password: '',
});

async function handleSubmit() {
  loading.value = true;
  try {
    // Simple login - store token directly
    // In production, replace with actual API call
    userStore.setToken('admin-token');
    userStore.setUserInfo({ name: formState.username, role: 'admin' });
    Message.success('登录成功');
    const redirect = (route.query.redirect as string) || '/kamailio/monitoring';
    router.push(redirect);
  } catch (error) {
    Message.error('登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped lang="less">
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #165DFF 0%, #722ED1 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
  background: var(--color-bg-1);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-header {
  margin-bottom: 32px;
  text-align: center;

  h1 {
    margin: 0 0 8px;
    font-size: 24px;
    font-weight: 600;
    color: var(--color-text-1);
  }

  p {
    margin: 0;
    font-size: 14px;
    color: var(--color-text-3);
  }
}
</style>
