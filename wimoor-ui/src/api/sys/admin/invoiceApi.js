import request from "@/utils/request.js";

function detail(data){ 
	 return request.get('/admin/api/v1/invoice/detail',{params:data});
}

function save(data) {
  return request.post('/admin/api/v1/invoice/save' ,data);
}

function upload(data) {
  return request({
    'method': 'POST',
    'url': '/admin/api/v1/invoice/upload',
    'data': data,
    'headers': { 'Content-Type': 'multipart/form-data' },
  });
}

export default{
	detail, save, upload
}