<template>
  <div class="header-container">
    <div class="header-left">
      <!-- 折叠按钮 -->
      <div class="collapse-btn" @click="toggleSidebar">
        <el-icon><Expand v-if="sidebarCollapsed" /><Fold v-else /></el-icon>
      </div>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item 
          v-for="(item, index) in breadcrumbItems" 
          :key="index"
        >
          {{ item }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="header-right">
      <!-- 全屏按钮 -->
      <div class="fullscreen-btn" @click="toggleFullscreen">
        <el-icon><FullScreen /></el-icon>
      </div>
      
      <!-- 用户信息 -->
      <el-dropdown @command="handleUserCommand">
        <div class="user-info">
          <el-avatar :size="30" icon="UserFilled" />
          <span class="username">{{ userInfo?.name || '未知用户' }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="settings">设置</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useGlobalStore } from '@/store'
import { logout } from '@/utils/auth'
import { 
  Expand, 
  Fold, 
  FullScreen, 
  UserFilled 
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const globalStore = useGlobalStore()

// 面包屑导航项
const breadcrumbItems = computed(() => {
  // 根据当前路由生成面包屑导航
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => item.meta.title as string)
})

// 用户信息
const userInfo = computed(() => globalStore.userInfo)

// 侧边栏是否折叠
const sidebarCollapsed = computed(() => globalStore.systemConfig.sidebarCollapsed)

// 切换侧边栏
const toggleSidebar = () => {
  globalStore.toggleSidebar()
}

// 全屏状态
const isFullscreen = ref(false)

// 切换全屏
const toggleFullscreen = () => {
  const element = document.documentElement
  if (!isFullscreen.value) {
    if (element.requestFullscreen) {
      element.requestFullscreen()
    }
    isFullscreen.value = true
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
    isFullscreen.value = false
  }
}

// 处理用户命令
const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      // 跳转到个人中心
      router.push('/profile')
      break
    case 'settings':
      // 跳转到设置页面
      router.push('/settings')
      break
    case 'logout':
      // 登出
      handleLogout()
      break
  }
}

// 处理登出
const handleLogout = () => {
  // 清除认证信息
  logout()
  
  // 清除全局状态中的用户信息
  globalStore.clearUserInfo()
  
  // 跳转到登录页
  router.push('/login')
}

// 监听全屏变化事件
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

onMounted(() => {
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onBeforeUnmount(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})
</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  background-color: #ffffff;
  border-bottom: 1px solid #eee;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.collapse-btn:hover {
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.fullscreen-btn {
  font-size: 18px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.fullscreen-btn:hover {
  color: #409eff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.username {
  font-size: 14px;
  color: #333;
}
</style>