import request from "@/utils/request.js";
function addWarehouseAddress(data){
	 return request.post("/erp/api/v1/warehouse/address",data)
}
function updateWarehouseAddress(id,data){
	 return request.put("/erp/api/v1/warehouse/address/"+id,data)
}
function listWarehouseAddress(data){
	 return request.post("/erp/api/v1/warehouse/address/list",data)
}
function detailWarehouseAddress(id){
	 return request.get("/erp/api/v1/warehouse/address/"+id);
}
function disableWarehouseAddress(id,data){
	 return request.get("/erp/api/v1/warehouse/address/disabled/"+id,{params:data});
}
function bindWarehouseAddress(id,data){
	 return request.get("/erp/api/v1/warehouse/address/bind/"+id,{params:data});
}
function unboundWarehouseAddress(addressid,data){
	return request.get("/erp/api/v1/warehouse/address/unbind/"+addressid,{params:data});
}
export default{
	addWarehouseAddress,updateWarehouseAddress,
	listWarehouseAddress,bindWarehouseAddress,
	unboundWarehouseAddress,
	detailWarehouseAddress,disableWarehouseAddress
}