import request from "@/utils/request.js";

/**
 * 登录成功后获取用户信息（昵称、头像、权限集合和角色集合）
 */
export function getUserInfo(){
  return request({
    url: '/admin/api/v1/users/me',
    method: 'get',
  });
}

/**
 * 获取用户分页列表
 *
 * @param queryParams
 */
export function listUserPages(data){
  return request.post('/admin/api/v1/users/list',data);
}

/**
 * 获取用户表单详情
 *
 * @param userId
 */
export function getUserFormData(userId){
  return request({
    url: '/admin/api/v1/users/sysrole/userid/' + userId ,
    method: 'get',
  });
}

/**
 * 添加用户
 *
 * @param data
 */
export function addUser(data) {
  return request({
    url: '/admin/api/v1/users',
    method: 'post',
    data: data,
  });
}

/**
 * 修改用户
 *
 * @param id
 * @param data
 */
export function updateUser(id, data) {
  return request({
    url: '/admin/api/v1/users/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 修改用户状态
 *
 * @param id
 * @param status
 */
export function updateUserStatus(id, status) {
  return request({
    url: '/admin/api/v1/users/' + id + '/status',
    method: 'patch',
    params: { status: status },
  });
}

/**
 * 修改用户密码
 *
 * @param id
 * @param password
 */
export function updateUserPassword(id, password) {
  return request({
    url: '/admin/api/v1/users/' + id + '/password',
    method: 'patch',
    params: { password: password },
  });
}

/**
 * 删除用户
 *
 * @param ids
 */
export function deleteUsers(ids) {
  return request({
    url: '/admin/api/v1/users/' + ids,
    method: 'delete',
  });
}

export function disableUsers(ids) {
  return request({
    url: '/admin/api/v1/users/diable/' + ids,
    method: 'post',
  });
}

export function enableUsers(ids) {
  return request({
    url: '/admin/api/v1/users/enable/' + ids,
    method: 'post',
  });
}
/**
 * 下载用户导入模板
 *
 * @returns
 */
export function downloadTemplate() {
  return request({
    url: '/admin/api/v1/users/template',
    method: 'get',
    responseType: 'arraybuffer',
  });
}

/**
 * 导出用户
 *
 * @param queryParams
 * @returns
 */
export function exportUser(queryParams) {
  return request({
    url: '/admin/api/v1/users/_export',
    method: 'get',
    params: queryParams,
    responseType: 'arraybuffer',
  });
}

/**
 * 导入用户
 *
 * @param file
 */
export function importUser(deptId, roleIds, file) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('deptId', deptId.toString());
  formData.append('roleIds', roleIds);
  return request({
    url: '/admin/api/v1/users/_import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}



