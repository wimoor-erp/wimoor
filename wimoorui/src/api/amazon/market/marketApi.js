import request from "@/utils/request.js";
function getMarketByGroup(data){
	 return request.get('/amazon/api/v1/amzauthority/getMarketByGroup',{params:data });
}
function getMarketAll(){
	 return request.get('/amazon/api/v1/amzauthority/getMarketAll');
}
 //国家列表
 function getMarketList(data){
 	return request.get("/amazon/api/v1/amzauthority/getMarketBind",{params:data})
 }
 function getBySku(data){
	 return request.get("/amazon/api/v1/amzmarketplace/getBySku",{params:data})
 }
 function getByMSku(data){
 	 return request.get("/amazon/api/v1/amzmarketplace/getByMSku",{params:data})
 }
 function getByCountry(data){
 	 return request.get("/amazon/api/v1/amzmarketplace/getByCountry",{params:data})
 }
export default{
	getMarketByGroup,getMarketList,getMarketAll,getBySku,getByMSku,getByCountry,
}
 