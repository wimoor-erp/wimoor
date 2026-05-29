import request from "@/utils/request.js";

function getProductAdList(data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getProductAdList',data);
}
function getPurchaseProductAdList(data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getPurchaseProductAdList',data);
}
function getSDPurchaseProductAdList(data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getSDPurchaseProductAdList',data);
}
function getSumProductAd(data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getSumProductAd',data);
}
function getProductAdChart(data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getProductAdChart',data);
}
function getProductAdotherAsin(data){
	return request.get('/amazonadv/api/v1/advProductAdsManager/getProductAdotherAsin',{params:data});
}
function getProductAdsExtList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getProductAdsExt/'+profileid+"/"+CampaignType+"/ignoreRepeat",data);
}
function getProductAds(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/getProductAds/'+profileid+'/'+CampaignType,data);
}
function updateProductAdList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/updateProductAdList/'+profileid+'/'+CampaignType,data);
}
function createProductAdList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductAdsManager/createProductAdList/'+profileid+'/'+CampaignType,data);
}
function getProductAdsDetail(profileid,CampaignType,data){
	return request.get('/amazonadv/api/v1/advProductAdsManager/getProductAdsDetail/'+profileid+'/'+CampaignType,{params:data});
}


export default{
	getProductAdList,getSumProductAd,getProductAdChart,getProductAdotherAsin,getProductAdsExtList,getProductAds,
	createProductAdList,updateProductAdList,getProductAdsDetail,getPurchaseProductAdList,getSDPurchaseProductAdList,
}