import request from '@/utils/request'

// 查询编码规则分类列表
export function listCategory(query) {
  return request({
    url: '/api/finance/category/list',
    method: 'get',
    params: query
  })
}

// 查询编码规则分类详细
export function getCategory(id) {
  return request({
    url: '/api/finance/category/' + id,
    method: 'get'
  })
}

// 新增编码规则分类
export function addCategory(data) {
  return request({
    url: '/api/finance/category',
    method: 'post',
    data: data
  })
}

// 修改编码规则分类
export function updateCategory(data) {
  return request({
    url: '/api/finance/category',
    method: 'put',
    data: data
  })
}

// 删除编码规则分类
export function delCategory(id) {
  return request({
    url: '/api/finance/category/' + id,
    method: 'delete'
  })
}
