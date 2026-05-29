import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function savePlanFrom(data){
	return request.post('/erp/api/v1/orderform/savePlanFrom',data);
}
function findPlanFrom(data){
	return request.post('/erp/api/v1/orderform/findPlanFrom',data);
}
function done(data){
	return request.post('/erp/api/v1/orderform/done',data);
}
function doneOut(data){
	return request.post('/erp/api/v1/orderform/doneOut',data);
}
function remove(data){
	return request.post('/erp/api/v1/orderform/remove',data);
}
function downloadOrderPlanForm(data,callback){
	return request({url:"/erp/api/v1/orderform/downloadOrderPlanForm",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"订单详情.xlsx")
											if(callback){
												callback(res);
											}
									}).catch(e=>{
											downloadhandler.downloadFail(e);
											if(callback){
												callback(e);
											}
									}); 
}
export default{
	savePlanFrom,findPlanFrom,done,remove,downloadOrderPlanForm,doneOut
}