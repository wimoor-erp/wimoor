import request from '@/utils/request';
import downloadhandler from "@/utils/download-handler.js";
// 上传凭证Excel文件
export function uploadVoucherFile(FormData,groupid) {
  return request({
    method: 'POST',
    url: '/api/finance/vouchers/upload/uploadFile/'+groupid,
    data: FormData,
    headers: { 'Content-Type': 'multipart/form-data' }
  });
}

// 下载凭证导入模板
export function downloadVoucherTemplate(data) {
  return request({
    url: "/api/finance/vouchers/upload/downExcelTemp",
    responseType: "blob",
    params: data,
    method: 'get'
  }).then(res => {
    downloadhandler.downloadSuccess(res, "voucherTemplate.xlsx")
  }).catch(e => {
    downloadhandler.downloadFail(e);
  });
}

// 获取上传历史记录
export function getUploadHistory(data) {
  return request({
    url: '/api/finance/vouchers/upload/list',
    method: 'get',
    params:data
  })
}

// 下载处理结果文件
export function downloadResultFile(id) {
  return request({
    url: `/api/finance/voucher/downloadResult/${id}`,
    method: 'get',
    responseType: 'blob'
  })
}