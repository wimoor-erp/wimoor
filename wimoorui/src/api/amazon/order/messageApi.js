import request from "@/utils/request.js";
import downloadhandler from "@/utils/download-handler.js";
function getMessagingActionsForOrder(data){
	 return request.get('/amazon/api/v0/orders/message/getMessagingActionsForOrder',{params:data});
}

export default{
	getMessagingActionsForOrder,
 
}