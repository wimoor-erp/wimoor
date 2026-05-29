import request from "@/utils/request.js";
 function getProStatusByShop(data){
 	 return request.get("/amazon/api/v1/report/product/productInfoStatus/getProStatusByShop",{params:data})
 }
 function deleteProductInfoStatus(data){
	 return request.get("/amazon/api/v1/report/product/productInfoStatus/deleteProductInfoStatus",{params:data})
 }
 function updateProductInfoStatus(data){
 	 return request.get("/amazon/api/v1/report/product/productInfoStatus/updateProductInfoStatus",{params:data})
 }
 export default{
 	getProStatusByShop,deleteProductInfoStatus,updateProductInfoStatus
 }