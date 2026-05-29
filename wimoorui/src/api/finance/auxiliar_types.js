import request from '@/utils/request'

// 查询辅助核算类别列表
export function listTypes(query) {
  return request({
    url: '/api/finance/auxiliary/types/list',
    method: 'get',
    params: query
  })
}

// 查询辅助核算类别详细
export function getTypes(typeId) {
  return request({
    url: '/api/finance/auxiliary/types/' + typeId,
    method: 'get'
  })
}

// 新增辅助核算类别
export function addTypes(data) {
  return request({
    url: '/api/finance/auxiliary/types',
    method: 'post',
    data: data
  })
}

// 修改辅助核算类别
export function updateTypes(data) {
  return request({
    url: '/api/finance/auxiliary/types',
    method: 'put',
    data: data
  })
}

// 删除辅助核算类别
export function delTypes(typeId) {
  return request({
    url: '/api/finance/auxiliary/types/' + typeId,
    method: 'delete'
  })
}
