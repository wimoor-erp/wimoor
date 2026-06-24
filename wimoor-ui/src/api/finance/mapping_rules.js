import request from '@/utils/request'

// 查询报表映射规则列表
export function listMappingRules(query) {
  return request({
    url: '/api/finance/report/mapping-rules/list',
    method: 'get',
    params: query
  })
}

// 根据模板类型查询规则列表
export function listRulesByTemplateType(templateType, groupid) {
  return request({
    url: `/api/finance/report/mapping-rules/list/${templateType}`,
    method: 'get',
    params: { groupid }
  })
}

// 根据项目编码查询规则列表
export function listRulesByItemCode(itemCode, groupid) {
  return request({
    url: `/api/finance/report/mapping-rules/list/item/${itemCode}`,
    method: 'get',
    params: { groupid }
  })
}

// 查询报表映射规则详细
export function getMappingRules(ruleId) {
  return request({
    url: '/api/finance/report/mapping-rules/' + ruleId,
    method: 'get'
  })
}

// 新增报表映射规则
export function addMappingRules(data) {
  return request({
    url: '/api/finance/report/mapping-rules',
    method: 'post',
    data: data
  })
}

// 批量新增报表映射规则
export function batchAddMappingRules(data) {
  return request({
    url: '/api/finance/report/mapping-rules/batch',
    method: 'post',
    data: data
  })
}

// 修改报表映射规则
export function updateMappingRules(data) {
  return request({
    url: '/api/finance/report/mapping-rules',
    method: 'put',
    data: data
  })
}

// 删除报表映射规则
export function delMappingRules(ruleIds) {
  return request({
    url: '/api/finance/report/mapping-rules/' + ruleIds,
    method: 'delete'
  })
}

// 初始化默认规则
export function initDefaultRules(groupid) {
  return request({
    url: `/api/finance/report/mapping-rules/init-default/${groupid}`,
    method: 'post'
  })
}

// 同步报表公式
export function syncReportFormulas(groupid, templateId, templateType) {
  return request({
    url: `/api/finance/report/mapping-rules/sync/${groupid}/${templateId}/${templateType}`,
    method: 'post'
  })
}
