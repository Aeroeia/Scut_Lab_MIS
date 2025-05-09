<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="index === levelList.length - 1" class="no-redirect">{{ item.meta.title }}</span>
        <router-link v-else :to="item.redirect || item.path">{{ item.meta.title }}</router-link>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
export default {
  name: 'Breadcrumb',
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route() {
      // 路由变化时更新导航
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      
      // 如果没有匹配到首页，就手动添加
      const first = matched[0]
      if (first && first.path !== '/dashboard') {
        matched = [{ path: '/dashboard', meta: { title: '首页' } }].concat(matched)
      }
      
      this.levelList = matched
    }
  }
}
</script>

<style scoped>
.app-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 60px;
  margin-left: 20px;
}

.app-breadcrumb .no-redirect {
  color: #97a8be;
  cursor: text;
}

/* 过渡动画 */
.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all .5s;
}

.breadcrumb-enter,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style> 