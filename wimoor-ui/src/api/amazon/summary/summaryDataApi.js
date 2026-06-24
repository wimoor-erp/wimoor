import request from "@/utils/request.js";
function getSummaryData(){
	 return request.get('/amazon/api/v1/summary/list');
}
function queryChartSales(data){
	 return request.post('/amazon/api/v1/summary/queryChartSales',data);
}
function queryChartSalesSummary(data){
	 return request.post('/amazon/api/v1/summary/queryChartSalesSummary',data);
}
function queryChartReturn(data){
	 return request.post('/amazon/api/v1/summary/queryChartReturn',data);
}
function queryChartReturnSummary(data){
	 return request.post('/amazon/api/v1/summary/queryChartReturnSummary',data);
}
function queryChartMarket(data){
	 return request.post('/amazon/api/v1/summary/queryChartMarket',data);
}
function querySales(data){
	 return request.post('/amazon/api/v1/summary/querySales',data);
}
function top5(data){
	 return request.post('/amazon/api/v1/summary/top5',data);
}
function sumPerformance(data){
	 return request.post('/amazon/api/v1/summary/sumPerformance',data);
}

export default{
	getSummaryData,
	queryChartSales,
	queryChartSalesSummary,
	queryChartReturn,
	queryChartReturnSummary,
	queryChartMarket,
	querySales,
	top5,
	sumPerformance,
}