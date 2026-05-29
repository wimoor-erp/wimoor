import request from '@/utils/request'

// 查询会计期间管理列表
export function listPeriods(query) {
  return request({
    url: '/api/finance/periods/list',
    method: 'get',
    params: query
  })
}

// 查询会计期间管理详细
export function getPeriods(periodId) {
  return request({
    url: '/api/finance/periods/' + periodId,
    method: 'get'
  })
}

// 新增会计期间管理
export function addPeriods(data) {
  return request({
    url: '/api/finance/periods',
    method: 'post',
    data: data
  })
}

// 修改会计期间管理
export function updatePeriods(data) {
  return request({
    url: '/api/finance/periods',
    method: 'put',
    data: data
  })
}

// 删除会计期间管理
export function delPeriods(periodId) {
  return request({
    url: '/api/finance/periods/' + periodId,
    method: 'delete'
  })
}
