import request from "@/utils/request.js";
function getCreativePreviewHTML(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/creative/getCreativePreviewHTML/'+profileid+'/'+CampaignType,data);
}

function getListCreativeModerations(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/creative/getListCreativeModerations/'+profileid+'/'+CampaignType,data);
}

function getListOfCreatives(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/creative/getListOfCreatives/'+profileid+'/'+CampaignType,data);
}

function requestCreatives(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/creative/requestCreatives/'+profileid+'/'+CampaignType,data);
}

function updatesCreatives(profileid,CampaignType,data){
	return request.post('/amazonadv/api/v1/creative/updatesCreatives/'+profileid+'/'+CampaignType,data);
}


export default{
	getCreativePreviewHTML,getListCreativeModerations,getListOfCreatives,requestCreatives,updatesCreatives,
}