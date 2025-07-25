 
 
 

// 在用户点击其他地方时，关闭弹窗
window.onclick = function(event) {
	var modal = document.getElementById('myWimoorModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}



function GetRequestUrl(url){
	var theRequest = new Object(); 
	if (url.indexOf("#") != -1) { 
	var str = url.substr(1); 
	strs = str.split("&"); 
	for(var i = 0; i < strs.length; i ++) { 
	    theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
	  } 
	} 
	return theRequest; 
}

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
 

function getData(){
	var site="";
	if(document.getElementById("partner-switcher")){
		site=document.getElementById("partner-switcher").innerText.split("|")[1];
	}
	var marketplaceid="";
	if(document.getElementById("partner-switcher")){
		marketplaceid=document.getElementById("partner-switcher").getAttribute("data-marketplace_selection");	
	}
	var sellerid="";
	if(document.getElementById("partner-switcher")){
		sellerid=document.getElementById("partner-switcher").getAttribute("data-merchant_selection").replace("amzn1.merchant.o.","");	
	}
	var country="us";
 	var host="";
	var timezone=0;
	if(site.indexOf("Canada")>0||site.indexOf("加拿大")>0||marketplaceid=='A2EUQ1WTGCTBG2'){
		timezone=-5;
		site="加拿大";
		country="ca";
		host="https://sellercentral.amazon.ca";
	}else if(site.indexOf("United States")>0||site.indexOf("美国")>0||marketplaceid=='ATVPDKIKX0DER'){
		timezone=-7;
		site="美国";
		country="us";
		host="https://sellercentral.amazon.com";
	}else if(site.indexOf("Mexico")>0||site.indexOf("墨西哥")>0||marketplaceid=='A1AM78C64UM0Y8'){
		timezone=-6;
		site="墨西哥";
		country="mx";
		host="https://sellercentral.amazon.com.mx";
	}else if(site.indexOf("United Kingdom")>0||site.indexOf("英国")>0||marketplaceid=='A1F83G8C2ARO7P'){
		timezone=0;
		site="英国";
		country="uk";
		host="https://sellercentral.amazon.co.uk";
	}else if(site.indexOf("Japan")>0||site.indexOf("日本")>0||marketplaceid=='A1VC38T7YXB528'){
		timezone=9;
		site="日本";
		country="jp";
		host="https://sellercentral.amazon.co.jp";
	}else if(site.indexOf("India")>0||site.indexOf("印度")>0||marketplaceid=='A21TJRUUN4KGV'){
		timezone=5;
		site="印度";
		country="in";
		host="https://sellercentral.amazon.in";
	}else if(site.indexOf("Germany")>0||site.indexOf("德国")>0||marketplaceid=='A1PA6795UKMFR9'){
		timezone=1;
		site="德国";
		country="de";
		host="https://sellercentral.amazon.de";
	}else if(site.indexOf("France")>0||site.indexOf("法国")>0||marketplaceid=='A13V1IB3VIYZZH'){
		timezone=1;
		site="法国";
		country="fr";
		host="https://sellercentral.amazon.fr";
	}else if(site.indexOf("Australia")>0||site.indexOf("澳大利亚")>0||marketplaceid=='A39IBJ37TRP1C6'){
		timezone=10;
		site="澳大利亚";
		country="au";
		host="https://sellercentral.amazon.com.au";
	}else if(site.indexOf("Spain")>0||site.indexOf("西班牙")>0||marketplaceid=='A1RKKUPIHCS9HS'){
		timezone=1;
		site="西班牙";
		country="es";
		host="https://sellercentral.amazon.es";
	}else if(site.indexOf("Italy")>0||site.indexOf("意大利")>0||marketplaceid=='APJ6JRA9NG5V4'){
		timezone=1;
		site="意大利";
		country="it";
		host="https://sellercentral.amazon.it";
	} 
  return {timezone:timezone,site:site,country:country,host:host};
 
};

function GetRequest() { 
	var url = location.href; //获取url中"?"符后的字串 
	return GetRequestUrl(url);
	} 
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "D+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt)||/(Y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}
 function getDateFromFormat(dateString,formatString){
     var regFormat = /[YyMmDdHhSs]+/g;
     var dateMatches = dateString.split("/");
     if(dateMatches.length<2){
    	 dateMatches = dateString.split("-");
     }
     var formatmatches = formatString.match(regFormat);
     var date = new Date();
     for(var i=0;i<dateMatches.length;i++){
         switch(formatmatches[i].substring(0,1)){
             case 'Y':
             case 'y':
                 date.setFullYear(parseInt(dateMatches[i]));break;
             case 'M':
                 date.setMonth(parseInt(dateMatches[i])-1);break;
             case 'D':
             case 'd':
                 date.setDate(parseInt(dateMatches[i]));break;
             case 'H':
             case 'h':
                 date.setHours(parseInt(dateMatches[i]));break;
             case 'm':
                 date.setMinutes(parseInt(dateMatches[i]));break;
             case 's':
                 date.setSeconds(parseInt(dateMatches[i]));break;
         }
     }
     return date;
 } 
setTimeout(function(){
          if(location.href.indexOf("DetailSalesTrafficBySKU")){
        	  $("body").find("#wimoorupmodal").remove();
        	  $("body").append("<div class='css-1lafdix'   id='wimoorupmodal'> <kat-button variant='primary' size='base' type='button' label='上传Wimoor'></kat-button></div>");
        	  $("body").find('#myWimoorModal').remove();
        	  $("body").find("#wimoorupmodal").attr("style","top:"+(document.querySelector(".css-1lafdix").offsetTop)+"px;left:"+(document.querySelector(".css-1lafdix").offsetLeft+180)+"px");
        	  var html='<br><br>'
        		  html+="类型选择：<select id='wmftype'  ><option value='days'>按日</option><option value='week'>按周</option></select><br><br>"
        		  html+="日期选择：<select id='wmyear'  >";
        		var date=new Date();
        		var year=date.getFullYear();
        	   for(var i=0;i<4;i++){
        		   html+="<option value='"+(year-i)+"'>"+(year-i)+"</option> ";
        	   }
        	   html+="</select>年 &nbsp;&nbsp;&nbsp;<select id='wmmonth'>";
        	   for(var i=1;i<=12;i++){
        		   if(i<10){
        	  		   html+="<option value='"+i+"'>0"+i+"</option> ";
        		   }else{
        	  		   html+="<option value='"+i+"'>"+i+"</option> ";
        		   }
      
        	   }
        	   html+="</select>月&nbsp;&nbsp;&nbsp;<select id='wmday'>";
        	   for(var i=1;i<=31;i++){
        		   if(i<10){
        	  		   html+="<option value='"+i+"'>0"+i+"</option> ";
        		   }else{
        	  		   html+="<option value='"+i+"'>"+i+"</option> ";
        		   }
        	   }
        	   html+="</select>日&nbsp;&nbsp;&nbsp;<br>";
        	  $("body").append('<div id="myWimoorModal" style="display:none" class="modal"> <!-- 弹窗内容 --> <div class="modal-content"><span class="close">&times;</span><p>'+html+' <br/><button  id="wmfileupload">上传</button>&nbsp;&nbsp;&nbsp;&nbsp;<span id="wmspanmsg"></span></p></div></div>');
  		
			date=date.setDate(date.getDate()-2);
			date=new Date(date);
			var year=date.getFullYear();
			var month=date.getMonth()+1;
			var day=date.getDate();
			$("#wmyear").val(year);
			$("#wmmonth").val(month);
			$("#wmday").val(day);
	   	      
        	  $("#myWimoorModal").find(".close").click(function(){
        			var modal = document.getElementById('myWimoorModal');
        		  modal.style.display = "none";
        	  });
        	  $("#wimoorupmodal").click(function(){
      			var fromdate=document.querySelectorAll(".css-jfggi0")[1].getAttribute("value");
      			var gtx=document.querySelectorAll(".css-jfggi0")[1];
      			var placeholder=gtx.shadowRoot.querySelector("kat-input").getAttribute("placeholder");
      			
      		     var regFormat = /[YyMmDdHhSs]+/g;
      		     var dateMatches =fromdate.split("/");
      		     if(dateMatches.length<2){
      		    	dateMatches =fromdate.split("-");
      		     }
      		     var formatmatches = placeholder.match(regFormat);
      		     for(var i=0;i<dateMatches.length;i++){
      		         switch(formatmatches[i].substring(0,1)){
      		             case 'Y':
      		             case 'y':
      		              	$("#wmyear").val(parseInt(dateMatches[i]));break;
      		             case 'M':
      		            	$("#wmmonth").val(parseInt(dateMatches[i])); break;
      		             case 'd':
      		             case 'D':
      		            	$("#wmday").val(parseInt(dateMatches[i]));break;
      		         }
      		     }
    			    $("#wmspanmsg").text("");
        			$("#myWimoorModal").show();
        	  })
        
 
        	  
        	 document.getElementById("wmfileupload").onclick=function(){
        			        var gtx=document.querySelectorAll(".css-jfggi0")[1];
        			    	var param=getData();
        			    	param.opttype="updateFile";
        			    	param.type="uploadsessionbyextension";
        			    	param.host=location.host;
        			    	param.startDate=document.querySelectorAll(".css-jfggi0")[0].getAttribute("value");
        			    	param.endDate=document.querySelectorAll(".css-jfggi0")[1].getAttribute("value");
        			    	param.formatDate=gtx.shadowRoot.querySelector("kat-input").getAttribute("placeholder");
        			    	param.myStartDate=new Date(getDateFromFormat(param.startDate,param.formatDate)).Format("YYYY-MM-DD") ;
        			    	param.myEndDate=new Date(getDateFromFormat(param.endDate,param.formatDate)).Format("YYYY-MM-DD") ;
        			    	param.sellerid=$("#partner-switcher").attr("data-merchant_selection");
        			    	param.marketplaceid=$("#partner-switcher").attr("data-marketplace_selection");
        			    	param.sellerid=param.sellerid.replaceAll("amzn1.merchant.o.","");
        			    	param.date=$("#wmyear").val()+"-"+$("#wmmonth").val()+"-"+$("#wmday").val();
        			    	param.ftype=$("#wmftype").val();
        			    	if($("#wmspanmsg")){
        			    		$("#wmspanmsg").text("上传中...");
        			    	}
        			    	chrome.runtime.sendMessage(param, function(response) { 
        			    		if(response.status==1){
        			    			$("#wmspanmsg").text("上传成功");
        			    		}else if(response.status==0){
        			    			$("#wmspanmsg").text("上传中...");
        			    		}else if(response.status==404){
        			    			$("#wmspanmsg").text("上传失败");
        			    		}
        			    		return true;});
        		      }
          }
},3000);


 
chrome.runtime.onMessage.addListener(
	    function (request, sender, sendResponse) {
	        if (request) {
				if(request.type=="downloadPageView"){
					var data=getData();
					var date=new Date();
					date=date.setDate(date.getDate()-2);
					date=new Date(date);
					var year=date.getFullYear();
					var month=date.getMonth()+1;
					var day=date.getDate();
					var datestr="";
					if(month<10){
						datestr=datestr+"0"+month+"/";
					}else{
						datestr=datestr+month+"/";
					}
					if(day<10){
						datestr=datestr+"0"+day+"/";
					}else{
						datestr=datestr+day+"/";
					}
					datestr+=year;
					window.location.href="https://"+location.host+"/business-reports#/report?id=102%3ADetailSalesTrafficBySKU&chartCols=&columns=0%2F1%2F2%2F3%2F4%2F5%2F6%2F7%2F8%2F9%2F10%2F11%2F12%2F13%2F14%2F15%2F16&fromDate="+datestr+"&toDate="+datestr+"";
				   return sendResponse({message: '系统自动下载'});
				}
	        }
	        if(request.status==1){
	        	$("#wmspanmsg").text("上传成功");
	        }
	        if(request.status==404){
	        	$("#wmspanmsg").text("上传失败");
	        }
	        return sendResponse({message: '操作处理'});
	    }
	);
 
 

 
 
 
