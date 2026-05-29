import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";

function downPDFShipForm(ftype,data,name,callback){
	return request({url:"/amazon/api/v2/shipInboundPlan/quota/downPDFShipForm/"+ftype,
			responseType:"blob",
			data:data,
			method:'post'}).then(res => {
					downloadhandler.downloadSuccess(res,name+".pdf")
				    if(callback){
						callback(res);
					}
			}).catch(e=>{
					downloadhandler.downloadFail(e);
					if(callback){
						callback(e);
					}
			});
	 
}

function downPDFShipmentForm(ftype,data,name){
	return request({url:"/amazon/api/v2/shipInboundPlan/quota/downPDFShipmentForm/"+ftype,
			responseType:"blob",
			data:data,
			method:'post'}).then(res => {
					downloadhandler.downloadSuccess(res,name+".pdf")
			}).catch(e=>{
					downloadhandler.downloadFail(e);
			});
	 
}
	 

function downPDFLabel(data,name){
	return request({url:"/amazon/api/v2/shipInboundPlan/quota/downPDFLabel",
			responseType:"blob",
			params:data,
			method:'get'}).then(res => {
					downloadhandler.downloadSuccess(res,name+"标签.pdf")
			}).catch(e=>{
					downloadhandler.downloadFail(e);
			});
	 
}


function downExcelLabel(data,name){
	return request({url:"/amazon/api/v2/shipInboundPlan/quota/downExcelLabel",
			responseType:"blob",
			params:data,
			method:'get'}).then(res => {
					downloadhandler.downloadSuccess(res,name+"标签.xlsx")
			}).catch(e=>{
					downloadhandler.downloadFail(e);
			});
	 
}
function info(id){
	return request.get('/amazon/api/v2/shipInboundPlan/quota/info/'+id);
}
function quotainfos(ids){
	return request.post('/amazon/api/v2/shipInboundPlan/quota/quotainfos',ids);
}

function checkinv(id,data){
	return request.post('/amazon/api/v2/shipInboundPlan/quota/checkinv/'+id,data);
}


 
 

export default{
	 downPDFShipForm,downPDFLabel,downExcelLabel,info,checkinv,downPDFShipmentForm,quotainfos
}

