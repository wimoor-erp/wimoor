import request from "@/utils/request.js";

/**
 * 获取角色分页数据
 *
 * @param queryParams
 */
export function listRolePages(queryParams){
  return request({
    url: '/admin/api/v1/roles/page',
    method: 'get',
    params: queryParams,
  });
}
/**
 * 获取角色分页数据
 *
 * @param queryParams
 */
export function listRole(){
  return request({url: '/admin/api/v1/roles',method: 'get',});
}
/**
 * 获取角色下拉数据
 *
 * @param queryParams
 */
export function listRoleOptions(queryParams){
  return request({
    url: '/admin/api/v1/roles/options',
    method: 'get',
    params: queryParams,
  });
}
 
/**
 * 获取角色拥有的菜单ID集合
 *
 * @param queryParams
 */
export function getRoleMenus(roleId){
  return request({
    url: '/admin/api/v1/roles/' + roleId + '/menus',
    method: 'get',
  });
}

/**
 * 修改角色菜单权限
 *
 * @param queryParams
 */
export function updateRoleMenu(roleId,data){
  return request({
    url: '/admin/api/v1/roles/' + roleId + '/menus',
    method: 'put',
    data: data,
  });
}
/**
 * 获取角色拥有的资源ID集合
 *
 * @param queryParams
 */
export function getRoleResources(roleId,queryParams){
  return request({
    url: '/admin/api/v1/roles/' + roleId + '/permissions',
    method: 'get',
	params: queryParams,
  });
}

/**
 * 修改角色资源权限
 *
 * @param queryParams
 */
export function updateRoleResource(roleId,data){
  return request({
    url: '/admin/api/v1/roles/' + roleId + '/permissions',
    method: 'put',
    data: data,
  });
}

/**
 * 获取角色详情
 *
 * @param id
 */
export function getRoleFormDetail(id){
  return request({
    url: '/admin/api/v1/roles/' + id,
    method: 'get',
  });
}

/**
 * 添加角色
 *
 * @param data
 */
export function addRole(data) {
  return request({
    url: '/admin/api/v1/roles',
    method: 'post',
    data: data,
  });
}

/**
 * 编辑角色
 *
 * @param id
 * @param data
 */
export function updateRole(id, data) {
  return request({
    url: '/admin/api/v1/roles/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除角色，多个以英文逗号(,)分割
 *
 * @param ids
 */
export function deleteRoles(ids) {
  return request({
    url: '/admin/api/v1/roles/' + ids,
    method: 'delete',
  });
}
