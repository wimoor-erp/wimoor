import request from "@/utils/request.js";

function showDataBase(data) {
	return request.get('/mdata/api/v1/data/showDataBase', { params: data });
}

function showTables(data) {
	return request.get('/mdata/api/v1/data/showTable', { params: data });
}

function taskAdd(database, data) {
	return request.post('/mdata/api/v1/data/taskAdd/' + database, data);
}
function showtask(data) {
	return request.get('/mdata/api/v1/data/showtask', { params: data });
}
function clear(data) {
	return request.get('/mdata/api/v1/data/clear', { params: data });
}
function showLog(data) {
	return request.get('/mdata/api/v1/data/showlog', { params: data });
}
function clearMove(data) {
	return request.get('/mdata/api/v1/datamove/clear', { params: data });
}
function startTask(data) {
	return request.get('/mdata/api/v1/data/startTask', { params: data });
}
function taskMoveAdd(database, data) {
	return request.post('/mdata/api/v1/datamove/taskAdd/' + database, data);
}
function showMoveTask(data) {
	return request.get('/mdata/api/v1/datamove/showtask', { params: data });
}
function startMoveTask(data) {
	return request.get('/mdata/api/v1/datamove/startTask', { params: data });
}
function showTableColumn(data) {
	return request.get('/mdata/api/v1/data/showTableColumn', { params: data });
}

function querySettlement(data) {
	return request.get('/mdata/api/v1/datamove/querySettlement', { params: data });
}
function moveSettlement(data) {
	return request.get('/mdata/api/v1/datamove/moveSettlement', { params: data });
}
function startmoveSettlement(data) {
	return request.get('/mdata/api/v1/datamove/startmoveSettlement', { params: data });
}

function clearSettlement(data) {
	return request.get('/mdata/api/v1/datamove/clearSettlement', { params: data });
}


export default {
	showDataBase, showTables, taskAdd, showtask, clear, startTask, showTableColumn, clearMove, showLog,
	taskMoveAdd, showMoveTask, startMoveTask, querySettlement, moveSettlement, startmoveSettlement, clearSettlement
}
