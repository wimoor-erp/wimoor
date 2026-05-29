import request from "@/utils/request.js";

/**
 * 获取字典分页列表
 *
 * @param queryParams
 */
export function listPageDictTypes(queryParams){
  return request({
    url: '/admin/api/v1/dicts/page', 
    method: 'get',
    params: queryParams,
  });
}
export function listDictsByCode(dictCode) {
    return request({
        url: '/admin/api/v1/dict-items',
        method: 'get',
        params: { dictCode: dictCode ,name:''}
    })
}
/**
 * 获取字典详情
 *
 * @param id
 */
export function getDictFormData(id){
  return request({
    url: '/admin/api/v1/dicts/' + id ,
    method: 'get',
  });
}

/**
 * 新增字典类型
 *
 * @param data
 */
export function addDictType(data) {
  return request({
    url: '/admin/api/v1/dicts',
    method: 'post',
    data: data,
  });
}

/**
 * 修改字典类型
 *
 * @param id
 * @param data
 */
export function updateDictType(id, data) {
  return request({
    url: '/admin/api/v1/dicts/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除字典类型
 *
 * @param ids 字典类型ID，多个以英文逗号(,)分割
 */
export function deleteDictTypes(ids) {
  return request({
    url: '/admin/api/v1/dicts/' + ids,
    method: 'delete',
  });
}

/**
 * 获取字典项分页列表
 *
 * @param queryParams
 */
export function listPageDictItems(queryParams){
  return request({
    url: '/admin/api/v1/dict-items/page',
    method: 'get',
    params: queryParams,
  });
}

/**
 * 根据字典类型编码获取字典数据项
 *
 * @param typeCode 字典类型编码
 */
export function getDictItemsByTypeCode(typeCode) {
  return request({
    url: '/admin/api/v1/dict-items/select_list/'+typeCode,
    method: 'get',
  });
}

 
/**
 * 获取字典数据项表单
 *
 * @param id
 */
export function getDictItemData(id){
  return request({
    url: '/admin/api/v1/dict-items/' + id ,
    method: 'get',
  });
}

/**
 * 新增字典项
 *
 * @param data
 */
export function addDictItem(data) {
  return request({
    url: '/admin/api/v1/dict-items',
    method: 'post',
    data: data,
  });
}

/**
 * 修改字典项
 *
 * @param id
 * @param data
 */
export function updateDictItem(id, data) {
  return request({
    url: '/admin/api/v1/dict-items/' + id,
    method: 'put',
    data: data,
  });
}

/**
 * 批量删除字典数据项
 *
 * @param ids 字典项ID，多个以英文逗号(,)分割
 */
export function deleteDictItems(ids) {
  return request({
    url: '/admin/api/v1/dict-items/' + ids,
    method: 'delete',
  });
}
