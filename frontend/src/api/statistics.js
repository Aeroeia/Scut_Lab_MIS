import request from '@/utils/request'

// 获取Dashboard统计数据
export function getDashboardStats() {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

// 获取学生平均成绩
export function getStudentAverageScore(studentId, params) {
  return request({
    url: `/statistics/student/${studentId}`,
    method: 'get',
    params
  })
}

// 获取班级平均成绩
export function getClassAverageScore(className, params) {
  return request({
    url: `/statistics/class/${className}`,
    method: 'get',
    params
  })
}

// 获取课程平均成绩
export function getCourseAverageScore(courseId, params) {
  return request({
    url: `/statistics/course/${courseId}`,
    method: 'get',
    params
  })
}

// 获取所有学生平均成绩
export function getAllStudentsAverageScore(params) {
  return request({
    url: '/statistics/students',
    method: 'get',
    params
  })
} 