 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
function list(data){
	return request.post('/erp/api/v1/fin/journal/list',data);
}
 function downExcelDate(data,callback){
 	return request({url:"/erp/api/v1/fin/journal/downExcelDate",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"finReport.xlsx");
											if(callback){
												callback();
											}
 									}).catch(e=>{
 											downloadhandler.downloadFail();
											if(callback){
												callback();
											}
 									}); 
 }
 function findDetialByCondition(data){
 	return request.post('/erp/api/v1/fin/journal/findDetialByCondition',data);
 }
 function outinsum(data){
 	return request.post('/erp/api/v1/fin/journal/outinsum',data);
 }
 function getLineData(data){
 	return request.get('/erp/api/v1/fin/journal/getLineData',{params:data});
 }
 function getPieData(data){
 	return request.get('/erp/api/v1/fin/journal/getPieData',{params:data});
 }
 function save(data){
 	return request.post('/erp/api/v1/fin/journal/save',data);
 }
 function cancel(data){
 	return request.get('/erp/api/v1/fin/journal/cancel',{params:data});
 }
 
 
 export default{
    list,downExcelDate,findDetialByCondition,outinsum,getLineData,getPieData,save,cancel
 }