import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import student from './modules/student'
import teacher from './modules/teacher'
import course from './modules/course'
import courseSelection from './modules/course-selection'
import score from './modules/score'
import statistics from './modules/statistics'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user,
    student,
    teacher,
    course,
    courseSelection,
    score,
    statistics
  },
  getters: {
    // 用户信息，包含username、realId、role、roleName等
    userInfo: state => state.user.userInfo,
    // 用户角色 (1: 管理员, 2: 教师, 3: 学生)
    userRole: state => state.user.role,
    // 用户真实ID (学号/工号)
    userRealId: state => state.user.realId,
    // 用户角色名称 (Administrator, Teacher, Student)
    userRoleName: state => state.user.roleName
  }
})
