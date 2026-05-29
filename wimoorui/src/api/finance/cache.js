import request from '@/utils/request'

// 查询编码缓存列表
export function listCache(query) {
  return request({
    url: '/api/finance/cache/list',
    method: 'get',
    params: query
  })
}

// 查询编码缓存详细
export function getCache(id) {
  return request({
    url: '/api/finance/cache/' + id,
    method: 'get'
  })
}

// 新增编码缓存
export function addCache(data) {
  return request({
    url: '/finance/cache',
    method: 'post',
    data: data
  })
}

// 修改编码缓存
export function updateCache(data) {
  return request({
    url: '/api/finance/cache',
    method: 'put',
    data: data
  })
}

// 删除编码缓存
export function delCache(id) {
  return request({
    url: '/finance/cache/' + id,
    method: 'delete'
  })
}
