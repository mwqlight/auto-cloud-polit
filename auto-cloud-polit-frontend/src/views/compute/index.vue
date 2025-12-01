<template>
  <div class="compute-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>计算资源管理</span>
          <el-button type="primary" @click="handleCreate">创建实例</el-button>
        </div>
      </template>
      
      <!-- 筛选区域 -->
      <div class="filter-section">
        <el-form :model="filterForm" label-width="80px" inline>
          <el-form-item label="实例名称">
            <el-input v-model="filterForm.name" placeholder="请输入实例名称" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="运行中" value="running" />
              <el-option label="已停止" value="stopped" />
              <el-option label="创建中" value="creating" />
              <el-option label="销毁中" value="destroying" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 表格区域 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="实例ID" width="100" />
        <el-table-column prop="name" label="实例名称" />
        <el-table-column prop="type" label="实例类型" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cpu" label="CPU" width="80" />
        <el-table-column prop="memory" label="内存(GB)" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(scope.row)"
              :disabled="scope.row.status === 'destroying'"
            >
              删除
            </el-button>
            <el-dropdown @command="handleCommand">
              <el-button size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{action: 'start', row: scope.row}" 
                    :disabled="scope.row.status !== 'stopped'">
                    启动
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'stop', row: scope.row}" 
                    :disabled="scope.row.status !== 'running'">
                    停止
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'restart', row: scope.row}"
                    :disabled="scope.row.status !== 'running'">
                    重启
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'scaling', row: scope.row}">
                    弹性伸缩
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 弹性伸缩策略对话框 -->
    <el-dialog v-model="scalingDialogVisible" title="资源弹性伸缩策略配置" width="600px">
      <el-form :model="scalingForm" label-width="120px">
        <el-form-item label="策略名称">
          <el-input v-model="scalingForm.policyName" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="触发条件">
          <el-select v-model="scalingForm.triggerCondition" placeholder="请选择触发条件">
            <el-option label="CPU使用率" value="cpu" />
            <el-option label="内存使用率" value="memory" />
            <el-option label="网络流量" value="network" />
          </el-select>
        </el-form-item>
        <el-form-item label="阈值(%)">
          <el-slider v-model="scalingForm.threshold" :min="1" :max="100" show-input />
        </el-form-item>
        <el-form-item label="扩容动作">
          <el-radio-group v-model="scalingForm.scalingAction">
            <el-radio label="scaleUp">增加实例</el-radio>
            <el-radio label="scaleDown">减少实例</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="实例数量">
          <el-input-number v-model="scalingForm.instanceCount" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="冷却时间(分钟)">
          <el-input-number v-model="scalingForm.cooldown" :min="1" :max="60" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="scalingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveScalingPolicy">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 定义数据类型
interface ComputeInstance {
  id: string
  name: string
  type: string
  status: string
  cpu: number
  memory: number
  createTime: string
}

// 筛选表单
const filterForm = reactive({
  name: '',
  status: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref<ComputeInstance[]>([])

// 加载状态
const loading = ref(false)

// 弹性伸缩对话框可见性
const scalingDialogVisible = ref(false)

// 弹性伸缩表单
const scalingForm = reactive({
  policyName: '',
  triggerCondition: 'cpu',
  threshold: 80,
  scalingAction: 'scaleUp',
  instanceCount: 1,
  cooldown: 5
})

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case 'running':
      return 'success'
    case 'stopped':
      return 'info'
    case 'creating':
    case 'destroying':
      return 'warning'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status: string) => {
  switch (status) {
    case 'running':
      return '运行中'
    case 'stopped':
      return '已停止'
    case 'creating':
      return '创建中'
    case 'destroying':
      return '销毁中'
    default:
      return status
  }
}

// 模拟获取数据
const fetchData = () => {
  loading.value = true
  
  // 模拟API调用
  setTimeout(() => {
    tableData.value = [
      {
        id: 'i-1234567890',
        name: 'web-server-01',
        type: 'ecs.c6.large',
        status: 'running',
        cpu: 2,
        memory: 4,
        createTime: '2023-05-15 10:30:00'
      },
      {
        id: 'i-0987654321',
        name: 'database-master',
        type: 'ecs.r6.xlarge',
        status: 'running',
        cpu: 4,
        memory: 16,
        createTime: '2023-05-14 14:20:00'
      },
      {
        id: 'i-1122334455',
        name: 'cache-node-01',
        type: 'ecs.c6.medium',
        status: 'stopped',
        cpu: 1,
        memory: 2,
        createTime: '2023-05-13 09:15:00'
      }
    ]
    
    pagination.total = tableData.value.length
    loading.value = false
  }, 500)
}

// 处理创建
const handleCreate = () => {
  ElMessage.info('创建实例功能待实现')
}

// 处理搜索
const handleSearch = () => {
  fetchData()
}

// 处理重置
const handleReset = () => {
  filterForm.name = ''
  filterForm.status = ''
  fetchData()
}

// 处理编辑
const handleEdit = (row: ComputeInstance) => {
  ElMessage.info(`编辑实例 ${row.name}`)
}

// 处理删除
const handleDelete = (row: ComputeInstance) => {
  ElMessageBox.confirm(
    `确定要删除实例 ${row.name} 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('删除成功')
    // 实际项目中这里应该调用API删除实例
  }).catch(() => {
    // 用户取消删除
  })
}

// 处理命令（更多操作）
const handleCommand = (command: any) => {
  const { action, row } = command
  
  switch (action) {
    case 'start':
      ElMessage.info(`启动实例 ${row.name}`)
      break
    case 'stop':
      ElMessage.info(`停止实例 ${row.name}`)
      break
    case 'restart':
      ElMessage.info(`重启实例 ${row.name}`)
      break
    case 'scaling':
      // 显示弹性伸缩策略配置对话框
      scalingDialogVisible.value = true
      break
  }
}

// 处理保存弹性伸缩策略
const handleSaveScalingPolicy = () => {
  ElMessage.success('弹性伸缩策略保存成功')
  scalingDialogVisible.value = false
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  fetchData()
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  fetchData()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.compute-container {
  padding: 20px;
}

.main-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>