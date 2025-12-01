<template>
  <div class="plugin-detail">
    <el-page-header @back="goBack" :content="plugin?.name || '插件详情'">
      <template #content>
        <div class="plugin-header">
          <div class="plugin-title">
            <el-tag :type="getComponentTypeTag(plugin?.type)" size="large">
              {{ getComponentTypeName(plugin?.type) }}
            </el-tag>
            <h1>{{ plugin?.name }}</h1>
          </div>
        </div>
      </template>
    </el-page-header>

    <div class="plugin-content" v-if="plugin">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>基本信息</span>
              </div>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="组件ID">{{ plugin.id }}</el-descriptions-item>
              <el-descriptions-item label="组件名称">{{ plugin.name }}</el-descriptions-item>
              <el-descriptions-item label="组件类型">{{ getComponentTypeName(plugin.type) }}</el-descriptions-item>
            </el-descriptions>
          </el-card>

          <el-card class="box-card" style="margin-top: 20px">
            <template #header>
              <div class="card-header">
                <span>支持的操作</span>
                <el-button type="primary" size="small" @click="refreshOperations">刷新</el-button>
              </div>
            </template>
            <el-table :data="operations" v-loading="operationsLoading" style="width: 100%">
              <el-table-column prop="id" label="操作ID" width="180"></el-table-column>
              <el-table-column prop="name" label="操作名称" width="180"></el-table-column>
              <el-table-column prop="description" label="描述"></el-table-column>
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="executeOperation(scope.row)">执行</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>仪表盘部件</span>
                <el-button type="primary" size="small" @click="refreshWidgets">刷新</el-button>
              </div>
            </template>
            <div v-if="widgetsLoading" class="widgets-loading">
              <el-skeleton animated />
            </div>
            <div v-else-if="widgets.length > 0">
              <div v-for="widget in widgets" :key="widget.id" class="widget-preview">
                <h4>{{ widget.name }}</h4>
                <p>{{ widget.type }}</p>
                <pre>{{ JSON.stringify(widget.config, null, 2) }}</pre>
              </div>
            </div>
            <div v-else>
              <el-empty description="暂无仪表盘部件" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div v-else class="plugin-not-found">
      <el-empty description="插件未找到" />
    </div>

    <!-- 执行操作对话框 -->
    <el-dialog v-model="executeDialogVisible" :title="`执行操作: ${currentOperation?.name}`" width="500px">
      <el-form :model="operationParams" label-width="100px">
        <el-form-item label="参数 (JSON)">
          <el-input 
            v-model="operationParamsJson" 
            type="textarea" 
            :rows="6"
            placeholder='{"key": "value"}'
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="executeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmExecuteOperation">确认执行</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePlugin } from '@/composables/usePlugin'
import { CloudComponent, Operation, DashboardWidget, ComponentType } from '@/types/plugin'

const route = useRoute()
const router = useRouter()
const { 
  components, 
  fetchComponents, 
  fetchComponentOperations, 
  executeOperation,
  fetchDashboardWidgets
} = usePlugin()

// 数据状态
const plugin = ref<CloudComponent | null>(null)
const operations = ref<Operation[]>([])
const widgets = ref<DashboardWidget[]>([])
const operationsLoading = ref(false)
const widgetsLoading = ref(false)

// 执行操作相关
const executeDialogVisible = ref(false)
const currentOperation = ref<Operation | null>(null)
const operationParams = ref<Record<string, any>>({})
const operationParamsJson = computed({
  get: () => JSON.stringify(operationParams.value, null, 2),
  set: (val) => {
    try {
      operationParams.value = JSON.parse(val)
    } catch (e) {
      // Ignore invalid JSON
    }
  }
})

// 返回上一页
const goBack = () => {
  router.back()
}

// 刷新操作列表
const refreshOperations = async () => {
  if (!plugin.value) return
  operationsLoading.value = true
  try {
    operations.value = await fetchComponentOperations(plugin.value.id)
  } catch (err) {
    ElMessage.error('获取操作列表失败: ' + (err as Error).message)
  } finally {
    operationsLoading.value = false
  }
}

// 刷新仪表盘部件
const refreshWidgets = async () => {
  if (!plugin.value) return
  widgetsLoading.value = true
  try {
    widgets.value = await fetchDashboardWidgets(plugin.value.id)
  } catch (err) {
    ElMessage.error('获取仪表盘部件失败: ' + (err as Error).message)
  } finally {
    widgetsLoading.value = false
  }
}

// 执行操作
const executeOperationHandler = async (operation: Operation) => {
  if (!plugin.value) return
  
  currentOperation.value = operation
  operationParams.value = {}
  executeDialogVisible.value = true
}

// 确认执行操作
const confirmExecuteOperation = async () => {
  if (!plugin.value || !currentOperation.value) return
  
  try {
    await executeOperation(plugin.value.id, currentOperation.value.id, operationParams.value)
    ElMessage.success(`操作 "${currentOperation.value.name}" 执行成功`)
    executeDialogVisible.value = false
  } catch (err) {
    ElMessage.error(`操作执行失败: ${(err as Error).message}`)
  }
}

// 组件类型标签样式
const getComponentTypeTag = (type?: ComponentType) => {
  if (!type) return 'default'
  
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
const getComponentTypeName = (type?: ComponentType) => {
  if (!type) return '未知'
  
  switch (type) {
    case ComponentType.COMPUTE: return '计算'
    case ComponentType.STORAGE: return '存储'
    case ComponentType.NETWORK: return '网络'
    case ComponentType.SECURITY: return '安全'
    case ComponentType.MONITORING: return '监控'
    default: return type
  }
}

onMounted(async () => {
  // 获取所有组件
  await fetchComponents()
  
  // 查找当前插件
  const pluginId = route.params.id as string
  if (pluginId) {
    plugin.value = components.value.find(c => c.id === pluginId) || null
    
    if (plugin.value) {
      // 加载操作和部件
      await Promise.all([
        refreshOperations(),
        refreshWidgets()
      ])
    }
  }
})
</script>

<style scoped>
.plugin-detail {
  padding: 20px;
}

.plugin-header {
  display: flex;
  align-items: center;
}

.plugin-title {
  display: flex;
  align-items: center;
  gap: 15px;
}

.plugin-title h1 {
  margin: 0;
  font-size: 24px;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.widget-preview {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 10px;
}

.widget-preview h4 {
  margin: 0 0 5px 0;
}

.widget-preview pre {
  background-color: #f5f5f5;
  padding: 8px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 12px;
}

.widgets-loading {
  padding: 20px 0;
}

.plugin-not-found {
  padding: 50px 0;
}
</style>