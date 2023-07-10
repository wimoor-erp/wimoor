
/*function submitPhoto(phnumber){
document.getElementsByClassName("phone-input")[0].scrollIntoView()
document.getElementsByClassName("phone-input")[0].focus();
document.getElementsByClassName("phone-input")[0].value=phnumber;
var evt = document.createEvent('HTMLEvents')
evt.initEvent('input', true, true)
document.getElementsByClassName("phone-input")[0].dispatchEvent(evt)
document.getElementsByClassName("phone-btn")[0].click();
document.getElementsByClassName("confirm")[0].click();
}*/

function GetRequestUrl(url){
	var theRequest = new Object(); 
	if (url.indexOf("?") != -1) { 
	var str = url.substr(1); 
	strs = str.split("&"); 
	for(var i = 0; i < strs.length; i ++) { 
	    theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
	  } 
	} 
	return theRequest; 
}

function GetRequest() { 
	var url = location.search; //获取url中"?"符后的字串 
	return GetRequestUrl(url);
	} 
function  getReview(){
	setTimeout(function(){
		 var request=GetRequest();
		 var page=request["page"];
		 var sort=request["sort"];
		 var orderId=request["orderId"];
		 var trindex=request["trindex"];
		 var isauto=request["isauto"];
		 if(isauto!='true'){
			 return;
		 }
		 var servercontent="https://"+location.host;
		 if(!page){
			 page=1;
		 }
		 var isnotuseableorder=false;
		 if($("span[data-test-id='order-summary-deliverby-value']").text()=="Currently unavailable"){
			 isnotuseableorder=true;
		 }
		 if(orderId&&!isnotuseableorder){
			 if(location.href.indexOf("orders-v3/order/")>=0){
			     var marketplaceid=$("span[data-test-id='request-a-review-button'] a").attr("href").split("marketplaceId=")[1];
				 window.location.href=servercontent+"/messaging/reviews?orderId="+orderId+"&marketplaceId="+marketplaceid+"&page="+page+"&trindex="+trindex+"&isauto=true";
				 return;
			 }
			 if($(".ayb-reviews-button-container").length>0&&$(".ayb-reviews-button-container").find("kat-button[label='Yes']").length>0){
				 $(".ayb-reviews-button-container").find("kat-button[label='Yes']").click();
			 }
		 }else{
			 var typeurl= $("#myo-spa-tabs-container").find(".alternate").find("a").attr("href");
				if(typeurl&&typeurl.indexOf("fba")>-1){
				     $("#myo-spa-tabs-container").find(".alternate").find("a")[0].click();
				 }
				if(sort!="ship_by_asc"&&!orderId&&location.href.indexOf("orders-v3/order/")<0){
				 setTimeout(function(){
				  window.location.href=servercontent+"/orders-v3/fba/all?page=1&sort=ship_by_asc&date-range=last-30&isauto=true";
				 },100);
				     
				}
				if(!trindex){
				    trindex=0;
				}else{
					trindex=parseInt(trindex)+1;
				}
				var $tr =$("#orders-table").find("tr").eq(trindex);
				var order=$tr.find("td").eq(2).find("a").text();
				var orderstatus=$tr.find("td").eq(6).find(".order-status-column").find(".main-status").find("span").text();
				while((orderstatus!="Payment complete"&&orderstatus!="付款已完成")&&trindex<15){
					  trindex=trindex+1;
					  $tr =$("#orders-table").find("tr").eq(trindex);
					  order=$tr.find("td").eq(2).find("a").text();
					  orderstatus=$tr.find("td").eq(6).find(".order-status-column").find(".main-status").find("span").text();
				}

				if(trindex>=15){
					page=parseInt(page)+1;
					trindex=0;
				}

				if(orderstatus=="Payment complete"||orderstatus=="付款已完成"){
					if(order&&order.length>0){
						order=order.substring(0,19);
					}
					window.location.href=servercontent+"/orders-v3/order/"+order+"?orderId="+order+"&page="+page+"&trindex="+trindex+"&isauto=true";
				}
		 }
		 
		 setTimeout(function(){
		    window.location.href=servercontent+"/orders-v3/fba/all?page="+page+"&date-range=last-30&sort=ship_by_asc&trindex="+trindex+"&isauto=true";
		 },1000);
		 
   },10000);
	
}

chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request) {
			if(request.data){
					 window.location.href="https://"+location.host+"/orders-v3/fba/all?page=1&sort=status_asc&date-range=last-30&isauto=true";
			}
		  sendResponse({message:request.data+'开始自动获取'});
        }
    }
);
getReview();
 
 
 
