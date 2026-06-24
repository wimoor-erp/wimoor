import request from "@/utils/request.js";

function saveRate(data){
	 return request.post('/amazon/api/v1/customsSet/saveRate',data);
}

function getRate(){
	 return request.post('/amazon/api/v1/customsSet/getRate');
}


export default{
	 saveRate,getRate,
}
