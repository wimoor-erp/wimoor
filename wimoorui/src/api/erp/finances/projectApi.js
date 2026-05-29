 import request from "@/utils/request.js";
function getProject(){
	return request.get('/erp/api/v1/fin/project/getProject');
}
function saveProject(data){
 	return request.get('/erp/api/v1/fin/project/saveProject',{params:data});
 } 
 function updateProject(data){
  	return request.get('/erp/api/v1/fin/project/updateProject',{params:data});
  } 
 function delProject(data){
  	return request.get('/erp/api/v1/fin/project/delProject',{params:data});
  } 
  
 export default{
    getProject, saveProject,updateProject,delProject
 }