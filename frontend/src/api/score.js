import request from '@/utils/request'

// 获取成绩列表
export function getScores(params) {
  return request({
    url: '/scores',
    method: 'get',
    params
  })
}

// 更新学生成绩
export function updateScore(id, data) {
  // 确保id不是undefined
  if (!id) {
    return Promise.reject(new Error('缺少选课ID参数'))
  }
  
  return request({
    url: `/scores/${id}`,
    method: 'put',
    data
  })
} 