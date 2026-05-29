/**
 * 盘点
 */
import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function view(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/view', {params:data});
}
function edit(data){
	return request.get('/erp/api/v1/inventory/stockTaking/edit', {params:data});
}
function list(data){
	 return request.post('/erp/api/v1/inventory/stockTaking/list', data);
}
function searchCondition(data){
	 return request.post('/erp/api/v1/inventory/stockTaking/searchCondition', data);
}
function changeWarehouse(data){
	return request.get('/erp/api/v1/inventory/stockTaking/changeWarehouse', {params:data});
}
function saveItem(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/saveItem', {params:data});
}
function removeItem(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/removeItem', {params:data});
}
function startAction(data){
	 return request.post('/erp/api/v1/inventory/stockTaking/startAction', data);
}
function continueAction(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/continueAction', {params:data});
}
function endAction(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/endAction', {params:data});
}
function cancelAction(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/cancelAction', {params:data});
}
function saveShelfStockItem(data){
	return request.post("/erp/api/v1/inventory/stockTaking/shelf/save",data)
}
function updateShelfStockItem(data){
	return request.post("/erp/api/v1/inventory/stockTaking/shelf/update",data)
}
function deleteShelfStockItem(data){
	return request.post("/erp/api/v1/inventory/stockTaking/shelf/delete",data)
}
function stocktakingShelfList(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/stocktakingShelf/list', {params:data});
}
function stocktakingWarehouseList(data){
	 return request.get('/erp/api/v1/inventory/stockTaking/stocktakingWarehouse/list', {params:data});
}
function stocktakingListWarehouse(data){
	return request.post("/erp/api/v1/inventory/stockTaking/stocktakingWarehouse/listwarehouse",data)
}
function stocktakingListShelf(data){
	return request.post("/erp/api/v1/inventory/stockTaking/stocktakingShelf/listshelf",data)
}
 function downloadStockingList(data,callback){
 	return request({url:"/erp/api/v1/inventory/stockTaking/downloadStockingList",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 											downloadhandler.downloadSuccess(res,"downloadStockTemplate.xlsx");
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
 function downloadWarehouseList(data,callback){
 	return request({url:"/erp/api/v1/inventory/stockTaking/downloadWarehouseList",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 											downloadhandler.downloadSuccess(res,"downloadStockTemplate.xlsx");
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
 
 
 function uploadBaseInfoFile(FormData){
 	 return request({'method':'POST',
 	                 'url':"/erp/api/v1/inventory/stockTaking/uploadBaseInfoFile",
 	 			     'data':FormData,
 	 				 'headers':{'Content-Type':"multipart/form-data"},
 	 });
 }
 function uploadBaseInfoFileSimple(FormData){
 	 return request({'method':'POST',
 	                 'url':"/erp/api/v1/inventory/stockTaking/uploadBaseInfoFileSimple",
 	 			     'data':FormData,
 	 				 'headers':{'Content-Type':"multipart/form-data"},
 	 });
 }

export default{
	view,edit,list,searchCondition,changeWarehouse,
	saveItem,removeItem,startAction,continueAction,
	endAction,cancelAction,
	stocktakingShelfList,stocktakingWarehouseList,
	stocktakingListShelf,stocktakingListWarehouse,
	updateShelfStockItem,saveShelfStockItem,
    deleteShelfStockItem,downloadStockingList,uploadBaseInfoFile,downloadWarehouseList,uploadBaseInfoFileSimple
}