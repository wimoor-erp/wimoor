import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
 
function getInvoice(data){
	return request.get('/erp/api/v1/order/invoice/getInvoice',{params:data});
}
function saveInvoice(FormData){
	return request({'method':'POST',
	                'url':"/erp/api/v1/order/invoice/saveInvoice",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
 function downInvoice(data){
 	return request({url:"/erp/api/v1/order/invoice/downInvoice",
					responseType:"blob",
					data:data,
					method:'post'}).then(res => {
							downloadhandler.downloadSuccess(res,"invoice.xlsx")
					}).catch(e=>{
							downloadhandler.downloadFail();
					}); 
 }
 
export default{
	  getInvoice,saveInvoice,downInvoice
}