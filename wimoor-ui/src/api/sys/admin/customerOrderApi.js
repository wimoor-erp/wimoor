import request from "@/utils/request.js";
function list(data){ 
	 return request.post('/admin/api/v1/sysCustomerOrder/list', data);
}
function listAll(data){
    return request.post('/admin/api/v1/sysCustomerOrder/listAll', data);
}
function detail(data){
	 return request.get('/admin/api/v1/sysCustomerOrder/detail',{params:data});
}
function save(data) {
  return request.post('/admin/api/v1/sysCustomerOrder/save' ,data);
}
function updateItem(data) {
   return request.post('/admin/api/v1/sysCustomerOrder/update' ,data);
}
function deleteItem(data) {
 return request.post('/admin/api/v1/sysCustomerOrder/delete' ,data);
}
export default{
	list,detail, save,updateItem,deleteItem,listAll
}
