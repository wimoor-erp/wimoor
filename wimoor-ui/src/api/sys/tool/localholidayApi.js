

import request from "@/utils/request.js";

function listHolid(data){
	 return request.post('/admin/api/v1/holiday/listHolid',data);
}
function generateYearData(data){
	 return request.post('/admin/api/v1/holiday/generateYearData',data);
}
function changeDateType(data){
	 return request.post('/admin/api/v1/holiday/changeDateType',data);
}


export default{
	listHolid,generateYearData,changeDateType
}