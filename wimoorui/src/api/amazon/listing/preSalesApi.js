import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
export function getSales(data){
	 return request.get('/amazon/api/v1/report/product/presale/getSales',{params:data});
}
export function save(data){
 	 return request.post('/amazon/api/v1/report/product/presale/save',data);
 }
export function getProductMonthSales(data){
  	 return request.post('/amazon/api/v1/report/product/presale/getProductMonthSales',data);
  }
 
export function clear(data){
  	 return request.post('/amazon/api/v1/report/product/presale/clear',data);
}
export function getProductPreSalesByMonth(data){
	return request.post('/amazon/api/v1/report/product/presale/getProductPreSalesByMonth',data);
}
export function getProductPreSales(data){
	return request.post('/amazon/api/v1/report/product/presale/getProductPreSales',data);
}

export function downExcelTemp(data,callback){
	return request({url:"/amazon/api/v1/report/product/presale/downExcelTemp",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"customSalesTemplate.xlsx");
						if(callback){
							callback(res);
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
						if(callback){
							callback(e);
						}
				});
}
export function downExcelSales(data,callback){
	return request({url:"/amazon/api/v1/report/product/presale/downExcelSales",
				                    responseType:"blob",
									params:data,
									method:'get'}).then(res => {
						downloadhandler.downloadSuccess(res,"customSales.xlsx");
						if(callback){
							callback(res);
						}
				}).catch(e=>{
					    downloadhandler.downloadFail(e);
						if(callback){
							callback(e);
						}
				});
}
export function uploadSalesExcel(FormData){
	return request({'method':'POST',
	                 'url':"/amazon/api/v1/report/product/presale/uploadSalesExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
	
}