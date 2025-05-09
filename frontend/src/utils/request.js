import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'
import { removeToken, getToken } from '@/utils/auth'
import store from '@/store'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 添加API前缀
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前添加token
    const token = getToken()
    if (token) {
      // 使用token，不添加Bearer前缀，使用后端需要的token名称
      config.headers['token'] = token
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回码不是200，则判断为错误
    if (res.code !== 200 && res.code !== 201) {
      Message({
        message: res.message || '请求错误',
        type: 'error',
        duration: 3 * 1000
      })

      // 401: 未登录或token过期
      if (res.code === 401) {
        // 只有未登录才跳转到登录页
        handleAuthError()
      }
      // 403: 权限不足，只提示错误，不跳转
      
      return Promise.reject(new Error(res.message || '请求错误'))
    } else {
      return res
    }
  },
  error => {
    console.log('err:', error)
    // 处理网络错误
    let message = '连接服务器失败'
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          // 只有未登录才跳转到登录页
          handleAuthError()
          break
        case 403:
          message = '权限不足，无法执行此操作'
          // 权限不足时不跳转，只提示错误
          break
        case 404:
          message = '请求错误,未找到该资源'
          break
        case 500:
          message = '服务器端出错'
          break
        default:
          message = `连接错误${error.response.status}`
      }
    } else if (error.message.includes('timeout')) {
      message = '请求超时，请稍后再试'
    } else {
      message = '网络异常，请检查您的网络连接'
    }
    
    Message({
      message: error.message || message,
      type: 'error',
      duration: 3 * 1000
    })
    
    return Promise.reject(error)
  }
)

// 统一处理认证错误（仅处理未登录情况）
function handleAuthError() {
  // 无论是否有token，只要收到401都需要重新登录
  if (router.currentRoute.path !== '/login') {
    // 清除token并重置状态
    removeToken()
    store.dispatch('user/resetToken').then(() => {
      // 重定向到登录页
      const redirect = router.currentRoute.fullPath
      router.replace(`/login?redirect=${redirect}`)
    })
  }
}

export default service 