import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";


 function getInvDayField(data){
 	 return request.get("/erp/api/v1/inventory/report/getInvDayField",{params:data})
 }
 
 function getInvDayDetail(data){
 	 return request.post("/erp/api/v1/inventory/report/getInvDayDetail",data)
 }
 
 function getInvDayDetailExport(data){
 	return request({url:"/erp/api/v1/inventory/report/getInvDayDetailExport",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"inventoryDaily.xlsx")
 									}).catch(e=>{
 											downloadhandler.downloadFail();
 									}); 
 }
 function findUnsalableReportExcel(data,callback){
 	return request({url:"/erp/api/v1/inventory/report/findUnsalableReportExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"UnsalableReport.xlsx");
											if(callback){
												callback();
											}
 									}).catch(e=>{
 											downloadhandler.downloadFail();
											if(callback){
												callback();
											}
 									}); 
 }
 function findUnsalableReportByDayExcel(data,callback){
 	return request({url:"/erp/api/v1/inventory/report/findUnsalableReportByDayExcel",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"UnsalableReport"+data.days+".xlsx");
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
 function downloadOutstockformOut(data,callback){
 	return request({url:"/erp/api/v1/inventory/report/downloadOutstockformOut",
 				                    responseType:"blob",
 									params:data,
 									method:'get'}).then(res => {
 											downloadhandler.downloadSuccess(res,"downloadOutstockformOut.xlsx");
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
 
 function findUnsalableReport(data){
 	 return request.post("/erp/api/v1/inventory/report/findUnsalableReport",data)
 }
 
 
 function downExcelInventorySummaryMonth(data,callback){
 	return request({url:"/erp/api/v1/inventory/report/downExcelInventorySummaryMonth",
 				                    responseType:"blob",
 									data:data,
 									method:'post'}).then(res => {
 											downloadhandler.downloadSuccess(res,"UnsalableReport"+data.days+".xlsx");
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
 
 function getAllInventorySummaryMonth(data){
 	 return request.post("/erp/api/v1/inventory/report/getAllInventorySummaryMonth",data)
 }
 function inventoryCost(data){
 	 return request.post("/erp/api/v1/inventory/report/inventoryCost",data)
 }
 
 function downLoadInvCost(data,callback){
 	return request({url:"/erp/api/v1/inventory/report/downLoadInvCost",
						responseType:"blob",
						data:data,
						method:'post'}).then(res => {
								downloadhandler.downloadSuccess(res,"selfWarehouseGoodReport"+data.days+".xlsx");
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
 	 getInvDayField,getInvDayDetail,getInvDayDetailExport,
	findUnsalableReport,findUnsalableReportExcel,
	 findUnsalableReportByDayExcel,getAllInventorySummaryMonth,
	 downExcelInventorySummaryMonth,inventoryCost,downLoadInvCost,
	 downloadOutstockformOut,
 }