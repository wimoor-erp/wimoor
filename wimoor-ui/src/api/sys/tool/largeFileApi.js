import request from "@/utils/request.js";
function upload(type,FormData){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/upload/"+type,
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
	       });
}
function deleteFile(type,fileUrl){
	return request({'method':'GET',
	                 'url':"/admin/api/v1/file/delete/"+type,
					'params':{'path':fileUrl},
	       });
}
export default{
	upload,
	deleteFile
}