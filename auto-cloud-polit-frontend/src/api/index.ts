import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 在发送请求之前做些什么
    // 可以添加认证token等
    const token = localStorage.getItem('access_token')
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 对响应数据做点什么
    const res = response.data
    
    // 如果返回的是文件流，直接返回response
    if (response.request.responseType === 'blob') {
      return response
    }
    
    // 根据code判断是否成功
    if (res.code !== 200) {
      // 处理错误状态码
      switch (res.code) {
        case 401:
          // 未认证，跳转到登录页
          localStorage.removeItem('access_token')
          window.location.href = '/login'
          break
        case 403:
          console.error('权限不足')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error(res.message || '请求失败')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  (error) => {
    // 对响应错误做点什么
    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

// 统一请求封装
const request = async <T = any>(config: AxiosRequestConfig): Promise<T> => {
  try {
    const response: AxiosResponse = await service(config)
    return response.data
  } catch (error) {
    throw error
  }
}

export default request