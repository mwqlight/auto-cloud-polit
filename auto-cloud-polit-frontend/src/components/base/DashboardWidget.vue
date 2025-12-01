<template>
  <div class="dashboard-widget" :class="`widget-${type.toLowerCase()}`">
    <div class="widget-header">
      <h3>{{ title }}</h3>
      <div class="widget-actions">
        <el-dropdown v-if="actions && actions.length > 0">
          <el-button size="small" type="primary">
            操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item 
                v-for="action in actions" 
                :key="action.id"
                @click="handleAction(action)"
              >
                {{ action.name }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="widget-content">
      <slot>
        <div v-if="data !== null">
          <div class="widget-data">
            <div class="data-value">{{ formattedData }}</div>
            <div class="data-description" v-if="description">{{ description }}</div>
          </div>
        </div>
        <div v-else-if="loading" class="widget-loading">
          <el-skeleton animated />
        </div>
        <div v-else class="widget-empty">
          <el-empty description="暂无数据" />
        </div>
      </slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue'

interface WidgetAction {
  id: string
  name: string
}

interface Props {
  title: string
  type: string
  data?: any
  description?: string
  loading?: boolean
  actions?: WidgetAction[]
}

const props = withDefaults(defineProps<Props>(), {
  data: null,
  description: '',
  loading: false,
  actions: () => []
})

const emit = defineEmits<{
  (e: 'action', action: WidgetAction): void
}>()

const formattedData = computed(() => {
  if (typeof props.data === 'number') {
    // 如果是数字，根据大小格式化
    if (props.data >= 1000000) {
      return (props.data / 1000000).toFixed(2) + 'M'
    } else if (props.data >= 1000) {
      return (props.data / 1000).toFixed(2) + 'K'
    }
    return props.data.toString()
  }
  return props.data
})

const handleAction = (action: WidgetAction) => {
  emit('action', action)
}
</script>

<style scoped>
.dashboard-widget {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: box-shadow 0.3s;
}

.dashboard-widget:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.widget-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.widget-content {
  min-height: 100px;
}

.widget-data {
  text-align: center;
}

.data-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.data-description {
  font-size: 12px;
  color: #909399;
}

.widget-loading {
  padding: 20px 0;
}

.widget-empty {
  padding: 20px 0;
}

/* 不同类型的部件样式 */
.widget-compute {
  border-top: 4px solid #409eff;
}

.widget-storage {
  border-top: 4px solid #67c23a;
}

.widget-network {
  border-top: 4px solid #e6a23c;
}

.widget-security {
  border-top: 4px solid #f56c6c;
}

.widget-monitoring {
  border-top: 4px solid #909399;
}
</style>