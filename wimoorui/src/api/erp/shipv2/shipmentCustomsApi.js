import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function downloadShipTemplateFile(data,callback,name){
	return request({url:"/amazon/api/v2/ship/customs/downloadShipTemplateFile",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,name);
						if(callback){
							callback(res);
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
						if(callback){
							callback(e);
						}
				});
}

function deleteCustomsFile(data){
	return request.get('/amazon/api/v2/ship/customs/deleteCustomsFile',{params:data});
}
function getShipmentCustomsRecord(data){
	return request.get('/amazon/api/v2/ship/customs/getShipmentCustomsRecord',{params:data});
}

function uploadCustomsFile(FormData){
	return request({'method':'POST',
	                'url':"/amazon/api/v2/ship/customs/uploadCustomsFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}


 



export default{
	 downloadShipTemplateFile,deleteCustomsFile,getShipmentCustomsRecord,uploadCustomsFile,
	 
}