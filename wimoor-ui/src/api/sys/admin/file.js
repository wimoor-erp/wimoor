import request from "@/utils/request.js";

/**
 * 上传文件
 *
 * @param file
 */
export function uploadFile(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: '/admin/api/v1/files',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

/**
 * 删除文件
 *
 * @param path
 */
export function deleteFile(path) {
  return request({
    url: '/admin/api/v1/files',
    method: 'delete',
    params: { path: path },
  });
}
