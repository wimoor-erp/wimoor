import request from "@/utils/request.js";

/**
 * 获取路由列表
 */
export function listRoutes() {
  return request({
    url: '/admin/api/v1/menus/routes',
    method: 'get',
  });
}
export function roleMenuIds(queryParams) {
  return request({
    url: '/admin/api/v1/menus/rolemenuids',
    method: 'get',
	params: queryParams,
  });
}

/**
 * 获取菜单表格列表
 *
 * @param queryParams
 */
export function listMenus(queryParams) {
  return request({
    url: '/admin/api/v1/menus/table',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 获取菜单下拉树形列表
 */
export function listMenuOptions() {
  return request({
    url: '/admin/api/v1/menus/tree-select',
    method: 'get',
  });
}
/**
 * 获取菜单下拉树形列表
 */
export function listCompanyMenuTree() {
  return request({
    url: '/admin/api/v1/menus/companytree',
    method: 'get',
  });
}

/**
 * 获取资源(菜单+权限)树形列表
 */
export function listResources() {
  return request({
    url: '/admin/api/v1/menus/resources',
    method: 'get',
  });
}

/**
 * 获取菜单详情
 * @param id
 */
export function getMenuDetail(id){
  return request({
    url: '/admin/api/v1/menus/' + id,
    method: 'get',
  });
}

/**
 * 添加菜单
 *
 * @param data
 */
export function addMenu(data) {
  return request({
    url: '/admin/api/v1/menus',
    method: 'post',
    data: data,
  });
}

/**
 * 修改菜单
 *
 * @param id
 * @param data
 */
export function updateMenu(id, data) {
  return request({
    url: '/admin/api/v1/menus/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除菜单
 *
 * @param ids 菜单ID，多个以英文逗号(,)分割
 */
export function deleteMenus(ids) {
  return request({
    url: '/admin/api/v1/menus/' + ids,
    method: 'delete',
  });
}
