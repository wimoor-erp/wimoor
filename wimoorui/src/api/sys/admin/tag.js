import request from "@/utils/request.js";

/**
 * 获取标签分页列表
 *
 * @param queryParams
 */
export function listPageDictTypes(queryParams){
  return request({
    url: '/admin/api/v1/sysTagsGroups/page', 
    method: 'get',
    params: queryParams,
  });
}
export function listDictsByCode(dictCode) {
    return request({
        url: '/admin/api/v1/sysTags',
        method: 'get',
        params: { dictCode: dictCode ,name:''}
    })
}
/**
 * 获取标签详情
 *
 * @param id
 */
export function getDictFormData(id){
  return request({
    url: '/admin/api/v1/sysTagsGroups/' + id ,
    method: 'get',
  });
}

/**
 * 新增标签类型
 *
 * @param data
 */
export function addDictType(data) {
  return request({
    url: '/admin/api/v1/sysTagsGroups',
    method: 'post',
    data: data,
  });
}

/**
 * 修改标签类型
 *
 * @param id
 * @param data
 */
export function updateDictType(id, data) {
  return request({
    url: '/admin/api/v1/sysTagsGroups/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除标签类型
 *
 * @param ids 标签类型ID，多个以英文逗号(,)分割
 */
export function deleteDictTypes(ids) {
  return request({
    url: '/admin/api/v1/sysTagsGroups/' + ids,
    method: 'delete',
  });
}

/**
 * 获取标签项分页列表
 *
 * @param queryParams
 */
export function listPageDictItems(queryParams){
  return request({
    url: '/admin/api/v1/sysTags/page',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 根据标签类型编码获取标签数据项
 *
 * @param typeCode 标签类型编码
 */
export function getDictItemsByTypeCode(typeCode) {
  return request({
    url: '/admin/api/v1/sysTags/select_list/'+typeCode,
    method: 'get',
  });
}

 
/**
 * 获取标签数据项表单
 *
 * @param id
 */
export function getDictItemData(id){
  return request({
    url: '/admin/api/v1/sysTags/' + id ,
    method: 'get',
  });
}

/**
 * 新增标签项
 *
 * @param data
 */
export function addDictItem(data) {
  return request({
    url: '/admin/api/v1/sysTags',
    method: 'post',
    data: data,
  });
}

/**
 * 修改标签项
 *
 * @param id
 * @param data
 */
export function updateDictItem(id, data) {
  return request({
    url: '/admin/api/v1/sysTags/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除标签数据项
 *
 * @param ids 标签项ID，多个以英文逗号(,)分割
 */
export function deleteDictItems(ids) {
  return request({
    url: '/admin/api/v1/sysTags/' + ids,
    method: 'delete',
  });
}

export function getAllTags() {
  return request({
    url: '/admin/api/v1/sysTagsGroups/getAllTags',
    method: 'get'
  });
}
