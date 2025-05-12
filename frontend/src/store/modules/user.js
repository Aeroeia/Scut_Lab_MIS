import { login, logout, getUserInfo as fetchUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken, getRole, setRole, getRealId, setRealId, getRoleName, setRoleName } from '@/utils/auth'

const state = {
  token: getToken(),
  role: getRole(),
  realId: getRealId(),
  roleName: getRoleName(),
  name: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ROLE: (state, role) => {
    state.role = role
  },
  SET_REAL_ID: (state, realId) => {
    state.realId = realId
  },
  SET_ROLE_NAME: (state, roleName) => {
    state.roleName = roleName
  },
  SET_NAME: (state, name) => {
    state.name = name
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
          commit('SET_ROLE_NAME', data.roleName)
          
          setToken(data.token)
          setRole(data.role.toString())
          setRealId(data.realId)
          setRoleName(data.roleName)
          
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
        resolve({
          role: state.role,
          realId: state.realId,
          roleName: state.roleName
        })
        return
      }
      
      // Otherwise fetch user information from server
      fetchUserInfo()
        .then(response => {
          const { data } = response
          
          if (!data) {
            reject(new Error('Verification failed, please login again'))
            return
          }
          
          const { role, realId, roleName, name } = data
          
          // Verify the returned role is not empty
          if (!role) {
            reject(new Error('Failed to get user role'))
            return
          }
          
          // Set user information
          commit('SET_ROLE', role.toString())
          commit('SET_REAL_ID', realId)
          commit('SET_ROLE_NAME', roleName)
          commit('SET_NAME', name || '')
          
          // Sync to cookies
          setRole(role.toString())
          setRealId(realId)
          setRoleName(roleName)
          
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
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
          commit('SET_NAME', '')
          
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
      commit('SET_NAME', '')
      
      removeToken()
      resolve()
    })
  }
}

const getters = {
  userInfo: state => {
    return {
      username: state.realId,
      realId: state.realId,
      role: state.role,
      roleName: state.roleName,
      name: state.name
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
} 