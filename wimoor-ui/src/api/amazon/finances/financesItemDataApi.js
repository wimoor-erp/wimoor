import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 function getFinDataList(data){
 	 return request.post("/amazon/api/v1/amzFinUserItemData/getFinDataList",data)
 }
 function getFinDataMonthList(data){
 	 return request.post("/amazon/api/v1/amzFinUserItemData/getFinDataMonthList",data)
 }
 function saveFinItemData(data){
 	 return request.post("/amazon/api/v1/amzFinUserItemData/saveFinItemData",data);
 }
 function deleteFinItemData(data){
 	 return request.get("/amazon/api/v1/amzFinUserItemData/deleteFinItemData",{params:data});
 }
 function downFinItemDataTemp(callback){
	  return request({url:"/amazon/api/v1/amzFinUserItemData/downFinItemDataTemp",
			responseType:"blob",
			method:'get'}).then(res => {
				downloadhandler.downloadSuccess(res,"settlementOtherTemplate.xlsx");
				if(callback){
					callback();
				}
		}).catch(e=>{
				downloadhandler.downloadFail(e);
				if(callback){
					callback(e);
				} 
		});
 }
 
 
 
 function downOtherFee(data,callback){
 	  return request({url:"/amazon/api/v1/amzFinUserItemData/downOtherFee",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"OtherFee.xlsx");
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
 function uploadFile(FormData){
	 return request({'method':'POST',
	                 'url':"/amazon/api/v1/amzFinUserItemData/uploadFile",
	 			     'data':FormData,
	 				 'headers':{'Content-Type':"multipart/form-data"},
	 });
 }
 
 export default{
 	getFinDataList,getFinDataMonthList,downFinItemDataTemp,downOtherFee,uploadFile,saveFinItemData,deleteFinItemData
 }