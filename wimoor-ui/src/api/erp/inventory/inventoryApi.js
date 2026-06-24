import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 function getInventoryByMaterialSKU(data){
 	 return request.get("/erp/api/v1/inventory/getInventoryByMaterialSKU",{params:data})
 }
 function getInventoryByMaterial(data){
 	 return request.get("/erp/api/v1/inventory/getInventoryByMaterial",{params:data})
 }
 function list(data){
 	 return request.get("/erp/api/v1/inventory/list",{params:data})
 }
 function getInventory(data){
 	 return request.get("/erp/api/v1/inventory/getInventory",{params:data})
 }
 function getSelfInvDetail(data){
 	 return request.get("/erp/api/v1/inventory/getSelfInvDetail",{params:data})
 }
 function getWarehouse(data){
 	 return request.post("/erp/api/v1/inventory/getWarehouse",data)
 }
 function findInboundDetail(data){
 	 return request.get("/erp/api/v1/inventory/findInboundDetail",{params:data})
 }
 function findOutboundDetail(data){
 	 return request.get("/erp/api/v1/inventory/findOutboundDetail",{params:data})
 }
 
 function getWarehouseExport(data){
 	return request({url:"/erp/api/v1/inventory/getWarehouseExport",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"inventoryDetail.xlsx")
 									}).catch(e=>{
 											downloadhandler.downloadFail();
 									}); 
 }
 function refreshInventory(data){
  	return request.get('/erp/api/v1/inventory/manager/refreshInventory',{params:data});
 }

 
 export default{
 	getInventoryByMaterialSKU,getInventoryByMaterial,list,getInventory,getWarehouse,findInboundDetail,
	findOutboundDetail,getWarehouseExport,refreshInventory,getSelfInvDetail
 }