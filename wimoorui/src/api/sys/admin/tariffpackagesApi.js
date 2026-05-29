import request from "@/utils/request.js";
function list(data){ 
	 return request.get('/admin/api/v1/sysTariffPackages/list',{params:data});
}
function detail(data){
	 return request.get('/admin/api/v1/sysTariffPackages/detail',{params:data});
}
function save(data) {
  return request.post('/admin/api/v1/sysTariffPackages/save' ,data);
}
function updateItem(data) {
   return request.post('/admin/api/v1/sysTariffPackages/update' ,data);
}
function deleteItem(data) {
 return request.post('/admin/api/v1/sysTariffPackages/delete' ,data);
}
export default{
	list,detail, save,updateItem,deleteItem
}