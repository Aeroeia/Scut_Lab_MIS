import request from '@/utils/request'

// 用户信息
export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

// 修改密码
export function updatePassword(realId, data) {
  return request({
    url: `/auth/${realId}`,
    method: 'put',
    data
  })
} 