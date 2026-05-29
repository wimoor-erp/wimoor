import request from "@/utils/request.js";
function summary(data){
	 return request.get('/admin/api/v1/managerLimit/summary',{params:data});
}
function queryByShopName(data){
	return request.post('/admin/api/v1/managerLimit/queryByShopName',data);
}
function pkglist(){
	return request.get('/admin/api/v1/sysTariffPackages/list');
}
function detail(data){
	 return request.get('/admin/api/v1/managerLimit/detail',{params:data});
}
function reportTypeList(){
	 return request.get('/amazon/api/v1/report/reportTypeList');
}
function requestGroupReport(data){
	 return request.get('/amazon/api/v1/report/requestGroupReport',{params:data});
}
function selectGroupList(data){
	 return request.get('/amazon/api/v1/report/selectGroupList',{params:data});
}
function getMyManagerLimitmeal(){
	 return request.get('/admin/api/v1/managerLimit/getMyManagerLimitmeal');
} 

export default{
	summary,queryByShopName,pkglist,detail,reportTypeList,requestGroupReport,selectGroupList,
	getMyManagerLimitmeal,
}