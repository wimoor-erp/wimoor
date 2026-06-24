import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 function getPlanList(data){
 	 return request.post("/amazon/api/v1/product/salesplan/getPlanModel",data)
 }
 function getExpandCountryData(data){
 	  return request.post("/amazon/api/v1/product/salesplan/getExpandCountryData",data);
 }
 function refreshDataByGroup(data){
 	 return request.get("/amazon/api/v1/product/salesplan/refreshDataByGroup",{params:data});
 }
 function refreshDataBySKU(data){
 	 return request.get("/amazon/api/v1/product/salesplan/refreshDataBySKU",{params:data});
 }
 function save(data){
 	 return request.post("/amazon/api/v1/product/salesplan/shipItem/save",data);
 }
 function batch(data){
 	 return request.post("/amazon/api/v1/product/salesplan/shipItem/batch",data);
 }
 function remove(data){
 	 return request.delete("/amazon/api/v1/product/salesplan/shipItem/remove",{params:data});
 }
 function getSummary(data){
 	  return request.get("/amazon/api/v1/product/salesplan/shipItem/getSummary",{params:data});
 }
 function clear(data){
 	  return request.get("/amazon/api/v1/product/salesplan/shipItem/clear",{params:data});
 }
 function list(data){
 	  return request.get("/amazon/api/v1/product/salesplan/shipItem/list",{params:data});
 }
 function subsplit(data){ 
 	  return request.post("/amazon/api/v1/product/salesplan/shipItem/subsplit",data);
 }
 function batchList(data){
 	  return request.get("/amazon/api/v1/product/salesplan/shipItem/getBatchList",{params:data});
 }
 function removeBatchItem(batchnumber){
 	 return request.post("/amazon/api/v1/product/salesplan/shipItem/removeBatch?batchnumber="+batchnumber);
 }
 
 function downloadlist(data){
	 return request({url:"/amazon/api/v1/product/salesplan/shipItem/downloadlist",
	 			    responseType:"blob",
	 				data:data,
	 				method:'post'}).then(res => {
	 						downloadhandler.downloadSuccess(res,"planItem.xlsx")
	 				}).catch(e=>{
	 						downloadhandler.downloadFail(e);
	 				});
 }

 export default{
 	getPlanList,refreshDataBySKU,
	refreshDataByGroup,removeBatchItem,downloadlist,
	getExpandCountryData,save,remove,getSummary,clear,list,batch,batchList,subsplit
 }