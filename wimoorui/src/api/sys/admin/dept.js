import request from "@/utils/request.js";


/**
 * 部门树形表格
 *
 * @param queryParams
 */
export function listDepartments( queryParams){
  return request({
    url: '/admin/api/v1/depts/table',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 部门下拉列表
 */
export function listSelectDepartments(){
  return request({
    url: '/admin/api/v1/depts/select',
    method: 'get',
  });
}

/**
 * 获取部门详情
 *
 * @param id
 */
export function getDeptForrmData(id) {
  return request({
    url: '/admin/api/v1/depts/' + id ,
    method: 'get',
  });
}

/**
 * 新增部门
 *
 * @param data
 */
export function addDept(data) {
  return request({
    url: '/admin/api/v1/depts',
    method: 'post',
    data: data,
  });
}

/**
 *  修改部门
 *
 * @param id
 * @param data
 */
export function updateDept(id, data) {
  return request({
    url: '/admin/api/v1/depts/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 删除部门
 *
 * @param ids
 */
export function deleteDept(ids) {
  return request({
    url: '/admin/api/v1/depts/' + ids,
    method: 'delete',
  });
}
