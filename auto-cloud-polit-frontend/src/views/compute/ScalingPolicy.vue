<template>
  <div class="scaling-policy-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>资源弹性伸缩策略配置</span>
          <el-button type="primary" @click="handleCreatePolicy">创建策略</el-button>
        </div>
      </template>
      
      <!-- 筛选区域 -->
      <div class="filter-section">
        <el-form :model="filterForm" label-width="80px" inline>
          <el-form-item label="策略名称">
            <el-input v-model="filterForm.name" placeholder="请输入策略名称" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="启用" value="enabled" />
              <el-option label="禁用" value="disabled" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 策略表格 -->
      <el-table :data="policyData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="策略名称" />
        <el-table-column prop="resourceType" label="资源类型" width="120" />
        <el-table-column prop="triggerMetric" label="触发指标" width="120" />
        <el-table-column prop="threshold" label="阈值" width="100" />
        <el-table-column prop="action" label="扩容动作" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.action === 'scaleUp' ? 'success' : 'warning'">
              {{ scope.row.action === 'scaleUp' ? '扩容' : '缩容' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="instanceCount" label="实例数量" width="100" />
        <el-table-column prop="cooldown" label="冷却时间(分钟)" width="130" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="enabled"
              inactive-value="disabled"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button size="small" @click="handleViewHistory(scope.row)">历史</el-button>
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
    
    <!-- 策略配置对话框 -->
    <el-dialog v-model="policyDialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="policyForm" :rules="policyRules" ref="policyFormRef" label-width="120px">
        <el-form-item label="策略名称" prop="name">
          <el-input v-model="policyForm.name" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="policyForm.resourceType" placeholder="请选择资源类型" style="width: 100%;">
            <el-option label="计算实例" value="compute" />
            <el-option label="存储资源" value="storage" />
            <el-option label="网络资源" value="network" />
          </el-select>
        </el-form-item>
        <el-form-item label="触发指标" prop="triggerMetric">
          <el-select v-model="policyForm.triggerMetric" placeholder="请选择触发指标" style="width: 100%;">
            <el-option label="CPU使用率" value="cpu" />
            <el-option label="内存使用率" value="memory" />
            <el-option label="网络流量" value="network" />
            <el-option label="磁盘IO" value="diskio" />
          </el-select>
        </el-form-item>
        <el-form-item label="阈值(%)" prop="threshold">
          <el-slider v-model="policyForm.threshold" :min="1" :max="100" show-input />
        </el-form-item>
        <el-form-item label="扩容动作" prop="action">
          <el-radio-group v-model="policyForm.action">
            <el-radio label="scaleUp">扩容</el-radio>
            <el-radio label="scaleDown">缩容</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="实例数量" prop="instanceCount">
          <el-input-number v-model="policyForm.instanceCount" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="冷却时间(分钟)" prop="cooldown">
          <el-input-number v-model="policyForm.cooldown" :min="1" :max="60" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="policyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePolicy">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 定义数据类型
interface ScalingPolicy {
  id?: string
  name: string
  resourceType: string
  triggerMetric: string
  threshold: number
  action: string
  instanceCount: number
  cooldown: number
  status: string
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

// 策略数据
const policyData = ref<ScalingPolicy[]>([])

// 加载状态
const loading = ref(false)

// 策略对话框可见性
const policyDialogVisible = ref(false)

// 对话框标题
const dialogTitle = computed(() => {
  return policyForm.id ? '编辑策略' : '创建策略'
})

// 策略表单
const policyForm = reactive<ScalingPolicy>({
  id: '',
  name: '',
  resourceType: 'compute',
  triggerMetric: 'cpu',
  threshold: 80,
  action: 'scaleUp',
  instanceCount: 1,
  cooldown: 5,
  status: 'enabled'
})

// 策略表单引用
const policyFormRef = ref<FormInstance>()

// 策略表单验证规则
const policyRules = reactive<FormRules>({
  name: [{ required: true, message: '请输入策略名称', trigger: 'blur' }],
  resourceType: [{ required: true, message: '请选择资源类型', trigger: 'change' }],
  triggerMetric: [{ required: true, message: '请选择触发指标', trigger: 'change' }],
  threshold: [{ required: true, message: '请设置阈值', trigger: 'change' }],
  action: [{ required: true, message: '请选择扩容动作', trigger: 'change' }],
  instanceCount: [{ required: true, message: '请设置实例数量', trigger: 'change' }],
  cooldown: [{ required: true, message: '请设置冷却时间', trigger: 'change' }]
})

// 模拟获取策略数据
const fetchPolicyData = () => {
  loading.value = true
  
  // 模拟API调用
  setTimeout(() => {
    policyData.value = [
      {
        id: 'sp-1234567890',
        name: 'Web服务器CPU扩容策略',
        resourceType: 'compute',
        triggerMetric: 'cpu',
        threshold: 80,
        action: 'scaleUp',
        instanceCount: 2,
        cooldown: 10,
        status: 'enabled'
      },
      {
        id: 'sp-0987654321',
        name: '数据库内存缩容策略',
        resourceType: 'compute',
        triggerMetric: 'memory',
        threshold: 30,
        action: 'scaleDown',
        instanceCount: 1,
        cooldown: 15,
        status: 'disabled'
      }
    ]
    
    pagination.total = policyData.value.length
    loading.value = false
  }, 500)
}

// 处理创建策略
const handleCreatePolicy = () => {
  // 重置表单
  Object.assign(policyForm, {
    id: '',
    name: '',
    resourceType: 'compute',
    triggerMetric: 'cpu',
    threshold: 80,
    action: 'scaleUp',
    instanceCount: 1,
    cooldown: 5,
    status: 'enabled'
  })
  
  policyDialogVisible.value = true
}

// 处理编辑策略
const handleEdit = (row: ScalingPolicy) => {
  Object.assign(policyForm, row)
  policyDialogVisible.value = true
}

// 处理删除策略
const handleDelete = (row: ScalingPolicy) => {
  ElMessageBox.confirm(
    `确定要删除策略 "${row.name}" 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('删除成功')
    // 实际项目中这里应该调用API删除策略
  }).catch(() => {
    // 用户取消删除
  })
}

// 处理状态变更
const handleStatusChange = (row: ScalingPolicy) => {
  ElMessage.success(`策略"${row.name}"已${row.status === 'enabled' ? '启用' : '禁用'}`)
  // 实际项目中这里应该调用API更新策略状态
}

// 处理查看历史
const handleViewHistory = (row: ScalingPolicy) => {
  ElMessage.info(`查看策略"${row.name}"的历史记录`)
}

// 处理保存策略
const handleSavePolicy = async () => {
  if (!policyFormRef.value) return
  
  await policyFormRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('策略保存成功')
      policyDialogVisible.value = false
      // 实际项目中这里应该调用API保存策略
    }
  })
}

// 处理搜索
const handleSearch = () => {
  fetchPolicyData()
}

// 处理重置
const handleReset = () => {
  filterForm.name = ''
  filterForm.status = ''
  fetchPolicyData()
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pagination.pageSize = val
  fetchPolicyData()
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  pagination.currentPage = val
  fetchPolicyData()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchPolicyData()
})
</script>

<style scoped>
.scaling-policy-container {
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