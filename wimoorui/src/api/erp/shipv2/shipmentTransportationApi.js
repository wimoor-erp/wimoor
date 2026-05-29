
import request from "@/utils/request.js";
 
function generateTransportationOptions(data){
	return request.post('/amazon/api/v2/shipInboundPlan/transportation/generateTransportationOptions',data);
}
function listTransportationOptions(data){
	return request.post("/amazon/api/v2/shipInboundPlan/transportation/listTransportationOptions",data);
}
function confirmTransportationOptions(formid,data){
	return request.post("/amazon/api/v2/shipInboundPlan/transportation/confirmTransportationOptions/"+formid,data);
}
function confirmTransportationOptionsByForm(formid){
	return request.get("/amazon/api/v2/shipInboundPlan/transportation/confirmTransportationOptionsByForm",{params:{"formid":formid}});
}
function saveSelfTrans(data){
	return request.post("/amazon/api/v2/shipInboundPlan/transportation/saveSelfTrans",data);
}
function getSelfTransHis(data){
	return request.get("/amazon/api/v2/shipInboundPlan/transportation/getSelfTransHis",{params:data});
}
function saveCustomsList(data){
	return request.post("/amazon/api/v2/shipInboundPlan/transportation/saveCustomsList",data);
}
function getCustomsList(data){
	return request.get("/amazon/api/v2/shipInboundPlan/transportation/getCustomsList",{params:data});
}
 function saveTransCompany(data){
 	return request.post("/amazon/api/v2/shipInboundPlan/transportation/saveTransCompany",data);
 }
 function saveTransportationNeedInfo(data){
 	return request.post("/amazon/api/v2/shipInboundPlan/transportation/saveTransportationNeedInfo",data);
 } 
 function saveConfirmTransportationOptionsInfo(data){
 	return request.get("/amazon/api/v2/shipInboundPlan/transportation/saveConfirmTransportationOptionsInfo",{params:data});
 }
 
export default{
	 generateTransportationOptions,listTransportationOptions,confirmTransportationOptions,saveSelfTrans,getSelfTransHis,getCustomsList,saveCustomsList,
	 saveTransCompany,saveTransportationNeedInfo,saveConfirmTransportationOptionsInfo,confirmTransportationOptionsByForm,
	 
}