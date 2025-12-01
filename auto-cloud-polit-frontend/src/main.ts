import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useUserStore } from './store'

// 引入全局样式
import './styles/index.css'

// 创建应用实例
const app = createApp(App)

// 使用Pinia状态管理
const pinia = createPinia()
app.use(pinia)

// 使用路由
app.use(router)

// 初始化用户状态
const userStore = useUserStore()
userStore.initializeUser()

// 挂载应用
app.mount('#app')