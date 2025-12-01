<template>
  <div class="monitoring-container">
    <el-row :gutter="20" class="metrics-row">
      <!-- CPU使用率卡片 -->
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-header">
            <div class="metric-icon cpu-bg">
              <el-icon><Cpu /></el-icon>
            </div>
            <div class="metric-info">
              <div class="metric-value">{{ cpuUsage }}%</div>
              <div class="metric-label">CPU使用率</div>
            </div>
          </div>
          <div class="metric-chart">
            <div class="chart-placeholder">CPU使用率趋势图</div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 内存使用率卡片 -->
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-header">
            <div class="metric-icon memory-bg">
              <el-icon><Box /></el-icon>
            </div>
            <div class="metric-info">
              <div class="metric-value">{{ memoryUsage }}%</div>
              <div class="metric-label">内存使用率</div>
            </div>
          </div>
          <div class="metric-chart">
            <div class="chart-placeholder">内存使用率趋势图</div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 网络流量卡片 -->
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-header">
            <div class="metric-icon network-bg">
              <el-icon><Connection /></el-icon>
            </div>
            <div class="metric-info">
              <div class="metric-value">{{ networkTraffic }} Mbps</div>
              <div class="metric-label">网络流量</div>
            </div>
          </div>
          <div class="metric-chart">
            <div class="chart-placeholder">网络流量趋势图</div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 告警数量卡片 -->
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-header">
            <div class="metric-icon alert-bg">
              <el-icon><Bell /></el-icon>
            </div>
            <div class="metric-info">
              <div class="metric-value">{{ alertCount }}</div>
              <div class="metric-label">告警数量</div>
            </div>
          </div>
          <div class="metric-chart">
            <div class="chart-placeholder">告警趋势图</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 告警列表 -->
    <el-card class="alert-list-card">
      <template #header>
        <div class="card-header">
          <span>实时告警</span>
          <el-button type="primary" @click="handleRefresh">刷新</el-button>
        </div>
      </template>
      
      <el-table :data="alertData" style="width: 100%">
        <el-table-column prop="level" label="告警级别" width="100">
          <template #default="scope">
            <el-tag :type="getAlertLevelType(scope.row.level)">
              {{ getAlertLevelText(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resource" label="资源名称" />
        <el-table-column prop="metric" label="监控指标" />
        <el-table-column prop="threshold" label="阈值" />
        <el-table-column prop="currentValue" label="当前值" />
        <el-table-column prop="time" label="告警时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleViewDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="alertPagination.currentPage"
          v-model:page-size="alertPagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="alertPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleAlertSizeChange"
          @current-change="handleAlertCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 告警详情对话框 -->
    <el-dialog v-model="alertDetailVisible" title="告警详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="告警ID">{{ alertDetail.id }}</el-descriptions-item>
        <el-descriptions-item label="告警级别">
          <el-tag :type="getAlertLevelType(alertDetail.level)">
            {{ getAlertLevelText(alertDetail.level) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="资源名称">{{ alertDetail.resource }}</el-descriptions-item>
        <el-descriptions-item label="监控指标">{{ alertDetail.metric }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ alertDetail.threshold }}</el-descriptions-item>
        <el-descriptions-item label="当前值">{{ alertDetail.currentValue }}</el-descriptions-item>
        <el-descriptions-item label="告警时间">{{ alertDetail.time }}</el-descriptions-item>
        <el-descriptions-item label="告警描述">{{ alertDetail.description }}</el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleAcknowledge">确认告警</el-button>
          <el-button type="primary" @click="handleProcess">处理告警</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Cpu, 
  Box, 
  Connection, 
  Bell 
} from '@element-plus/icons-vue'

// 定义数据类型
interface AlertItem {
  id: string
  level: string
  resource: string
  metric: string
  threshold: string
  currentValue: string
  time: string
  description: string
}

// 指标数据
const cpuUsage = ref(65)
const memoryUsage = ref(42)
const networkTraffic = ref(12.5)
const alertCount = ref(3)

// 告警数据
const alertData = ref<AlertItem[]>([])

// 告警分页
const alertPagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 告警详情对话框可见性
const alertDetailVisible = ref(false)

// 告警详情
const alertDetail = ref<AlertItem>({
  id: '',
  level: '',
  resource: '',
  metric: '',
  threshold: '',
  currentValue: '',
  time: '',
  description: ''
})

// 获取告警级别标签类型
const getAlertLevelType = (level: string) => {
  switch (level) {
    case 'critical':
      return 'danger'
    case 'warning':
      return 'warning'
    case 'info':
      return 'info'
    default:
      return ''
  }
}

// 获取告警级别文本
const getAlertLevelText = (level: string) => {
  switch (level) {
    case 'critical':
      return '严重'
    case 'warning':
      return '警告'
    case 'info':
      return '提醒'
    default:
      return level
  }
}

// 模拟获取告警数据
const fetchAlertData = () => {
  // 模拟API调用
  setTimeout(() => {
    alertData.value = [
      {
        id: 'AL-20230515-001',
        level: 'critical',
        resource: 'web-server-01',
        metric: 'CPU使用率',
        threshold: '> 80%',
        currentValue: '85%',
        time: '2023-05-15 14:30:22',
        description: 'CPU使用率超过阈值，可能导致服务响应缓慢'
      },
      {
        id: 'AL-20230515-002',
        level: 'warning',
        resource: 'database-master',
        metric: '内存使用率',
        threshold: '> 75%',
        currentValue: '78%',
        time: '2023-05-15 14:25:10',
        description: '内存使用率较高，请关注系统性能'
      },
      {
        id: 'AL-20230515-003',
        level: 'info',
        resource: 'cache-node-01',
        metric: '网络延迟',
        threshold: '> 100ms',
        currentValue: '120ms',
        time: '2023-05-15 14:15:45',
        description: '网络延迟略有增加，目前不影响服务'
      }
    ]
    
    alertPagination.total = alertData.value.length
  }, 300)
}

// 处理刷新
const handleRefresh = () => {
  fetchAlertData()
  ElMessage.success('刷新成功')
}

// 处理查看告警详情
const handleViewDetail = (row: AlertItem) => {
  alertDetail.value = { ...row }
  alertDetailVisible.value = true
}

// 处理确认告警
const handleAcknowledge = () => {
  ElMessage.info('告警已确认')
  alertDetailVisible.value = false
}

// 处理处理告警
const handleProcess = () => {
  ElMessage.success('告警处理完成')
  alertDetailVisible.value = false
}

// 处理告警分页大小变化
const handleAlertSizeChange = (val: number) => {
  alertPagination.pageSize = val
  fetchAlertData()
}

// 处理告警当前页变化
const handleAlertCurrentChange = (val: number) => {
  alertPagination.currentPage = val
  fetchAlertData()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchAlertData()
})
</script>

<style scoped>
.monitoring-container {
  padding: 20px;
}

.metrics-row {
  margin-bottom: 20px;
}

.metric-card {
  height: 180px;
}

.metric-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.metric-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.metric-icon el-icon {
  font-size: 24px;
  color: white;
}

.cpu-bg {
  background-color: #409eff;
}

.memory-bg {
  background-color: #67c23a;
}

.network-bg {
  background-color: #e6a23c;
}

.alert-bg {
  background-color: #f56c6c;
}

.metric-info {
  flex: 1;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.metric-label {
  font-size: 14px;
  color: #999;
}

.metric-chart {
  height: 80px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #999;
  font-size: 14px;
}

.alert-list-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>