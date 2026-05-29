import request from "@/utils/request.js";
function createAds(profileid,type,data){
	return request.post('/amazonadv/api/v1/ads/createAds/'+profileid+"/"+type,data);
}

function getAdsList(data){
	return request.post('/amazonadv/api/v1/ads/getAdsList',data);
}

function getAdsChart(data){
	return request.post('/amazonadv/api/v1/ads/getAdsChart',data);
}

function getSumAds(data){
	return request.post('/amazonadv/api/v1/ads/getSumAds',data);
}

function getAds(profileid,type,data){
	return request.post('/amazonadv/api/v1/ads/getAds/'+profileid+"/"+type,data);
}
function getPurchaseAdsList(data){
	return request.post('/amazonadv/api/v1/ads/getPurchaseAdsList',data);
}
 
 


export default{
	createAds,getAdsList,getAdsChart,getSumAds,getAds,getPurchaseAdsList,
}