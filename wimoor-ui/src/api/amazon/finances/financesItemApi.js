 import request from "@/utils/request.js";
 function getFinList(data){
 	 return request.post("/amazon/api/v1/amzFinUserItem/getFinList",data)
 }
 function deleteFinItem(data){
	  return request.get("/amazon/api/v1/amzFinUserItem/deleteFinItem",{params:data})
 }
 function saveFinItemName(data){
 	  return request.post("/amazon/api/v1/amzFinUserItem/saveFinItemName",data)
 }
 function getFinListWithoutPage(data){
 	  return request.get("/amazon/api/v1/amzFinUserItem/getFinListWithoutPage",{params:data})
 }
 function getFinListNoPage(data){
 	  return request.get("/amazon/api/v1/amzFinUserItem/getFinListNoPage",{params:data})
 }
 
 
 export default{
 	getFinList,deleteFinItem,saveFinItemName,getFinListWithoutPage,getFinListNoPage,
 }




