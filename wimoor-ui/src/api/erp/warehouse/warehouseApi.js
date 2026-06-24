import request from "@/utils/request.js";
const getShelfList=(data)=>{
	return request({url:'/erp/api/v1/warehoue/shelfInventory/getShelfList',method:"POST","data":data});
}
const getShelfInventoryList=(data)=>{
	return request({url:'/erp/api/v1/warehoue/shelfInventory/getShelfInventoryList',method:"POST","data":data});
}
const shelfInventoryOptRecord=(data)=>{
	return request({url:'/erp/api/v1/warehouse/shelfInventoryOptRecord',method:"GET","data":data});
}
const getShelfInfo=(data)=>{
	return request({url:'/erp/api/v1/warehouse/shelf/getShelfInfo',method:"GET","data":data});
}
const invAdd=(data)=>{
 	return request({url:'/erp/api/v1/warehoue/shelfInventory/add',method:"POST","data":data});
}
const invSub=(data)=>{
  	return request({url:'/erp/api/v1/warehoue/shelfInventory/sub',method:"POST","data":data});
 }
 function getWarehouseList(data){
 	 return request.get("/erp/api/v1/warehouse/list",{params:data})
 }
 function getWarehouseUseable(){
 	 return request.get("/erp/api/v1/warehouse/getlist",{params:{"ftype":"self_usable"} })
 }
function getWarehouseListPage(data){
	 return request.post("/erp/api/v1/warehouse/listpage",data)
}
function getWarehouse(data){
	 return request.get("/erp/api/v1/warehouse/getlist",{params:data })
}
function getOversaWarehouse(data){
	 return request.get("/erp/api/v1/warehouse/getOverseaList",{params:data });
}
function getOversaCountry(){
	return request.get("/erp/api/v1/warehouse/getOverseaCountry");
}
function getOversaWarehouseUseable(){
	 return request.get("/erp/api/v1/warehouse/getlist",{params:{"ftype":"oversea_usable"} })
}
function getWarehouseUnUseable(){
	 return request.get("/erp/api/v1/warehouse/getlist",{params:{"ftype":"self_unusable"} })
}
function getWarehouseTest(){
	 return request.get("/erp/api/v1/warehouse/getlist",{params:{"ftype":"self_test"} })
}
function getSelfWarehouseById(data){
	 return request.get("/erp/api/v1/warehouse/getSelfWarehouseById",{params:data})
}
function getWarehouseNameList(data){
	 return request.get("/erp/api/v1/warehouse/getNamelist",{params:data})
}
function deleteInfo(ids){
	return request.get("/erp/api/v1/warehouse/deleteInfo",{params:{ids:ids}});
}
function saveData(data){
	return request.post("/erp/api/v1/warehouse/saveData",data);
}
function updateData(id,data){ 
	return request.post("/erp/api/v1/warehouse/updateData/"+id,data);
}
function detail(id){
	return request.get("/erp/api/v1/warehouse/detail/"+id);
}
function getNamelistByAddressid(addressid){
		return request.get("/erp/api/v1/warehouse/getNamelistByAddressid/"+addressid);
}
function updateDefault(id){
	return request.post("/erp/api/v1/warehouse/updateDefault/"+id);
}
function updateIndex(data){
	return request.post("/erp/api/v1/warehouse/updateIndex",data);
}
function updateStockByChange(data){
	return request.post("/erp/api/v1/material/stockcycle/updateStockByChange",data);
}
function checkDeleteWarehouse(ids){
	return request.get("/erp/api/v1/warehouse/checkDeleteWarehouse",{params:{ids:ids}});
}

 
export default{
	getWarehouseList,getWarehouseUseable,getWarehouseUnUseable,
	getWarehouseTest,getSelfWarehouseById,getWarehouseNameList,
	getWarehouseListPage,deleteInfo,saveData,detail,updateData,updateDefault,
	updateIndex,updateStockByChange,getOversaWarehouseUseable,getWarehouse,
	getOversaWarehouse,getNamelistByAddressid,getOversaCountry,
	getShelfList,
	getShelfInventoryList,
	getShelfInfo,
	invAdd,
	invSub,
  shelfInventoryOptRecord,checkDeleteWarehouse
}