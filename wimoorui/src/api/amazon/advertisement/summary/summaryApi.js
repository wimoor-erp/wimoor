import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getsumproduct(data){
	return request.post('/amazonadv/api/v1/advReport/getsumproduct',data );
}

function getmonthsum(data){
 	return request.post('/amazonadv/api/v1/advReport/getmonthsum',data );
}
function getenablesumtype(data){
 	return request.get('/amazonadv/api/v1/advManager/getenablesumtype' );
}
 function getsumproductAll(data){
  	return request.post('/amazonadv/api/v1/advManager/getsumproduct',data );
 }
 function getTop5(data){
  	return request.get('/amazonadv/api/v1/advManager/getTop5',{params:data});
 }
 function getKeywordsWarningIndicator(data){
  	return request.get('/amazonadv/api/v1/advManager/getKeywordsWarningIndicator',{params:data});
 }
 function getProductWarningIndicator(data){
  	return request.get('/amazonadv/api/v1/advManager/getProductWarningIndicator',{params:data});
 }
 function getKeywordsWarningIndicatorDetail(ftype,downtype,data){
  	return request.post('/amazonadv/api/v1/advManager/getKeywordsWarningIndicatorDetail/'+ftype+"/"+downtype,data);
 }
 function getProductWarningIndicatorDetail(ftype,downtype,data){
  	return request.post('/amazonadv/api/v1/advManager/getProductWarningIndicatorDetail/'+ftype+"/"+downtype,data);
 }
 function getAdvWrningData(data){
  	return request.get('/amazonadv/api/v1/advManager/getAdvWrningDate',{params:data});
 }
 function saveAdvWrningData(data){
  	return request.post('/amazonadv/api/v1/advManager/saveAdvWrningDate',data);
 }
 function downExcelDate(data){
	 return request({url:"/amazonadv/api/v1/advReport/downExcelDate",
	 			                    responseType:"blob",
	 								data:data,
	 								method:'post'}).then(res => {
	 										downloadhandler.downloadSuccess(res,"advReport.xlsx")
	 								}).catch(e=>{
	 										downloadhandler.downloadFail(e);
	 								}); 
  	 
 }
 function getInvoicesSummary(data){
	 return request.post('/amazonadv/api/v1/ads/invoices/getInvoicesSummary',data);
 }
 
export default{
	getsumproduct,getmonthsum,getenablesumtype,getsumproductAll,getTop5,
	getKeywordsWarningIndicator,getProductWarningIndicator,
	getKeywordsWarningIndicatorDetail,getProductWarningIndicatorDetail,
	getAdvWrningData,saveAdvWrningData,downExcelDate,getInvoicesSummary
}

