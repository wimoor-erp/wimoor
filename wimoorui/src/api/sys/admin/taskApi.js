import request from "@/utils/request.js";

function getTaskList(){
	return request.get('/admin/api/v1/task/getTaskList');
}
function runApi(data){
	return request.post('/admin/api/v1/task/runApi',data);
}
function disableApi(data){
	return request.post('/admin/api/v1/task/disableApi',data);
}
function enableApi(data){
	return request.post('/admin/api/v1/task/enableApi',data);
}
function saveQuartzTask(data){
	return request.post('/admin/api/v1/task/saveQuartzTask',data);
}

function refreshTask(){
	return request.get('/admin/api/v1/task/refresh');
}


export default{
	getTaskList,runApi,enableApi,disableApi,refreshTask,saveQuartzTask
}