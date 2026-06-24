

import request from "@/utils/request.js";

function loadProgress(key){
	 return request.get('/admin/api/v1/sysprogress',{params:{'key':key}});
}

export default{
	loadProgress
}