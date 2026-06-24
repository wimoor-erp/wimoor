import request from "@/utils/request.js";
function getAddress(data){
	 return request.post('/amazon/api/v1/shipaddress/list',data);
}
function deleteAddress(data){
	 return request.get('/amazon/api/v1/shipaddress/disable',{params:data });
}
function updateAddress(data){
	 return request.post('/amazon/api/v1/shipaddress/updateData',data);
}
function saveAddress(data){
	 return request.post('/amazon/api/v1/shipaddress/saveData',data);
}
export default{
	getAddress,deleteAddress,updateAddress,saveAddress
}