import request from "@/utils/request";
import downloadhandler from "@/utils/download-handler";

function getProductFollowList(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/getProductFollowList',data);
}
function saveProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/saveProfuctFollow',data);
}
function updateProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/updateProfuctFollow',data);
}
function pushProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/pushProfuctFollow',data);
}
function salesProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/salesProfuctFollow',data);
}
function unsalesProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/unsalesProfuctFollow',data);
}
function deleteProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/deleteProfuctFollow',data);
}
function priceProfuctFollow(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/priceProfuctFollow',data);
}

 function downloadDetailExport(data){
 	return request({url:"/amazonfollow/api/v1/follow/productInfoFollow/downloadDetailExport",
 			responseType:"blob",
 			params:data,
 			method:'get'}).then(res => {
 					downloadhandler.downloadSuccess(res,"productRelations.xlsx")
 			}).catch(e=>{
 					downloadhandler.downloadFail(e);
 			}); 
 }
 
 
 function downExcelTemp(data){
 	return request({url:"/amazonfollow/api/v1/follow/productInfoFollow/downExcelTemp",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 						downloadhandler.downloadSuccess(res,"amazonfollowtemplate.xlsx");
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 				});
 }
 function downFeeExcelTemp(data){
 	return request({url:"/amazonfollow/api/v1/follow/productInfoFollow/downFeeExcelTemp",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 						downloadhandler.downloadSuccess(res,"amazonshipFeetemplate.xlsx");
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 				});
 }
 function uploadExcel(FormData){
 	return request({'method':'POST',
 	                 'url':"/amazonfollow/api/v1/follow/productInfoFollow/uploadExcel",
 				    'data':FormData,
 					'headers':{'Content-Type':"multipart/form-data"},
 				
 	});
 }
 function uploadFeeExcel(FormData){
 	return request({'method':'POST',
 	                 'url':"/amazonfollow/api/v1/follow/productInfoFollow/uploadFeeExcel",
 				    'data':FormData,
 					'headers':{'Content-Type':"multipart/form-data"},
 				
 	});
 }
 function findRecordList(data){
 	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/findRecordList',{params:data});
 }
 function findWarningList(data){
	 return request.get('/amazonfollow/api/v1/follow/productInfoFollow/findWarningList',{params:data});
 }
 function ignoreWarningList(data){
 	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/ignoreWarningList',data);
 }
 function ignoreWarningListAll(data){
 	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/ignoreWarningListAll',data);
 }
 function findWarningListNum(data){
 	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/findWarningListNum',{params:data});
 }
 function changeProfuctFollowTime(data){
 	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/changeProfuctFollowTime',data);
 }
 function changeFollow(data,ftype){
 	return request.post('/amazonfollow/api/v1/follow/productInfoFollow/changeFollow/'+ftype,data);
 }
function changeAllCycle(data){
	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/changeAllCycle',{params:data});
}
function syncProductInfo(data){
	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/syncProductInfo',{params:data});
}
function refreshFulfillableProfuctFollow(data){
	return request.get('/amazonfollow/api/v1/follow/productInfoFollow/refreshFulfillableProfuctFollow',{params:data});
}
 
 
export default{
	getProductFollowList,saveProfuctFollow,deleteProfuctFollow,
	updateProfuctFollow,pushProfuctFollow,
	unsalesProfuctFollow,salesProfuctFollow,
	downloadDetailExport,downExcelTemp,uploadExcel,findRecordList,
	findWarningList,ignoreWarningList,findWarningListNum,changeProfuctFollowTime,
	changeFollow,changeAllCycle,downFeeExcelTemp,uploadFeeExcel,
	syncProductInfo,ignoreWarningListAll,priceProfuctFollow,refreshFulfillableProfuctFollow
}