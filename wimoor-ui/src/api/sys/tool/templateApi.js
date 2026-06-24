import request from '@/utils/request'
import downloadhandler from "@/utils/download-handler.js";

// 模板文件API
export function uploadTemplateFile(data) {
  return request({
    url: '/admin/api/v2/template/uploadFile',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function downloadTemplateFile( data,name) {
  return request({
    url: "/admin/api/v2/template/downloadTemplateFile",
    responseType: "blob",
    method: 'post',
    data: data
  }).then(res => {
    downloadhandler.downloadSuccess(res, data.ftype+".xlsx");
  }).catch(e => {
    downloadhandler.downloadFail(e);
  });
}


export function getTemplateRecord() {
  return request({
    url: '/admin/api/v2/template/getRecord',
    method: 'get'
  })
}

export function deleteTemplateFile(data) {
  return request({
    url: '/admin/api/v2/template/deleteFile',
    method: 'get',
    params: data
  })
}