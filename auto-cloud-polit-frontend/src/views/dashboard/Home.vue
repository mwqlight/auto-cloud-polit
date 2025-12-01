<template>
  <div class="dashboard-home">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <div class="welcome-content">
            <h1>欢迎使用云计算资源驾驶舱</h1>
            <p>一站式云资源管理平台，助您轻松掌控云端资源</p>
            <div class="stats">
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="stat-item">
                    <div class="stat-icon bg-primary">
                      <el-icon><cpu /></el-icon>
                    </div>
                    <div class="stat-info">
                      <div class="stat-number">{{ stats.compute }}</div>
                      <div class="stat-label">计算实例</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="stat-item">
                    <div class="stat-icon bg-success">
                      <el-icon><folder /></el-icon>
                    </div>
                    <div class="stat-info">
                      <div class="stat-number">{{ stats.storage }}</div>
                      <div class="stat-label">存储资源</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="stat-item">
                    <div class="stat-icon bg-warning">
                      <el-icon><connection /></el-icon>
                    </div>
                    <div class="stat-info">
                      <div class="stat-number">{{ stats.network }}</div>
                      <div class="stat-label">网络设备</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="stat-item">
                    <div class="stat-icon bg-danger">
                      <el-icon><lock /></el-icon>
                    </div>
                    <div class="stat-info">
                      <div class="stat-number">{{ stats.security }}</div>
                      <div class="stat-label">安全策略</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>资源概览</span>
            </div>
          </template>
          <div class="chart-container">
            <div ref="resourceChartRef" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最新活动</span>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.timestamp"
              :type="activity.type"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>3D资源可视化</span>
            </div>
          </template>
          <ThreeDViewer />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>插件资源</span>
              <el-button type="primary" size="small" @click="goToPlugins">管理插件</el-button>
            </div>
          </template>
          <el-table :data="plugins" style="width: 100%">
            <el-table-column prop="id" label="插件ID" width="200"></el-table-column>
            <el-table-column prop="name" label="插件名称" width="200"></el-table-column>
            <el-table-column prop="type" label="类型" width="150">
              <template #default="scope">
                <el-tag :type="getComponentTypeTag(scope.row.type)">
                  {{ getComponentTypeName(scope.row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="viewPluginDetail(scope.row)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { cpu, folder, connection, lock } from '@element-plus/icons-vue'
import { usePlugin } from '@/composables/usePlugin'
import { ComponentType } from '@/types/plugin'
import ThreeDViewer from '@/components/ThreeDViewer.vue'

const router = useRouter()
const { components, fetchComponents } = usePlugin()

// 图表引用
const resourceChartRef = ref<HTMLDivElement | null>(null)
let chartInstance: echarts.ECharts | null = null

// 数据统计
const stats = ref({
  compute: 12,
  storage: 8,
  network: 15,
  security: 24
})

// 最新活动
const activities = ref([
  {
    content: '创建了新的计算实例',
    timestamp: '2023-08-01 10:30',
    type: 'primary'
  },
  {
    content: '配置了安全组策略',
    timestamp: '2023-08-01 09:45',
    type: 'success'
  },
  {
    content: '扩容了存储空间',
    timestamp: '2023-08-01 08:20',
    type: 'warning'
  },
  {
    content: '更新了网络ACL规则',
    timestamp: '2023-07-31 17:30',
    type: 'danger'
  }
])

// 插件列表
const plugins = ref(components)

// 初始化图表
const initChart = () => {
  if (resourceChartRef.value) {
    chartInstance = echarts.init(resourceChartRef.value)
    
    const option = {
      title: {
        text: '资源使用趋势'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['计算', '存储', '网络']
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '计算',
          type: 'line',
          data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
          name: '存储',
          type: 'line',
          data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
          name: '网络',
          type: 'line',
          data: [150, 232, 201, 154, 190, 330, 410]
        }
      ]
    }
    
    chartInstance.setOption(option)
  }
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

// 跳转到插件管理页面
const goToPlugins = () => {
  router.push('/plugins')
}

// 查看插件详情
const viewPluginDetail = (plugin: any) => {
  router.push(`/plugins/${plugin.id}`)
}

onMounted(async () => {
  // 初始化图表
  initChart()
  
  // 获取插件列表
  await fetchComponents()
  plugins.value = components.value
})

onBeforeUnmount(() => {
  // 销毁图表实例
  if (chartInstance) {
    chartInstance.dispose()
  }
})
</script>

<style scoped>
.dashboard-home {
  padding: 20px;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.welcome-content h1 {
  font-size: 28px;
  margin-bottom: 10px;
}

.welcome-content p {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 30px;
}

.stats {
  margin-top: 30px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon.bg-primary {
  background-color: #409eff;
}

.stat-icon.bg-success {
  background-color: #67c23a;
}

.stat-icon.bg-warning {
  background-color: #e6a23c;
}

.stat-icon.bg-danger {
  background-color: #f56c6c;
}

.stat-icon .el-icon {
  font-size: 24px;
  color: white;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}
</style>