<template>
  <div v-if="!item.hidden">
    <!-- 无子菜单或只有一个显示的子菜单 -->
    <template v-if="hasOneShowingChild(item.children, item)">
      <app-link v-if="onlyOneChild.meta && hasPermission(onlyOneChild)" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" @click="handleMenuItemClick">
          <i v-if="onlyOneChild.meta && onlyOneChild.meta.icon" :class="onlyOneChild.meta.icon"></i>
          <span slot="title">{{ onlyOneChild.meta.title }}</span>
        </el-menu-item>
      </app-link>
    </template>

    <!-- 有子菜单，且显示多级 -->
    <el-submenu v-else ref="submenu" :index="resolvePath(item.path)">
      <template slot="title">
        <i v-if="item.meta && item.meta.icon" :class="item.meta.icon"></i>
        <span slot="title">{{ item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in filterChildren(item.children)"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import AppLink from './Link'
import { mapGetters } from 'vuex'

export default {
  name: 'SidebarItem',
  components: { AppLink },
  props: {
    // 路由对象
    item: {
      type: Object,
      required: true
    },
    // 基础路径
    basePath: {
      type: String,
      default: ''
    },
    // 是否是嵌套菜单
    isNest: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters(['userRole'])
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    // 检查是否有权限访问菜单项
    hasPermission(route) {
      if (route.meta && route.meta.roles) {
        return route.meta.roles.includes(this.userRole)
      } else {
        return true
      }
    },
    
    // 过滤子菜单项，只显示有权限的
    filterChildren(children) {
      if (!children) return []
      return children.filter(child => {
        if (child.hidden) return false
        return this.hasPermission(child)
      })
    },
    
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = []
      }
      
      // 过滤掉隐藏的子菜单和没有权限的子菜单
      const showingChildren = children.filter(item => {
        if (item.hidden || !this.hasPermission(item)) {
          return false
        } else {
          // 记录唯一的子路由
          this.onlyOneChild = item
          return true
        }
      })

      // 当只有一个子路由时，显示这个子路由
      if (showingChildren.length === 1) {
        return true
      }

      // 如果没有要显示的子路由，且父级路由设置了meta.title，则显示父级路由
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      
      return path.resolve(this.basePath, routePath)
    },
    
    handleMenuItemClick(e) {
      const fullPath = this.resolvePath(this.onlyOneChild.path);
      if (fullPath.includes('/student/create') || fullPath.includes('/course-selection/create')) {
        // 阻止默认行为，让Vue Router自己处理
        e.preventDefault();
      }
    }
  }
}
</script>

<style scoped>
.nest-menu .el-menu-item {
  padding-left: 50px !important;
}

/* 确保移除所有菜单项的边框和线条 */
.el-menu-item, 
.el-submenu__title, 
.nest-menu .el-menu-item {
  border: none !important;
  border-bottom: none !important;
  border-top: none !important;
  border-left: none !important;
  border-right: none !important;
  text-decoration: none !important;
  outline: none !important;
}

/* 确保菜单图标正常显示 */
.el-menu-item i,
.el-submenu__title i {
  margin-right: 5px;
  width: 24px;
  text-align: center;
  font-size: 18px;
  display: inline-block !important;
  vertical-align: middle;
}

/* 确保菜单项在不同状态下不会有边框和线条 */
.el-menu-item:focus, 
.el-menu-item:hover, 
.el-menu-item:active,
.el-menu-item.is-active,
.el-submenu__title:focus,
.el-submenu__title:hover,
.el-submenu__title:active,
.el-submenu.is-active .el-submenu__title {
  border: none !important;
  border-bottom: none !important;
  text-decoration: none !important;
  outline: none !important;
}
</style> 