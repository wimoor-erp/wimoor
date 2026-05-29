import request from '@/utils/request'

// 查询会计科目列表
export function listSubjects(query) {
  return request({
    url: '/api/finance/subjects/list',
    method: 'get',
    params: query
  })
}

// 查询会计科目详细
export function getSubjects(subjectId) {
  return request({
    url: '/api/finance/subjects/' + subjectId,
    method: 'get'
  })
}

// 新增会计科目
export function addSubjects(data) {
  return request({
    url: '/api/finance/subjects',
    method: 'post',
    data: data
  })
}

// 修改会计科目
export function updateSubjects(data) {
  return request({
    url: '/api/finance/subjects',
    method: 'put',
    data: data
  })
}

// 删除会计科目
export function delSubjects(subjectId) {
  return request({
    url: '/api/finance/subjects/' + subjectId,
    method: 'delete'
  })
}

// 查询下一个凭证号
export function initFinAccountingSubjects(groupid) {
  return request({
    url: '/api/finance/subjects/init/'+groupid,
    method: 'get',
  })
}
