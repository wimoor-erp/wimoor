import request from "@/utils/request.js";

function shareFee(data){
	return request.post('/erp/api/v1/purchase_form_ext/share', data);
}
function autoShipPay(ftype,acct,data){
	return request.post('/erp/api/v1/purchase_form_ext/autoShipPay/'+ftype+'/'+acct, data);
}


export default{
	 shareFee,autoShipPay,
	 
}