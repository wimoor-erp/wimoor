import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function saveAuthority(data){
	return request.post('/amazon/api/v0/transparency/saveAuthority',data);
}
function listAuthority(data){
	return request.get('/amazon/api/v0/transparency/listAuthority',{params:data });
}

function saveSkuinfo(data){
	return request.post('/amazon/api/v0/transparency/skuinfo/save',data);
}

function listSkuinfo(data){
	return request.post('/amazon/api/v0/transparency/skuinfo/list',data);
}
function deleteSkuinfo(data){
	return request.post('/amazon/api/v0/transparency/skuinfo/delete',data);
}

function listTask(data){
	return request.post('/amazon/api/v0/transparency/task/list',data);
}
function saveTask(data){
	return request.post('/amazon/api/v0/transparency/task/save',data);
}
function listCode(data){
	return request.post('/amazon/api/v0/transparency/tcode/list',data);
}
function saveCode(data){
	return request.post('/amazon/api/v0/transparency/tcode/save',data);
}
function usedByTask(data){
	return request.post('/amazon/api/v0/transparency/tcode/usedByTask',data);
}
function unUsedByTask(data){
	return request.post('/amazon/api/v0/transparency/tcode/unUsedByTask',data);
}

function refreshTask(data){
	return request.get('/amazon/api/v0/transparency/task/refresh',{params:data});
}

function downExcel(data){
	return request({url:"/amazon/api/v0/transparency/tcode/downloadList",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,"Tcode.xlsx")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}
function downloadPdfList(data){
	return request({url:"/amazon/api/v0/transparency/tcode/downloadPdfList",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
											downloadhandler.downloadSuccess(res,"Tcode.pdf")
									}).catch(e=>{
											downloadhandler.downloadFail(e);
									}); 
}

function upload(FormData){
	return request({'method':'POST',
	                'url':"/amazon/api/v0/transparency/skuinfo/upload",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
	});
}
export default{
	saveAuthority,listAuthority,
	saveSkuinfo,listSkuinfo,deleteSkuinfo,
	listTask,usedByTask,unUsedByTask,refreshTask,saveTask,
	listCode,saveCode,downExcel,upload,downloadPdfList
}