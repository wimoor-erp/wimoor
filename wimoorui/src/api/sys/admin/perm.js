import request from "@/utils/request.js";

/**
 * 获取权限分页列表
 *
 * @param queryParams
 */
export function listPermPages(queryParams){
  return request({
    url: '/admin/api/v1/permissions/page',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 获取权限列表
 *
 * @param queryParams
 */
export function listPerms(queryParams){
  return request({
    url: '/admin/api/v1/permissions',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 获取权限详情
 *
 * @param id
 */
export function getPermFormDetail(id){
  return request({
    url: '/admin/api/v1/permissions/' + id,
    method: 'get',
  });
}

/**
 * 添加权限
 *
 * @param data
 */
export function addPerm(data) {
  return request({
    url: '/admin/api/v1/permissions',
    method: 'post',
    data: data,
  });
}

/**
 * 更新权限
 *
 * @param id
 * @param data
 */
export function updatePerm(id, data) {
  return request({
    url: '/admin/api/v1/permissions/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除权限，多个以英文逗号(,)分割
 *
 * @param ids
 */
export function deletePerms(ids) {
  return request({
    url: '/admin/api/v1/permissions/' + ids,
    method: 'delete',
  });
}
