import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";

function list(data){
	return request.post('/chelsea/api/v1/chelsea/finances/getList',data);
}

function getMonth(data){
	return request.post('/chelsea/api/v1/chelsea/finances/getMonth',data);
}

function getQuarter(data){
	return request.post('/chelsea/api/v1/chelsea/finances/getQuarter',data);
}

 function downloadList(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/finances/downloadList",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"FinancesExcel.xlsx")
 											if(callback){
 												callback();
 											}
 									}).catch(e=>{
 											downloadhandler.downloadFail(e);
 											if(callback){
 												callback();
												
 											}
 									}); 
 }
 function downloadMonth(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/finances/downloadMonth",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"FinancesMonthExcel.xlsx")
 											if(callback){
 												callback();
 											}
 									}).catch(e=>{
 											downloadhandler.downloadFail(e);
 											if(callback){
 												callback();
 												
 											}
 									}); 
 }

export default{
 	list,downloadList,getMonth,getQuarter,downloadMonth,
 }