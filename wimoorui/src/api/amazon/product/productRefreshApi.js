import request from "@/utils/request.js";
 function refreshItemBySKU(data){
 	 return request.get("/amazon/api/v1/report/product/amzProductRefresh/refreshItemBySKU",{params:data})
 }
 export default{
 	refreshItemBySKU
 }