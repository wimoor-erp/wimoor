import request from "@/utils/request.js";
function getAmazonGroup(){
	 return request.get('/amazon/api/v1/amzauthority/getAmazonGroup');
}
function getAmazonGroupById(id){
	 return request.get('/amazon/api/v1/amzgroup/id/'+id);
}
function AmazonGroupSave(data){
	 return request.put('/amazon/api/v1/amzgroup/save',data);
}
function getAmazongroupList(){
	 return request.get('/amazon/api/v1/amzgroup/list');
}
function deleteAmazongroup(id){
	 return request.delete('/amazon/api/v1/amzgroup/delete/'+id);
}
function updateBatch(data){
	return request.post('/amazon/api/v1/amzgroup/updateBatch',data);
}
function selectTaskInfoList(data){
	return request.get('/amazon/api/v1/report/selectTaskInfoList',{params:data});
}

export default{
	AmazonGroupSave,getAmazongroupList,deleteAmazongroup,getAmazonGroup,updateBatch,getAmazonGroupById,selectTaskInfoList,	
}