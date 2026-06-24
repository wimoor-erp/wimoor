/**
 * 产品
 */
import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getMaterialList(data){
	 return request.post('/erp/api/v1/material/list', data);
}
function consumableList(data){
	 return request.post('/erp/api/v1/material/consumableList', data);
}
function packageList(data){
	 return request.post('/erp/api/v1/material/packageList', data);
}

function consumableSafetyStockSave(data){
	 return request.post('/erp/api/v1/material/consumable/safety/save', data);
}
function consumableSafetyStockShow(){
	 return request.get('/erp/api/v1/material/consumable/safety/show');
}

 function getOwnerList(data){
	 return request.get("/erp/api/v1/material/getOwnerList",{params:data})
 }
function uploadImage(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/material/uploadimg",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function getMaterialInventoryInfo(data){
	return request.get('/erp/api/v1/material/getMaterialInventoryInfo',{params:data});
}
function getMaterialInfo(data){
	return request.get('/erp/api/v1/material/getMaterialInfo',{params:data});
}
function getCategory(data){
	return request.get('/erp/api/v1/material/getCategory',{params:data});
}
function getMaterialByInfo(data){
	return request.get('/erp/api/v1/material/getMaterialByInfo',{params:data});
}
function saveMaterialTags(data){
	return request.get('/erp/api/v1/material/saveMaterialTags',{params:data});
}
function findMaterialTags(data){
	return request.get('/erp/api/v1/material/findMaterialTags',{params:data});
}

function getWisePriceList(data){
	return request.get('/erp/api/v1/material/getWisePriceList',{params:data});
}
function findSKUImageForProduct(data){
	return request.post('/amazon/api/v1/report/product/productInOpt/findSKUImageForProduct',data);
}
function copyImageForProduct(data){
	return request.get('/erp/api/v1/material/copyImageForProduct',{params:data});
}
function copyDimsForProduct(data){
	return request.post('/erp/api/v1/material/copyDimsForProduct',data);
}

function saveData(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/material/saveData",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}

function deleteData(data){
	return request.get('/erp/api/v1/material/deleteData',{params:data});
}
function recoverData(data){
	return request.get('/erp/api/v1/material/recoverData',{params:data});
}

function downloadMaterialList(data){
	return request({url:"/erp/api/v1/material/downloadMaterialList",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
											downloadhandler.downloadSuccess(res,"materialList.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
function uploadBaseInfoFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadBaseInfoFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function uploadSupplierFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadSupplierFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function uploadMoreSupplierFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadMoreSupplierFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function uploadConsumableFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadConsumableFile",
				    'data':FormData,
					'responseType':"blob",
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}

function uploadCustomsFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadCustomsFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}

function uploadAssemblyFile(FormData){
	return request({'method':'POST',
	                 'url':"/erp/api/v1/material/uploadAssemblyFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function downExcelRecords(data,callback){
	return request({url:"/erp/api/v1/material/downExcelRecords",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,data.downtype+"Records.xlsx")
											if(callback){
												callback(res);
											}
									}).catch(e=>{
											if(callback){
												callback(e);
											}
											downloadhandler.downloadFail(e);
									}); 
}
function packageListAll(data){
	return request.post('/erp/api/v1/material/packageListAll',data);
}
function updateMaterial(ftype,data){
	return request.post('/erp/api/v1/material/updateMaterial/'+ftype,data);
}
function batchUpdatePrice(data){
	return request.post('/erp/api/v1/material/batchUpdatePrice',data);
}
function batchUpdateCons(data){
	return request.post('/erp/api/v1/material/batchUpdateCons',data);
}
function batchClearCons(data){
	return request.post('/erp/api/v1/material/batchClearCons',data);
}
function syncProductList(){
	return request.get('/amazon/api/v1/report/product/productInOpt/syncProductList');
}
 function getCustoms(data){
 	return request.get('/erp/api/v1/material/getCustoms',{params:data});
 }
 function saveCustoms(data){
 	return request.post('/erp/api/v1/material/saveCustoms',data);
 }
 
 function getMskuInventory(data){
	 return request.post('/erp/api/v1/material/getMskuInventory',data);
 }
 function getSerialNumber(data){
	 return request.get("/erp/api/v1/material/getSerialNumber",{params:data});
 }
 
 
  
export default{
	getMaterialList,uploadImage,
	getMaterialInventoryInfo,getMaterialInfo,getCategory,
	getMaterialByInfo,saveMaterialTags,
	findMaterialTags,getWisePriceList,saveData,
	deleteData,uploadBaseInfoFile,
	downloadMaterialList,uploadSupplierFile,
	uploadConsumableFile,uploadCustomsFile,
	uploadAssemblyFile,findSKUImageForProduct,
	copyImageForProduct,copyDimsForProduct,
	recoverData,downExcelRecords,getOwnerList,consumableList,
	consumableSafetyStockSave,consumableSafetyStockShow,
	packageList,packageListAll,updateMaterial,batchUpdatePrice,syncProductList,
	batchUpdateCons,batchClearCons,getCustoms,saveCustoms,getMskuInventory,uploadMoreSupplierFile,getSerialNumber,
	
}