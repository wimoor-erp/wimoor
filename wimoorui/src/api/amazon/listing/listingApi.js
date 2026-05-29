import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function amzProductRefresh(){
	 return request.get('/amazon/api/v1/report/product/productInfo/createpojo');
}
function getProductInfoWithFnSKU(data){
	 return request.post('/amazon/api/v1/report/product/productInfo/getProductInfoWithFnSKU',data);
}
function searchCatalogProducts(data){
	 return request.post('/amazon/api/v1/report/product/amzProductRefresh/searchCatalogProducts',data);
}
export function pushAsin(data){
 	 return request.post("/amazon/api/v1/report/product/listing/pushAsin",data);
 }
export function saveAsin(data){
 	 return request.post("/amazon/api/v1/report/product/listing/saveAsin",data);
 }
export function deleteSku(data){
  	 return request.post("/amazon/api/v1/report/product/listing/deleteSku",data);
  }
export function getSku(data){
    	 return request.post("/amazon/api/v1/report/product/listing/sku",data);
    }
export function refreshInfoBySKU(data){
  	 return request.get("/amazon/api/v1/report/product/listing/refreshInfoBySKU",{params:data});
  }
export function initProductTask(data){
	return request.post("/amazon/api/v1/report/product/listing/initProductTask",data);
}
export function getPriceRecord(data){
  	 return request.get("/amazon/api/v1/product/priceRecord/getPriceRecord",{params:data});
  }
export function findAsin(data){
    	 return request.post("/amazon/api/v1/report/product/listing/findAsin",data);
    }
export function findAsinInfo(data){
    	 return request.post("/amazon/api/v1/report/product/listing/findAsinInfo",data);
    }
function recordFollowList(data){
    	 return request.post("/amazon/api/v1/report/product/listing/recordFollowList",data);
    }
function changePrice(data){
	 return request.post("/amazon/api/v1/report/product/listing/changePrice",data);
} 
function deleteListingsItem(data){
	 return request.post("/amazon/api/v1/report/product/listing/deleteListingsItem",data);
} 
function patchListingsItem(data){
	 return request.post("/amazon/api/v1/report/product/listing/patchListingsItem",data);
} 
function putListingsItem(data){
	 return request.post("/amazon/api/v1/report/product/listing/putListingsItem",data);
} 
function getListingsItem(data){
	 return request.post("/amazon/api/v1/report/product/listing/getListingsItem",data);
} 

function downExcelTemp(data){
	return request({url:"/amazon/api/v1/report/product/productInOpt/downLabelExcelTemp",
			responseType:"blob",
			params:data,
			method:'get'}).then(res => {
					downloadhandler.downloadSuccess(res,"labelTemplate.xlsx")
			}).catch(e=>{
					downloadhandler.downloadFail();
			}); 
}
function uploadExcel(FormData){
	return request({'method':'POST',
	                'url':"/amazon/api/v1/report/product/productInOpt/uploadLabelExcel",
				    'data':FormData,
					'headers':{'Content-Type':"multipart/form-data"},
				
	});
}
function getItemReviewTopics(data){
	 return request.get("/amazon/api/v1/report/product/review/getItemReviewTopics",{params:data});
} 
function getItemReviewTrends(data){
	 return request.get("/amazon/api/v1/report/product/review/getItemReviewTrends",{params:data});
} 
function getItemBrowseNode(data){
	 return request.get("/amazon/api/v1/report/product/review/getItemBrowseNode",{params:data});
} 
function getBrowseNodeReviewTopics(data){
	 return request.get("/amazon/api/v1/report/product/review/getBrowseNodeReviewTopics",{params:data});
} 
function getBrowseNodeReviewTrends(data){
	 return request.get("/amazon/api/v1/report/product/review/getBrowseNodeReviewTrends",{params:data});
} 
function getBrowseNodeReturnTopics(data){
	 return request.get("/amazon/api/v1/report/product/review/getBrowseNodeReturnTopics",{params:data});
} 
function getBrowseNodeReturnTrends(data){
	 return request.get("/amazon/api/v1/report/product/review/getBrowseNodeReturnTrends",{params:data});
} 
 
export default{
	amzProductRefresh,getProductInfoWithFnSKU,
	searchCatalogProducts,pushAsin,findAsinInfo,
	getPriceRecord,saveAsin,deleteSku,refreshInfoBySKU,
	getSku,recordFollowList,changePrice,
	downExcelTemp,uploadExcel,deleteListingsItem,
	patchListingsItem,putListingsItem,getListingsItem,
	getItemReviewTopics,getItemReviewTrends,
	getBrowseNodeReviewTopics,getBrowseNodeReviewTrends,getBrowseNodeReturnTopics,getBrowseNodeReturnTrends,
	getItemBrowseNode,initProductTask
} 

  
