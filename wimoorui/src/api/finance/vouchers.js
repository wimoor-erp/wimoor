import request from '@/utils/request'

// 查询记账凭证列表
export function listVouchers(query) {
  return request({
    url: '/api/finance/vouchers/list',
    method: 'get',
    params: query
  })
}
// 审核记账凭证
export function approveVoucher(data) {
  return request({
    url: '/api/finance/vouchers/approve',
    method: 'post',
    data: data
  })
}

// 记账凭证过账
export function passVouchers(data) {
  return request({
    url: '/api/finance/vouchers/passVouchers',
    method: 'post',
    data: data
  })
}



// 查询记账凭证详细
export function getVouchers(voucherId) {
  return request({
    url: '/api/finance/vouchers/' + voucherId,
    method: 'get'
  })
}

// 新增记账凭证
export function addVouchers(data) {
  return request({
    url: '/api/finance/vouchers',
    method: 'post',
    data: data
  })
}

// 修改记账凭证
export function updateVouchers(data) {
  return request({
    url: '/api/finance/vouchers',
    method: 'put',
    data: data
  })
}
export function updateVouchersFiles(data) {
  return request({
    url: '/api/finance/vouchers/files',
    method: 'post',
    data: data
  })
}

// 删除记账凭证
export function delVouchers(voucherId) {
  return request({
    url: '/api/finance/vouchers/' + voucherId,
    method: 'delete'
  })
}

// 查询下一个凭证号
export function nextVoucherNo(data) {
  return request({
    url: '/api/finance/vouchers/nextVoucherNo',
    method: 'post',
    data: data
  })
}

