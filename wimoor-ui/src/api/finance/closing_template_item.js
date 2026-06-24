import request from '@/utils/request'

// 查询结账科目对应关系列表
export function listTemplateItem(query) {
  return request({
    url: '/api/finance/templateItem/list',
    method: 'get',
    params: query
  })
}

// 查询结账科目对应关系详细
export function getTemplateItem(id) {
  return request({
    url: '/api/finance/templateItem/' + id,
    method: 'get'
  })
}

// 新增结账科目对应关系
export function addTemplateItem(data) {
  return request({
    url: '/api/finance/templateItem',
    method: 'post',
    data: data
  })
}

// 修改结账科目对应关系
export function updateTemplateItem(data) {
  return request({
    url: '/api/finance/templateItem',
    method: 'put',
    data: data
  })
}

// 删除结账科目对应关系
export function delTemplateItem(id) {
  return request({
    url: '/api/finance/templateItem/' + id,
    method: 'delete'
  })
}

// 获取科目列表
export function getSubjects() {
  return request({
    url: '/api/finance/subjects/list',
    method: 'get'
  })
}
