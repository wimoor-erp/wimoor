import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function list(data){
	 return request.post('/erp/api/v1/inventory/dispatchform/list', data);
}
function getData(data){
	return request.get('/erp/api/v1/inventory/dispatchform/getData', {params:data});
}
function deleteData(data){
	return request.get('/erp/api/v1/inventory/dispatchform/deleteData', {params:data});
}
function examine(data){
	return request.get('/erp/api/v1/inventory/dispatchform/examine', {params:data});
}
function saveData(data){
	 return request.post('/erp/api/v1/inventory/dispatchform/saveData', data);
}
function downExcelTemp(data){
	return request({url:"/erp/api/v1/inventory/dispatchform/downExcelTemp",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"dispatchFormTemplate.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail();
									}); 
}
function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/inventory/dispatchform/uploadExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}





export default{
	 list,getData,deleteData,downExcelTemp,uploadExcel,saveData,examine
}