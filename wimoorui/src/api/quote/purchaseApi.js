import request from "@/utils/request.js";
 
function save(data){
	return request.post('/quote/api/v1/quote/purchase/save',data);
}

function list(data){
	return request.post('/quote/api/v1/quote/purchase/list',data);
}
function getItem(data){
	return request.get('/quote/api/v1/quote/purchase/getItem',{params:data});
}
 
 export default{
 	 save,list,getItem
 }