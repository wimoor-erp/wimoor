import request from "@/utils/request.js"; 
function getAuthData(){
	 return request.get('/erp/api/v1/purchase1688/getAuthData');
}
function get1688Url(countid){
	let obj={}
	let url = location.origin+location.pathname;
	obj.redirectUrl = url;
	obj.id = countid
	request.get("/erp/api/v1/purchase1688/get1688Url",{params:obj}).then((res)=>{
		window.open(res.data)
	})
}
function logicDelete(data){
	 return request.get("/erp/api/v1/purchase1688/delete",{params:data})
}
function refreshAuthData(data){
	 return request.get("/erp/api/v1/purchase1688/refreshAuthData",{params:data})
}
function bindAuthData(data){
	 return request.get("/erp/api/v1/purchase1688/bindAuthData",{params:data})
}
function submitname(data){
	 return request.post("/erp/api/v1/purchase1688/submitname",data)
}
function updateName(data){
	 return request.get("/erp/api/v1/purchase1688/updateName",{params:data})
}
function bindAlibabaOrder(data){
 	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/bindAlibabaOrder',{params:data});
 }
function unbindAlibabaOrder(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/unbindAlibabaOrder',{params:data});
}
function getAlibabaOrder(data){
return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/getAlibabaOrder',{params:data});
}
function getAddress(data){
return request.get('/erp/api/v1/purchase1688/getAddress',{params:data});
}
function addFeedback(data){
return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/addFeedback',{params:data});
}
function cancelOrder(data){
return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/cancelOrder',{params:data});
}
function productInfo(data){
return request.get('/erp/api/v1/purchase1688/productInfo',{params:data});
}
function productInfoByEntry(data){
return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/productInfoByEntry',{params:data});
}
function bindProductInfoByEntry(data){
   return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/bindProductInfoByEntry',data);
}
function preview(data){
return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/preview',data);
}
function catchLogisticsInfo(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/catchLogisticsInfo',{params:data});
}
function getBindProductInfoByEntry(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/getBindProductInfoByEntry',{params:data});
}
function createCrossOrder(data){
return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/createCrossOrder',data);
}
function payWay(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/payWay',{params:data});
}
function payUrl(data){
	return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/payUrl',data);
}
function getRefundReasonList(data){
	return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/getRefundReasonList',data);
}
function createRefund(data){
	return request.post('/erp/api/v1/purchase/alibaba/entry/alibabainfo/createRefund',data);
}
function getLogisticsId(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/getLogisticsId',{params:data});
}
function bindLogisticsId(data){
	return request.get('/erp/api/v1/purchase/alibaba/entry/alibabainfo/bindLogisticsId',{params:data});
}
		
export default{
	getAuthData,
	get1688Url,
	logicDelete,
	bindAuthData,
	updateName,
	submitname,
	refreshAuthData,
	bindAlibabaOrder,
	unbindAlibabaOrder,
	getAlibabaOrder,
	getAddress,
	addFeedback,
	cancelOrder,
	productInfo,
	productInfoByEntry,
	bindProductInfoByEntry,
	preview,
	catchLogisticsInfo,
	getBindProductInfoByEntry,
	createCrossOrder,
	payWay,
	payUrl,
	getRefundReasonList,
	createRefund,
	getLogisticsId,
	bindLogisticsId
}