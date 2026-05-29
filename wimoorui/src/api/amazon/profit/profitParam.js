import request from "@/utils/request.js";

function getFBA(data){
	return request.post('/amazon/api/v1/profit/profitParam/getFBA',data );
}
 
function updateFBA(data){
  	return request.post('/amazon/api/v1/profit/profitParam/updateFBA',data);
  }
  
function saveFBA(data){
    	return request.post('/amazon/api/v1/profit/profitParam/saveFBA',data);
}
function deleteFBA(data){
    	return request.post('/amazon/api/v1/profit/profitParam/deleteFBA',data);
}
  
function getTier(data){
	return request.get('/amazon/api/v1/profit/profitParam/getTier',{params:data });
}
  
function getTierFormat(data){
	return request.post('/amazon/api/v1/profit/profitParam/getTierFormat',data );
}
 
function saveTierFormat(data){
  	return request.post('/amazon/api/v1/profit/profitParam/saveTierFormat',data);
  }
  
function updateTierFormat(data){
    	return request.post('/amazon/api/v1/profit/profitParam/updateTierFormat',data);
}
function deleteTierFormat(data){
    	return request.post('/amazon/api/v1/profit/profitParam/deleteTierFormat',data);
}
 
 function getTierPage(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getTierPage',data );
 }
  
 function saveTier(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveTier',data);
   }
   
 function updateTier(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateTier',data);
 }
 function deleteTier(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteTier',data);
 }
 
 function getWeightFormat(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getWeightFormat',data );
 }
 function saveWeightFormat(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveWeightFormat',data);
   }
   
 function updateWeightFormat(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateWeightFormat',data);
 }
 function deleteWeightFormat(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteWeightFormat',data);
 }	
  
 function getReferralFee(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getReferralFee',data );
 }
 function saveReferralFee(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveReferralFee',data);
   }
   
 function updateReferralFee(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateReferralFee',data);
 }
 function deleteReferralFee(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteReferralFee',data);
 }
 
 
 function getStorageFee(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getStorageFee',data );
 }
 function saveStorageFee(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveStorageFee',data);
   }
   
 function updateStorageFee(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateStorageFee',data);
 }
 function deleteStorageFee(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteStorageFee',data);
 }
 
 
 function getInplaceList(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getInplaceList',data );
 }
 function getInplace(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getInplace',data );
 }
 function saveInplace(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveInplace',data);
   }
   
 function updateInplace(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateInplace',data);
 }
 function deleteInplace(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteInplace',data);
 }
 
 function getInplaceFeeFormat(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getInplaceFeeFormat',data );
 }
 function saveInplaceFeeFormat(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveInplaceFeeFormat',data);
   }
   
 function updateInplaceFeeFormat(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateInplaceFeeFormat',data);
 }
 function deleteInplaceFeeFormat(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteInplaceFeeFormat',data);
 }
  
 function getFBASipp(data){
 	return request.post('/amazon/api/v1/profit/profitParam/getFBASipp',data );
 }
 
 function saveFBASipp(data){
   	return request.post('/amazon/api/v1/profit/profitParam/saveFBASipp',data);
}

 function updateFBASipp(data){
     	return request.post('/amazon/api/v1/profit/profitParam/updateFBASipp',data);
 }
 function deleteFBASipp(data){
     	return request.post('/amazon/api/v1/profit/profitParam/deleteFBASipp',data);
 }
	  
export default{
	 getFBA,updateFBA,getTier,saveFBA,deleteFBA,
	 getTierFormat,saveTierFormat,updateTierFormat,deleteTierFormat,
	 getTierPage,saveTier,updateTier,deleteTier,
	 getWeightFormat,saveWeightFormat,updateWeightFormat,deleteWeightFormat,
	 getReferralFee,saveReferralFee,updateReferralFee,deleteReferralFee,
	 getStorageFee,saveStorageFee,updateStorageFee,deleteStorageFee,
	 getInplace,saveInplace,updateInplace,deleteInplace,
	 getInplaceFeeFormat,saveInplaceFeeFormat,updateInplaceFeeFormat,deleteInplaceFeeFormat,
	 getFBASipp,saveFBASipp,updateFBASipp,deleteFBASipp,getInplaceList,
}