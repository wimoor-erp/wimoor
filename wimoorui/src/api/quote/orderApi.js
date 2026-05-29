import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function saveShipment(data){
	return request.post('/quote/api/v1/quote/save',data);
}
function listShipment(data){
	return request.post('/quote/api/v1/quote/listShipment',data);
}
function addBuyer(data){
	return request.post('/quote/api/v1/quote/addBuyer',data);
}
function getBuyer(data){
	return request.get('/quote/api/v1/quote/getBuyer',{params:data});
}
function listOrder(data){
	return request.post('/quote/api/v1/quote/listOrder',data);
}
function saveOrder(data,token){
	return request.post('/quote/api/v1/quote/saveOrder/'+token,data);
}
function confirmPrice(data){
	return request.get('/quote/api/v1/quote/confirmPrice',{params:data});
}
 
 function updateShipmentRemark(data){
 	return request.get('/quote/api/v1/quote/updateShipmentRemark',{params:data});
 }
 function updateOrderRemark(data){
 	return request.post('/quote/api/v1/quote/updateOrderRemark',data);
 }
 function updateOrderStatus(data){
 	return request.get('/quote/api/v1/quote/updateOrderStatus',{params:data});
 }
 function deleteShipment(data){
 	return request.post('/quote/api/v1/quote/deleteShipment',data);
 }
 function deleteOrderShipment(data){
 	return request.get('/quote/api/v1/quote/deleteOrderShipment',{params:data});
 }	 
 function addOrderShipment(data){
 	return request.post('/quote/api/v1/quote/addOrderShipment',data);
 }
 
 function downloadOrderExport(data){
 	return request({url:"/quote/api/v1/quote/downloadOrderExport",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 											downloadhandler.downloadSuccess(res,"OrderDetail.xlsx")
 									}).catch(e=>{
 											downloadhandler.downloadFail();
 									}); 
 }
 function showDetail(data){
 	return request.get('/quote/api/v1/quote/showDetail',{params:data});
 }	 
 
 function refreshShipment(data){
	 return request.post('/amazon/api/v2/shipInboundPlan/transportation/refreshShipment',data);
 }
 
 
 export default{
 	saveShipment,listShipment,saveOrder,listOrder,confirmPrice,addBuyer,getBuyer,updateShipmentRemark,
	updateOrderRemark,updateOrderStatus,deleteShipment,deleteOrderShipment,addOrderShipment,downloadOrderExport,
	showDetail,refreshShipment
 }
