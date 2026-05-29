import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 function list(data){
 	 return request.post("/erp/api/v1/inventoryRecord/list",data)
 }
 function getFormTypeList(data){
 	 return request.get("/erp/api/v1/inventoryRecord/getFormTypeList",{params:data})
 }
 function downloadRecordsExcel(data){
 	return request({url:"/erp/api/v1/inventoryRecord/downloadRecordsExcel",
 			responseType:"blob",
 			data:data,
 			method:'post'}).then(res => {
 					downloadhandler.downloadSuccess(res,"inventoryFormRecords.xlsx")
 			}).catch(e=>{
 					downloadhandler.downloadFail(e);
 			}); 
 }
 function downloadConsumableExcel(data){
 	return request({url:"/erp/api/v1/inventoryRecord/downloadConsumableExcel",
 			responseType:"blob",
 			data:data,
 			method:'post'}).then(res => {
 					downloadhandler.downloadSuccess(res,"inventoryFormConsumable.xlsx")
 			}).catch(e=>{
 					downloadhandler.downloadFail(e);
 			}); 
 }
 function findInvDayList(data){
 	 return request.post("/erp/api/v1/inventoryRecord/findInvDayList",data)
 }
 
 
 export default{
 	list,getFormTypeList,downloadRecordsExcel,findInvDayList,downloadConsumableExcel,
 }