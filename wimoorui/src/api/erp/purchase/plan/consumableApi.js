import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function save(data){
 	 return request.post("/erp/api/v1/purchase/plan/consumable/save",data);
}
function deleteItem(data){
 	 return request.get("/erp/api/v1/purchase/plan/consumable/delete",{params:data});
}
function clear(data){
 	 return request.get("/erp/api/v1/purchase/plan/consumable/clear",{params:data});
}
function getSummary(data){
 	 return request.get("/erp/api/v1/purchase/plan/consumable/getSummary",{params:data});
}
function list(data){
 	 return request.get("/erp/api/v1/purchase/plan/consumable/list",{params:data});
}



export default{
	save,deleteItem,clear,list,getSummary
}