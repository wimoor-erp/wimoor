import request from "@/utils/request.js";
function saveStock(data){
  return request.get('/erp/api/v1/thirdparty/warehouse/saveStock',{params:data});
}
function list(data){
  return request.get('/erp/api/v1/thirdparty/warehouse/list',{params:data});
}
function searchStock(data){
  return request.post('/erp/api/v1/thirdparty/warehouse/searchStock',data);
}
function order(data){
  return request.post('/erp/api/v1/thirdparty/warehouse/order',data);
}
function bindlist(data){
  return request.post('/erp/api/v1/thirdparty/warehouse/bindlist',data);
}
function savebind(data){
  return request.post('/erp/api/v1/thirdparty/warehouse/savebind',data);
}
function deletebind(data){
  return request.post('/erp/api/v1/thirdparty/warehouse/deletebind',data);
}
 
export default{
	 saveStock,list,searchStock,bindlist,savebind,deletebind,order
}