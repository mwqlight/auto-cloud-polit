// 认证相关工具函数

// 获取访问令牌
export const getAccessToken = (): string | null => {
  return localStorage.getItem('access_token')
}

// 设置访问令牌
export const setAccessToken = (token: string): void => {
  localStorage.setItem('access_token', token)
}

// 移除访问令牌
export const removeAccessToken = (): void => {
  localStorage.removeItem('access_token')
}

// 检查是否已认证
export const isAuthenticated = (): boolean => {
  return !!getAccessToken()
}

// 登出
export const logout = (): void => {
  removeAccessToken()
  // 可以在这里添加其他登出逻辑，如清除用户信息等
}

// 获取用户信息（模拟）
export const getUserInfo = (): any => {
  // 这里应该从JWT token中解析用户信息或调用API获取
  return {
    id: 1,
    username: 'admin',
    name: '管理员',
    avatar: '',
    roles: ['admin']
  }
}