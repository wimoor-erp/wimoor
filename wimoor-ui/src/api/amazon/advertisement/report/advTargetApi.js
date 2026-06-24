import request from "@/utils/request.js";

function getProductTargeList(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getProductTargeList',data);
}
function getSumProductTarge(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getSumProductTarge',data);
}
function getProductTargeChart(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getProductTargeChart',data);
}
function getTargetBidRecommendations(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetBidRecommendations',data);
}
function getNegativaTargeList(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getNegativaTargeList',data);
}
function getProductTargeQueryList(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getProductTargeQueryList',data);
}
function getProductTargeQueryChart(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getProductTargeQueryChart',data);
}

function getTargetExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/targetsExt/'+profileid+"/"+CampaignType+"/ignoreRepeat",data);
}
function getTarget(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/targets/'+profileid+'/'+CampaignType,data);
}
function targetsByParam(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/targetsByParam/'+profileid+'/'+CampaignType,data);
}
 
function updateTargetList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/updateTargetList/'+profileid+'/'+CampaignType,data);
}
function createTargetList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/createTargetList/'+profileid+'/'+CampaignType,data);
}
function createNegativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/createNegativaTargets/'+profileid+'/'+CampaignType,data);
}
function createCampNegativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/createCampNegativaTargets/'+profileid+'/'+CampaignType,data);
}
function updateNegativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/updateNegativaTargets/'+profileid+'/'+CampaignType,data);
}
function updateCampNegativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/updateCampNegativaTargets/'+profileid+'/'+CampaignType,data);
}
function negativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/negativaTargets/'+profileid+'/'+CampaignType,data);
}
function campNegativaTargets(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/campNegativaTargets/'+profileid+'/'+CampaignType,data);
}
function negativaTargetsExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/negativaTargetsExt/'+profileid+'/'+CampaignType+"/ignoreRepeat",data);
}
function campNegativaTargetsExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/campNegativaTargetsExt/'+profileid+'/'+CampaignType+"/ignoreRepeat",data);
}
function getBrandRecommendations(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getBrandRecommendations',data);
} 
function getNegativeTargetsBrandsRecommendations(profileid,CampaignType,data){
	return request.get('/amazonadv/api/v1/advProductTargeManager/getNegativeTargetsBrandsRecommendations/'+profileid+'/'+CampaignType,{params:data});
} 
function getTargetsCategoriesRecommendations(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetsCategoriesRecommendations/'+profileid+'/'+CampaignType,data);
}
function getRecommendationsProductCount(profileid,CampaignType,data){
	data.profileid=profileid;
	data.CampaignType=CampaignType;
	return request.post('/amazonadv/api/v1/advProductTargeManager/getRecommendationsProductCount',data);
}
function getTargetsCategoriesAllRecommendations(profileid,CampaignType,data){
		return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetsCategoriesAllRecommendations/'+profileid+'/'+CampaignType,data);
}
function getTargetsRefineRecommendations(profileid,CampaignType,data){
		return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetsRefineRecommendations/'+profileid+'/'+CampaignType,data);
}
function getTargetsProductsRecommendations(profileid,CampaignType,data){
		return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetsProductsRecommendations/'+profileid+'/'+CampaignType,data);
}

function getTargetsBidRecommendations(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getTargetsBidRecommendations/'+profileid+'/'+CampaignType+"/ignoreRepeat",data);
}
function getNagetiveTargetforQuery(data){
	return request.post('/amazonadv/api/v1/advProductTargeManager/getNagetiveTargetforQuery',data);
}

 
export default{
	getTargetsCategoriesRecommendations,getRecommendationsProductCount,getTargetsCategoriesAllRecommendations,
	getTargetsRefineRecommendations,getTargetsProductsRecommendations,getTargetsBidRecommendations,getNagetiveTargetforQuery,
	getSumProductTarge,getProductTargeChart,getTargetBidRecommendations,getBrandRecommendations,
	
	getProductTargeQueryList,getProductTargeQueryChart,
	
	getProductTargeList,
	getTarget,getTargetExt,targetsByParam,createTargetList,updateTargetList,
	
	getNegativaTargeList,
	negativaTargets,negativaTargetsExt,createNegativaTargets,updateNegativaTargets,
	campNegativaTargets, campNegativaTargetsExt,createCampNegativaTargets,updateCampNegativaTargets,
	getNegativeTargetsBrandsRecommendations,
	
}