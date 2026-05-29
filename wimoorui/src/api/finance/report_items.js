import request from '@/utils/request'

// 查询报表项目列表
export function listItems(query) {
  return request({
    url: '/api/finance/report/items/list',
    method: 'get',
    params: query
  })
}

// 查询报表项目详细
export function getItems(itemId) {
  return request({
    url: '/api/finance/report/items/' + itemId,
    method: 'get'
  })
}

// 新增报表项目
export function addItems(data) {
  return request({
    url: '/api/finance/report/items',
    method: 'post',
    data: data
  })
}

// 修改报表项目
export function updateItems(data) {
  return request({
    url: '/api/finance/report/items',
    method: 'put',
    data: data
  })
}

// 删除报表项目
export function delItems(itemId) {
  return request({
    url: '/api/finance/report/items/' + itemId,
    method: 'delete'
  })
}
