import request from '@/utils/request'

// 查询编码生成记录列表
export function listLog(query) {
  return request({
    url: '/api/finance/log/list',
    method: 'get',
    params: query
  })
}

// 查询编码生成记录详细
export function getLog(id) {
  return request({
    url: '/api/finance/log/' + id,
    method: 'get'
  })
}

// 新增编码生成记录
export function addLog(data) {
  return request({
    url: '/api/finance/log',
    method: 'post',
    data: data
  })
}

// 修改编码生成记录
export function updateLog(data) {
  return request({
    url: '/api/finance/log',
    method: 'put',
    data: data
  })
}

// 删除编码生成记录
export function delLog(id) {
  return request({
    url: '/api/finance/log/' + id,
    method: 'delete'
  })
}
