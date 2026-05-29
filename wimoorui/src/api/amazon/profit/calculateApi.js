import request from "@/utils/request.js";

function showProfitDetial(data){
	return request.get('/amazon/api/v1/profit/detail/showProfitDetial',{params:data});
}
 function showProfitPage(data){
 	return request.get('/amazon/api/v1/profit/profitParam/showProfitPage',{params:data});
 }
 function showCost(data){
 	return request.post('/amazon/api/v1/profit/calculateCost/showCost',data);
 }
 function inplacefee(data){
 	return request.post('/amazon/api/v1/profit/calculateCost/ignoreRepeat/inplacefee',data);
 }
 function showProfit(data){
 	return request.post('/amazon/api/v1/profit/detail/showProfit',data);
 }
export default{
	showProfitDetial,showProfitPage,showCost,showProfit,inplacefee
}