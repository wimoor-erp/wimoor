 export default function myUtil() {
   function statusFunc(value){
   	if(value=="Unshipped"){
   		return "warning";
   	}
   	if(value=="Shipped"){
   		return "success";
   	}
   	if(value=="Cancelled"){
   		return "danger";
   	}
   	if(value=="Pending"){
   		return "info";
   	}
   	return "info";
   }
   function countryFunc(value){
   	if(value=="fr"){
   		return "法国";
   	}
   	if(value=="de"){
   		return "德国";
   	}
   	if(value=="uk"){
   		return "英国";
   	}
   	if(value=="it"){
   		return "意大利";
   	}
   	if(value=="es"){
   		return "西班牙";
   	}
   }
   function currencyFunc(currency){
   	if (currency == "USD" || currency == "AUD")
   			currency = "$";
   		if (currency == "GBP")
   			currency = "￡";
   		if (currency == "JPY")
   			currency = "￥";
   		if (currency == "INR")
   			currency = "R";
   		if (currency == "EUR")
   			currency = "€";
   		if (currency == "CAD")
   			currency = "C$";
   		if (currency == "--" || currency == null)
   			currency = "";
   		return currency;
   }
   function totalRowFunc(row){
   	var currencycode= currencyFunc(row.currency);
   	var totalprice=((row.quantity*row.itemprice)+(row.shipprice)-(row.itemdiscount+row.shipdiscount)).toFixed(2);
   	return currencycode+totalprice;
   }
   return {statusFunc,countryFunc,currencyFunc,totalRowFunc};
 }