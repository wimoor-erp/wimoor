import request from "@/utils/request.js";

 function getpage(data){
 	return request.post('/amazon/api/v1/amzShipFulfillmentCenter/getpage',data );
 }
 
 function save(data){
   	return request.post('/amazon/api/v1/amzShipFulfillmentCenter/save',data);
}

 function update(data){
     	return request.post('/amazon/api/v1/amzShipFulfillmentCenter/update',data);
 }
 function remove(data){
     	return request.post('/amazon/api/v1/amzShipFulfillmentCenter/delete',data);
 }
	  
export default{
	 getpage,save,update,remove,
}