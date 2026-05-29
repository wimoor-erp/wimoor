import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";



function update(data){
	return request.get("/erp/api/v1/purchase/preprocess/update",{params:data});
}
function getRun(data){
	return request.get("/erp/api/v1/purchase/preprocess/update",{params:data});
}
function getRunsForm(data){
	return request.get("/erp/api/v1/purchase/preprocess/getRunsForm",{params:data});
}
function getFormReceiveById(data){
	return request.get("/erp/api/v1/purchase/preprocess/getFormReceiveById",{params:data});
}
function receiveQuotainfos(data){
	return request.get("/erp/api/v1/purchase/preprocess/receiveQuotainfos",{params:data});
}
function saveItem(data){
	return request.post("/erp/api/v1/purchase/preprocess/saveItem",data);
}
function clearRunsForm(data){
	return request.get("/erp/api/v1/purchase/preprocess/clearRunsForm",{params:data});
}
function getFormList(data){
	return request.post("/erp/api/v1/purchase/preprocess/getFormList",data);
}


function getItemsFormConsumable(data){
	return request.get("/erp/api/v1/purchase/preprocess/getItemsFormConsumable",{params:data});
}
 

function updateRunsConsumableForm(data){
	return request.get("/erp/api/v1/purchase/plan/consumable/updateRunsConsumableForm",{params:data});
}

function downRecPDFForm(data,callback){
	  return request({url:"/erp/api/v1/purchase/preprocess/downRecPDFForm",
	                responseType:"blob",
					params:data,
					method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"consumableForm.pdf")
						if(callback){
							callback();
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});;
} 
function downPDFAssemblyFormId(data,callback){
	return request({url:"/erp/api/v1/purchase/preprocess/downPDFAssemblyFormId",
	              responseType:"blob",
						params:data,
						method:'get'}).then(res => {
							downloadhandler.downloadSuccess(res,"assemblyConsForm.pdf");
							if(callback){
								callback();
							}
					}).catch(e=>{
						    downloadhandler.downloadFail(e);
					});;
}
function downPDFAssemblyForm(data){
	  return request({url:"/erp/api/v1/purchase/preprocess/downPDFAssemblyForm",
	                responseType:"blob",
					data:data,
					method:'post'}).then(res => {
						downloadhandler.downloadSuccess(res,"assemblyConsForm.pdf")
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});;
} 

function getSkuListByRunid(data){
	return request.get("/erp/api/v1/purchase/preprocess/getSkuListByRunid",{params:data});
}
function doneAssemblyForm(data){
	return request.get("/erp/api/v1/purchase/preprocess/doneAssemblyForm",{params:data});
}

export default{
	update,getRunsForm,getFormReceiveById,receiveQuotainfos,saveItem,clearRunsForm,getFormList,
	getItemsFormConsumable,updateRunsConsumableForm,
	downRecPDFForm,downPDFAssemblyForm,downPDFAssemblyFormId,getSkuListByRunid,doneAssemblyForm
}