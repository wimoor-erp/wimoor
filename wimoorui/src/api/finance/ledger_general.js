import request from '@/utils/request'

// 查询总账列表
export function listLedger(query) {
  return request({
    url: '/api/finance/ledger/list',
    method: 'get',
    params: query
  })
}

// 查询总账列表
export function listInfo(query) {
  return request({
    url: '/api/finance/ledger/listInfo',
    method: 'post',
    data: query
  })
}

// 查询总账详细
export function getLedger(ledgerId) {
  return request({
    url: '/api/finance/ledger/' + ledgerId,
    method: 'get'
  })
}

// 新增总账
export function addLedger(data) {
  return request({
    url: '/api/finance/ledger',
    method: 'post',
    data: data
  })
}

// 修改总账
export function updateLedger(data) {
  return request({
    url: '/api/finance/ledger',
    method: 'put',
    data: data
  })
}

// 删除总账
export function delLedger(ledgerId) {
  return request({
    url: '/api/finance/ledger/' + ledgerId,
    method: 'delete'
  })
}
