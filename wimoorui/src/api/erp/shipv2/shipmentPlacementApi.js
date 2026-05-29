
import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function listPlacementOptions(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/listPlacementOptions',data);
}
function generatePlacementOptions(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/generatePlacementOptions',{params:data});
}
function confirmPlacementOption(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/confirmPlacementOption',{params:data});
}
function listDeliveryWindowOptions(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/listDeliveryWindowOptions/ignoreRepeat',data);
}
function generateDeliveryWindowOptions(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/generateDeliveryWindowOptions/ignoreRepeat',{params:data});
}
function confirmDeliveryWindowOptions(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/confirmDeliveryWindowOptions/ignoreRepeat',data);
}

function getShipment(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/getShipment/ignoreRepeat',{params:data});
}
function getShipmentItems(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/getShipmentItems/ignoreRepeat',data);
}
function listShipmentBoxes(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/listShipmentBoxes/ignoreRepeat',data);
}
function shippedInboundPlan(id,data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/shippedInboundPlan/'+id,data);
}

function saveTransportation(data){
	return request.post('/amazon/api/v2/shipInboundPlan/transportation/saveTransportation',data);
	
}
function list(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/list',data);
}

function getBaseInfo(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/getBaseInfo',{params:data});
}
function getBaseInfoByPlan(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/getBaseInfosByPlan',{params:data});
}
function downLabel(data,callback){
	return request({url:"/amazon/api/v2/shipInboundPlan/shipment/getPkgLabelUrl",
				    responseType:"blob",
				    params:data,
					method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"shipment.pdf");
						if(callback){
							callback();
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});;
}
function getShipCart(data){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/getShipCart',{params:data});
} 
function getPaper(){
	return request.get('/amazon/api/v2/shipInboundPlan/shipment/getPaper');
} 
function saveTransTrace(data){
	return request.post('/amazon/api/v2/shipInboundPlan/shipment/saveTransTrace',data);
}
 function feedbackTransTrace(data){
	 return request.post("/amazon/api/v2/shipInboundPlan/shipment/feedbackTransTrace",data);
 }
 function downExcelBoxDetail(data){
 	return request({url:"/amazon/api/v2/shipInboundPlan/shipment/downExcelBoxDetail",
 				    responseType:"blob",
 				    params:data,
 					method:'get'});
 }
 function downExcelBox(data){
 	return request({url:"/amazon/api/v2/shipInboundPlan/shipment/downExcelBox",
 				    responseType:"blob",
 				    params:data,
 					method:'get'});
 }
 
 function boxShipment(formid,data){
 	return request.post('/amazon/api/v2/shipInboundPlan/shipment/doneShipmentBox/'+formid,data);
 }
 
 function updateFeeByShipment(data){
 	return request.post('/amazon/api/v2/shipInboundPlan/shipment/updateFeeByShipment',data);
 } 
 
  function updateRemarkByShipment(data){
  	return request.get('/amazon/api/v2/shipInboundPlan/shipment/updateRemarkByShipment',{params:data});
  } 
  
  function saveshipment(data){
	  return request.get('/amazon/api/v2/shipInboundPlan/shipment/saveshipment',{params:data});
  }
  
  function saveshipments(formid,data){
  	  return request.post('/amazon/api/v2/shipInboundPlan/shipment/saveshipments/'+formid,data);
  }
  
 function toQuote(data,token){
 	return request.post('/amazon/api/v2/shipInboundPlan/shipment/toQuote/'+token,data);
 } 
 
 function ignoreShipment(data){
 	return request.get('/amazon/api/v2/shipInboundPlan/shipment/ignoreShipment',{params:data});
 } 
 function saveDestinationBox(data){
 	return request.get('/amazon/api/v2/shipInboundPlan/shipment/saveDestinationBox',{params:data});
 } 
 function listDestination(data){
 	return request.get('/amazon/api/v2/shipInboundPlan/shipment/listDestination',{params:data});
 } 
 
export default{
	 listPlacementOptions,generatePlacementOptions,confirmPlacementOption,
	 listDeliveryWindowOptions,generateDeliveryWindowOptions,confirmDeliveryWindowOptions,
	 getShipment,getShipmentItems,listShipmentBoxes,shippedInboundPlan,
	 saveTransportation,downLabel,list,getBaseInfo,getShipCart,
	 saveTransTrace,feedbackTransTrace,downExcelBoxDetail,getPaper,downExcelBox,
	 boxShipment,updateFeeByShipment,updateRemarkByShipment,saveshipment,getBaseInfoByPlan,
	 saveshipments,toQuote,ignoreShipment,saveDestinationBox,listDestination
}