import request from "@/utils/request.js";
 
function listShipGroup(data){
	 return request.post('/amazon/api/v1/product/merchantShippingGroup/list',data);
}
function refreshShipGroup(data){
	 return request.post('/amazon/api/v1/product/merchantShippingGroup/refresh',data);
}
 
 
export default{
	 listShipGroup,refreshShipGroup
} 