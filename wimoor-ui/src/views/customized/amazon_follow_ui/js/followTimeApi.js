import request from "@/utils/request";

function list(){
	return request.get('/amazonfollow/api/v1/follow/productInfoFollowTime/list');
}
function add(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollowTime/add',data);
}
function update(data){
	return request.post('/amazonfollow/api/v1/follow/productInfoFollowTime/update',data);
}
function deleteItem(data){
	return request.get('/amazonfollow/api/v1/follow/productInfoFollowTime/delete',{params:data});
}

export default{
	list,add,update,deleteItem,
}