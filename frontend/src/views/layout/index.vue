<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="sidebar-logo">
        <span class="logo-title">华南理工大学计算机学院</span>
      </div>
      
      <!-- 菜单 -->
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          :default-active="activeMenu"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :active-text-color="variables.menuActiveText"
          :unique-opened="false"
          :collapse-transition="false"
          mode="vertical"
        >
          <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu>
      </el-scrollbar>
    </div>

    <!-- 主容器 -->
    <div class="main-container" :style="{ marginLeft: '210px' }">
      <!-- 顶部导航 -->
      <div class="navbar">
        <breadcrumb class="app-breadcrumb" />
        <div class="right-menu">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link right-menu-item">
              {{ userRole }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item icon="el-icon-user" @click.native="toProfile">个人信息</el-dropdown-item>
              <el-dropdown-item icon="el-icon-switch-button" @click.native="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <!-- 主内容区 -->
      <div class="container">
        <router-view :key="key" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import SidebarItem from './components/SidebarItem'
import Breadcrumb from './components/Breadcrumb'
import { constantRoutes, asyncRoutes } from '@/router'
import { getRole, getRoleName } from '@/utils/auth'

export default {
  name: 'Layout',
  components: {
    SidebarItem,
    Breadcrumb
  },
  data() {
    return {
      variables: {
        menuBg: '#304156',
        menuText: '#bfcbd9',
        menuActiveText: '#409EFF'
      }
    }
  },
  computed: {
    ...mapState('user', [
      'role'
    ]),
    routes() {
      // 根据用户角色筛选路由
      const userRole = getRole()
      if (!userRole) return constantRoutes
      
      const filteredRoutes = asyncRoutes.filter(route => {
        if (route.meta && route.meta.roles) {
          return route.meta.roles.includes(userRole)
        }
        return true
      })
      
      return [...constantRoutes, ...filteredRoutes]
    },
    activeMenu() {
      const { meta, path } = this.$route
      if (meta && meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    key() {
      return this.$route.path
    },
    userRole() {
      return getRoleName() || '未知角色'
    }
  },
  methods: {
    ...mapActions('user', ['logout']),
    async handleLogout() {
      try {
        await this.logout()
        this.$router.push('/login')
      } catch (error) {
        console.error('Logout failed:', error)
      }
    },
    toProfile() {
      this.$router.push('/profile/index')
    }
  }
}
</script>

<style scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}

.sidebar-container {
  transition: width 0.3s;
  width: 210px;
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
}

.sidebar-logo {
  height: 60px;
  line-height: 60px;
  background: #2b2f3a;
  text-align: center;
}

.sidebar-logo .logo-title {
  display: inline-block;
  color: #fff;
  font-weight: 600;
  line-height: 60px;
  font-size: 14px;
  font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
  vertical-align: middle;
}

.main-container {
  min-height: 100%;
  transition: margin-left 0.3s;
}

.scrollbar-wrapper {
  overflow-x: hidden !important;
}

.container {
  padding: 20px;
}

.navbar {
  height: 60px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.right-menu {
  float: right;
  height: 100%;
  line-height: 60px;
  margin-right: 15px;
}

.right-menu-item {
  color: #606266;
  cursor: pointer;
}
</style> 