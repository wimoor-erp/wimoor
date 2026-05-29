import request from "@/utils/request.js";
export function listRolePages(queryParams){
  return request({
    url: '/admin/api/v1/roles/pages',
    method: 'get',
    params: queryParams,
  });
}