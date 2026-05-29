import request from "@/utils/request.js";
 
function search(data){
	 return request.post('/admin/api/v1/deepseek/search',data);
}
function getSession(){
	 return request.get('/admin/api/v1/deepseek/getSession');
}

function getKey(){
	 return request.get('/admin/api/v1/deepseek/getKey');
}


export default{
	 search,getSession,getKey
}