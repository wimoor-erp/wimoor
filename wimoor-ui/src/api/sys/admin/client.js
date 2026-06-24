import request from "@/utils/request.js";


export function listClientPages(queryParams){
  return request({
    url: '/admin/api/v1/oauth-clients',
    method: 'get',
    params: queryParams,
  });
}

export function getClientFormDetial(id){
  return request({
    url: '/admin/api/v1/oauth-clients/' + id,
    method: 'get',
  });
}

export function addClient(data) {
  return request({
    url: '/admin/api/v1/oauth-clients',
    method: 'post',
    data: data,
  });
}

export function updateClient(id, data) {
  return request({
    url: '/admin/api/v1/oauth-clients/' + id,
    method: 'put',
    data: data,
  });
}

export function deleteClients(ids) {
  return request({
    url: '/admin/api/v1/oauth-clients/' + ids,
    method: 'delete',
  });
}

export function updateClientPart(id, data) {
  return request({
    url: '/admin/api/v1/oauth-clients/' + id,
    method: 'patch',
    data: data,
  });
}
