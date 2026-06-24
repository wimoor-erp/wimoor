import request from '@/utils/request'

// 查询凭证分录明细列表
export function listEntries(query) {
  return request({
    url: '/api/finance/entries/list',
    method: 'get',
    params: query
  })
}
// 查询凭证分录明细列表
export function listEntriesByVoucherId(query) {
  return request({
    url: '/api/finance/entries/getEntries',
    method: 'get',
    params: query
  })
}

// 查询凭证分录明细详细
export function getEntries(entryId) {
  return request({
    url: '/api/finance/entries/' + entryId,
    method: 'get'
  })
}

// 新增凭证分录明细
export function addEntries(data) {
  return request({
    url: '/api/finance/entries',
    method: 'post',
    data: data
  })
}

// 修改凭证分录明细
export function updateEntries(data) {
  return request({
    url: '/api/finance/entries',
    method: 'put',
    data: data
  })
}

// 删除凭证分录明细
export function delEntries(entryId) {
  return request({
    url: '/api/finance/entries/' + entryId,
    method: 'delete'
  })
}
