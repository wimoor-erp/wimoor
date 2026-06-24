import request from "@/utils/request.js";
function summary(data){ 
	 return request.get('/amazon/api/v1/product/salesplanafter/summary',{params:data});
}
function list(data){ 
	 return request.post('/amazon/api/v1/product/salesplanafter/list',data);
}
 
export default{
	 summary,list
}