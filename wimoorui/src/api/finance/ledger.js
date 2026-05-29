import request from '@/utils/request'

// 查询明细账表列表
export function listLedger(query) {
  return request({
    url: '/api/finance/detail_ledger/list',
    method: 'get',
    params: query
  })
}
// 查询明细账表列表
export function listLedgerInfo(query) {
  return request({
    url: '/api/finance/detail_ledger/listInfo',
    method: 'post',
    data: query
  })
}

// 查询明细账表详细
export function getLedger(detailId) {
  return request({
    url: '/api/finance/detail_ledger/' + detailId,
    method: 'get'
  })
}

// 新增明细账表
export function addLedger(data) {
  return request({
    url: '/api/finance/detail_ledger',
    method: 'post',
    data: data
  })
}

// 修改明细账表
export function updateLedger(data) {
  return request({
    url: '/api/finance/detail_ledger',
    method: 'put',
    data: data
  })
}

// 删除明细账表
export function delLedger(detailId) {
  return request({
    url: '/api/finance/detail_ledger/' + detailId,
    method: 'delete'
  })
}
