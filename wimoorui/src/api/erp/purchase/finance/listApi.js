import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function list(data){
	return request.post('/erp/api/v1/purchase_finance_form/list',data);
}
function getdetail(data){
	return request.get('/erp/api/v1/purchase_finance_form/getdetail',{params:data});
}
function approve(data){
	return request.post('/erp/api/v1/purchase_finance_form/approve',data);
}
function approveReturn(data){
	return request.post('/erp/api/v1/purchase_finance_form/approveReturn',data);
}
function updatePay(data){
	return request.post('/erp/api/v1/purchase_finance_form/updatePay',data);
}
function updateRemark(data){
	return request.get('/erp/api/v1/purchase_finance_form/updateRemark',{params:data});
}
function getData(data){
	return request.post('/erp/api/v1/purchase_finance_form/getData',data);
}
function paymentForm(data){
	return request.post('/erp/api/v1/purchase_finance_form/paymentForm',data);
}



 

export default{
	list,getdetail,approve,updatePay,updateRemark,approveReturn,getData,paymentForm,
}