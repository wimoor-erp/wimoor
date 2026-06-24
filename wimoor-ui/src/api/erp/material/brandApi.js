/**
 * 品牌
 */
import request from "@/utils/request.js";

function saveData(data){
	return request.post('/erp/api/v1/materialBrand/saveData',data);
}
function list(data){
	return request.post('/erp/api/v1/materialBrand/list',data);
}
function getBrand(data){
	return request.get('/erp/api/v1/materialBrand/getBrand',{params:data});
}
function delBrand(data){
	return request.get('/erp/api/v1/materialBrand/delBrand',{params:data});
}


export default{
	 saveData,list,getBrand,delBrand
}