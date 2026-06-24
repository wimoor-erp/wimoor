import request from "@/utils/request.js";

function getKeywordList(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getKeywordList',data);
}
function getSumAdvKeywords(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getSumAdvKeywords',data);
}
function getKeywordChart(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getKeywordChart',data);
}
function getKeywordSuggestBid(data){
	return request.get('/amazonadv/api/v1/advKeywordManager/getKeywordSuggestBid',{params:data});
}
function getKeywordQueryList(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getKeywordQueryList',data);
}
function getKeywordQueryChart(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getKeywordQueryChart',data);
}
function getNegativaKeywordsList(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getNegativaKeywordsList',data);
}
function getkeywordsExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/keywordsExt/'+profileid+"/"+CampaignType+"/ignoreRepeat",data);
}
function getKeywords(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/keywords/'+profileid+'/'+CampaignType,data);
}
function updateKeywordList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/updateKeywordList/'+profileid+'/'+CampaignType,data);
}
function createKeywordList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/createKeywordList/'+profileid+'/'+CampaignType,data);
}
function getKeywordforQuery(data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getKeywordforQuery',data);
}
function amzCreateKeywordNegativa(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/amzCreateKeywordNegativa/'+profileid+'/'+CampaignType,data);
}
function amzCreateCampKeywordNegativa(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/amzCreateCampKeywordNegativa/'+profileid+'/'+CampaignType,data);
}
function campKeywordNegativaExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/campKeywordNegativaExt/'+profileid+"/"+CampaignType,data);
}
function keywordNegativaExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/keywordNegativaExt/'+profileid+"/"+CampaignType,data);
}
function updateCampNegativaKeywordList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/updateCampNegativaKeywordList/'+profileid+'/'+CampaignType,data);
}
function updateNegativaKeywordList(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/updateNegativaKeywordList/'+profileid+'/'+CampaignType,data);
}
function campKeywordNegativa(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/campKeywordNegativa/'+profileid+'/'+CampaignType,data);
}
function keywordNegativa(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/keywordNegativa/'+profileid+'/'+CampaignType,data);
}
function getSuggestedkeywords(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getSuggestedkeywords/'+profileid+'/'+CampaignType,data);
}
function getAmzKeywordBid(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advKeywordManager/getAmzKeywordBid/'+profileid+'/'+CampaignType,data);
}
 
 
 
 
export default{
	getKeywordList,getSumAdvKeywords,getKeywordChart,getKeywordSuggestBid,getKeywordQueryList,getKeywordQueryChart,
	getNegativaKeywordsList,createKeywordList,updateKeywordList,getKeywords,getkeywordsExt,getKeywordforQuery,
	amzCreateKeywordNegativa,amzCreateCampKeywordNegativa,campKeywordNegativaExt,updateCampNegativaKeywordList,
	campKeywordNegativa,updateNegativaKeywordList,keywordNegativaExt,keywordNegativa,getSuggestedkeywords,getAmzKeywordBid,
}