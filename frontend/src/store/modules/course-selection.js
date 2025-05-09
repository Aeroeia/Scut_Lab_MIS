import { getCourseSelections, getCourseSelectionById, addCourseSelection, updateCourseSelection, deleteCourseSelection } from '@/api/course-selection'

const state = {
  courseSelections: [],
  total: 0,
  courseSelectionDetail: null
}

const mutations = {
  SET_COURSE_SELECTIONS: (state, { records, total }) => {
    state.courseSelections = records
    state.total = total
  },
  SET_COURSE_SELECTION_DETAIL: (state, detail) => {
    state.courseSelectionDetail = detail
  }
}

const actions = {
  // 获取选课列表
  getCourseSelections({ commit }, params) {
    return new Promise((resolve, reject) => {
      getCourseSelections(params)
        .then(response => {
          const { data } = response
          commit('SET_COURSE_SELECTIONS', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 获取选课详情
  getCourseSelectionDetail({ commit }, id) {
    return new Promise((resolve, reject) => {
      getCourseSelectionById(id)
        .then(response => {
          const { data } = response
          commit('SET_COURSE_SELECTION_DETAIL', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 添加选课
  addCourseSelection({ commit }, selectionData) {
    return new Promise((resolve, reject) => {
      addCourseSelection(selectionData)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 更新选课信息
  updateCourseSelection({ commit }, { id, data }) {
    return new Promise((resolve, reject) => {
      updateCourseSelection(id, data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 删除选课
  deleteCourseSelection({ commit }, id) {
    return new Promise((resolve, reject) => {
      deleteCourseSelection(id)
        .then(response => {
          resolve(response)
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