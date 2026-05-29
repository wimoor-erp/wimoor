import request from "@/utils/request.js";

function getPortfoliosById(data){
	return request.get('/amazonadv/api/v1/AdvertPortfolios/getPortfoliosById',{params:data});
}

function findPortfolios(data){
	return request.get('/amazonadv/api/v1/AdvertPortfolios/findPortfoliosForProfileId',{params:data});
}
 
function createPortfolios(data){
	return request.post('/amazonadv/api/v1/AdvertPortfolios/createPortfolios',data);
}

 
function updatePortfolios(data){
	return request.post('/amazonadv/api/v1/AdvertPortfolios/updatePortfolios',data);
}

export default{getPortfoliosById,findPortfolios,createPortfolios,updatePortfolios}
