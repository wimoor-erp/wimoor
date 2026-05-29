import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getOrderList(data){
	 return request.post('/amazon/api/v0/orders/manager/list',data);
}
function getOrderInvoinceList(data){
	 return request.post('/amazon/api/v0/orders/invoince/list',data);
}
function getOrderReturnList(data){
	 return request.post("/amazon/api/v0/orders/return/returnlist",data)
}
function getOrderReturnSKUList(data){
	 return request.post("/amazon/api/v0/orders/return/returnSKUlist",data)
}
function selectReturnsSummaryByDay(data){
	 return request.post("/amazon/api/v0/orders/return/selectReturnsSummaryByDay",data)
}
function selectReturnsSummaryByType(data){
	 return request.post("/amazon/api/v0/orders/return/selectReturnsSummaryByType",data)
}

function getOrderRemoveList(data){
	 return request.post("/amazon/api/v0/orders/remove/removelist",data)
}

function getOrderShipList(data){
	 return request.post("/amazon/api/v0/orders/ship/shiplist",data)
}
function showOrderDetail(data){
	 return request.get("/amazon/api/v0/orders/manager/showOrderDetail",{params:data })
}
function selectVatInfoByGroup(data){
	 return request.get("/amazon/api/v0/orders/invoince/selectVatInfoByGroup",{params:data })
}
function getOrderShipListExcel(data){
	  return request({url:"/amazon/api/v0/orders/manager/downloadOrderAddressList",
	                responseType:"blob",
					"data":data,
					method:'post'});
}
function saveOrderVat(FormData){
	return request({'method':'POST',
	                'url':"/amazon/api/v0/orders/invoince/saveOrderVat",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
	
}
function downloadOrderVatInvoice(data){
	return request({url:"/amazon/api/v0/orders/invoince/downloadOrderVatInvoice",
				    responseType:"blob",
					params:data,
					method:'get'});
}
function sendAmzVatInvoince(data){
	return request.get("/amazon/api/v0/orders/invoince/sendAmzVatInvoince",{params:data })
}

function downloadOrderList(data){
	return request({url:"/amazon/api/v0/orders/manager/downloadOrderList",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"ordersList.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									});
}
function downloadReturnlist(data){
	return request({url:"/amazon/api/v0/orders/return/downloadReturnlist",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"ordersReturn.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
function downloadRemovelist(data){
	return request({url:"/amazon/api/v0/orders/remove/downloadRemovelist",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"ordersRemove.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}

function downExcelTodayOrdersData(data){
	return request({url:"/amazon/api/v0/orders/today/downExcelTodayOrdersData",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,"salesToday.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
function todaylist(data){
	  return request.post('/amazon/api/v0/orders/today/todaylist',data);
}
function getParamOfTodayOrder(data){
	  return request.post('/amazon/api/v0/orders/today/getParamOfTodayOrder',data);
}
function sendReviewMessage(data){
	  return request.get('/amazon/api/v0/orders/manager/sendReviewMessage',{params:data});
}
function updateRemark(data){
	  return request.get('/amazon/api/v0/orders/manager/updateRemark',{params:data});
}
function getRemark(data){
	  return request.get('/amazon/api/v0/orders/manager/getRemark',{params:data});
}




export default{
	getOrderList,
	getOrderReturnList,
	getOrderRemoveList,
	getOrderShipList,
	getOrderShipListExcel,
	showOrderDetail,
	selectVatInfoByGroup,
	saveOrderVat,
	downloadOrderVatInvoice,
	sendAmzVatInvoince,
	downloadOrderList,
	getOrderInvoinceList,
	downloadReturnlist,
	todaylist,
	downloadRemovelist,
	getParamOfTodayOrder,
	downExcelTodayOrdersData,
	sendReviewMessage,
	updateRemark,
	getRemark,
	getOrderReturnSKUList,
	selectReturnsSummaryByDay,
	selectReturnsSummaryByType
}