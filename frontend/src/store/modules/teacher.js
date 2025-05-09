import { getTeachers, getTeacherById, addTeacher, updateTeacher, deleteTeacher, getTeacherCourses } from '@/api/teacher'

const state = {
  teachers: [],
  total: 0,
  teacherDetail: null,
  teacherCourses: []
}

const mutations = {
  SET_TEACHERS: (state, { records, total }) => {
    state.teachers = records
    state.total = total
  },
  SET_TEACHER_DETAIL: (state, detail) => {
    state.teacherDetail = detail
  },
  SET_TEACHER_COURSES: (state, courses) => {
    state.teacherCourses = courses
  }
}

const actions = {
  // 获取教师列表
  getTeachers({ commit }, params) {
    return new Promise((resolve, reject) => {
      getTeachers(params)
        .then(response => {
          const { data } = response
          commit('SET_TEACHERS', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取教师详情
  getTeacherDetail({ commit }, teacherId) {
    return new Promise((resolve, reject) => {
      getTeacherById(teacherId)
        .then(response => {
          const { data } = response
          commit('SET_TEACHER_DETAIL', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 添加教师
  addTeacher({ commit }, teacherData) {
    return new Promise((resolve, reject) => {
      addTeacher(teacherData)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 更新教师信息
  updateTeacher({ commit }, { teacherId, data }) {
    return new Promise((resolve, reject) => {
      updateTeacher(teacherId, data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 删除教师
  deleteTeacher({ commit }, teacherId) {
    return new Promise((resolve, reject) => {
      deleteTeacher(teacherId)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取教师可教授课程
  getTeacherCourses({ commit }, teacherId) {
    return new Promise((resolve, reject) => {
      getTeacherCourses(teacherId)
        .then(response => {
          const { data } = response
          commit('SET_TEACHER_COURSES', data.courses || [])
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