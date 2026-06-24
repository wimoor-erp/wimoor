import request from "@/utils/request.js";

function list(){
	return request({'method':'GET',
	                 'url':"/admin/api/v1/file/userfile/list",
	       });
}
function listCompany(){
	return request({'method':'GET',
	                 'url':"/admin/api/v1/file/userfile/listCompany",
	       });
}
function upload(FormData){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/upload",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
	       });
}
function uploadCompany(FormData){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/uploadCompany",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
	       });
}
function rename(id, name){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/rename",
					'params':{'id':id, 'name':name},
	       });
}
function renameCompany(id, name){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/renameCompany",
					'params':{'id':id, 'name':name},
	       });
}
function remove(id){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/delete",
					'params':{'id':id},
	       });
}
function removeCompany(id){
	return request({'method':'POST',
	                 'url':"/admin/api/v1/file/userfile/deleteCompany",
					'params':{'id':id},
	       });
}
function download(id){
	return request({'method':'GET',
	                 'url':"/admin/api/v1/file/userfile/download",
					'params':{'id':id},
					'responseType':'blob',
	       });
}
function getLink(id){
	return request({'method':'GET',
	                 'url':"/admin/api/v1/file/userfile/link",
					'params':{'id':id},
	       });
}
export default{
	list, listCompany, upload, uploadCompany, rename, renameCompany, remove, removeCompany, download, getLink
}