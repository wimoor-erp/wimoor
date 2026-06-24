import request from "@/utils/request.js";

function getOrderChannel(data){
	 return request.get('/amazon/api/v0/orders/osum/getOrderChannel',{params:data});
}
function search(data){
	 return request.post('/amazon/api/v0/orders/osum/search', data);
}
function getMyOrderTotal(data){
	 return request.post('/amazon/api/v1/salessum/getMyOrderTotal', data);
}
function getOrderTotal(data){
	 return request.post('/amazon/api/v1/salessum/getOrderTotal', data);
}

export default{
	getOrderChannel,
	search,
	getMyOrderTotal,
	getOrderTotal,
}