import request from '@/utils/request'

// 根据科目ID查询辅助核算设置列表
export function listSubjectAuxiliarySetting(subjectId) {
  return request({
    url: '/api/finance/setting/list',
    method: 'get',
    params: { subjectId }
  })
}

// 查询辅助核算项目列表
export function listAuxiliaryItems(query) {
  return request({
    url: '/api/finance/auxiliary/items/list',
    method: 'get',
    params: query
  })
}

// 查询辅助核算类型列表
export function listAuxiliaryTypes(query) {
  return request({
    url: '/api/finance/auxiliary/types/list',
    method: 'post',
    data: query
  })
}