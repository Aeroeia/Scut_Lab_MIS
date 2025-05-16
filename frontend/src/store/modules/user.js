import { login, logout, getUserInfo as fetchUserInfo } from '@/api/auth'
import { updatePassword } from '@/api/user'
import {
    getToken,
    setToken,
    removeToken,
    getRole,
    setRole,
    getRealId,
    setRealId,
    getRoleName,
    setRoleName,
    getUserName, setUserName
} from '@/utils/auth'

const state = {
  token: getToken(),
  role: getRole(),
  realId: getRealId(),
  roleName: getRoleName(),
  name:getUserName(),
  userInfo: {
    username: getUserName() || '',
    realId: getRealId() || '',
    role: getRole() || '',
    roleName: getRoleName() || '',
  }
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ROLE: (state, role) => {
    state.role = role
    state.userInfo.role = role
  },
  SET_REAL_ID: (state, realId) => {
    state.realId = realId
    state.userInfo.realId = realId
  },
  SET_ROLE_NAME: (state, roleName) => {
    state.roleName = roleName
    state.userInfo.roleName = roleName
  },
  SET_NAME: (state, name) => {
    state.name = name
    state.userInfo.username = name
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  }
}

const actions = {
  // User login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          const { data } = response
          commit('SET_TOKEN', data.token)
          commit('SET_ROLE', data.role.toString())
          commit('SET_REAL_ID', data.realId)
          // 这里 roleName 应该是根据 role 设置的角色名称
          let roleName = 'User'
          if (data.role === 1) {
            roleName = 'Admin'
          } else if (data.role === 2) {
            roleName = 'Teacher'
          } else if (data.role === 3) {
            roleName = 'Student'
          }
          
          commit('SET_ROLE_NAME', roleName)
          commit('SET_NAME', username)
          
          // Update the complete userInfo object
          const completeUserInfo = {
            username: data.roleName,
            realId: data.realId,
            role: data.role.toString(),
            roleName: roleName,
          }
          commit('SET_USER_INFO', completeUserInfo)
          
          setToken(data.token)
          setRole(data.role.toString())
          setRealId(data.realId)
          setRoleName(roleName)
          setUserName(username)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // Get user information
  getUserInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // If role information already exists, return directly
      if (state.role) {
        const userInfo = {
          username: state.name,
          realId: state.realId,
          role: state.role,
          roleName: state.roleName,
        }
        commit('SET_USER_INFO', userInfo)
        resolve(userInfo)
      }
    })
  },

  // User logout
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logout()
        .then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLE', '')
          commit('SET_REAL_ID', '')
          commit('SET_ROLE_NAME', '')
          commit('SET_USER_INFO', {
            username: '',
            realId: '',
            role: '',
            roleName: '',
          })
          
          removeToken()
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // Reset token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLE', '')
      commit('SET_REAL_ID', '')
      commit('SET_ROLE_NAME', '')
      commit('SET_USER_INFO', {
        username: '',
        realId: '',
        role: '',
        roleName: '',
      })
      
      removeToken()
      resolve()
    })
  },

  // 修改密码
  updatePassword({ commit, state }, passwordData) {
    return new Promise((resolve, reject) => {
      updatePassword(state.realId, passwordData)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

const getters = {
  userInfo: state => {
    // 直接返回 state 中的 userInfo 对象
    return state.userInfo
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
} 