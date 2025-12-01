<template>
  <el-container class="dashboard-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
      <div class="logo" :class="{ 'collapsed': isCollapse }">
        <img src="@/assets/logo.png" alt="Logo" class="logo-img" v-if="!isCollapse">
        <span class="logo-text" v-if="!isCollapse">云计算资源驾驶舱</span>
        <img src="@/assets/logo.png" alt="Logo" class="logo-img collapsed" v-else>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard/home">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/plugins">
          <el-icon><Grid /></el-icon>
          <template #title>插件管理</template>
        </el-menu-item>
        <el-sub-menu index="resources">
          <template #title>
            <el-icon><Cpu /></el-icon>
            <span>资源管理</span>
          </template>
          <el-menu-item index="/compute">计算资源</el-menu-item>
          <el-menu-item index="/scaling-policy">弹性伸缩策略</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/monitoring">
          <el-icon><Monitor /></el-icon>
          <template #title>监控告警</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <Header />
      </el-header>

      <!-- 页面内容 -->
      <el-main class="main-content">
        <!-- 条件渲染组件或路由视图 -->
        <router-view v-slot="{ Component }">
          <component :is="Component" v-if="Component" />
          <router-view v-else />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '@/components/layout/Header.vue'
import { 
  House, 
  Grid, 
  Cpu, 
  Monitor,
  Expand, 
  Fold 
} from '@element-plus/icons-vue'

// 获取当前路由
const route = useRoute()
const activeMenu = ref(route.path)

// 监听路由变化
watch(
  () => route.path,
  (newPath) => {
    activeMenu.value = newPath
  }
)
</script>

<style scoped>
.dashboard-layout {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  color: #fff;
  transition: width 0.3s ease;
  height: 100%;
  overflow-y: auto;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  background-color: #2d3e50;
  overflow: hidden;
  transition: all 0.3s ease;
}

.logo.collapsed {
  justify-content: center;
}

.logo-img {
  height: 32px;
  width: 32px;
  margin-right: 12px;
}

.logo-img.collapsed {
  margin-right: 0;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none;
  height: calc(100% - 60px);
}

.header {
  padding: 0;
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.main-content {
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
  height: calc(100vh - 60px);
}
</style>