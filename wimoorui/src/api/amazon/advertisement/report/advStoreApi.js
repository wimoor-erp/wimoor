import request from "@/utils/request.js";
function findBrandForProfileId(profileid){
	return request.get('/amazonadv/api/v1/AdvertStores/findBrandForProfileId',{params:{"profileid":profileid}});
}
 
function findAssetsForBrandEntityId(profileid,entityid){
	return request.get('/amazonadv/api/v1/AdvertStores/findAssetsForBrandEntityId',{params:{"profileid":profileid,"brandEntityId":entityid}});
}
function getAsset(data){
	return request.get('/amazonadv/api/v1/AdvertStores/getAsset',{params:data});
}

function findStoresForProfileId(profileid){
	return request.get('/amazonadv/api/v1/AdvertStores/findStoresForProfileId',{params:{"profileid":profileid}});
}

function findAsinsForStorePageUrl(profileId,brandEntityId,storePageUrl){
	return request.get('/amazonadv/api/v1/AdvertStores/findAsinsForStorePageUrl',
	{params:{"profileId":profileId,"brandEntityId":brandEntityId,"storePageUrl":storePageUrl}});
}


export default{
	findAssetsForBrandEntityId,findBrandForProfileId,getAsset,findAsinsForStorePageUrl,findStoresForProfileId,
}