import Vue from 'vue'
import VueRouter from 'vue-router'
import { getToken, getRole } from '@/utils/auth'
import store from '@/store'

Vue.use(VueRouter)

/* 路由配置 */
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index'),
    meta: { title: '登录' },
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: () => import('@/views/layout/index'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '首页', icon: 'el-icon-s-home' }
      }
    ]
  }
]

// 权限路由
export const asyncRoutes = [
  // 学生管理
  {
    path: '/student',
    component: () => import('@/views/layout/index'),
    redirect: '/student/list',
    meta: { title: '学生管理', icon: 'el-icon-user', roles: ['1'] }, // 只有管理员可见
    children: [
      {
        path: 'list',
        name: 'StudentList',
        component: () => import('@/views/student/list'),
        meta: { title: '学生列表', icon: 'el-icon-user' }
      },
      {
        path: 'create',
        name: 'StudentCreate',
        component: () => import('@/views/student/create'),
        meta: { title: '添加学生', icon: 'el-icon-plus' }
      },
      {
        path: 'edit/:id',
        name: 'StudentEdit',
        component: () => import('@/views/student/edit'),
        meta: { title: '编辑学生', activeMenu: '/student/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'StudentDetail',
        component: () => import('@/views/student/detail'),
        meta: { title: '学生详情', activeMenu: '/student/list' },
        hidden: true
      }
    ]
  },

  // 教师管理
  {
    path: '/teacher',
    component: () => import('@/views/layout/index'),
    redirect: '/teacher/list',
    meta: { title: '教师管理', icon: 'el-icon-s-custom', roles: ['1'] }, // 只有管理员可见
    children: [
      {
        path: 'list',
        name: 'TeacherList',
        component: () => import('@/views/teacher/list'),
        meta: { title: '教师列表', icon: 'el-icon-s-custom' }
      },
      {
        path: 'create',
        name: 'TeacherCreate',
        component: () => import('@/views/teacher/create'),
        meta: { title: '添加教师', icon: 'el-icon-plus' }
      },
      {
        path: 'edit/:id',
        name: 'TeacherEdit',
        component: () => import('@/views/teacher/edit'),
        meta: { title: '编辑教师', activeMenu: '/teacher/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'TeacherDetail',
        component: () => import('@/views/teacher/detail'),
        meta: { title: '教师详情', activeMenu: '/teacher/list' },
        hidden: true
      }
    ]
  },

  // 课程管理
  {
    path: '/course',
    component: () => import('@/views/layout/index'),
    redirect: '/course/list',
    meta: { title: '课程管理', icon: 'el-icon-reading', roles: ['1'] }, // 只有管理员可见
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/course/list'),
        meta: { title: '课程列表', icon: 'el-icon-reading' }
      },
      {
        path: 'create',
        name: 'CourseCreate',
        component: () => import('@/views/course/create'),
        meta: { title: '添加课程', icon: 'el-icon-plus' }
      },
      {
        path: 'edit/:id',
        name: 'CourseEdit',
        component: () => import('@/views/course/edit'),
        meta: { title: '编辑课程', activeMenu: '/course/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/detail'),
        meta: { title: '课程详情', activeMenu: '/course/list' },
        hidden: true
      }
    ]
  },

  // 选课管理
  {
    path: '/course-selection',
    component: () => import('@/views/layout/index'),
    redirect: '/course-selection/list',
    meta: { title: '选课管理', icon: 'el-icon-notebook-1', roles: ['1', '2', '3'] }, // 所有角色可见
    children: [
      {
        path: 'list',
        name: 'CourseSelectionList',
        component: () => import('@/views/course-selection/list'),
        meta: { title: '选课列表', icon: 'el-icon-notebook-1' }
      },
      {
        path: 'create',
        name: 'CourseSelectionCreate',
        component: () => import('@/views/course-selection/create'),
        meta: { title: '添加选课', icon: 'el-icon-plus', roles: ['1'] } // 只有管理员可添加选课
      }
    ]
  },

  // 成绩管理
  {
    path: '/score',
    component: () => import('@/views/layout/index'),
    redirect: '/score/list',
    meta: { title: '成绩管理', icon: 'el-icon-s-data', roles: ['1', '2', '3'] }, // 所有角色可见
    children: [
      {
        path: 'list',
        name: 'ScoreList',
        component: () => import('@/views/score/list'),
        meta: { title: '成绩列表', icon: 'el-icon-s-data' }
      },
      {
        path: 'edit/:id',
        name: 'ScoreEdit',
        component: () => import('@/views/score/edit'),
        meta: { title: '编辑成绩', activeMenu: '/score/list', roles: ['2'] }, // 只有教师可编辑成绩
        hidden: true
      }
    ]
  },

  // 统计分析
  {
    path: '/statistics',
    component: () => import('@/views/layout/index'),
    redirect: '/statistics/student',
    meta: { title: '统计分析', icon: 'el-icon-pie-chart', roles: ['1', '2', '3'] }, // 所有角色可见
    children: [
      {
        path: 'student',
        name: 'StudentStatistics',
        component: () => import('@/views/statistics/student'),
        meta: { title: '学生成绩统计', icon: 'el-icon-s-data' }
      },
      {
        path: 'class',
        name: 'ClassStatistics',
        component: () => import('@/views/statistics/class'),
        meta: { title: '班级成绩统计', icon: 'el-icon-s-marketing' }
      },
      {
        path: 'course',
        name: 'CourseStatistics',
        component: () => import('@/views/statistics/course'),
        meta: { title: '课程成绩统计', icon: 'el-icon-s-help' }
      },
      {
        path: 'all',
        name: 'AllStatistics',
        component: () => import('@/views/statistics/all'),
        meta: { title: '全体学生统计', icon: 'el-icon-data-line', roles: ['1', '2'] } // 管理员和教师可见
      }
    ]
  },

  // 个人信息
  {
    path: '/profile',
    component: () => import('@/views/layout/index'),
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index'),
        meta: { title: '个人信息', icon: 'el-icon-user' }
      }
    ]
  },

  // 404页面必须放在最后
  { path: '*', redirect: '/404', hidden: true }
]

// 创建路由实例
const createRouter = () => new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL || '/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 判断是否有权限
function hasPermission(route, role) {
  if (route.meta && route.meta.roles) {
    return route.meta.roles.includes(role)
  } else {
    return true
  }
}

// 过滤异步路由
export function filterAsyncRoutes(routes, role) {
  const res = []

  // 管理员拥有所有权限
  if (role === '1') {
    return routes
  }

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(tmp, role)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, role)
      }
      res.push(tmp)
    }
  })

  return res
}

// 保存路由状态，避免重复添加
let asyncRoutesAdded = false

// 添加异步路由
export async function addAsyncRoutes() {
  const hasToken = getToken()
  const role = getRole()
  
  // 已有token和角色，但未添加过异步路由
  if (hasToken && role && !asyncRoutesAdded) {
    // 根据角色获取可访问路由
    const accessRoutes = filterAsyncRoutes(asyncRoutes, role)
    
    // 动态添加路由
    router.addRoutes(accessRoutes)
    
    // 标记已添加路由
    asyncRoutesAdded = true
    
    return accessRoutes
  }
  
  return []
}

// 初始化路由
addAsyncRoutes()

// 路由守卫
router.beforeEach(async (to, from, next) => {
  // 获取token
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登录，重定向到首页
      next({ path: '/' })
    } else {
      // 判断是否已经有角色
      const hasRoles = store.state.user && store.state.user.role
      
      if (hasRoles && asyncRoutesAdded) {
        // 如果有角色并已添加路由，直接放行
        next()
      } else {
        try {
          // 获取用户信息
          const { role } = await store.dispatch('user/getUserInfo')
          
          // 添加动态路由
          const accessRoutes = await addAsyncRoutes()
          
          // 确保路由完成
          next({ ...to, replace: true })
        } catch (error) {
          // 出错时，重置token并跳转到登录页
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    // 没有token
    if (to.path === '/login') {
      // 未登录状态访问登录页，放行
      next()
    } else {
      // 未登录状态访问其他页面，跳转到登录页
      next(`/login?redirect=${to.path}`)
    }
  }
})

// 重置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // 重置路由
  asyncRoutesAdded = false // 重置路由状态
}

export default router
