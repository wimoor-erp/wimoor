 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
 
 function downExcelTemp(data,action){
 	var myActionUrl='/chelsea/api/v1/chelsea/ship/downExcelTemp';
 	if(action&&action.indexOf("/")>=0){
 		 myActionUrl=action;
 	} 
 	return request({url:myActionUrl,
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 						downloadhandler.downloadSuccess(res,data.ftype+".xlsx");
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 				});
 }
 
function columnWeeks(data){
	return request.post('/chelsea/api/v1/chelsea/ship/columnWeeks',data);
}
 
function list(data){
	return request.post('/chelsea/api/v1/chelsea/ship/list',data);
}
 
function save(data){
	return request.post('/chelsea/api/v1/chelsea/ship/save',data);
}
function refresh(data){
	return request.get('/chelsea/api/v1/chelsea/ship/refresh',{params:data});
}
 function uploadFile(FormData){
 	return request({'method':'POST',
 	                 'url':"/chelsea/api/v1/chelsea/ship/uploadExcel",
 				     'data':FormData,
 					 'headers':{'Content-Type':"multipart/form-data"},
 				
 	});
 }
 export default{
 	list,columnWeeks,uploadFile,save,downExcelTemp,refresh
 }