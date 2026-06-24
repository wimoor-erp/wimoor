import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function list(data){
	 return request.post('/erp/api/v1/inventory/outwform/list', data);
}
function getData(data){
	return request.get('/erp/api/v1/inventory/outwform/getData', {params:data});
}
function deleteData(data){
	return request.get('/erp/api/v1/inventory/outwform/deleteData', {params:data});
}
function saveData(data){
	 return request.post('/erp/api/v1/inventory/outwform/saveData', data);
}
function getlast(data){
	return request.get('/erp/api/v1/inventory/outwform/getlast', {params:data});
}

function getListDetailExport(data){
	return request({url:"/erp/api/v1/inventory/outwform/getListDetailExport",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"outForm.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail();
									}); 
}
function downExcelTemp(data){
	return request({url:"/erp/api/v1/inventory/outwform/downExcelTemp",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"outFormTemplate.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail();
									}); 
}
function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/inventory/outwform/uploadExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}





export default{
	 list,getData,deleteData,getListDetailExport,downExcelTemp,uploadExcel,saveData,getlast
}