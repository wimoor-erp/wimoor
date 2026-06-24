 import request from "@/utils/request.js";
  import downloadhandler from "@/utils/download-handler.js";
 function getSettlementAccReport(data){
 	 return request.post("/amazon/api/v1/fin/settlementAccReport/getSettlementAccReport",data)
 }
 function checkSettlementSummary(data){
 	 return request.post("/amazon/api/v1/fin/settlementAccReport/checkSettlementSummary",data)
 }
 function doSettlementSummary(data){
 	 return request.post("/amazon/api/v1/fin/settlementAccReport/doSettlementSummary",data)
 }
 
 
 function getSettlementAccReportSum(data){
 	 return request.post("/amazon/api/v1/fin/settlementAccReport/getSettlementAccReportSum",data)
 }
 
 function downSettlementExcel(data,callback){
 	  return request({url:"/amazon/api/v1/settlement/downSettlementExcel",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"settlementAccExcel.xlsx");
 						if(callback){
 							callback();
 						}
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 						if(callback){
 							callback(e);
 						}
 				});;
 } 
 
 function getSettlementOverAll(data){
	 return request.post("/amazon/api/v1/settlement/getSettlementOverAll",data)
 }
 
 
 function downloadAllSettlementExcel(data,callback){
 	  return request({url:"/amazon/api/v1/fin/settlementAccReport/downloadAllSettlementExcel",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"settlementAllExcel.xlsx");
 						if(callback){
 							callback();
 						}
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 						if(callback){
 							callback(e);
 						}
 				});;
 } 
 
 function downloadSettlementOverAll(data,callback){
 	  return request({url:"/amazon/api/v1/settlement/downloadSettlementOverAll",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"settlementOverAllExcel.xlsx");
 						if(callback){
 							callback();
 						}
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 						if(callback){
 							callback(e);
 						}
 				});;
 } 
 

function getSummaryMonth(data){
	 return request.post("/amazon/api/v1/settlement/getSummaryMonth",data)
}
function getDetail(data){
	 return request.post("/amazon/api/v1/settlement/getDetail",data)
}
function getSettmentDetail(data){
	 return request.post("/amazon/api/v1/settlement/getSettmentDetail",data)
}
 
 
 export default{
 	getSettlementAccReport,getSettlementAccReportSum,downSettlementExcel,getSettlementOverAll,
	downloadSettlementOverAll,getSummaryMonth,getDetail,getSettmentDetail,downloadAllSettlementExcel,
	checkSettlementSummary,doSettlementSummary
 }