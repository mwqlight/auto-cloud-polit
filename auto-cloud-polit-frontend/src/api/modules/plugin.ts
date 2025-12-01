import request from '../index'

// 获取所有插件组件
export const getPlugins = () => {
  return request({
    url: '/api/plugins',
    method: 'get'
  })
}

// 获取指定组件支持的操作
export const getComponentOperations = (componentId: string) => {
  return request({
    url: `/api/plugins/${componentId}/operations`,
    method: 'get'
  })
}

// 执行组件操作
export const executeComponentOperation = (componentId: string, operationId: string, params: any) => {
  return request({
    url: `/api/plugins/${componentId}/execute/${operationId}`,
    method: 'post',
    data: params
  })
}

// 获取组件仪表盘部件
export const getComponentDashboardWidgets = (componentId: string) => {
  return request({
    url: `/api/plugins/${componentId}/dashboard`,
    method: 'get'
  })
}