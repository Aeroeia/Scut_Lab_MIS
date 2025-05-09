<template>
  <div class="not-found">
    <div class="container">
      <h1>404</h1>
      <h2>页面未找到</h2>
      <p>抱歉，您访问的页面不存在或已被删除。</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: '404',
  methods: {
    goHome() {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.container {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

h1 {
  font-size: 80px;
  margin: 0;
  color: #1890ff;
}

h2 {
  margin-top: 10px;
  margin-bottom: 20px;
  color: #303133;
}

p {
  margin-bottom: 30px;
  color: #606266;
}
</style> 