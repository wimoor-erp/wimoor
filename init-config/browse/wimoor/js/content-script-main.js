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
var timezone = 8; // 时区 ： 东区时区1~12 西区时区-1~-12
var date;
date = getLocalTime(timezone).toString().split("GMT+")[0].toString();
$("#showdate").html(date);

function getLocalTime(i) {
	if (typeof i !== "number") {
	return new Date();
	}
var d = new Date();
var len = d.getTime();
var offset = d.getTimezoneOffset() * 60000;
var utcTime = len + offset;
return new Date(utcTime + 3600000 * i);
}
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}


          // 调用时间
var setdate = setInterval(() => {
             date = getLocalTime(timezone)
               .toString()
               .split("GMT+")[0]
               .toString();
             $("#showdate").html(new Date(date).Format("yyyy-MM-dd hh:mm:ss"));
 }, 1000);


$("#wimoor-css-eaeacs").remove();
setTimeout(function(){
	var site=document.getElementById("partner-switcher").innerText.split("|")[1];
	var marketplaceid=document.getElementById("partner-switcher").getAttribute("data-marketplace_selection");
	var sellerid=document.getElementById("partner-switcher").getAttribute("data-merchant_selection").replace("amzn1.merchant.o.","");
	var country="us";
	if(site.indexOf("Canada")>0||site.indexOf("加拿大")>0||marketplaceid=='A2EUQ1WTGCTBG2'){
		timezone=-5;
		site="加拿大";
		country="ca";
	}else if(site.indexOf("United States")>0||site.indexOf("美国")>0||marketplaceid=='ATVPDKIKX0DER'){
		timezone=-7;
		site="美国";
		country="us";
	}else if(site.indexOf("Mexico")>0||site.indexOf("墨西哥")>0||marketplaceid=='A1AM78C64UM0Y8'){
		timezone=-6;
		site="墨西哥";
		country="mx";
	}else if(site.indexOf("United Kingdom")>0||site.indexOf("英国")>0||marketplaceid=='A1F83G8C2ARO7P'){
		timezone=0;
		site="英国";
		country="us";
	}else if(site.indexOf("Japan")>0||site.indexOf("日本")>0||marketplaceid=='A1VC38T7YXB528'){
		timezone=9;
		site="日本";
		country="jp";
	}else if(site.indexOf("India")>0||site.indexOf("印度")>0||marketplaceid=='A21TJRUUN4KGV'){
		timezone=5;
		site="印度";
		country="in";
	}else if(site.indexOf("Germany")>0||site.indexOf("德国")>0||marketplaceid=='A1PA6795UKMFR9'){
		timezone=1;
		site="德国";
		country="de";
	}else if(site.indexOf("France")>0||site.indexOf("法国")>0||marketplaceid=='A13V1IB3VIYZZH'){
		timezone=1;
		site="法国";
		country="fr";
	}else if(site.indexOf("Australia")>0||site.indexOf("澳大利亚")>0||marketplaceid=='A39IBJ37TRP1C6'){
		timezone=10;
		site="澳大利亚";
		country="au";
	}else if(site.indexOf("Spain")>0||site.indexOf("西班牙")>0||marketplaceid=='A1RKKUPIHCS9HS'){
		timezone=1;
		site="西班牙";
		country="es";
	}else if(site.indexOf("Italy")>0||site.indexOf("意大利")>0||marketplaceid=='APJ6JRA9NG5V4'){
		timezone=1;
		site="意大利";
		country="it";
	} 
	$("#wimoorsite").text(site+"时间");
	var param={opttype:"getHoliday",marketplaceid:marketplaceid,sellerid:sellerid,country:country};
	chrome.runtime.sendMessage(param, function(response) { return true;});
},1000);

var datestr= "<span id='wimoorsite'></span><br><span id='showdate'></span>";
    datestr+= "<br><span style='float:right;cursor:pointer' id='holidlist'>假日列表 ☰</span>";
$("#KpiCardList").find(".css-1l6jcgf").append('<div class="css-eaeacs wimoor-eaescs" style="margin-top:0px;" id="wimoor-css-eaeacs">'+datestr+'</div>');
$("#holidlist").hover(function(){
	$("#holiday-wimoor").show();
},function(){
	
});
$("body").click(function(){
	if($("#holiday-wimoor:visible")){
		$("#holiday-wimoor").hide();
	}
});

function GetRequest() { 
	var url = location.search; //获取url中"?"符后的字串 
	return GetRequestUrl(url);
	} 

chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request) {
		    if(request.jsontext){
		    	var myJsonArray = eval(request.jsontext);
		    	if($("#holiday-wimoor").length>0){
		    		$("#holiday-wimoor").remove();
		    	}
		             var html="<div id='holiday-wimoor' style='display:none'><span class='wholidayclose'>×</span><div class='sub'>";
                                             var today=new Date(date).Format("yyyy年MM月dd日")
		             for(var i=0;i<myJsonArray.length;i++){
		        	var item=myJsonArray[i];
                                                if(item.date.substring(0,11)<today){
                                                   continue;   
                                                }
		        	html+="<div class='wholiday'><div class='wholidayhead'><span class='wholidaydate'>"+item.date+"</span><span class='wholidayname'>"+item.name+"</span></div><div class='wholidaydescrption'>"+item.description+"</div></div>";
		        }
		        html+="</div></div>";
		        $(".wimoor-eaescs").prepend(html);
		    }
        }
        return sendResponse({success:true});
    }
);

 
 
 
