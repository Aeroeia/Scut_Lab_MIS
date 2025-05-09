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
  return request({
    url: `/scores/${id}`,
    method: 'put',
    data
  })
} 