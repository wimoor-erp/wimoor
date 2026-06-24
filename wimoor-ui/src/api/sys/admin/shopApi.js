import request from "@/utils/request.js";

function list(data){ 
	 return request.get('/admin/api/v1/shop/list',{params:data});
}

export default{
	list
}