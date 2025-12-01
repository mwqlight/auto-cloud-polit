<template>
  <div class="plugin-manager">
    <h1>插件管理</h1>
    <div class="toolbar">
      <el-button type="primary" @click="refreshPlugins">刷新插件</el-button>
    </div>
    
    <el-table 
      :data="components" 
      v-loading="loading" 
      element-loading-text="加载中..."
      style="width: 100%">
      <el-table-column prop="id" label="组件ID" width="200"></el-table-column>
      <el-table-column prop="name" label="组件名称" width="200"></el-table-column>
      <el-table-column prop="type" label="组件类型" width="150">
        <template #default="scope">
          <el-tag :type="getComponentTypeTag(scope.row.type)">
            {{ getComponentTypeName(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="showOperations(scope.row)">查看操作</el-button>
          <el-button size="small" @click="showDashboard(scope.row)">仪表盘</el-button>
          <el-button size="small" type="primary" @click="goToDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 操作对话框 -->
    <el-dialog v-model="operationsDialogVisible" :title="currentComponent?.name + ' - 操作列表'" width="600px">
      <el-table :data="currentOperations" style="width: 100%">
        <el-table-column prop="id" label="操作ID" width="150"></el-table-column>
        <el-table-column prop="name" label="操作名称" width="150"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="执行" width="100">
          <template #default="scope">
            <el-button size="small" @click="executeOperation(scope.row)">执行</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
    <!-- 仪表盘对话框 -->
    <el-dialog v-model="dashboardDialogVisible" :title="currentComponent?.name + ' - 仪表盘部件'" width="600px">
      <div v-if="currentDashboardWidgets.length === 0">
        <el-empty description="暂无仪表盘部件"></el-empty>
      </div>
      <div v-else>
        <div v-for="widget in currentDashboardWidgets" :key="widget.id" class="widget-item">
          <h3>{{ widget.name }}</h3>
          <p>类型: {{ widget.type }}</p>
          <pre>{{ JSON.stringify(widget.config, null, 2) }}</pre>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePlugin } from '@/composables/usePlugin'
import { CloudComponent, Operation, DashboardWidget, ComponentType } from '@/types/plugin'

const router = useRouter()

const { 
  components, 
  loading, 
  error, 
  fetchComponents, 
  fetchComponentOperations, 
  executeOperation,
  fetchDashboardWidgets
} = usePlugin()

// 对话框相关
const operationsDialogVisible = ref(false)
const dashboardDialogVisible = ref(false)
const currentComponent = ref<CloudComponent | null>(null)
const currentOperations = ref<Operation[]>([])
const currentDashboardWidgets = ref<DashboardWidget[]>([])

// 刷新插件
const refreshPlugins = async () => {
  await fetchComponents()
  if (error.value) {
    ElMessage.error(error.value)
  } else {
    ElMessage.success('插件列表刷新成功')
  }
}

// 显示组件操作
const showOperations = async (component: CloudComponent) => {
  currentComponent.value = component
  currentOperations.value = await fetchComponentOperations(component.id)
  operationsDialogVisible.value = true
}

// 显示仪表盘部件
const showDashboard = async (component: CloudComponent) => {
  currentComponent.value = component
  currentDashboardWidgets.value = await fetchDashboardWidgets(component.id)
  dashboardDialogVisible.value = true
}

// 执行操作
const handleExecuteOperation = async (operation: Operation) => {
  if (!currentComponent.value) return
  
  try {
    // 这里可以添加参数输入对话框
    await ElMessageBox.prompt(
      `请输入操作 "${operation.name}" 的参数 (JSON格式):`,
      '执行操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^.*$/,
        inputErrorMessage: '请输入有效的JSON参数'
      }
    ).then(async ({ value }) => {
      let params = {}
      try {
        params = JSON.parse(value || '{}')
      } catch (e) {
        ElMessage.error('参数格式错误，请输入有效的JSON')
        return
      }
      
      try {
        await executeOperation(currentComponent.value!.id, operation.id, params)
        ElMessage.success(`操作 "${operation.name}" 执行成功`)
      } catch (err) {
        ElMessage.error(`操作执行失败: ${(err as Error).message}`)
      }
    }).catch(() => {
      // 用户取消操作
    })
  } catch (err) {
    console.error('Failed to execute operation:', err)
  }
}

// 跳转到插件详情页面
const goToDetail = (component: CloudComponent) => {
  router.push(`/plugins/${component.id}`)
}

// 组件类型标签样式
const getComponentTypeTag = (type: ComponentType) => {
  switch (type) {
    case ComponentType.COMPUTE: return 'primary'
    case ComponentType.STORAGE: return 'success'
    case ComponentType.NETWORK: return 'warning'
    case ComponentType.SECURITY: return 'danger'
    case ComponentType.MONITORING: return 'info'
    default: return 'default'
  }
}

// 组件类型名称
const getComponentTypeName = (type: ComponentType) => {
  switch (type) {
    case ComponentType.COMPUTE: return '计算'
    case ComponentType.STORAGE: return '存储'
    case ComponentType.NETWORK: return '网络'
    case ComponentType.SECURITY: return '安全'
    case ComponentType.MONITORING: return '监控'
    default: return type
  }
}

onMounted(() => {
  refreshPlugins()
})
</script>

<style scoped>
.plugin-manager {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.widget-item {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
}

.widget-item h3 {
  margin-top: 0;
}

.widget-item pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}
</style>