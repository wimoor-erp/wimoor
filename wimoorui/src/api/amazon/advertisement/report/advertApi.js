import request from "@/utils/request.js";

function loadProfile(data){
	return request.get('/amazonadv/api/v1/advert/loadProfile',{params:data});
}
function addSerchHistory(data){
	return request.get('/amazonadv/api/v1/searchHistory/addSerchHistory',{params:data});
}
function getSerchHistory(data){
	return request.get('/amazonadv/api/v1/searchHistory/getSerchHistory',{params:data});
}
function deleteSerchHistory(data){
	return request.get('/amazonadv/api/v1/searchHistory/deleteSerchHistory',{params:data});
}
function loadCampaignsNotArchived(data){
	return request.post('/amazonadv/api/v1/advert/loadCampaignsNotArchived',data);
}
function findPortfoliosForProfileId(data){
	return request.get('/amazonadv/api/v1/AdvertPortfolios/findPortfoliosForProfileId',{params:data});
}
function getallsumtype(data){
	return request.post('/amazonadv/api/v1/advManager/getallsumtype',data);
}
function saleorder(data){
	return request.get('/amazonadv/api/v1/advManager/saleorder',{params:data});
}
function cpcdata(data){
	return request.get('/amazonadv/api/v1/advManager/cpcdata',{params:data});
}




export default{
	loadProfile,addSerchHistory,getSerchHistory,deleteSerchHistory,loadCampaignsNotArchived,
	findPortfoliosForProfileId,getallsumtype,saleorder,cpcdata,
}


