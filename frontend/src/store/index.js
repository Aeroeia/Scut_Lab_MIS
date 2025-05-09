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
    // 用户信息
    userInfo: state => state.user.userInfo
  }
})
