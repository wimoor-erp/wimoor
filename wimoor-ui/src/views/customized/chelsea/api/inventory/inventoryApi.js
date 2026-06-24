 import request from "@/utils/request.js";
 
function getList(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/getList',data);
}
 
function saveSetting(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/saveSetting',data);
}

function savePlan(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/savePlan',data);
}

function removePlan(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/removePlan',data);
}

function clearPlan(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/clearPlan',data);
}
function countPlan(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/countPlan',data);
}
function getPlan(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/getPlan',data);
}
function getInventorySetting(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/getInventorySetting',data);
}
function saveGlobalSetting(data){
	return request.post('/chelsea/api/v1/chelsea/inventory/saveGlobalSetting',data);
}
function getGlobalSetting(data){
	return request.get('/chelsea/api/v1/chelsea/inventory/getGlobalSetting');
}




 export default{
 	getList,saveSetting,savePlan,removePlan,clearPlan,countPlan,getPlan,getInventorySetting,
	saveGlobalSetting,getGlobalSetting,
 }