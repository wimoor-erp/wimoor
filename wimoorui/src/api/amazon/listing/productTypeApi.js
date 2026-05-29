import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function searchDefinitionsProductTypes(data){
	 return request.post('/amazon/api/v1/product/amzProductType/searchDefinitionsProductTypes',data);
}
function getProductDefinition(data){
	 return request.post('/amazon/api/v1/product/amzProductType/getProductDefinition',data);
}
function getSchemaJson(url){
	return request.get('/amazon/api/v1/product/amzProductType/getSchema',{params:{"url":url}});
}

export default{
	searchDefinitionsProductTypes,getProductDefinition,getSchemaJson
} 