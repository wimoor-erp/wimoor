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
    method: 'get',
    params: query
  })
}

// 查询顶级科目总账汇总
export function topLevelSummary(query) {
  return request({
    url: '/api/finance/ledger/topLevelSummary',
    method: 'get',
    params: query
  })
}

// 查询顶级科目总账汇总（完整数据，包含期初余额、本期合计、本年累计）
export function topLevelSummaryFull(query) {
  return request({
    url: '/api/finance/ledger/topLevelSummaryFull',
    method: 'get',
    params: query
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

// 重建总账
export function rebuildLedger(groupid) {
  return request({
    url: '/api/finance/ledger/rebuild',
    method: 'post',
    params: { groupid }
  })
}

// 验证总账公式
export function verifyLedger(groupid) {
  return request({
    url: '/api/finance/ledger/verify',
    method: 'get',
    params: { groupid }
  })
}
