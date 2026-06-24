import request from '@/utils/request'

// 查询结算飞书表格同步列表
export function listFinClosingTemplateFeishu(query) {
  return request({
    url: '/api/finance/closing_template/feishu/list',
    method: 'get',
    params: query
  })
}

// 查询结算飞书表格同步详细
export function getFinClosingTemplateFeishu(templateid) {
  return request({
    url: '/api/finance/closing_template/feishu/' + templateid,
    method: 'get'
  })
}

// 新增结算飞书表格同步
export function addFinClosingTemplateFeishu(data) {
  return request({
    url: '/api/finance/closing_template/feishu',
    method: 'post',
    data: data
  })
}

// 修改结算飞书表格同步
export function updateFinClosingTemplateFeishu(data) {
  return request({
    url: '/api/finance/closing_template/feishu',
    method: 'put',
    data: data
  })
}

// 删除结算飞书表格同步
export function delFinClosingTemplateFeishu(templateid) {
  return request({
    url: '/api/finance/closing_template/feishu/' + templateid,
    method: 'delete'
  })
}

// 根据模板ID获取飞书配置
export function getFeishuConfigByTemplateId(templateid) {
  return request({
    url: '/api/finance/closing_template/feishu/' + templateid,
    method: 'get'
  })
}