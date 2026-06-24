/**
 * 耗材
 */
import request from "@/utils/request.js";

function getConsumableList(data){
	return request.post('/erp/api/v1/material/consumable/getConsumableList',data);
}
function saveConsumable(data){
	return request.post('/erp/api/v1/material/consumable/saveConsumable',data);
}
function getConsumablePList(data){
	return request.get('/erp/api/v1/material/consumable/getConsumablePList',{params:data});
}

export default{
	 getConsumableList,saveConsumable,getConsumablePList,
}
