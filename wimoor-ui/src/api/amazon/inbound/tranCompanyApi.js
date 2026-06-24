import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getShipmentFeeReport(data){ 
	 return request.post('/amazon/api/v1/shipTransCompany/getShipmentFeeReport',data);
}
function getShipmentFeeDetailReport(data){ 
	 return request.post('/amazon/api/v1/shipTransCompany/getShipmentFeeDetailReport',data);
}
function getShipmentSkuFeeReport(data){ 
	 return request.post('/amazon/api/v1/shipTransCompany/getShipmentSkuFeeReport',data);
}

function getShipmentFeeRecordReport(data){ 
	 return request.post('/amazon/api/v1/shipTransCompany/getShipmentFeeRecordReport',data);
}
 
 function downShipmentFeeReportExcel(data,callback){
 	return request({url:"/amazon/api/v1/shipTransCompany/downShipmentFeeReportExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"ShipmentFeeReportExcel.xlsx")
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
 function downShipmentFeeDetailReportExcel(data,callback){
 	return request({url:"/amazon/api/v1/shipTransCompany/downShipmentFeeDetailReportExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"ShipmentFeeDetailReportExcel.xlsx")
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
 function downShipmentFeeRecordReportExcel(data,callback){
 	return request({url:"/amazon/api/v1/shipTransCompany/downShipmentFeeRecordReportExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"ShipmentFeeRecordReportExcel.xlsx")
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
 
 
 
 

export default{
	 getShipmentFeeReport,getShipmentFeeDetailReport,getShipmentSkuFeeReport,downShipmentFeeReportExcel,downShipmentFeeDetailReportExcel,getShipmentFeeRecordReport,downShipmentFeeRecordReportExcel,
}
