// 云组件类型
export interface CloudComponent {
  id: string
  name: string
  type: ComponentType
}

// 组件类型枚举
export enum ComponentType {
  COMPUTE = 'COMPUTE',
  STORAGE = 'STORAGE',
  NETWORK = 'NETWORK',
  SECURITY = 'SECURITY',
  MONITORING = 'MONITORING'
}

// 操作类型
export interface Operation {
  id: string
  name: string
  description: string
}

// 仪表盘部件类型
export interface DashboardWidget {
  id: string
  name: string
  type: string
  config: any
}

// API响应类型
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  timestamp: number
}