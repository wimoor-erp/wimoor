import request from "@/utils/request.js";
function unbindAccount(data){
	 return request.get('/admin/api/v1/users/unbindAccount',{params:data});
}
function userinfo(data){
	 return request.get('​/admin​/api​/v1​/users​/info',{params:data});
}
function limitData(type){
	 return request({ url:'/admin/api/v1/users/limitData?type='+type,method: 'get'});
}
function getInfo() {
  return request({ url: '/admin/api/v1/users/info',method: 'get'})
}
function updatePassword(data) {
  return request.post('/admin/api/v1/users/updatePassword' ,data)
}
function getSmsCode(data) {
   return request.get( '/admin/api/v1/sms/getSmsCode' ,{params:data});
}
function checkSmsCode(data) {
   return request.get( '/admin/api/v1/sms/checkSmsCode' ,{params:data});
}
function findOwnerAll(){
	 return request.get('/admin/api/v1/users/getEnable');
}
function detail(){
	 return request.get('/admin/api/v1/users/detail');
}
function saveImage(data) {
  return request.post('/admin/api/v1/users/saveImage' ,data)
}
function updateSelf(data) {
  return request.post('/admin/api/v1/users/updateSelf',data)
}
function verifyPassword(data){
  return request.post('/admin/api/v1/users/verifyPassword',data)
}
function verifySmsCode(data){
	 return request.get( '/admin/api/v1/auth/verifySmsCode' ,{params:data});
}
function getSmsCodes(data){
	 return request.get('/admin/api/v1/users/getSmsCode' ,{params:data});
}
function getEmailCode(data){
	 return request.get('/admin/api/v1/users/getEmailCode' ,{params:data});
}
function updateAccountSelf(data){
  return request.post('/admin/api/v1/users/updateAccountSelf',data)
}
function updateEmailSelf(data){
  return request.post('/admin/api/v1/users/updateEmailSelf',data)
}

function mergeAccount(data){
  return request.post('/admin/api/v1/users/mergeAccount',data)
}
function updatePasswordSelf(data){
	 return request.post('/admin/api/v1/users/updatePasswordSelf',data)
}
function findbindlist(){
	 return request.get('/admin/api/v1/users/findbindlist')
}
function openidbind(data){
	 return request.get('/admin/api/v1/users/sysrole/openidbind',{params:data})
}
function getbindId(data){
	 return request.get('/admin/api/v1/users/getbindId',{params:data});
}
function changeLoginWechatApp(data){
	  data.jsessionid= localStorage.getItem("jsessionid");  
	 return request.get('/admin/api/v1/auth/changeLoginWechatApp',{params:data})
}
function register(data){
  return request.post('/admin/api/v1/users/register',data)
}
function updatePasswordForget(data){
  return request.post('/admin/api/v1/users/updatePasswordForget',data)
}
const unbindWechat=(data)=>{
	return request.get('/admin/api/v1/users/unbindWechat',{params:data});
}
function showInvitePage() {
   return request.get('/admin/api/v1/users/showInvitePage');
}
export default{
	unbindAccount,userinfo,findOwnerAll,limitData,detail,unbindWechat,
	getInfo,updatePassword,getSmsCode,checkSmsCode,saveImage,updateSelf,verifyPassword,
	verifySmsCode,getSmsCodes,getEmailCode,updateEmailSelf,updateAccountSelf,mergeAccount,updatePasswordSelf,
	findbindlist,openidbind,changeLoginWechatApp,getbindId,register,updatePasswordForget,showInvitePage
}