import request from "@/utils/request.js";
 function list(data){
 	 return request.post("/amazon/api/v0/orders/fulfillment/list",data)
 }
 // function list(data){
 // 	 return request.get("/amazon/api/v1/report/order/salesLine",{params:data })
 // }
  
 export default{
 	list,
 }