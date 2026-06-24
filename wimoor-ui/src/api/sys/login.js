import request from "@/utils/request";
const loginWechat=(data)=>{
	return request.get('/admin/api/v1/auth/loginWechat',{"params":data});
}
const getOpenUserlist=(data)=>{
	return request.get('/admin/api/v1/auth/getOpenUserlist',{"params":data});
}
const unbindWechat=(data)=>{
	return request.get('/admin/api/v1/users/unbindWechat',{"params":data});
}
const verifyWechatApp=(data)=>{
	return request.get('/admin/api/v1/auth/verifyWechatApp',{"params":data});
}
const changeLoginWechatApp=(data)=>{
	return request.get('/admin/api/v1/auth/changeLoginWechatApp',{"params":data});
}
const verifyAppLogin=(data)=>{
	return request.post('/admin/api/v1/auth/verifyAppLogin',{"data":data});
}

function findbindlist(){
	 return request.get('/admin/api/v1/users/findbindlist')
}
 
export default {loginWechat,getOpenUserlist,unbindWechat,findbindlist,
verifyWechatApp,changeLoginWechatApp,verifyAppLogin};
