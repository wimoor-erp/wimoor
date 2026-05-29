

import request from "@/utils/request.js";
function getPlanList(data){
	return request.post('/amazon/api/v1/shipForm/getPlanList',data);
}
function getProductInfoList(data){
	return request.post('/amazon/api/v1/report/product/productInfo/getProductInfoList',data);
}
function guidance(data){
	return request.get('/amazon/api/v1/shipForm/guidance',{params:data});
}
function saveInboundPlan(data){
	return request.post('/amazon/api/v1/shipInboundPlan/saveInboundPlan',data);
}
function findPlanSubDetail(data){
	return request.get('/amazon/api/v1/shipForm/findPlanSubDetail',{params:data});
}
function getPlanInfo(data){
	return request.get('/amazon/api/v1/shipForm/getPlanInfo',{params:data});
}
function updatePlanRemark(data){
	return request.get('/amazon/api/v1/shipForm/updatePlanRemark',{params:data});
}
function updateShipmentRemark(data){
	return request.get('/amazon/api/v1/shipForm/updateShipmentRemark',{params:data});
}
function updateShipmentReferenceid(data){
	return request.get('/amazon/api/v1/shipForm/updateShipmentReferenceid',{params:data});
}

function getItemlistByShipmentId(data){
	return request.get('/amazon/api/v1/shipForm/getItemlistByShipmentId',{params:data});
}
function getItemlistByInboundPlanId(data){
	return request.get('/amazon/api/v1/shipForm/getItemlistByInboundPlanId',{params:data});
}

function saveTrans(data){
	return request.get('/amazon/api/v1/shipForm/saveTrans',{params:data});
}
function createShipment(data){
	return request.get('/erp/api/v1/shipForm/createShipment',{params:data});
}
function handlerExpShipment(data){
	return request.get('/erp/api/v1/shipForm/handlerExpShipment',{params:data});
}
function cancelShipment(data){
	return request.get('/amazon/api/v1/shipForm/cancelShipment',{params:data});
}
function updatePlan(data){
	return request.get('/amazon/api/v1/shipForm/updatePlan',{params:data});
}
function downExcelLabelBySku(data){
	return request({url:"/amazon/api/v1/shipForm/downExcelLabelBySku",
				    responseType:"blob",
					params:data,
					method:'get'});
}
function downExcelTemp(data){
	return request({url:"/amazon/api/v1/shipForm/downExcelTemp",
				    responseType:"blob",
					params:data,
					method:'get'});
}
function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/amazon/api/v1/shipForm/uploadExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}







 
export default{
	getPlanList,getProductInfoList,guidance,saveInboundPlan,findPlanSubDetail,getPlanInfo,updatePlanRemark,uploadExcel,
	updateShipmentRemark,updateShipmentReferenceid,getItemlistByShipmentId,saveTrans,createShipment,cancelShipment,updatePlan,downExcelLabelBySku,
	downExcelTemp,handlerExpShipment,getItemlistByInboundPlanId
}