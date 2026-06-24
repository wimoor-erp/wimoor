import request from "@/utils/request.js";

 function productList(data){
 	 return request.post("/amazon/api/v1/report/product/productInfo/productList",data)
 }
 function getProStatusList(data){
	 return request.get("/amazon/api/v1/report/product/productInfo/getProStatusList",{params:data})
 }
 function changeProPrice(data){
 	 return request.get("/amazon/api/v1/report/product/productInfo/changeProPrice",{params:data})
 }
 function updateRemark(data){
	  return request.get("/amazon/api/v1/report/product/productInfo/updateRemark",{params:data})
 }
 function ProductPriceLocked(data){
 	  return request.get("/amazon/api/v1/report/product/productInfo/ProductPriceLocked",{params:data})
 }
 function cancelProductPriceLocked(data){
 	  return request.get("/amazon/api/v1/report/product/productInfo/cancelProductPriceLocked",{params:data})
 }
 function priceQueue(data){
 	  return request.post("/amazon/api/v1/report/product/productInfo/priceQueue",data)
 }
 function getDim(data){
 	  return request.get("/amazon/api/v1/report/product/productInfo/getDim",{params:data})
 }
 function disable(data){
	 return request.get("/amazon/api/v1/report/product/productInfo/disable",{params:data})
 }
 function undisable(data){
 	 return request.get("/amazon/api/v1/report/product/productInfo/undisable",{params:data})
 }
 function getInfoByMsku(data){
 	 return request.get('/amazon/api/v1/report/product/productInfo/getInfoByMsku',{params:data});
 }
 function follow(followid){
 	 return request.get('/amazon/api/v1/report/product/productInfo/follow',{params:{"followid":followid}});
 }
 function recover(data){
	 return request.get('/amazon/api/v1/report/product/productInfo/updateProInvlid',{params:data});
 }
 function getProductInfoSimple(data){
 	return request.post('/amazon/api/v1/report/product/productInfo/getProductInfoSimple',data);
 }
 function getProductInfoList(data){
 	return request.post('/amazon/api/v1/report/product/productInfo/getProductInfoList',data);
 }
 function productParentListDetail(data){
  return request.post("/amazon/api/v1/report/product/productInfo/productParentListDetail",data)
 	}
 export default{
 	productList,getProStatusList,changeProPrice,updateRemark,ProductPriceLocked,follow,
	cancelProductPriceLocked,priceQueue,getDim,disable,undisable,getInfoByMsku,recover,
	getProductInfoSimple,getProductInfoList,productParentListDetail
 }