import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getShipment(shipmentid){ 
	 return request.get('/amazon/api/v1/shipForm/info/'+shipmentid);
}

function getshipList(data){ 
	 return request.post('/amazon/api/v1/shipForm/list',data);
}


function downPDFShipForm(data){
	return request({url:"/erp/api/v1/shipForm/downPDFShipForm",
				    responseType:"blob",
					params:data,
					method:'get'});
}
function downPDFShipForms(type,data){
	return request({url:"/erp/api/v1/shipForm/downPDFShipForm/"+type,
				    responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"downPDFShipForm.pdf")
					}).catch(e=>{
							downloadhandler.downloadFail(e);
					}); 
}
function downPDFLabel(data){
	return request({url:"/amazon/api/v1/shipForm/downPDFLabel",
				    responseType:"blob",
					data:data,
					method:'post'});
}
function downExcelLabel(data){
	return request({url:"/amazon/api/v1/shipForm/downExcelLabel",
				    responseType:"blob",
					data:data,
					method:'post'});
}

function updateShipment(data){
	 return request.post('/erp/api/v1/shipForm/updateShipment',data);
}

function requestInboundShipment(data){
	return request.get('/amazon/api/v1/shipForm/requestInboundShipment',{params:data });
}
function disableShipment(data){
	return request.get('/erp/api/v1/shipForm/disableShipment',{params:data });
}
function localDoneShipment(data){
	return request.get('/amazon/api/v1/shipForm/marketShipped',{params:data});
}
function amazonDoneShipment(data){
	return request.get('/amazon/api/v1/shipForm/marketAmazonShipped',{params:data});
}

function validateShipment(data){
	return request.get('/amazon/api/v1/shipForm/validateShipment',{params:data });
}
function getBaseInfo(data){
	return request.get('/amazon/api/v1/shipForm/getBaseInfo',{params:data});
}
function getCarrier(data){
	return request.get('/amazon/api/v1/shipForm/getCarrier',{params:data});
}
function getFbaCenter(data){
	return request.get('/amazon/api/v1/amzShipFulfillmentCenter/list');
}

function getBoxDetial(data){
	return request.get('/amazon/api/v1/shipForm/getBoxDetial',{params:data});
}
function submitCart(data){
	return request.post('/amazon/api/v1/shipForm/cart',data);
}
function loadShipmentPrep(data){
	return request.get('/amazon/api/v1/shipForm/loadShipmentPrep',{params:data});
}
function getBoxUse(){
	return request.get('/amazon/api/v1/shipForm/getBoxUse');
}
function checkinv(formid,data){
	return request.post('/amazon/api/v1/shipForm/checkinv/'+formid,data);
}

function downExcelBoxDetail(data){ 
	return request({url:"/amazon/api/v1/shipForm/downExcelBoxDetail",
				    responseType:"blob",
				    params:data,
					method:'get'});
}
function downLabel(data){
	return request({url:"/amazon/api/v1/shipForm/getPkgLabelUrl",
				    responseType:"blob",
				    params:data,
					method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"shipment.pdf");
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});;
}
 
function getShipAmazonInfo(data){
	return request.get('/amazon/api/v1/shipForm/getShipAmazonInfo',{params:data});
}
function saveSelfTrans(data){
	return request.post('/amazon/api/v1/shipForm/saveSelfTrans',data);
}
function saveTransTrace(data){
	return request.post('/amazon/api/v1/shipForm/saveTransTrace',data);
}
function getSelfTransHis(data){
	return request.get('/amazon/api/v1/shipForm/getSelfTransHis',{params:data});
}
function getConsumableList(data){
	return request.get('/erp/api/v1/shipForm/getConsumableList',{params:data});
}
function saveInventoryConsumable(data){
	return request.post('/erp/api/v1/shipForm/saveInventoryConsumable',data);
}
function getShipmentCustomsRecord(data){
	return request.get('/erp/api/v1/shipForm/getShipmentCustomsRecord',{params:data});
}
function uploadCustomsFile(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/shipForm/uploadCustomsFile",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function deleteCustomsFile(data){
	return request.get('/erp/api/v1/shipForm/deleteCustomsFile',{params:data});
}
function getUnSyncShipmentDetial(data){
	return request.get('/erp/api/v1/shipForm/getUnSyncShipmentDetial',{params:data});
}
function saveShipmentItemAndPlanBath(data){
	return request.get('/amazon/api/v1/shipFormSync/saveShipmentItemAndPlanBath',{params:data});
}
function getShipCart(data){
	return request.get('/amazon/api/v1/shipForm/getShipCart',{params:data});
}
function estimateTransTrace(data){
return request.get('/amazon/api/v1/shipForm/estimateTransTrace',{params:data});
}

function confirmTransTrace(data){
return request.get('/amazon/api/v1/shipForm/confirmTransTrace',{params:data});
}
      
   

export default{
	getShipment,
	getshipList,
	downPDFShipForm,
	downPDFLabel,
	downExcelLabel,
	updateShipment,
	disableShipment,
	requestInboundShipment,
	amazonDoneShipment,
	localDoneShipment,
	validateShipment,
	getBaseInfo,
	getCarrier,
	getBoxDetial,
	submitCart,
	downExcelBoxDetail,
	downLabel,
	getShipAmazonInfo,
	saveSelfTrans,
	getSelfTransHis,
	getConsumableList,
	saveInventoryConsumable,
	getShipmentCustomsRecord,
	uploadCustomsFile,
	deleteCustomsFile,
	getUnSyncShipmentDetial,
	saveShipmentItemAndPlanBath,
	saveTransTrace,
	getShipCart,
	estimateTransTrace,
	loadShipmentPrep,
	confirmTransTrace,
	downPDFShipForms,
	checkinv,getFbaCenter,
	getBoxUse,
}