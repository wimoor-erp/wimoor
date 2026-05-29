import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

 
function generatePackingOptions(data){
	return request.get('/amazon/api/v2/shipInboundPlan/box/generatePackingOptions',{params:data});
}
function listPackingOptions(data){
	return request.post('/amazon/api/v2/shipInboundPlan/box/listPackingOptions',data);
}
function listPackingGroupItems(data){
	return request.post('/amazon/api/v2/shipInboundPlan/box/listPackingGroupItems', data);
}
function savePackingInformation(data){
	return request.post('/amazon/api/v2/shipInboundPlan/box/savePackingInformation', data);
}

function getBoxDetail(data){
	return request.post('/amazon/api/v2/shipInboundPlan/box/getBoxDetail',data);
}

function confirmPackingOption(data){
	return request.post('/amazon/api/v2/shipInboundPlan/box/confirmPackingOption',data);
}


function donePlanBox(data){
	return request.get('/amazon/api/v2/shipInboundPlan/box/donePlanBox',{params:data});
}

function selectPackgroupInfo(data){
	return request.get('/amazon/api/v2/shipInboundPlan/box/selectPackgroupInfo',{params:data});
}
function getAllBoxDim(data){
	return request.get('/amazon/api/v2/shipInboundPlan/box/getAllBoxDim',{params:data});
}
function submitPackingInformation(data){
	return request.get('/amazon/api/v2/shipInboundPlan/box/submitPackingInformation',{params:data});
}




export default{
	 generatePackingOptions,listPackingOptions,listPackingGroupItems,savePackingInformation,
	 getBoxDetail,confirmPackingOption,donePlanBox,selectPackgroupInfo,getAllBoxDim,submitPackingInformation
}

