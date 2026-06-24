import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function searchContentPublishRecords(data){
	 return request.get('/amazon/api/v1/product/aplus/searchContentPublishRecords',{params:data});
}

export default{
	searchContentPublishRecords
} 