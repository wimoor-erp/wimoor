 import request from "@/utils/request.js";
 import downloadhandler from "@/utils/download-handler.js";
 function getMyCurrencyRate(data){
 	 return request.get("/amazon/api/v1/exchangeRate/getMyCurrencyRate",{params:data});
 }
 function saveMyCurrencyRate(data){
 	 return request.post("/amazon/api/v1/exchangeRate/saveMyCurrencyRate",data);
 }
 function downFinRate(data,callback){
 	  return request({url:"/amazon/api/v1/exchangeRate/downFinRate",
 	                responseType:"blob",
 					params:data,
 					method:'get'}).then(res => {
 						downloadhandler.downloadSuccess(res,"ExchangeRateReport.xlsx");
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
 	getMyCurrencyRate,saveMyCurrencyRate,downFinRate
 }