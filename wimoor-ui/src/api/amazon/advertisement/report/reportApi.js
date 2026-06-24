import request from "@/utils/request.js";
function getsumproduct(data){
	 return request.post('/amazonadv/api/v1/advReport/getsumproduct',data);
}

export default{
	getsumproduct,
 
}