import { getStudentAverageScore, getClassAverageScore, getCourseAverageScore, getAllStudentsAverageScore } from '@/api/statistics'

const state = {
  studentAverage: null,
  classAverage: null,
  courseAverage: null,
  allStudentsAverage: null
}

const mutations = {
  SET_STUDENT_AVERAGE: (state, data) => {
    state.studentAverage = data
  },
  SET_CLASS_AVERAGE: (state, data) => {
    state.classAverage = data
  },
  SET_COURSE_AVERAGE: (state, data) => {
    state.courseAverage = data
  },
  SET_ALL_STUDENTS_AVERAGE: (state, data) => {
    state.allStudentsAverage = data
  }
}

const actions = {
  // 获取学生平均成绩
  getStudentAverageScore({ commit }, { studentId, params }) {
    return new Promise((resolve, reject) => {
      getStudentAverageScore(studentId, params)
        .then(response => {
          const { data } = response
          commit('SET_STUDENT_AVERAGE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取班级平均成绩
  getClassAverageScore({ commit }, { className, params }) {
    return new Promise((resolve, reject) => {
      getClassAverageScore(className, params)
        .then(response => {
          const { data } = response
          commit('SET_CLASS_AVERAGE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取课程平均成绩
  getCourseAverageScore({ commit }, { courseId, params }) {
    return new Promise((resolve, reject) => {
      getCourseAverageScore(courseId, params)
        .then(response => {
          const { data } = response
          commit('SET_COURSE_AVERAGE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取所有学生平均成绩
  getAllStudentsAverageScore({ commit }, params) {
    return new Promise((resolve, reject) => {
      getAllStudentsAverageScore(params)
        .then(response => {
          const { data } = response
          commit('SET_ALL_STUDENTS_AVERAGE', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 