import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
// function getMaterialList(data){
// 	 return request.get('/erp/api/v1/material',{params:data});
// }
// function uploadImage(FormData){
// 	return request({'method':'POST',
// 	                'url':"/erp/api/v1/material/uploadimg",
// 				    'data':FormData,
// 					'headers':{'Content-Type':"multipart/form-data"},
				
// 	});
// }

function getData(data){
	return request.get('/erp/api/v1/customer/getData',{params:data});
}
function saveData(data){
	return request.post('/erp/api/v1/customer/saveData',data);
}
function downloadCustomerList(data){
	return request({url:"/erp/api/v1/customer/downloadCustomerList",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"customerList.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
function uploadCustomerFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/customer/uploadCustomerFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
	
}
function list(data){
	return request.post('/erp/api/v1/customer/list',data);
}
function getCustomer(data){
	return request.get('/erp/api/v1/customer/getCustomer',{params:data});
}
function deletecust(data){
	return request.get('/erp/api/v1/customer/delete',{params:data});
}
function getSupplierByMid(data){
	return request.get('/erp/api/v1/customer/getSupplierByMid',{params:data});
}
function listAll(){
	return request.get('/erp/api/v1/customer/listAll');
}




export default{
	 getData,saveData,downloadCustomerList,list,getCustomer,deletecust,uploadCustomerFile,getSupplierByMid,listAll
}