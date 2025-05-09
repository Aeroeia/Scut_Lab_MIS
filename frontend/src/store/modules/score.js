import { getScores, updateScore } from '@/api/score'

const state = {
  scores: [],
  total: 0
}

const mutations = {
  SET_SCORES: (state, { records, total }) => {
    state.scores = records
    state.total = total
  }
}

const actions = {
  // 获取成绩列表
  getScores({ commit }, params) {
    return new Promise((resolve, reject) => {
      getScores(params)
        .then(response => {
          const { data } = response
          commit('SET_SCORES', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 更新学生成绩
  updateScore({ commit }, { id, data }) {
    return new Promise((resolve, reject) => {
      updateScore(id, data)
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