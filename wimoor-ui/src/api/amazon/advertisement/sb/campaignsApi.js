import request from "@/utils/request.js";
function getCampaignList(data){
	 return request.post('/amazonadv/api/v1/sb/campaigns/getCampaignList',data);
}

export default{
	getCampaignList,
 
}
