 import request from "@/utils/request.js";
  import downloadhandler from "@/utils/download-handler.js";
 
 function proCommodity(data){
 	 return request.post("/amazon/api/v1/settlement/proCommodity",data)
 }
 
 function downDataExcel(data,callback){
 	  return request({url:"/amazon/api/v1/settlement/downDataExcel",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"settlementSkuExcel.xlsx");
 						if(callback){
 							callback();
 						}
 				}).catch(e=>{
 					    downloadhandler.downloadFail(e);
 						if(callback){
 							callback(e);
 						}
 				});
 } 
 function findAmzSettlementAccStatement(data){
	 return request.post("/amazon/api/v1/settlementAccStatement/findAmzSettlementAccStatement",data)
 } 
 function getGroupMarket(data){
 	 return request.get("/amazon/api/v1/settlement/getGroupMarket",{params:data})
 }
 function saveFinStatement(data){
 	 return request.post("/amazon/api/v1/settlementAccStatement/saveFinStatement",data)
 } 
 function deleteAmzSettlementAccStatement(data){
 	 return request.get("/amazon/api/v1/settlementAccStatement/deleteAmzSettlementAccStatement",{params:data})
 } 
function selectSettlementOpen(data){
	return request.post("/amazon/api/v1/settlementAccStatement/selectSettlementOpen",data)
}
 function downloadSettlementOpen(data,callback){
 	  return request({url:"/amazon/api/v1/settlementAccStatement/downloadSettlementOpen",
 	                responseType:"blob",
 					data:data,
 					method:'post'}).then(res => {
 						downloadhandler.downloadSuccess(res,"AmzSettementsOpenReport.xlsx");
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
 
 
 export default{
 	proCommodity,downDataExcel,findAmzSettlementAccStatement,getGroupMarket,saveFinStatement,
	deleteAmzSettlementAccStatement,selectSettlementOpen,downloadSettlementOpen,
 }