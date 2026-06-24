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
function addScheduler(data){
	return request.post('/admin/api/v1/task/addScheduler',data);
}

function refreshTask(){
	return request.get('/admin/api/v1/task/refresh');
}

function listScheduler(){
	return request.get('/admin/api/v1/task/listScheduler');
}

function puaseScheduler(data){
	return request.get('/admin/api/v1/task/puaseScheduler',{params:data});
}

function resumeScheduler(data){
	return request.get('/admin/api/v1/task/resumeScheduler',{params:data});
}

function deleteScheduler(data){
	return request.get('/admin/api/v1/task/deleteScheduler',{params:data});
}

export default{
	getTaskList,runApi,enableApi,disableApi,refreshTask,saveQuartzTask,
	listScheduler,puaseScheduler,resumeScheduler,deleteScheduler,addScheduler
}