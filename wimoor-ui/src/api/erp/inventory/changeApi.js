/**
 * 产品库存互调
 */
import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function list(data){
	 return request.post('/erp/api/v1/inventory/changeform/list', data);
}
function getData(data){
	return request.get('/erp/api/v1/inventory/changeform/getData', {params:data});
}
function deleteData(data){
	return request.get('/erp/api/v1/inventory/changeform/deleteData', {params:data});
}
function saveData(data){
	 return request.post('/erp/api/v1/inventory/changeform/saveData', data);
}
function downExcelTemp(data){
	return request({url:"/erp/api/v1/inventory/changeform/downExcelTemp",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"changeFormTemplate.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail();
									}); 
}
function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/inventory/changeform/uploadExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}





export default{
	 list,getData,deleteData,downExcelTemp,uploadExcel,saveData,
}