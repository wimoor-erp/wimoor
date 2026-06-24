import request from "@/utils/request.js";
function getlist() {
	return request.get('/erp/api/v1/thirdparty/getList');
 }

function getAllSupportSystem(){
  return request.get('/erp/api/v1/thirdparty/getAllSupportSystem');
}

function update(data){
  return request.post('/erp/api/v1/thirdparty/update',data);
}

function save(data){
  return request.post('/erp/api/v1/thirdparty/save',data);
}
function deleteItem(data){
  return request.get('/erp/api/v1/thirdparty/disable',{params:data});
}
function enableItem(data){
  return request.get('/erp/api/v1/thirdparty/enable',{params:data});
}
function info(data){
  return request.get('/erp/api/v1/thirdparty/info',{params:data});
}
function getSupportApi(data){
  return request.get('/erp/api/v1/thirdparty/getSupportApi',{params:data});
}
function getQuoteToken(){
  return request.get('/erp/api/v1/thirdparty/getQuoteToken');
}
function unbindQuoteToken(){
  return request.get('/erp/api/v1/thirdparty/unbindQuoteToken');
}

function saveQuoteToken(data){
  return request.post('/erp/api/v1/thirdparty/saveQuoteToken',data);
}

 
export default{
	 getAllSupportSystem,getlist,update,save,deleteItem,info,enableItem,getSupportApi,getQuoteToken,unbindQuoteToken,saveQuoteToken
}