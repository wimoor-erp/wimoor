 import request from "@/utils/request.js";
 
 
 function save(token,data){
 	return request.post('/quote/api/v1/transchannel/save/'+token,data);
 }
 function tsave(token,data){
 	return request.post('/quote/api/v1/transchannel/tsave/'+token,data);
 }
 function update(token,data){
 	return request.post('/quote/api/v1/transchannel/update/'+token,data);
 }
 function tupdate(token,data){
 	return request.post('/quote/api/v1/transchannel/tupdate/'+token,data);
 }
 function list(token,data){
 	return request.post('/quote/api/v1/transchannel/list/'+token,data);
 }
 function listall(token,data){
 	return request.post('/quote/api/v1/transchannel/listall/'+token,data);
 }
 
 function tlist(token,data){
 	return request.post('/quote/api/v1/transchannel/tlist/'+token,data);
 }
 function supplierTransList(token,data){
	return request.post('/quote/api/v1/quote/supplier/submitPrice/'+token+"/supplierTransList",data);
 }
 function supplierTransSave(token,data){
	return request.post('/quote/api/v1/quote/supplier/submitPrice/'+token+"/supplierTransSave",data);
 }
  function uploadFile(token,FormData){
  	return request({'method':'POST',
  	                 'url':"/quote/api/v1/transchannel/upload/"+token,
  				     'data':FormData,
  					 'headers':{'Content-Type':"multipart/form-data"},
  	               });
  }
  export default{
  	save,tsave,update,tupdate,list,listall,tlist,supplierTransList,supplierTransSave,uploadFile
  }
 