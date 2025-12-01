import { ref, Ref } from 'vue'
import * as pluginApi from '@/api/modules/plugin'
import { CloudComponent, Operation, DashboardWidget } from '@/types/plugin'

// 插件组合式API
export function usePlugin() {
  // 插件组件列表
  const components: Ref<CloudComponent[]> = ref([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 获取所有插件组件
  const fetchComponents = async () => {
    try {
      loading.value = true
      error.value = null
      const response = await pluginApi.getPlugins()
      components.value = response.data || []
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to fetch plugins'
      console.error('Failed to fetch plugins:', err)
    } finally {
      loading.value = false
    }
  }
  
  // 获取组件操作列表
  const fetchComponentOperations = async (componentId: string) => {
    try {
      const response = await pluginApi.getComponentOperations(componentId)
      return response.data || []
    } catch (err) {
      console.error(`Failed to fetch operations for component ${componentId}:`, err)
      return []
    }
  }
  
  // 执行组件操作
  const executeOperation = async (componentId: string, operationId: string, params: any) => {
    try {
      const response = await pluginApi.executeComponentOperation(componentId, operationId, params)
      return response.data
    } catch (err) {
      console.error(`Failed to execute operation ${operationId} for component ${componentId}:`, err)
      throw err
    }
  }
  
  // 获取仪表盘部件
  const fetchDashboardWidgets = async (componentId: string) => {
    try {
      const response = await pluginApi.getComponentDashboardWidgets(componentId)
      return response.data || []
    } catch (err) {
      console.error(`Failed to fetch dashboard widgets for component ${componentId}:`, err)
      return []
    }
  }
  
  return {
    components,
    loading,
    error,
    fetchComponents,
    fetchComponentOperations,
    executeOperation,
    fetchDashboardWidgets
  }
}