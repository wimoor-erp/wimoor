import request from '@/utils/request'

// 查询结转损益模板配置
export function getProfitLossConfig(templateId) {
  return request({
    url: '/api/finance/closing_template_profit_loss/' + templateId,
    method: 'get'
  })
}

// 新增结转损益模板配置
export function addProfitLossConfig(data) {
  return request({
    url: '/api/finance/closing_template_profit_loss',
    method: 'post',
    data: data
  })
}

// 修改结转损益模板配置
export function updateProfitLossConfig(data) {
  return request({
    url: '/api/finance/closing_template_profit_loss',
    method: 'put',
    data: data
  })
}

// 删除结转损益模板配置
export function delProfitLossConfig(id) {
  return request({
    url: '/api/finance/closing_template_profit_loss/' + id,
    method: 'delete'
  })
}

// 根据模板ID查询结转损益配置
export function getProfitLossConfigByTemplateId(templateId) {
  return request({
    url: '/api/finance/closing_template_profit_loss/template/' + templateId,
    method: 'get'
  })
}
