 import request from "@/utils/request.js";
 function loadformula(data){
 	 return request.get("/amazon/api/v1/amzFinAccount/loadformula",{params:data})
 }
 
 export default{
 	loadformula,
 }