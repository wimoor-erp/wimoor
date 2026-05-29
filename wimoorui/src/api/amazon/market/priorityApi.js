import request from "@/utils/request.js";
function save(data){
	 return request.post('/amazon/api/v1/amzmarketplace/priority/save',data);
}
function list(data){
	 return request.get('/amazon/api/v1/amzmarketplace/priority/list',{params:data});
} 
export default{
	save,list
}
 