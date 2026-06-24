import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function save(data){
	return request.post('/erp/api/v1/OverseaCycle/save',data);
}
function list(data){
	return request.get('/erp/api/v1/OverseaCycle/list',{params:data});
}
 
export default{
	list, save
}