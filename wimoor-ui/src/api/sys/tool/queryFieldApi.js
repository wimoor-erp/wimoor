import request from "@/utils/request.js";

 
function getMyVersionFieldByUser(data){
	 return request.get('/admin/api/v1/sysQueryField/getMyVersionFieldByUser',{params:data});
}
function deleteMyVersionField(data){
	 return request.get('/admin/api/v1/sysQueryField/deleteMyVersionField',{params:data});
}
function saveMyVersionFieldWithName(data){
	 return request.post('/admin/api/v1/sysQueryField/saveMyVersionFieldWithName',data);
}
function loadfield(data){
	 return request.get('/admin/api/v1/sysQueryField/loadfield',{params:data});
}
function saveMyVersionField(queryname,data){
	 return request.post('/admin/api/v1/sysQueryField/saveMyVersionField/'+queryname,data);
}
function getVersionFieldByUserQueryName(data){
	 return request.get('/admin/api/v1/sysQueryField/getVersionFieldByUserQueryName',{params:data});
}
function getVersionFieldById(data){
	 return request.get('/admin/api/v1/sysQueryField/getVersionFieldById',{params:data});
}




export default{
	 getMyVersionFieldByUser,deleteMyVersionField,getVersionFieldById,
	 saveMyVersionFieldWithName,loadfield,saveMyVersionField,getVersionFieldByUserQueryName,
}