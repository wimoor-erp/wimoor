import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 export function getPlanList(data){
 	 return request.get("/erp/api/v1/purchase/plan/getPlan",{params:data})
 }
 export function updatePlan(data){
 	 return request.post("/erp/api/v1/purchase/plan/updatePlan",data)
 }
 export function savePlan(data){
 	 return request.post("/erp/api/v1/purchase/plan/savePlan",data)
 }
 export function deletePlan(data){
 	 return request.get("/erp/api/v1/purchase/plan/deletePlan",{params:data})
 }
 export function getLast(data){
 	 return request.get("/erp/api/v1/purchase/plan/getLast",{params:data})
 }
 export function getLast3(data){
 	 return request.get("/erp/api/v1/purchase/plan/getLast3",{params:data})
 }
 export function changeWarehouse(data){
 	 return request.post("/erp/api/v1/purchase/plan/item/changewh",data);
 }
 export function saveItem(data){
 	 return request.post("/erp/api/v1/purchase/plan/item/save",data);
 }
 export function deleteItem(data){
 	 return request.get("/erp/api/v1/purchase/plan/item/delete",{params:data});
 }
 export function getSummary(data){
 	 return request.get("/erp/api/v1/purchase/plan/item/getSummary",{params:data});
 }
 export function clearItem(data){
 	 return request.get("/erp/api/v1/purchase/plan/item/clear",{params:data});
 }
 export function listItem(planid,data){
 	 return request.post("/erp/api/v1/purchase/plan/item/list/"+planid,data);
 }
 export function batchItem(data){
 	 return request.post("/erp/api/v1/purchase/plan/item/batch",data);
 }
 export function removeBatchItem(batchnumber){
 	 return request.get("/erp/api/v1/purchase/plan/item/removeBatch",
	                    {params:{"batchnumber":batchnumber}});
 }
 
 export function getBatchListItem(planid,batchnumber){
 	 return request.get("/erp/api/v1/purchase/plan/item/getBatchList",
	 {params:{"planid":planid,"batchnumber":batchnumber}});
 }
 
 export function listHisItem(warehouseid){
 	 return request.get("/erp/api/v1/purchase/plan/item/listHis",
 	 {params:{"warehouseid":warehouseid}});
 }
 export function uploadPurchaseWarehouse(FormData){
 	return request({'method':'POST',
 	                 'url':"/erp/api/v1/purchase/plan/item/uploadWarhouseSKUFile",
 				    'data':FormData,
 					'headers':{'Content-Type':"multipart/form-data"},
 				
 	});
 	
 }
 
