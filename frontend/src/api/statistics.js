import request from '@/utils/request'

// 获取学生平均成绩
export function getStudentAverageScore(studentId, params) {
  return request({
    url: `/statistics/student/${studentId}/average`,
    method: 'get',
    params
  })
}

// 获取班级平均成绩
export function getClassAverageScore(className, params) {
  return request({
    url: `/statistics/class/${className}/average`,
    method: 'get',
    params
  })
}

// 获取课程平均成绩
export function getCourseAverageScore(courseId, params) {
  return request({
    url: `/statistics/course/${courseId}/average`,
    method: 'get',
    params
  })
}

// 获取所有学生平均成绩
export function getAllStudentsAverageScore(params) {
  return request({
    url: '/statistics/all/average',
    method: 'get',
    params
  })
}

// 获取全校成绩统计信息 (用于all.vue页面)
export function getAllStatistics(params) {
  return request({
    url: '/statistics/all/average',
    method: 'get',
    params
  })
} 