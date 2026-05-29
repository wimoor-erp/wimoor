import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 function findFBA(data){
 	 return request.get("/amazon/api/v0/inventry/findFBA",{params:data})
 }
 function syncInventorySupply(data){
	 return request.get("/amazon/api/v0/inventry/syncInventorySupply",{params:data})
 }
 function getInventorySupply(data){
 	 return request.get("/amazon/api/v0/inventry/getInventorySupply",{params:data})
 }
 function findEUFBA(data){
 	 return request.get("/amazon/api/v0/inventry/findEUFBA",{params:data})
 }
 function list(data){
 	 return request.get("/amazon/api/v1/inventoryRpt/list",{params:data})
 }
 
 function planninglist(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/planninglist", data)
 }
 function summaryPlanning(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/summaryPlanning", data)
 }
 
 function downloadPlanList(data,callback){
 	return request({url:"/amazon/api/v1/inventoryRpt/downloadPlanList",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 											downloadhandler.downloadSuccess(res,"planningReport.xlsx")
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
 
 function getWarehouse(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/getWarehouse", data)
 }
 function getWarehouseExport(data,callback){
 	return request({url:"/amazon/api/v1/inventoryRpt/getWarehouseExport",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"inventoryFBAReport.xlsx")
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

 function getFBACountry(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/getFBACountry", data)
 }
 function getFBAInvDayDetailField(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/getFBAInvDayDetailField",data)
 }
 function getFBAInvDayDetail(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/getFBAInvDayDetail", data)
 }
 function getFBAInvDayDetailExport(data){
 	return request({url:"/amazon/api/v1/inventoryRpt/getFBAInvDayDetailExport",
 							responseType:"blob",
 							data:data,
 							method:'post'}).then(res => {
 									downloadhandler.downloadSuccess(res,"inventoryFBAtoDayDetail.xlsx")
 							}).catch(e=>{
 									downloadhandler.downloadFail();
 							}); 
 }
 function inventoryCostFBA(data){
 	 return request.post("/amazon/api/v1/inventoryRpt/inventoryCostFBA", data)
 }
 function downloadInvCostFBA(data,callback){
 	return request({url:"/amazon/api/v1/inventoryRpt/downloadInvCostFBA",
 							responseType:"blob",
 							data:data,
 							method:'post'}).then(res => {
 									downloadhandler.downloadSuccess(res,"inventoryCostFBA.xlsx");
									if(callback){
										callback(res);
									}
 							}).catch(e=>{
 									downloadhandler.downloadFail();
									if(callback){
										callback(e);
									}
 							}); 
 }
 
 
 export default{
 	findFBA,syncInventorySupply,getInventorySupply,findEUFBA,list,planninglist,downloadPlanList,
	summaryPlanning,getWarehouse,getWarehouseExport,getFBACountry,getFBAInvDayDetailField,getFBAInvDayDetail,getFBAInvDayDetailExport,
	downloadInvCostFBA,inventoryCostFBA
 }