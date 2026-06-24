 import request from "@/utils/request.js";
   import downloadhandler from "@/utils/download-handler.js";
function list(data){
	return request.post('/chelsea/api/v1/chelsea/productinfo/list',data);
}
function chart(data){
	return request.post('/chelsea/api/v1/chelsea/productinfo/chart',data);
}
function downloadReport(data,callback){
 	return request({url:"/chelsea/api/v1/chelsea/productinfo/downloadReport",
					responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"report.xlsx");
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
 	list,chart,downloadReport
 }