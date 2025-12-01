import { defineStore } from 'pinia'
import { getUserInfo, isAuthenticated, logout as authLogout } from '@/utils/auth'

// 定义用户状态接口
interface UserState {
  isLoggedIn: boolean
  userInfo: any | null
}

// 创建用户store
export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    isLoggedIn: isAuthenticated(),
    userInfo: null
  }),

  getters: {
    // 获取用户信息
    getUserInfo(state): any | null {
      return state.userInfo
    },

    // 检查是否已登录
    getIsLoggedIn(state): boolean {
      return state.isLoggedIn
    }
  },

  actions: {
    // 设置登录状态
    setLoginStatus(status: boolean) {
      this.isLoggedIn = status
    },

    // 设置用户信息
    setUserInfo(userInfo: any) {
      this.userInfo = userInfo
    },

    // 初始化用户信息
    async initializeUser() {
      if (isAuthenticated()) {
        try {
          const userInfo = getUserInfo()
          this.setUserInfo(userInfo)
          this.setLoginStatus(true)
        } catch (error) {
          console.error('Failed to initialize user:', error)
          this.logout()
        }
      }
    },

    // 登出
    logout() {
      authLogout()
      this.setLoginStatus(false)
      this.setUserInfo(null)
    }
  }
})