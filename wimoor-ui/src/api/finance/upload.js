import request from '@/utils/request'

// 查询凭证分录明细，存储凭证的每一笔分录信息列表
export function listUpload(query) {
  return request({
    url: '/api/finance/upload/list',
    method: 'get',
    params: query
  })
}

// 查询凭证分录明细，存储凭证的每一笔分录信息详细
export function getUpload(id) {
  return request({
    url: '/api/finance/upload/' + id,
    method: 'get'
  })
}

// 新增凭证分录明细，存储凭证的每一笔分录信息
export function addUpload(data) {
  return request({
    url: '/api/finance/upload',
    method: 'post',
    data: data
  })
}

// 修改凭证分录明细，存储凭证的每一笔分录信息
export function updateUpload(data) {
  return request({
    url: '/api/finance/upload',
    method: 'put',
    data: data
  })
}

// 删除凭证分录明细，存储凭证的每一笔分录信息
export function delUpload(id) {
  return request({
    url: '/api/finance/upload/' + id,
    method: 'delete'
  })
}
