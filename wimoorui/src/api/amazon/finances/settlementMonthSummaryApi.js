import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 
 function list(data){
 	 return request.post("/amazon/api/v1/settlementSummarySkuMonth/list",data)
 } 
 function getRefreshData(data){
 	 return request.post("/amazon/api/v1/settlementSummarySkuMonth/getRefreshData",data)
 } 
 
 function downloadList(data,callback){
 	  return request({url:"/amazon/api/v1/settlementSummarySkuMonth/downloadList",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"AmzFinSettmentMonthFeeReport.xlsx");
 						if(callback){
 							callback();
 						}
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 						if(callback){
 							callback(e);
 						}
 				});;
 } 
 
 export default{
 	list,downloadList,getRefreshData
 }