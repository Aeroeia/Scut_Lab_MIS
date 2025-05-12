import request from '@/utils/request'

// 获取教师列表
export function getTeachers(params) {
  return request({
    url: '/teachers',
    method: 'get',
    params
  })
}

// 获取教师详情
export function getTeacherDetail(teacherId) {
  return request({
    url: `/teachers/${teacherId}`,
    method: 'get'
  })
}

// 添加教师
export function addTeacher(data) {
  return request({
    url: '/teachers',
    method: 'post',
    data
  })
}

// 更新教师信息
export function updateTeacher(teacherId, data) {
  return request({
    url: `/teachers/${teacherId}`,
    method: 'put',
    data
  })
}

// 删除教师
export function deleteTeacher(teacherId) {
  return request({
    url: `/teachers/${teacherId}`,
    method: 'delete'
  })
}

// // 获取教师可教授课程
// export function getTeacherCourses(teacherId) {
//   return request({
//     url: `/teachers/${teacherId}/courses`,
//     method: 'get'
//   })
// }