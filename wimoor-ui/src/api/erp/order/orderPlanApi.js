import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

 function getPlan(data){
 	 return request.post("/erp/api/v1/order/plan/getPlan",data)
 }
 
 function getExpandCountryData(data){
 	 return request.post("/erp/api/v1/order/plan/getExpandCountryData",data)
 }
 
 function updateStockCycleTranstype(data){
 	 return request.post("/erp/api/v1/skucycle/updateStockCycleTranstype",data)
 }
 
function updateStock(data){
	 return request.get("/erp/api/v1/skucycle/updateStock",{params:data})
}

 

 function refreshData(data){
 	 return request.post("/erp/api/v1/order/plan/refreshData",data);
 }
 
 function saveItem(data){
 	 return request.post("/erp/api/v1/order/plan/saveItem",data);
 }
 
 function saveSales(data){
 	 return request.post("/erp/api/v1/order/plan/saveSales",data);
 }
 
 function removeItem(data){
 	 return request.post("/erp/api/v1/order/plan/removeItem",data);
 }
 
 function bindMsku(data){
 	 return request.post("/erp/api/v1/order/plan/bindMsku",data);
 }
 function summary(){
 	 return request.get("/erp/api/v1/order/plan/summary");
 }
 function getPurchase(data){
	  return request.get("/erp/api/v1/order/plan/getPurchase",{params:data});
 }
 function getShip(){
	  return request.get("/erp/api/v1/order/plan/getShip");
 }
 
 function removeWarehouseItem(data){
	  return request.post("/erp/api/v1/order/plan/removeWarehouseItem",data);
 }

 
 
 function clear(data){
	return request.post("/erp/api/v1/order/plan/clear",data);
}

 export default{
 	getPlan,getExpandCountryData,updateStock,updateStockCycleTranstype,refreshData, 
	saveItem,saveSales,removeItem,bindMsku,summary,getPurchase,getShip,removeWarehouseItem,clear
 }
