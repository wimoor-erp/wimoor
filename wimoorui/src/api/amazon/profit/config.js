import request from "@/utils/request.js";

function findConfig(data){
	return request.get('/amazon/api/v1/profit/profitCfg/findConfig',{params:data });
}
function showProfitDetial(){
	return request.get('/amazon/api/v1/profit/profitCfg/showProfitDetial');
}
function setDefaultPlan(data){
	return request.get('/amazon/api/v1/profit/profitCfg/setDefaultPlan',{params:data });
}

function findInplacefee(country){
	return request.get('/amazon/api/v1/profit/profitCfg/findInplacefee/'+country); 
}

function findManualProcessingFee(){
	return request.get('/amazon/api/v1/profit/profitCfg/findManualProcessingFee');
}
 
function storageFee(){
 	return request.get('/amazon/api/v1/profit/profitParam/storageFee');
 }
 
function editConfig(data){
  	return request.post('/amazon/api/v1/profit/profitCfg/edit',data);
  }
  
function deleteConfig(data){
    	return request.get('/amazon/api/v1/profit/profitCfg/setDeletePlan',{params:data});
    }
export default{
	findConfig ,showProfitDetial,setDefaultPlan,findInplacefee,findManualProcessingFee,storageFee,editConfig,deleteConfig
}