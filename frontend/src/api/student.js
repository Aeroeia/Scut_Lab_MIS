import request from '@/utils/request'

// 获取学生列表
export function getStudents(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

// 获取学生详情
export function getStudentById(studentId,params) {
  return request({
    url: `/students/${studentId}`,
    method: 'get',
    params
  })
}

// 添加学生
export function addStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

// 更新学生信息
export function updateStudent(studentId, data) {
  return request({
    url: `/students/${studentId}`,
    method: 'put',
    data
  })
}

// 删除学生
export function deleteStudent(studentId) {
  return request({
    url: `/students/${studentId}`,
    method: 'delete'
  })
}

//获取学生选课信息
// export function getStudentCourses(studentId, params) {
//   return request({
//     url: `/students/${studentId}/courses`,
//     method: 'get',
//     params
//   })
// }