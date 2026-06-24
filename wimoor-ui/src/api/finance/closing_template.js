import request from '@/utils/request'

// 查询结账模板列表
export function listFinClosingTemplate(query) {
  return request({
    url: '/api/finance/closing_template/list',
    method: 'get',
    params: query
  })
}

// 查询结账模板详细
export function getFinClosingTemplate(id) {
  return request({
    url: '/api/finance/closing_template/' + id,
    method: 'get'
  })
}

// 新增结账模板
export function addFinClosingTemplate(data) {
  return request({
    url: '/api/finance/closing_template',
    method: 'post',
    data: data
  })
}

// 修改结账模板
export function updateFinClosingTemplate(data) {
  return request({
    url: '/api/finance/closing_template',
    method: 'put',
    data: data
  })
}

// 删除结账模板
export function delFinClosingTemplate(id) {
  return request({
    url: '/api/finance/closing_template/' + id,
    method: 'delete'
  })
}

export function voucher(templateId,period){
  return request({
    url:'/api/finance/closing_template/voucher?templateId='+templateId+'&period='+period,
    method:'get'
  })
}

export function initTemplateItem(templateid){
  return request({
    url:'/api/finance/closing_template/initTemplateItem?templateid='+templateid,
    method:'get'
  })
}

export function templateVouchers(templateId, period){
  return request({
    url:'/api/finance/closing_template/templateVouchers?templateid='+templateId+'&period='+period,
    method:'get'
  })
}

// 查询金额计算逻辑明细
export function getCalculationDetail(templateId, period){
  return request({
    url:'/api/finance/closing_template/calculationDetail?templateId='+templateId+'&period='+period,
    method:'get'
  })
}

// 查询凭证生成日志
export function getVoucherLog(templateId, period){
  return request({
    url:'/api/finance/closing_template/voucherLog?templateid='+templateId+'&period='+period,
    method:'get'
  })
}

