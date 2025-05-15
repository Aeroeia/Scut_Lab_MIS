import request from '@/utils/request'

// 获取课程列表
export function getCourses(params) {
  return request({
    url: '/courses',
    method: 'get',
    params
  })
}

// 获取课程详情
export function getCourseById(courseId, teacherId) {
  return request({
    url: `/courses/${courseId}`,
    method: 'get',
    params: { teacherId }
  })
}

// 添加课程
export function addCourse(data) {
  return request({
    url: '/courses',
    method: 'post',
    data
  })
}

// 更新课程信息
export function updateCourse(courseId, data) {
  return request({
    url: `/courses/${courseId}`,
    method: 'put',
    data
  })
}

// 删除课程
export function deleteCourse(courseId) {
  return request({
    url: `/courses/${courseId}`,
    method: 'delete'
  })
}

// 获取选修课程的学生列表
export function getCourseStudents(courseId, params) {
  return request({
    url: `/courses/${courseId}/students`,
    method: 'get',
    params
  })
} 