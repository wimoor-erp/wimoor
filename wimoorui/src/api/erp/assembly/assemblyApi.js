import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
export function sublit(data){
	return request.get('/erp/api/v1/assembly/sublit',{params:data});
}
export function assemblyFormNeedInv(data){
	return request.get('/erp/api/v1/assembly/assemblyFormNeedInv',{params:data});
}
export function listForm(data){
 	return request.post('/erp/api/v1/assembly/list',data);
}

export function getCountNum(data){
 	return request.get('/erp/api/v1/assembly/getCountNum',{params:data});
}
export function downloadInfoWord(data){
	return request({url:"/erp/api/v1/assembly/downloadInfoWord",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"AssemblyFormContract.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
export function deleteFrom(data){
 	return request.get('/erp/api/v1/assembly/deleteFrom',{params:data});
}
export function getSubForm(data){
 	return request.get('/erp/api/v1/assembly/getSubForm',{params:data});
}
export function saveData(data){
 	return request.post('/erp/api/v1/assembly/saveData',data);
}
export function getData(data){
 	return request.get('/erp/api/v1/assembly/getData',{params:data});
}
export function changeAssAmount(data){
 	return request.get('/erp/api/v1/assembly/changeAssAmount',{params:data});
}
export function startAssemblyEvent(data){
 	return request.get('/erp/api/v1/assembly/startAssemblyEvent',{params:data});
}
export function saveRecord(data){
 	return request.get('/erp/api/v1/assembly/saveRecord',{params:data});
}
export function stopAssemblyEvent(data){
 	return request.get('/erp/api/v1/assembly/stopAssemblyEvent',{params:data});
}
export function assemblyCancelInstock(data){
 	return request.get('/erp/api/v1/assembly/assemblyCancelInstock',{params:data});
}
export function assemblyShipment(data){
 	return request.get('/erp/api/v1/assembly/assemblyShipment',{params:data});
}
export function resetAssForm(data){
 	return request.get('/erp/api/v1/assembly/resetAssForm',{params:data});
}

export function assemblyCompareToSku(sku,data){
 	return request.post('/erp/api/v1/assembly/assemblyCompareToSku/'+sku,data);
}

export function assemblyQuotainfos(data){
	return request.post('/erp/api/v1/assembly/assemblyQuotainfos',data);
}
export function assemblyQuotainfosById(data){
	return request.get('/erp/api/v1/assembly/assemblyQuotainfosById',{params:data});
}
export function getItemByFormids(data){
 	return request.post('/erp/api/v1/assembly/getItemByFormids',data);
}

export function assemblyCheckInv(checkinv,data){
 	return request.post('/erp/api/v1/assembly/assemblyCheckInv/'+checkinv,data);
}

export function downloadDetailExcel(data){
	return request({url:"/erp/api/v1/assembly/downloadDetailList",
					responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"processDetailList.xlsx")
					}).catch(e=>{
							downloadhandler.downloadFail(e);
					}); 
}

export function downloadFormExcel(data){
	return request({url:"/erp/api/v1/assembly/downloadFormList",
					responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"processFormList.xlsx")
					}).catch(e=>{
							downloadhandler.downloadFail(e);
					}); 
}

export function findProcessHandle(data){
	return request({url:"/erp/api/v1/assembly/findProcessHandle",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"processHandle.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
export function changeAssemblyWarehouse(data){
 	return request.get('/erp/api/v1/assembly/changeAssemblyWarehouse',{params:data});
}



export function downExcelTemp(data){
	return request({url:"/erp/api/v1/assembly/downExcelTemp",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"assemblyFormTemplate.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail();
									}); 
}
export function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/assembly/uploadExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}




