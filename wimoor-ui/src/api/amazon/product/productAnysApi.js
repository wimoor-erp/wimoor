import request from "@/utils/request.js";

 function productAsinList(data){
	 return request.post("/amazon/api/v1/report/product/analysis/productAsinList",data)
 }
 function productdetail(data){
	 return request.get("/amazon/api/v1/report/product/analysis/productdetail",{params:data})
 }
 function productdetailByInfo(data){
	  return request.get("/amazon/api/v1/report/product/analysis/productdetailByInfo",{params:data})
 }
 function updateAnyRemark(data){
	  return request.get("/amazon/api/v1/report/product/analysis/updateAnyRemark",{params:data})
 }
 function getChartData(data){
 	  return request.get("/amazon/api/v1/report/product/analysis/getChartData",{params:data})
 }
 

 export default{
	productAsinList,productdetail,updateAnyRemark,getChartData,productdetailByInfo,
 }