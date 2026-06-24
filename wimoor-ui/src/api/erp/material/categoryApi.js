/**
 * 分类
 */
import request from "@/utils/request.js";

function saveData(data){
	return request.post('/erp/api/v1/materialCategory/saveData',data);
}
function list(data){
	return request.post('/erp/api/v1/materialCategory/list',data);
}
function getcategory(data){
	return request.get('/erp/api/v1/materialCategory/getcategory',{params:data});
}
function delcategory(data){
	return request.get('/erp/api/v1/materialCategory/delcategory',{params:data});
}
function getcategoryType(data){
	return request.get('/erp/api/v1/materialCategory/getcategoryType',{params:data});
}


export default{
	 saveData,list,getcategory,delcategory,getcategoryType
}