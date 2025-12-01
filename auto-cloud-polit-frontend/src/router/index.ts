import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { title: '驾驶舱' },
    redirect: '/dashboard/home',
    children: [
      {
        path: 'home',
        name: 'DashboardHome',
        component: () => import('@/views/dashboard/Home.vue'),
        meta: { title: '首页' }
      }
    ]
  },
  {
    path: '/plugins',
    name: 'Plugins',
    component: () => import('@/views/dashboard/PluginManager.vue'),
    meta: { title: '插件管理' }
  },
  {
    path: '/plugins/:id',
    name: 'PluginDetail',
    component: () => import('@/views/dashboard/PluginDetail.vue'),
    meta: { title: '插件详情' }
  },
  {
    path: '/compute',
    name: 'Compute',
    component: () => import('@/views/compute/index.vue'),
    meta: { title: '计算资源' }
  },
  {
    path: '/scaling-policy',
    name: 'ScalingPolicy',
    component: () => import('@/views/compute/ScalingPolicy.vue'),
    meta: { title: '弹性伸缩策略' }
  },
  {
    path: '/storage',
    name: 'Storage',
    component: () => import('@/views/storage/index.vue'),
    meta: { title: '存储资源' }
  },
  {
    path: '/network',
    name: 'Network',
    component: () => import('@/views/network/index.vue'),
    meta: { title: '网络资源' }
  },
  {
    path: '/security',
    name: 'Security',
    component: () => import('@/views/security/index.vue'),
    meta: { title: '安全合规' }
  },
  {
    path: '/monitoring',
    name: 'Monitoring',
    component: () => import('@/views/monitoring/index.vue'),
    meta: { title: '监控告警' }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/errors/NotFound.vue'),
    meta: { title: '页面未找到' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 云计算资源驾驶舱`
  }
  
  // 检查是否需要认证
  const isAuthenticated = !!localStorage.getItem('access_token')
  
  // 如果访问需要认证的页面但未登录，重定向到登录页
  if (to.name !== 'Login' && !isAuthenticated) {
    next({ name: 'Login' })
  } 
  // 如果已登录且访问登录页，重定向到首页
  else if (to.name === 'Login' && isAuthenticated) {
    next({ name: 'DashboardHome' })
  } 
  // 其他情况允许访问
  else {
    next()
  }
})

export default router