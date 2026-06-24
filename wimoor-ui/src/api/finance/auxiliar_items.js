import request from '@/utils/request'

// 查询辅助核算具体项目列表
export function listItems(query) {
  return request({
    url: '/api/finance/auxiliary/items/list',
    method: 'get',
    params: query
  })
}

// 查询辅助核算具体项目详细
export function getItems(itemId) {
  return request({
    url: '/api/finance/auxiliary/items/' + itemId,
    method: 'get'
  })
}

// 新增辅助核算具体项目
export function addItems(data) {
  return request({
    url: '/api/finance/auxiliary/items',
    method: 'post',
    data: data
  })
}

// 修改辅助核算具体项目
export function updateItems(data) {
  return request({
    url: '/api/finance/auxiliary/items',
    method: 'put',
    data: data
  })
}

// 删除辅助核算具体项目
export function delItems(itemId) {
  return request({
    url: '/api/finance/auxiliary/items/' + itemId,
    method: 'delete'
  })
}

// 从ERP模块同步供应商数据
export function syncSupplier(data) {
  return request({
    url: '/api/finance/auxiliary/items/syncSupplier',
    method: 'post',
    data: data
  })
}

// 从Admin模块同步员工数据
export function syncEmployee(data) {
  return request({
    url: '/api/finance/auxiliary/items/syncEmployee',
    method: 'post',
    data: data
  })
}

// 从Admin模块同步部门数据
export function syncDept(data) {
  return request({
    url: '/api/finance/auxiliary/items/syncDept',
    method: 'post',
    data: data
  })
}
