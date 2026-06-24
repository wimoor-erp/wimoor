 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
function saveItem(data){
	return request.post('/chelsea/api/v1/chelsea/electronics/save',data);
}
function vatFc(data){
	return request.post('/chelsea/api/v1/chelsea/electronics/vatFc',data);
}
function eprEEE(data){
	return request.post('/chelsea/api/v1/chelsea/electronics/eprEEE',data);
}
 
function getProductInfoList(data){
	return request.post('/chelsea/api/v1/chelsea/electronics/getProductInfoList',data);
}

 function downloadElectronicsExcel(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/electronics/downloadElectronicsExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"ProductElectronicsExcel.xlsx")
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
 

 function uploadElectronicsExcel(FormData){
 	return request({'method':'POST',
 	                 'url':"/chelsea/api/v1/chelsea/electronics/uploadElectronicsExcel",
 				     'data':FormData,
 					 'headers':{'Content-Type':"multipart/form-data"},
 	});
 }

 function uploadSalesExcel(FormData){
 	return request({'method':'POST',
 	                 'url':"/chelsea/api/v1/chelsea/electronics/uploadSalesExcel",
 				     'data':FormData,
 					 'headers':{'Content-Type':"multipart/form-data"},
 	});
 }


 export default{
 	getProductInfoList,saveItem,downloadElectronicsExcel,vatFc,eprEEE,uploadElectronicsExcel,uploadSalesExcel
 }