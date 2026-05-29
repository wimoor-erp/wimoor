
import request from "@/utils/request.js";

function getAdGroupList(data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/getAdGroupList',data);
}
function getSumAdGroup(data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/getSumAdGroup',data);
}
function getAdGroupSuggestBid(data){
	return request.get('/amazonadv/api/v1/advAdgroupManager/getAdGroupSuggestBid',{params:data});
}
function getAdGroupsBid(profileid,campaignType,adgroupid){
	return request.get('/amazonadv/api/v1/advAdgroupManager/getAdGroupsBid/'+profileid+'/'+campaignType,{params:{'adgroupid':adgroupid}});
}
function getAdGroupChart(data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/getAdGroupChart',data);
}
function getAdgroupExtList(profileid,campaignType,data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/getAdgroupExtList/'+profileid+"/"+campaignType+"/ignoreRepeat",data);
}
function createAdGroups(profileid,campaignType,data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/createAdGroups/'+profileid+'/'+campaignType,data);
}
function getAdgroups(profileid,campaignType,data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/getAdgroups/'+profileid+'/'+campaignType,data);
}
function updateAdGroups(profileid,campaignType,data){
	return request.post('/amazonadv/api/v1/advAdgroupManager/updateAdGroups/'+profileid+'/'+campaignType,data);
}
function loadAdGroup(data){
	return request.get('/amazonadv/api/v1/advAdgroupManager/loadAdGroup',{params:data});
}
 
export default{
	getAdGroupList,getSumAdGroup,getAdGroupSuggestBid,getAdGroupChart,getAdgroupExtList,createAdGroups,getAdgroups,
	updateAdGroups,loadAdGroup,getAdGroupsBid,
}