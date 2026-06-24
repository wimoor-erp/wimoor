import request from "@/utils/request.js";
function list(data){
	 return request.get('/amazon/api/v1/amzInboundFbaCycle/list',{params:data});
}
function save(data){
	 return request.post('/amazon/api/v1/amzInboundFbaCycle/save',data);
}
 function updateStockByChange(data){
 	 return request.post("/amazon/api/v1/skucycle/updateStockByChange",data)
 }
 function updateStockCycleTranstype(data){
 	 return request.post("/amazon/api/v1/skucycle/updateStockCycleTranstype", data)
 }
 
export default{ list,save,updateStockByChange,updateStockCycleTranstype }
