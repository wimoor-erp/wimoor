import request from "@/utils/request.js";
import uploadhandler from "@/utils/upload-handler";
function uploadPayDateFile(FormData,callback){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/uploadPayDate",
				    'data':FormData,
					'responseType':"blob",
					'headers':{'Content-Type':"multipart/form-data"},
	}).then(function(res){
		  	  uploadhandler.uploadResult(res,(response)=>{
				 if(callback){
				 	 callback(response);
				 }
		  	  });
		  	 
		  }).catch(e=>{
		  	  uploadhandler.uploadResult(e,(response)=>{
				  if(callback){
				  	  callback(response);
				  }
			  });
			   
	  });
}
 function list(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/list',data);
 }
 function orderList(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/orderList',data);
 }
 function payList(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/payList',data);
 }
 function returnPayList(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/returnPayList',data);
 }
 function orderReturnList(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/orderReturnList',data);
 }
 function deleteSettlement(data){
 	return request.post('/erp/api/v1/purchase/alibaba/entry/purchaseAlibabaSettlement/delete',data);
 }
export default{
	uploadPayDateFile,list,orderList,payList,returnPayList,orderReturnList,deleteSettlement
}