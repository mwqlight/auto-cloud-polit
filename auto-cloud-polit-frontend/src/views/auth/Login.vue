<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>云计算资源驾驶舱</h1>
        <p>一站式云资源管理平台</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="用户名" 
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="密码" 
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            class="login-button" 
            :loading="loading"
            @click="handleLogin"
            native-type="submit"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2023 云计算资源驾驶舱. 保留所有权利.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { setAccessToken } from '@/utils/auth'
import { useUserStore } from '@/store'

const router = useRouter()
const loginFormRef = ref<FormInstance>()

// 登录表单数据
const loginForm = reactive({
  username: 'admin',
  password: 'admin123',
  remember: true
})

// 登录状态
const loading = ref(false)

// 表单验证规则
const loginRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ]
})

// 处理登录
const handleLogin = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      loading.value = true
      // 模拟登录请求
      setTimeout(() => {
        // 模拟登录成功，保存token到localStorage
        const fakeToken = 'fake-jwt-token'
        setAccessToken(fakeToken)
        
        // 更新用户状态
        const userStore = useUserStore()
        userStore.setLoginStatus(true)
        userStore.setUserInfo({
          id: 1,
          username: loginForm.username,
          name: '管理员',
          avatar: '',
          roles: ['admin']
        })
        
        ElMessage.success('登录成功')
        // 跳转到首页
        router.push('/dashboard/home')
        loading.value = false
      }, 1000)
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.login-form {
  margin-bottom: 30px;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  color: #999;
  font-size: 12px;
}
</style>