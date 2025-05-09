import { getCourses, getCourseById, addCourse, updateCourse, deleteCourse, getCourseStudents } from '@/api/course'

const state = {
  courses: [],
  total: 0,
  courseDetail: null,
  courseStudents: []
}

const mutations = {
  SET_COURSES: (state, { records, total }) => {
    state.courses = records
    state.total = total
  },
  SET_COURSE_DETAIL: (state, detail) => {
    state.courseDetail = detail
  },
  SET_COURSE_STUDENTS: (state, students) => {
    state.courseStudents = students
  }
}

const actions = {
  // 获取课程列表
  getCourses({ commit }, params) {
    return new Promise((resolve, reject) => {
      getCourses(params)
        .then(response => {
          const { data } = response
          commit('SET_COURSES', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取课程详情
  getCourseDetail({ commit }, courseId) {
    return new Promise((resolve, reject) => {
      getCourseById(courseId)
        .then(response => {
          const { data } = response
          commit('SET_COURSE_DETAIL', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 添加课程
  addCourse({ commit }, courseData) {
    return new Promise((resolve, reject) => {
      addCourse(courseData)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 更新课程信息
  updateCourse({ commit }, { courseId, data }) {
    return new Promise((resolve, reject) => {
      updateCourse(courseId, data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 删除课程
  deleteCourse({ commit }, courseId) {
    return new Promise((resolve, reject) => {
      deleteCourse(courseId)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取选修课程的学生列表
  getCourseStudents({ commit }, { courseId, params }) {
    return new Promise((resolve, reject) => {
      getCourseStudents(courseId, params)
        .then(response => {
          const { data } = response
          commit('SET_COURSE_STUDENTS', data.students || [])
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