import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getlist(data){ 
	 return request.post('/erp/api/v1/shipTransCompany/list',data);
}
function loadApiCompany(){
  return request.get('/erp/api/v1/shipTransCompany/loadApiCompany');
}
function saveData(data){
  return request.post('/erp/api/v1/shipTransCompany/saveData',data);
}
function updateData(data){
  return request.post('/erp/api/v1/shipTransCompany/updateData',data);
}
function channelList(){
  return request.get('/erp/api/v1/shipTransCompany/channelList');
}
function updateChannel(data){
  return request.post('/erp/api/v1/shipTransCompany/updateChannel',data);
}
function saveChannel(data){
  return request.post('/erp/api/v1/shipTransCompany/saveChannel',data);
}
function deleteChannel(data){
  return request.get('/erp/api/v1/shipTransCompany/deleteChannel',{params:data});
}
function saveCompanyDetail(data){
  return request.post('/erp/api/v1/shipTransCompany/saveDetail',data);
}
function showCompanyDetail(data){
  return request.get('/erp/api/v1/shipTransCompany/showDetail',{params:data});
}
function showDelDetail(data){
  return request.get('/erp/api/v1/shipTransCompany/showDelDetail',{params:data});
}

function deleteCompany(data){
  return request.get('/erp/api/v1/shipTransCompany/deleteInfo',{params:data});
}
function getTransType(){
  return request.get('/erp/api/v1/shipTransCompany/getTransType');
}
function getTransTypeAll(){
  return request.get('/erp/api/v1/shipTransCompany/getTransTypeAll');
}
function delTransType(data){
  return request.get('/erp/api/v1/shipTransCompany/delTransType',{params:data});
}
function saveTransType(data){
  return request.post('/erp/api/v1/shipTransCompany/saveTransType',data);
}
function getCompanyList(){
  return request.get('/erp/api/v1/shipTransCompany/getCompanyList');
}
function getCompanyTranstypeList(data){
  return request.get('/erp/api/v1/shipTransCompany/getCompanyTranstypeList',{params:data});
}
function getChannel(data){ 
	 return request.get('/erp/api/v1/shipTransCompany/getChannel',{params:data });
}
function listitem(data){
  return request.post('/erp/api/v1/shipTransCompany/listitem',data);
}
function downloadList(data){
	return request({url:"/erp/api/v1/shipTransCompany/downloadShipTransList",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"ShipTransList.xlsx")
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
				});
		}
function uploadTransFile(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/shipTransCompany/uploadTransDetail",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
	
}
function uploadPriceFile(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/shipTransCompany/uploadPrice",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
	
}
function getTranlist(data){ 
	 return request.get('/erp/api/v1/shipTransCompany/getTranlist',{params:data });
}
function shipTransDetial(data){ 
	 return request.get('/erp/api/v1/shipTransCompany/shipTransDetial',{params:data });
}
function shipTransDetialShipment(data){ 
	 return request.get('/erp/api/v1/shipTransCompany/shipTransDetialShipment',{params:data });
}
function getShipFeeDetailReport(data){
  return request.post('/erp/api/v1/inventory/dispatch/overseaTrans/getShipFeeDetailReport',data);
}
function downShipFeeDetailReportExcel(data,callback){
	return request({url:"/erp/api/v1/inventory/dispatch/overseaTrans/downShipFeeDetailReportExcel",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
						downloadhandler.downloadSuccess(res,"ShipFeeDetailReport.xlsx");
						if(callback){
							callback();
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
						if(callback){
							callback();
						}
				});
}
function getShipSkuFeeReport(data){
  return request.post('/erp/api/v1/inventory/dispatch/overseaTrans/getShipSkuFeeReport',data);
}
function downShipFeeReportExcel(data,callback){
	return request({url:"/erp/api/v1/inventory/dispatch/overseaTrans/downShipFeeReportExcel",
				                    responseType:"blob",
									data:data,
									method:'post'}).then(res => {
						downloadhandler.downloadSuccess(res,"ShipFeeReport.xlsx");
						if(callback){
							callback();
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
						if(callback){
							callback();
						}
				});
}
function getShipFeeReport(data){
  return request.post('/erp/api/v1/inventory/dispatch/overseaTrans/getShipFeeReport',data);
}

export default{
	getlist,
	getTranlist,
	getChannel,
	loadApiCompany,
	saveData,
	updateData,
	channelList,
	updateChannel,
	saveChannel,
	deleteChannel,
	saveCompanyDetail,
	showCompanyDetail,
	deleteCompany,
	getTransType,
	getTransTypeAll,
	delTransType,
	saveTransType,
	getCompanyList,
	getCompanyTranstypeList,
	listitem,
	showDelDetail,
	downloadList,
	uploadTransFile,
	uploadPriceFile,
	shipTransDetial,
	shipTransDetialShipment,
	getShipFeeDetailReport,
	downShipFeeDetailReportExcel,
	getShipSkuFeeReport,
	downShipFeeReportExcel,
	getShipFeeReport, 
}