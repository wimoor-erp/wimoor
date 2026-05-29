/**
 * 商品媒体管理 API
 * 后端: wimoor-erp / MaterialMediaController (/erp/api/v1/material/media)
 */
import request from '@/utils/request.js'

const base = '/erp/api/v1/material/media'

/** 单文件上传（multipart） */
export function uploadMedia(formData) {
  return request({
    method: 'POST',
    url: `${base}/upload`,
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/** ZIP 批量上传 */
export function uploadMediaBatch(formData) {
  return request({
    method: 'POST',
    url: `${base}/uploadBatch`,
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/** SKU 展示图列表 */
export function listMedia(materialid) {
  return request.get(`${base}/list`, { params: { materialid } })
}

/** SPU 图片池 */
export function poolMedia(materialid) {
  return request.get(`${base}/pool`, { params: { materialid } })
}

/** 分配（图片关联到 material/slot） */
export function assignMedia(payload) {
  return request.post(`${base}/assign`, payload)
}

/** 批量分配 */
export function batchAssignMedia(payload) {
  return request.post(`${base}/batchAssign`, payload)
}

/** 取消分配 */
export function unassignMedia(refId) {
  return request({ method: 'DELETE', url: `${base}/unassign/${refId}` })
}

/** 设为主图 */
export function setMainMedia(refId) {
  return request({ method: 'PUT', url: `${base}/setMain/${refId}` })
}

/** 拖拽排序 */
export function sortMedia(refIds) {
  return request.post(`${base}/sort`, refIds)
}

/** 修改图片用途分类 */
export function updateUsage(refId, picClass) {
  return request({ method: 'PUT', url: `${base}/usage/${refId}`, params: { picClass } })
}

/** 删除媒体资源 */
export function deleteMedia(mediaId, force = false) {
  return request({ method: 'DELETE', url: `${base}/delete/${mediaId}`, params: { force } })
}

/** 从 Amazon 同步媒体 */
export function syncFromAmazon(payload) {
  return request.post(`${base}/syncFromAmazon`, payload)
}

export default {
  uploadMedia,
  uploadMediaBatch,
  listMedia,
  poolMedia,
  assignMedia,
  batchAssignMedia,
  unassignMedia,
  setMainMedia,
  sortMedia,
  updateUsage,
  deleteMedia,
  syncFromAmazon
}
