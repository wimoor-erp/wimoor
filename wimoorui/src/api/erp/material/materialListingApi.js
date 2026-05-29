/**
 * 产品多语言Listing信息API
 */
import request from "@/utils/request.js";

/**
 * 获取产品所有语言的Listing概要
 */
function getListingList(materialid) {
  return request.get('/erp/api/v1/material/listing/list', { params: { materialid } });
}

/**
 * 获取产品指定语言的完整Listing信息
 */
function getListingByLang(materialid, lang) {
  return request.get('/erp/api/v1/material/listing/get', { params: { materialid, lang } });
}

/**
 * 保存Listing信息
 */
function saveListing(data) {
  return request.post('/erp/api/v1/material/listing/save', data);
}

/**
 * 删除Listing记录
 */
function deleteListing(id) {
  return request.delete('/erp/api/v1/material/listing/delete', { params: { id } });
}

export {
  getListingList,
  getListingByLang,
  saveListing,
  deleteListing
};
