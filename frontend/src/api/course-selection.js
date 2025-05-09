import request from '@/utils/request'

// 获取选课列表
export function getCourseSelections(params) {
  return request({
    url: '/course-selections',
    method: 'get',
    params
  })
}

// 获取选课详情
export function getCourseSelectionById(id) {
  return request({
    url: `/course-selections/${id}`,
    method: 'get'
  })
}

// 添加选课
export function addCourseSelection(data) {
  return request({
    url: '/course-selections',
    method: 'post',
    data
  })
}

// 更新选课信息
export function updateCourseSelection(id, data) {
  return request({
    url: `/course-selections/${id}`,
    method: 'put',
    data
  })
}

// 删除选课
export function deleteCourseSelection(id) {
  return request({
    url: `/course-selections/${id}`,
    method: 'delete'
  })
} 