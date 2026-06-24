 import request from "@/utils/request.js";
  import downloadhandler from "@/utils/download-handler.js";
function saveForm(data){
	return request.post('/chelsea/api/v1/chelsea/productionform/saveForm',data);
}
function saveOktime(data){
	return request.get('/chelsea/api/v1/chelsea/productionform/saveOktime',{params:data});
}

function listForm(data){
	return request.post('/chelsea/api/v1/chelsea/productionform/listForm',data);
}

 function downloadReport(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/productionform/downloadReport",
					responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"purchaseReport.xlsx");
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
 function downloadForm(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/productionform/downloadForm",
					responseType:"blob",
					params:data,
					method:'get'}).then(res => {
						     var ext="docx";
							 if(data.type=="Pdf"){
								 ext="pdf";
							 }
							downloadhandler.downloadSuccess(res,"purchasePlanForm."+ext);
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
 
 function deleteForm(data){
 	return request.post('/chelsea/api/v1/chelsea/productionform/deleteForm',data);
 }
 
 

 

 export default{
 	saveForm,listForm,downloadForm,downloadReport,deleteForm,saveOktime
 }