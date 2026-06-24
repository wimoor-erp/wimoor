 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
 function list(data){
 	 return request.post("/amazon/api/v1/amzReimbursementsFee/list",data)
 }
 function downloadList(data,callback){
 	  return request({url:"/amazon/api/v1/amzReimbursementsFee/downloadList",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"AmzReimbursementsFeeReport.xlsx");
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
 	list,downloadList,
 }