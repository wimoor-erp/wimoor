import request from '@/utils/request';

/**
 * 生成报告
 * @param {Object} data - 报告生成请求参数
 * @returns {Promise}
 */
export function generateReport(data) {
  return request({
    url: '/finance/api/report/generate',
    method: 'post',
    data
  });
}

/**
 * 获取报告模板列表
 * @param {string} groupid - 分组ID
 * @returns {Promise}
 */
export function getTemplates(groupid) {
  return request({
    url: '/finance/api/report/templates',
    method: 'get',
    params: { groupid }
  });
}

/**
 * 验证报告数据
 * @param {Object} params - 验证参数
 * @param {string} params.groupid - 分组ID
 * @param {string} params.templateCode - 模板编码
 * @param {string} params.period - 报告周期
 * @returns {Promise}
 */
export function validateReport(params) {
  return request({
    url: '/finance/api/report/validate',
    method: 'post',
    params
  });
}

/**
 * 清除报告缓存
 * @param {Object} params - 清除缓存参数
 * @param {string} params.groupid - 分组ID
 * @param {string} [params.templateCode] - 模板编码（可选）
 * @param {string} [params.period] - 报告周期（可选）
 * @returns {Promise}
 */
export function clearReportCache(params) {
  return request({
    url: '/finance/api/report/cache/clear',
    method: 'post',
    params
  });
}

