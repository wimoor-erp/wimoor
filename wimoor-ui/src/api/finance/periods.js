import request from '@/utils/request'

// 查询会计期间管理列表
export function listPeriods(query) {
  return request({
    url: '/api/finance/periods/list',
    method: 'get',
    params: query
  })
}

// 查询会计期间管理详细
export function getPeriods(periodId) {
  return request({
    url: '/api/finance/periods/' + periodId,
    method: 'get'
  })
}

// 新增会计期间管理
export function addPeriods(data) {
  return request({
    url: '/api/finance/periods',
    method: 'post',
    data: data
  })
}

// 修改会计期间管理
export function updatePeriods(data) {
  return request({
    url: '/api/finance/periods',
    method: 'put',
    data: data
  })
}

// 删除会计期间管理
export function delPeriods(periodId) {
  return request({
    url: '/api/finance/periods/' + periodId,
    method: 'delete'
  })
}

// 获取当前会计期间
export function getCurrentPeriod(groupId) {
  //console.log('调用getCurrentPeriod API，租户ID:', groupId);
  //console.log('API URL:', '/api/finance/periods/current');
  return request({
    url: '/api/finance/periods/current',
    method: 'get',
    params: { groupId }
  }).then(response => {
    //console.log('getCurrentPeriod API返回结果:', response);
    return response;
  }).catch(error => {
    console.error('getCurrentPeriod API调用失败:', error);
    console.error('错误详情:', error.response);
    console.error('错误状态:', error.response?.status);
    console.error('错误数据:', error.response?.data);
    throw error;
  });
}


// 查询会计期间管理列表
export function listGroups() {
  return request({
    url: '/api/finance/periods/groups',
    method: 'get'
  })
}

// 标记会计期间为已结账
export function closePeriod(periodId) {
  return request({
    url: '/api/finance/periods/close/' + periodId,
    method: 'put'
  })
}

// 获取下一个会计期间
export function getNextPeriod(groupId, currentPeriod) {
  return request({
    url: '/api/finance/periods/next',
    method: 'get',
    params: { groupId, currentPeriod }
  })
}

// 执行结账操作
export function closing(groupId, period) {
  return request({
    url: '/api/finance/periods/closing',
    method: 'get',
    params: { groupId, period }
  })
}

// 执行反结账操作
export function unclosing(groupId, period, reason) {
  return request({
    url: '/api/finance/periods/unclosing',
    method: 'get',
    params: { groupId, period, reason }
  })
}

// 修正会计期间状态
export function fixCurrentPeriod(groupId) {
  return request({
    url: '/api/finance/periods/fixCurrentPeriod',
    method: 'get',
    params: { groupId }
  })
}
