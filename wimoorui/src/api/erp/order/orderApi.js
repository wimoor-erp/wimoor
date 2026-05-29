import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 
function listPlatform(){
	return request.get('/erp/api/v1/order/listPlatform');
}
function countrys(){
		return request.get('/erp/api/v1/order/countrys');
}
function getPlatform(data){
	return request.get('/erp/api/v1/order/getPlatform',{params:data});
}
function removePlatform(data){
	return request.get('/erp/api/v1/order/removePlatform',{params:data});
}
function savePlatform(data){
	return request.post('/erp/api/v1/order/savePlatform',data);
}
function save(data){
	return request.post('/erp/api/v1/order/save',data);
}
function remove(data){
	return request.get('/erp/api/v1/order/remove',{params:data});
}
function get(data){
	return request.get('/erp/api/v1/order/get',{params:data});
}
function findByCondition(data){
	return request.post('/erp/api/v1/order/findByCondition',data);
}
function findOrderByCondition(data){
	return request.post('/erp/api/v1/order/findOrderByCondition',data);
}
function findMaterialByCondition(data){
	return request.post('/erp/api/v1/order/findMaterialByCondition',data);
}
function findMaterialBySelect(data){
	return request.post('/erp/api/v1/order/findMaterialBySelect',data);
}

function set(data){
	return request.get('/erp/api/v1/order/set',{params:data});
}
function clear(data){
	return request.get('/erp/api/v1/order/clear',{params:data});
}

function downloadOrderByCondition(data,callback){
	return request({url:"/erp/api/v1/order/downloadOrderByCondition",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,"多平台订单.xlsx")
											if(callback){
												callback(res);
											}
									}).catch(e=>{
											downloadhandler.downloadFail(e);
											if(callback){
												callback(e);
											}
									}); 
}
 
function downloadOrderPlanForm(data,callback){
	return request({url:"/erp/api/v1/order/downloadMaterialByCondition",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,"海外仓库存.xlsx")
											if(callback){
												callback(res);
											}
									}).catch(e=>{
											downloadhandler.downloadFail(e);
											if(callback){
												callback(e);
											}
									}); 
}
 function downExcelTemp(data){
 	return request({url:"/erp/api/v1/order/downExcelTemp",
					responseType:"blob",
					params:data,
					method:'get'}).then(res => {
							downloadhandler.downloadSuccess(res,"orderTemplate.xlsx")
					}).catch(e=>{
							downloadhandler.downloadFail();
					}); 
 }
 
 function uploadExcel(FormData){
 	return request({'method':'POST',
 	                'url':"/erp/api/v1/order/uploadExcel",
 				    'data':FormData,
 					'headers':{'Content-Type':"multipart/form-data"},
 				
 	});
 }
 function salesLine(data){
 	  return request.get("/erp/api/v1/order/salesLine",{params:data});
 }
export default{
	  listPlatform,getPlatform,savePlatform,removePlatform,
	  save,remove,get,set,findByCondition,
	  findOrderByCondition,findMaterialByCondition,clear,
	  findMaterialBySelect,downloadOrderPlanForm,
	  downloadOrderByCondition,downExcelTemp,uploadExcel,
	  countrys,salesLine
}