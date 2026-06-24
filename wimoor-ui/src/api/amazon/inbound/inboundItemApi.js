import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getInboundItemBatchList(data){ 
	 return request.post('/amazon/api/v1/shipInboundItem/getInboundItemBatchList',data);
}
function findDetailByShipment(data){ 
	 return request.get('/amazon/api/v1/shipInboundItem/findDetailByShipment',{params:data});
}
function updateFeeByShipment(data){ 
	 return request.post('/amazon/api/v1/shipInboundItem/updateFeeByShipment',data);
}

 function downloadList(data,callback){
 	return request({url:"/amazon/api/v1/shipInboundItem/downloadList",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"downloadItemBatchList.xlsx")
 											if(callback){
 												callback();
 											}
 									}).catch(e=>{
 											downloadhandler.downloadFail(e);
 											if(callback){
 												callback();
 											}
 									}); 
 }

function updateReceiveDate(data){
	 return request.post('/amazon/api/v1/shipInboundItem/updateReceiveDate',data);
}
function handleResetFee(data){
	return request.get('/amazon/api/v1/shipInboundItem/handleResetFee',{params:data});
}

export default{
	 getInboundItemBatchList,findDetailByShipment,downloadList,updateFeeByShipment,updateReceiveDate,handleResetFee
}