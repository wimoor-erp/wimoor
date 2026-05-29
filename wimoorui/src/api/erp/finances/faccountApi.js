 import request from "@/utils/request.js";
function getProject(data){
	return request.get('/erp/api/v1/faccount/getProject',{params:data});
}
function getPaymentMethod(data){
	return request.get('/erp/api/v1/faccount/getPaymentMethod',{params:data});
}
function getPaymentAccount(data){
	return request.get('/erp/api/v1/faccount/getPaymentAccount',{params:data});
}
function getAccountAll(data){
	return request.get('/erp/api/v1/faccount/getAccountAll',{params:data});
}
function updateAccountDefault(data){
	return request.post('/erp/api/v1/faccount/updateAccountDefault',data);
}
function cancelAccountDefault(data){
	return request.post('/erp/api/v1/faccount/cancelAccountDefault',data);
}

function updateAccountName(data){
	return request.post('/erp/api/v1/faccount/updateAccountName',data);
}
function saveAccount(data){
	return request.post('/erp/api/v1/faccount/saveAccount',data);
}
function updateAccountDelete(data){
	return request.post('/erp/api/v1/faccount/updateAccountDelete',data);
}
function findAccountArchiveAll(data){
	return request.get('/erp/api/v1/faccount/findAccountArchiveAll',{params:data});
}
function recoverAccountDelete(data){
	return request.post('/erp/api/v1/faccount/recoverAccountDelete',data);
}
function savePaymethodIndex(data){
	return request.post('/erp/api/v1/faccount/savePaymethodIndex',data);
}





 export default{
 	getProject,getPaymentMethod,getPaymentAccount,getAccountAll,updateAccountDefault,updateAccountName,saveAccount,updateAccountDelete,
	findAccountArchiveAll,recoverAccountDelete,cancelAccountDefault,savePaymethodIndex,
 }