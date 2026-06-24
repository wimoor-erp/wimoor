 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
 function list(data){
 	 return request.post("/amazon/api/v1/amzStorageFee/list",data)
 }
 function storageList(data){
 	 return request.post("/amazon/api/v1/amzStorageFee/storageList",data)
 }
 function downloadList(data,callback){
 	  return request({url:"/amazon/api/v1/amzStorageFee/downloadList",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"AmzStorageFeeReport.xlsx");
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
 function downloadLongList(data,callback){
 	  return request({url:"/amazon/api/v1/amzStorageFee/downloadLongList",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"AmzLongStorageFeeReport.xlsx");
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
 	list,storageList,downloadLongList,downloadList,
 }