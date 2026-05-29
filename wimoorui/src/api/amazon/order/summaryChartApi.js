import request from "@/utils/request.js";

function getOrderChannel(data){
	 return request.get('/amazon/api/v0/orders/osum/getOrderChannel',{params:data});
}
function search(data){
	 return request.post('/amazon/api/v0/orders/osum/search', data);
}

export default{
	getOrderChannel,
	search,
}