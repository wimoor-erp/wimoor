 import request from "@/utils/request.js";
 function getFinDataList(data){
 	 return request.post("/amazon/api/v1/amzFinUserItem/getFinDataList",data)
 }
 function findFinItemByUser(data){
 	 return request.get("/amazon/api/v1/amzFinUserItem/findFinItemByUser",{params:data})
 }
 function saveFinItemData(data){
 	 return request.get("/amazon/api/v1/amzFinUserItemData/saveFinItemData",{params:data})
 }
 
 
 export default{
 	getFinDataList,findFinItemByUser,saveFinItemData
 }