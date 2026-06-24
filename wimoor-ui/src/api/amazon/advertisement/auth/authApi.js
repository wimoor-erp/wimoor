import request from "@/utils/request.js";
function getAuthUrl(data){
	return request.get('/amazonadv/api/v1/advert/bindUrl',{params:data });
}
function authSeller(data){
	return request.post('/amazonadv/api/v1/advert/bindAdvAuthData',data);
}
function disableAuth(data){
	return request.get('/amazonadv/api/v1/advert/disableAuth',{params:data });
}
function getGroup(data){
	return request.get('/amazonadv/api/v1/advManager/getGroup');
}
function loadProfile(data){
	return request.get('/amazonadv/api/v1/advert/loadProfile',{params:data});
}
function captureProfileById(data){
	return request.get('/amazonadv/api/v1/advert/captureProfileById',{params:data});
}
function requestReportByDate(data){
	return request.get('/amazonadv/api/v1/advschedule/requestReportByDate',{params:data});
}
function listReport(data){
	return request.get('/amazonadv/api/v1/advschedule/listReport',{params:data});
}

export default{
	getAuthUrl,authSeller,disableAuth,getGroup,loadProfile,captureProfileById,requestReportByDate,listReport
}