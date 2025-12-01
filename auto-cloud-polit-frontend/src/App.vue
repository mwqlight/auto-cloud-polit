<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
// 应用根组件
</script>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

/* Element Plus 样式覆盖 */
.el-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-card__header {
  font-weight: 600;
  border-bottom: 1px solid #eee;
}

/* 响应式断点 */
@media (max-width: 768px) {
  .el-aside {
    width: 60px !important;
  }
  
  .logo span {
    display: none;
  }
  
  .el-menu-item span,
  .el-sub-menu__title span {
    display: none;
  }
}
</style>