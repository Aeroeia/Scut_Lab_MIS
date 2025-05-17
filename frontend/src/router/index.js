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
    meta: { title: 'Login' },
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
        meta: { title: 'Dashboard', icon: 'el-icon-s-home' }
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
    meta: { title: 'Student Manage', icon: 'el-icon-user', roles: ['1', '2'] }, // 管理员和角色2可见
    children: [
      {
        path: 'list',
        name: 'StudentList',
        component: () => import('@/views/student/list'),
        meta: { title: 'Student List', icon: 'el-icon-user-solid' }
      },
      {
        path: 'create',
        name: 'StudentCreate',
        component: () => import('@/views/student/create'),
        meta: { title: 'Add Student', icon: 'el-icon-plus', roles: ['1'] } // 只有管理员可见
      },
      {
        path: 'edit/:id',
        name: 'StudentEdit',
        component: () => import('@/views/student/edit'),
        meta: { title: 'Edit Student', icon: 'el-icon-edit', activeMenu: '/student/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'StudentDetail',
        component: () => import('@/views/student/detail'),
        meta: { title: 'Student Details', icon: 'el-icon-view', activeMenu: '/student/list' },
        hidden: true
      }
    ]
  },

  // 教师管理
  {
    path: '/teacher',
    component: () => import('@/views/layout/index'),
    redirect: '/teacher/list',
    meta: { title: 'Teacher Manage', icon: 'el-icon-s-custom', roles: ['1'] }, // 只有管理员可见
    children: [
      {
        path: 'list',
        name: 'TeacherList',
        component: () => import('@/views/teacher/list'),
        meta: { title: 'Teacher List', icon: 'el-icon-s-check' }
      },
      {
        path: 'create',
        name: 'TeacherCreate',
        component: () => import('@/views/teacher/create'),
        meta: { title: 'Add Teacher', icon: 'el-icon-plus' }
      },
      {
        path: 'edit/:id',
        name: 'TeacherEdit',
        component: () => import('@/views/teacher/edit'),
        meta: { title: 'Edit Teacher', icon: 'el-icon-edit', activeMenu: '/teacher/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'TeacherDetail',
        component: () => import('@/views/teacher/detail'),
        meta: { title: 'Teacher Details', icon: 'el-icon-view', activeMenu: '/teacher/list' },
        hidden: true
      }
    ]
  },

  // 课程管理
  {
    path: '/course',
    component: () => import('@/views/layout/index'),
    redirect: '/course/list',
    meta: { title: 'Course Manage', icon: 'el-icon-reading', roles: ['1'] }, // 只有管理员可见
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/course/list'),
        meta: { title: 'Course List', icon: 'el-icon-notebook-2' }
      },
      {
        path: 'create',
        name: 'CourseCreate',
        component: () => import('@/views/course/create'),
        meta: { title: 'Add Course', icon: 'el-icon-plus' }
      },
      {
        path: 'edit/:id',
        name: 'CourseEdit',
        component: () => import('@/views/course/edit'),
        meta: { title: 'Edit Course', icon: 'el-icon-edit', activeMenu: '/course/list' },
        hidden: true
      },
      {
        path: 'detail/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/detail'),
        meta: { title: 'Course Details', icon: 'el-icon-view', activeMenu: '/course/list' },
        hidden: true
      }
    ]
  },

  // 选课管理
  {
    path: '/course-selection',
    component: () => import('@/views/layout/index'),
    redirect: '/course-selection/list',
    meta: { title: 'Course Selection', icon: 'el-icon-notebook-1', roles: ['1', '2', '3'] }, // 所有角色可见
    children: [
      {
        path: 'list',
        name: 'CourseSelectionList',
        component: () => import('@/views/course-selection/list'),
        meta: { title: 'Selection List', icon: 'el-icon-document' }
      },
      {
        path: 'create',
        name: 'CourseSelectionCreate',
        component: () => import('@/views/course-selection/create'),
        meta: { title: 'Add Selection', icon: 'el-icon-plus', roles: ['1'] } // 只有管理员可添加选课
      }
    ]
  },

  // 成绩管理
  {
    path: '/score',
    component: () => import('@/views/layout/index'),
    redirect: '/score/edit',
    meta: { title: 'Grade Manage', icon: 'el-icon-s-data', roles: ['2'] }, // 只有教师可见
    children: [
      {
        path: 'edit',
        name: 'ScoreEdit',
        component: () => import('@/views/score/edit'),
        meta: { title: 'Edit Grade', icon: 'el-icon-edit', roles: ['2'] }, // 只有教师可编辑成绩
        hidden: true
      }
    ]
  },

  // 统计分析
  {
    path: '/statistics',
    component: () => import('@/views/layout/index'),
    redirect: '/statistics/student',
    meta: { title: 'Statistics', icon: 'el-icon-pie-chart', roles: ['1', '2', '3'] }, // 所有角色可见
    children: [
      {
        path: 'student',
        name: 'StudentStatistics',
        component: () => import('@/views/statistics/student'),
        meta: { title: 'Student Statistics', icon: 'el-icon-s-data' }
      },
      {
        path: 'class',
        name: 'ClassStatistics',
        component: () => import('@/views/statistics/class'),
        meta: { title: 'Class Statistics', icon: 'el-icon-s-marketing' }
      },
      {
        path: 'course',
        name: 'CourseStatistics',
        component: () => import('@/views/statistics/course'),
        meta: { title: 'Course Statistics', icon: 'el-icon-s-help' }
      },
      {
        path: 'all',
        name: 'AllStatistics',
        component: () => import('@/views/statistics/all'),
        meta: { title: 'Overall Statistics', icon: 'el-icon-data-line', roles: ['1', '2'] } // 管理员和教师可见
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
        meta: { title: 'Profile', icon: 'el-icon-user' }
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

// Initialize router
addAsyncRoutes()

// Route guard
router.beforeEach(async (to, from, next) => {
  // 对于特定页面，防止发送多余后端请求
  if (to.path === '/student/create' || to.path === '/course-selection/create') {
    const hasToken = getToken()
    const hasRoles = store.state.user && store.state.user.role
    if (hasToken && hasRoles) {
      next()
      return
    }
  }

  // Get token
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // If already logged in, redirect to homepage
      next({ path: '/' })
    } else {
      // Check if user already has roles
      const hasRoles = store.state.user && store.state.user.role
      
      if (hasRoles && asyncRoutesAdded) {
        // If user has roles and routes are added, proceed
        next()
      } else {
        try {
          // Get user info
          await store.dispatch('user/getUserInfo')
          
          // Add dynamic routes
          await addAsyncRoutes()
          
          // Ensure routes are complete
          next({ ...to, replace: true })
        } catch (error) {
          // If error, reset token and redirect to login page
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    // No token
    if (to.path === '/login') {
      // If accessing login page without login, allow access
      next()
    } else {
      // If accessing other pages without login, redirect to login page
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
