import request from "@/utils/request.js";

function getCampaignList(data){
	return request.post('/amazonadv/api/v1/advCampaignManager/getCampaignList',data);
}
function getSumCampaign(data){
	return request.post('/amazonadv/api/v1/advCampaignManager/getSumCampaign',data);
}
function getCampaignChart(data){
	return request.post('/amazonadv/api/v1/advCampaignManager/getCampaignChart',data);
}
function getCampaignPlacement(data){
	return request.get('/amazonadv/api/v1/advCampaignManager/getCampaignPlacement',{params:data});
}
function getSBVideo(data){
	return request.get('/amazonadv/api/v1/sb/campaigns/getSBVideo',{params:data});
}
function updateCampaignList(data){
	return request.post('/amazonadv/api/v1/advCampaignManager/updateCampaignList',data);
}
function createCampaignList(data){
	return request.post('/amazonadv/api/v1/advCampaignManager/createCampaignList',data);
}
function getCampaigns(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advCampaignManager/getCampaigns/'+profileid+'/'+CampaignType,data);
}
function getCampaignsExt(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/advCampaignManager/getCampaignsExt/'+profileid+'/'+CampaignType+"/ignoreRepeat",data);
}
function getCampaignDetail(profileid,CampaignType){
	return request.get('/amazonadv/api/v1/advCampaignManager/getCampaignDetail/'+profileid+'/'+CampaignType);
}
export default{
	getCampaignList,getSumCampaign,getCampaignChart,getCampaignPlacement,getSBVideo,updateCampaignList,createCampaignList,
	getCampaigns,getCampaignsExt,getCampaignDetail,
}