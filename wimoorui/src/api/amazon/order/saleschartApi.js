import request from "@/utils/request.js";
 function salesLine(data){
 	 return request.post("/amazon/api/v1/report/order/salesLine",data)
 }
  
 export default{
 	salesLine
 }