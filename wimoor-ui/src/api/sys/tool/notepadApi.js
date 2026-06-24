
import request from "@/utils/request.js";

function list(data){
	 return request.post('/admin/api/v1/notepad/list',data);
}
function save(data){
	 return request.post('/admin/api/v1/notepad/save',data);
}
function removeNote(data){
	 return request.post('/admin/api/v1/notepad/removeNote',data);
}
function view(data){
	return request.get('/admin/api/v1/notepad/view',{params:data});
}
export default{
	list,save,removeNote,view
}