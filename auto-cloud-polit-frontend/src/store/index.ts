import { defineStore } from 'pinia'
import { useUserStore } from './user'

// 全局状态管理
export const useGlobalStore = defineStore('global', {
  state: () => ({
    // 用户信息
    userInfo: {
      id: '',
      username: '',
      avatar: '',
      roles: [] as string[]
    },
    
    // 系统配置
    systemConfig: {
      theme: 'dark', // 主题模式
      language: 'zh-CN', // 语言
      sidebarCollapsed: false // 侧边栏是否折叠
    },
    
    // 加载状态
    loading: false,
    
    // 通知消息
    notifications: [] as Array<{
      id: string
      type: 'success' | 'warning' | 'error' | 'info'
      title: string
      message: string
      timestamp: number
    }>
  }),
  
  getters: {
    // 是否已登录
    isLoggedIn: (state) => !!state.userInfo.id,
    
    // 当前主题
    currentTheme: (state) => state.systemConfig.theme
  },
  
  actions: {
    // 设置用户信息
    setUserInfo(userInfo: any) {
      this.userInfo = { ...this.userInfo, ...userInfo }
    },
    
    // 清除用户信息
    clearUserInfo() {
      this.userInfo = {
        id: '',
        username: '',
        avatar: '',
        roles: []
      }
    },
    
    // 切换主题
    toggleTheme() {
      this.systemConfig.theme = this.systemConfig.theme === 'dark' ? 'light' : 'dark'
    },
    
    // 切换侧边栏状态
    toggleSidebar() {
      this.systemConfig.sidebarCollapsed = !this.systemConfig.sidebarCollapsed
    },
    
    // 显示加载状态
    showLoading() {
      this.loading = true
    },
    
    // 隐藏加载状态
    hideLoading() {
      this.loading = false
    },
    
    // 添加通知
    addNotification(notification: {
      type: 'success' | 'warning' | 'error' | 'info'
      title: string
      message: string
    }) {
      const newNotification = {
        id: Date.now().toString(),
        ...notification,
        timestamp: Date.now()
      }
      
      this.notifications.push(newNotification)
      
      // 5秒后自动移除通知
      setTimeout(() => {
        this.removeNotification(newNotification.id)
      }, 5000)
    },
    
    // 移除通知
    removeNotification(id: string) {
      const index = this.notifications.findIndex(n => n.id === id)
      if (index !== -1) {
        this.notifications.splice(index, 1)
      }
    }
  }
})