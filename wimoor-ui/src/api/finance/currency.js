import request from '@/utils/request'

// 查询currency列表
export function listCurrency(query) {
  return request({
    url: '/api/finance/currency/list',
    method: 'get',
    params: query
  })
}

// 查询currency详细
export function getCurrency(query) {
  return request({
    url: '/api/finance/currency/getInfo',
    method: 'get',
    params: query
  })
}

// 新增currency
export function saveCurrency(data) {
  return request({
    url: '/api/finance/currency',
    method: 'post',
    data: data
  })
}



// 删除currency
export function delCurrency(code) {
  return request({
    url: '/api/finance/currency/' + code,
    method: 'delete'
  })
}
