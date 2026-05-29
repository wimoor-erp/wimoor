import request from "@/utils/request.js";

function getAuthUrl(data){
	return request.get('/amazon/api/v1/amzauthority/getAuthUrl',{params:data });
}
function getBindSeller(){
	return request.get('/amazon/api/v1/amzauthority/getBindSeller');
}
function deleteByLogic(data){
	return request.get('/amazon/api/v1/amzauthority/deleteByLogic',{params:data });
}
function authSeller(data){
	return request.get('/amazon/api/v1/amzauthority/authSeller',{params:data });
}
function getRegionByGroup(data){
	return request.get('/amazon/api/v1/amzauthority/getRegionByGroup',{params:data });
}
function saveAuth(data){
	return request.post('/amazon/api/v1/amzauthority/saveAuth',data);
}
function getAuthMaketplace(data){
	return request.get('/amazon/api/v1/amzauthority/getAuthMaketplace',{params:data});
}
function requestReportParamAuth(data){
	return request.get('/amazon/api/v1/report/requestReportParamAuth',{params:data});
}
function refreshMarketByAuth(data){
	return request.get('/amazon/api/v1/amazonSellerMarket/refreshMarketByAuth',{params:data });
}
function uploadReportFile(FormData){
	return request({'method':'POST',
	                 'url':"/amazon/api/v1/report/uploadReportFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function getPerformance(data){
	return request.get('/amazon/api/v1/amzauthority/getPerformance',{params:data});
}
function statusChange(data){
	return request.get('/amazon/api/v1/amazonSellerMarket/statusChange',{params:data});
}

export default{
	getAuthUrl,getBindSeller,deleteByLogic,authSeller,getRegionByGroup,saveAuth,getAuthMaketplace,
	requestReportParamAuth,refreshMarketByAuth,uploadReportFile,getPerformance,statusChange
}