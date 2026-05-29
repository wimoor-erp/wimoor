import request from '@/utils/request'

// 查询财务报表模板列表
export function listTemplates(query) {
  return request({
    url: '/api/finance/report/templates/list',
    method: 'get',
    params: query
  })
}

// 查询财务报表模板详细
export function getTemplates(templateId) {
  return request({
    url: '/api/finance/report/templates/' + templateId,
    method: 'get'
  })
}

// 新增财务报表模板
export function addTemplates(data) {
  return request({
    url: '/api/finance/report/templates',
    method: 'post',
    data: data
  })
}

// 修改财务报表模板
export function updateTemplates(data) {
  return request({
    url: '/api/finance/report/templates',
    method: 'put',
    data: data
  })
}

// 删除财务报表模板
export function delTemplates(templateId) {
  return request({
    url: '/api/finance/report/templates/' + templateId,
    method: 'delete'
  })
}
