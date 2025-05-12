import { getStudents, getStudentById, addStudent, updateStudent, deleteStudent,  } from '@/api/student'

const state = {
  students: [],
  total: 0,
  studentDetail: null,
  studentCourses: []
}

const mutations = {
  SET_STUDENTS: (state, { records, total }) => {
    state.students = records
    state.total = total
  },
  SET_STUDENT_DETAIL: (state, detail) => {
    state.studentDetail = detail
  },
  SET_STUDENT_COURSES: (state, courses) => {
    state.studentCourses = courses
  }
}

const actions = {
  // 获取学生列表
  getStudents({ commit }, params) {
    return new Promise((resolve, reject) => {
      getStudents(params)
        .then(response => {
          const { data } = response
          commit('SET_STUDENTS', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取学生详情
  getStudentDetail({ commit }, studentId) {
    return new Promise((resolve, reject) => {
      getStudentById(studentId)
        .then(response => {
          const { data } = response
          commit('SET_STUDENT_DETAIL', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 添加学生
  addStudent({ commit }, studentData) {
    return new Promise((resolve, reject) => {
      addStudent(studentData)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 更新学生信息
  updateStudent({ commit }, { studentId, data }) {
    return new Promise((resolve, reject) => {
      updateStudent(studentId, data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 删除学生
  deleteStudent({ commit }, studentId) {
    return new Promise((resolve, reject) => {
      deleteStudent(studentId)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取学生选课信息
  // getStudentCourses({ commit }, { studentId, params }) {
  //   return new Promise((resolve, reject) => {
  //     getStudentCourses(studentId, params)
  //       .then(response => {
  //         const { data } = response
  //         commit('SET_STUDENT_COURSES', data.courses || [])
  //         resolve(data)
  //       })
  //       .catch(error => {
  //         reject(error)
  //       })
  //   })
  // }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 