<template>
  <div class="not-found">
    <div class="not-found-content">
      <el-icon class="not-found-icon"><Warning /></el-icon>
      <h1>404</h1>
      <h2>页面未找到</h2>
      <p>抱歉，您访问的页面不存在。</p>
      <div class="actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="goBack">返回上一页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Warning } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.not-found-content {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.not-found-icon {
  font-size: 64px;
  color: #f56c6c;
  margin-bottom: 20px;
}

.not-found-content h1 {
  font-size: 48px;
  color: #333;
  margin-bottom: 10px;
}

.not-found-content h2 {
  font-size: 24px;
  color: #666;
  margin-bottom: 20px;
}

.not-found-content p {
  font-size: 16px;
  color: #999;
  margin-bottom: 30px;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}
</style>