import request from "@/utils/request.js";

function getOperateLogList(data){
	return request.post('/amazonadv/api/v1/advOperateLogManager/getOperateLogList',data);
}

function updateOperateLogRemark(data){
	return request.get('/amazonadv/api/v1/advOperateLogManager/updateOperateLogRemark',{params:data});
}

export default{getOperateLogList,updateOperateLogRemark }
