 import request from "@/utils/request.js";

 
function listSupplierOrder(data){
	return request.post('/quote/api/v1/quote/supplier/listSupplierOrder',data);
}
function listsupplier(data){
	return request.post('/quote/api/v1/quote/supplier/listSupplier',data);
}
function sendOrderToSupplier(data){
	return request.post('/quote/api/v1/quote/supplier/sendOrderToSupplier',data);
}

function submitPrice(token,data){
	return request.post('/quote/api/v1/quote/supplier/submitPrice/'+token,data);
}
function addsupplier(token,data){
	return request.post('/quote/api/v1/quote/supplier/addSupplier/'+token,data);
}
function updatesupplier(data){
	return request.post('/quote/api/v1/quote/supplier/updateSupplier',data);
}
function deletesupplier(data){
	return request.get('/quote/api/v1/quote/supplier/deleteSupplier',{params:data});
}
function deleteSupplierPrice(data){
	return request.get('/quote/api/v1/quote/supplier/deleteSupplierPrice',{params:data});
}
function updateBase(data){
	return request.get('/quote/api/v1/quote/supplier/updateBase',{params:data});
}
function submitlogin(token,data){
	return request.post('/quote/api/v1/quote/supplier/submitPrice/'+token+"/login",data);
} 
 export default{
 	 listSupplierOrder,listsupplier,sendOrderToSupplier,submitPrice,addsupplier,updatesupplier,
	 deletesupplier,deleteSupplierPrice,updateBase,submitlogin
 }
