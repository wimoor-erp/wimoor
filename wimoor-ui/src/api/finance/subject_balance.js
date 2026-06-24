import request from '@/utils/request'

export function querySubjectBalanceList(data) {
  return request({
    url: '/finance/api/report/subject-balance/list',
    method: 'post',
    data: data
  })
}

export function querySubjectBalanceTree(data) {
  return request({
    url: '/finance/api/report/subject-balance/tree',
    method: 'post',
    data: data
  })
}

export function getBalanceSummary(params) {
  return request({
    url: '/finance/api/report/subject-balance/summary',
    method: 'get',
    params: params
  })
}

export function exportSubjectBalance(data) {
  return request({
    url: '/finance/api/report/subject-balance/export',
    method: 'post',
    data: data
  })
}

export function simpleQuerySubjectBalance(params) {
  return request({
    url: '/finance/api/report/subject-balance/simple',
    method: 'get',
    params: params
  })
}