import request from '@/utils/request'

// 查询凭证字列表
export function listVoucherTypes(query) {
  return request({
    url: '/api/finance/type/list',
    method: 'get',
    params: query
  })
}

// 新增凭证字
export function addVoucherType(data) {
  return request({
    url: '/api/finance/type',
    method: 'post',
    data: data
  })
}

// 修改凭证字
export function updateVoucherType(data) {
  return request({
    url: '/api/finance/type',
    method: 'put',
    data: data
  })
}

// 删除凭证字
export function deleteVoucherType(ids) {
  return request({
    url: '/api/finance/type/' + ids,
    method: 'delete'
  })
}
