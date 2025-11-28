
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
function fomatFloat(src,pos){   //四舍五入,保留2位小数    
	if(pos==null){
		pos=2;
	}
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);       
}
function GetRequest() { 
	var url = location.search; //获取url中"?"符后的字串 
	return GetRequestUrl(url);
	} 

if($("#wmprofit").length>0){
	$("#wmprofit").remove();
}
var host=location.host;
var marketplaceid="";
var country="";
if(host.indexOf("amazon.com")>=0){
	country="us";
	marketplaceid="ATVPDKIKX0DER";
}else if(host.indexOf("amazon.it")>=0){
	country="it";
	marketplaceid="APJ6JRA9NG5V4";
}else if(host.indexOf("amazon.co.jp")>=0){
	country="jp";
	marketplaceid="A1VC38T7YXB528";
}else if(host.indexOf("amazon.co.uk")>=0){
	country="uk";
	marketplaceid="A1F83G8C2ARO7P";
}else if(host.indexOf("amazon.de")>=0){
	country="de";
	marketplaceid="A1PA6795UKMFR9";
}else if(host.indexOf("amazon.fr")>=0){
	country="fr";
	marketplaceid="A13V1IB3VIYZZH";
}else if(host.indexOf("amazon.es")>=0){
	country="es";
	marketplaceid="A1RKKUPIHCS9HS";
}else if(host.indexOf("amazon.ca")>=0){
	country="ca";
	marketplaceid="A2EUQ1WTGCTBG2";
}else if(host.indexOf("amazon.com.mx")>=0){
	country="mx";
	marketplaceid="A1AM78C64UM0Y8";
}else if(host.indexOf("amazon.pl")>=0){
	country="pl";
	marketplaceid="A1C3SOZRARQ6R3";
}else if(host.indexOf("amazon.in")>=0){
	country="in";
	marketplaceid="A21TJRUUN4KGV";
}
var asin=$("#ASIN").val();
var price="";
if($("#price_inside_buybox").length>0){
	price=$("#price_inside_buybox").text().replaceAll("\n","");
	price=price.replaceAll("JP","");
	price=price.replaceAll("￥","");
}else if($("#priceblock_ourprice").length>0){
	price=$("#priceblock_ourprice").text().replaceAll("\n","");
	price=price.replaceAll("₹","");
	price=price.replaceAll("&nbsp;","");
	price=price.replaceAll(" ","");
	price=price.replaceAll("JP","");
	price=$.trim(price);
}else if(("#priceblock_dealprice").length>0){
	price=$("#priceblock_dealprice").text().replaceAll("\n","");
	price=price.replaceAll("₹","");
	price=price.replaceAll("&nbsp;","");
	price=price.replaceAll(" ","");
	price=price.replaceAll("JP","");
	price=$.trim(price);
}else if($("#priceblock_ourprice").length>0){
	price=$("#priceblock_ourprice").text().replaceAll("\n","");
	price=price.replaceAll("₹","");
	price=price.replaceAll("&nbsp;","");
	price=price.replaceAll(" ","");
	price=price.replaceAll("JP","");
	price=$.trim(price);
}else if($("#newBuyBoxPrice").lenth>0){
	price=$("#newBuyBoxPrice").text().replaceAll("\n","");
	price=price.replaceAll("₹","");
	price=price.replaceAll("&nbsp;","");
	price=price.replaceAll(" ","");
	price=price.replaceAll("JP","");
	price=$.trim(price);
}
 price=price.replaceAll(",","");
 if(price==""){
	 price= $(".a-price").find("span").eq(0).text();
	    price=price.replaceAll("₹","");
	    price=price.replaceAll("£","");
	    price=price.replaceAll("$","");
	    price=price.replaceAll("€","");
	    price=price.replaceAll("￥","");
	 	price=price.replaceAll("&nbsp;","");
		price=price.replaceAll(" ","");
		price=price.replaceAll("JP","");
		price=$.trim(price);
 }
var param={opttype:"getProfitConfig",marketplaceid:marketplaceid,
		"profittype":"single",country:country,asin:asin,price:price};
param.ftype=$("#wayfinding-breadcrumbs_feature_div ul").find("li:eq(0) a").text().replaceAll("\n","").trim();
param.shipment=0;
param.cost  =0;
param.costcurrency="RMB";
param.shipmentcurrency="RMB";
param.wmsalesmonth=0;
param.wmadvcostmonth=0;
param.wmadvcpcmonth=0;
param.wmadvcrmonth=0;
param.wmadvcprmonth=0;
chrome.runtime.sendMessage(param, function(response) { return true;});
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request) {
			if(request.result){
				var costDetail=JSON.parse(request.result);
				if(costDetail.profitCfgId){
					costDetail.costDetail.profitCfgId=costDetail.profitCfgId;
					costDetail.costDetail.profitCfgList=costDetail.profitCfgList;
					costDetail.costDetail.inputDimensions=costDetail.inputDimensions;
					costDetail.costDetail.inputDimensions.length.value=fomatFloat(costDetail.costDetail.inputDimensions.length.value);
					costDetail.costDetail.inputDimensions.width.value =fomatFloat(costDetail.costDetail.inputDimensions.width.value);
					costDetail.costDetail.inputDimensions.height.value=fomatFloat(costDetail.costDetail.inputDimensions.height.value);
					costDetail.costDetail.inputDimensions.weight.value=fomatFloat(costDetail.costDetail.inputDimensions.weight.value);
				}
				var template="";
				if($("#wmprofitpriceInsideBuyBox").length>0&&$("#wmprofitpriceInsideBuyBox").is(":visible")){
					template=getTemplate("font-size:14px!important;");
				}else{
					template=getTemplate("font-size:14px!important;display:none");
				}
		     	if($("#wmprofit").length>0){
		     		$("#wmprofit").remove();
		     	}
		     	if($("#wmprofitpriceInsideBuyBox").length>0){
		     		$("#wmprofitpriceInsideBuyBox").remove();
		     	}
			     var html = juicer(template,costDetail.costDetail);
			        if($("#productTitle").length>0){
			        	$("#productTitle").append("<div id='wmprofit' style='font-size:12px!important;' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
			        }
			        else if($("#exports_desktop_qualifiedBuybox_priceInsideBuyBox").length>0){
			     		$("#exports_desktop_qualifiedBuybox_priceInsideBuyBox").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
			     	}else if($("#snsBuyBoxAccordion").length>0){
						 $("#snsBuyBoxAccordion").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
					}else if($("#priceInsideBuyBox_feature_div").length>0){
			     		$("#priceInsideBuyBox_feature_div").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
			     	}else if($("#priceblock_ourprice_row").length>0){
			     		$("#priceblock_ourprice_row").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
			     	}else if($("#exports_desktop_undeliverable_buybox_priceInsideBuybox_feature_div").length>0){
				   	    $("#exports_desktop_undeliverable_buybox_priceInsideBuybox_feature_div").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
					}else if($("#tell-a-friend").length){
			     		 $("#tell-a-friend").append("<div id='wmprofit' >利润："+costDetail.costDetail.currency+costDetail.costDetail.profit+"<i id='wmprofitdetail'  style='float:right;cursor:pointer'>▼</i></div>"+html);
			     	}
			     	
				   if($("#wmprofit").length){
					   $("#wmprofit").click(function(){
						   if($("#wmprofitpriceInsideBuyBox").is(":visible")){
							   $("#wmprofitpriceInsideBuyBox").hide();
						   }else{
							   $("#wmprofitpriceInsideBuyBox").show();
						   }
					   });
					   var num = 1;
				       $("#wmprofitpriceInsideBuyBox li").each(function(){
								 $(this).find(".fee-num").text(num);
								 num=num+1;
					    })
						$("#wmrecalculate").click(function(event){
							event.preventDefault();
							param.cost =$("#wmcost").val();
							param.profitCfg=$("#profitCfg").val();
							param.shipment=$("#wmshipment").val();
							param.costcurrency=$("#wmcostcurrency").val();
							param.shipmentcurrency=$("#wmshipmentcurrency").val();
							param.price=$("#sellingprice").val();
							if(param.shipment=="0")param.shipment=="";
							var wmsalesmonth=0;
					     	if($("#wmsalesmonth").val()){
					     		wmsalesmonth=$("#wmsalesmonth").val();
					     	}
					     	param.wmsalesmonth=wmsalesmonth;
					     	var wmadvordermonth=0;
					     	if($("#wmadvordermonth").val()){
					     		wmadvordermonth=$("#wmadvordermonth").val();
					     	}
					    	param.wmadvordermonth=wmadvordermonth;
					     	var wmadvcpcmonth=0;
					     	if($("#wmadvcpcmonth").val()){
					     		wmadvcpcmonth=$("#wmadvcpcmonth").val();
					     		}
					    	param.wmadvcpcmonth=wmadvcpcmonth;
					     	var wmadvcrmonth=0;
					     	if($("#wmadvcrmonth").val()){
					     		wmadvcrmonth=$("#wmadvcrmonth").val();
					     		}
					    	param.wmadvcrmonth=wmadvcrmonth;
					     	var wmadvcprmonth=0;
					     	if($("#wmadvcprmonth").val()){
					     		wmadvcprmonth=$("#wmadvcprmonth").val();
					     	}
					    	param.wmadvcprmonth=wmadvcprmonth;
							chrome.runtime.sendMessage(param, function(response) { return true;});
						});
				    	var cost=0;
				    	if(costDetail&&costDetail.costDetail&&costDetail.costDetail.purchase){
				    		cost=costDetail.costDetail.purchase;
				    	}
				    	var shipment=0;
				     	if(costDetail&&costDetail.costDetail&&costDetail.costDetail.shipment){
				     		shipment=costDetail.costDetail.shipment;
				    	}
				     	var profit=0;
				     	if(costDetail&&costDetail.costDetail&&costDetail.costDetail.profit){
				     		profit=costDetail.costDetail.profit;
				    	}
				     	var wmsalesmonth=param.wmsalesmonth;
				     	    $("#wmsalesmonth").val(wmsalesmonth);
				     	var wmadvordermonth=param.wmadvordermonth;
				            $("#wmadvordermonth").val(wmadvordermonth);
				     	var wmadvcpcmonth=param.wmadvcpcmonth;
				            $("#wmadvcpcmonth").val(wmadvcpcmonth);
				     	var wmadvcrmonth=param.wmadvcrmonth;
				     	    $("#wmadvcrmonth").val(wmadvcrmonth);
				     	var wmadvcprmonth=param.wmadvcprmonth;
				            $("#wmadvcprmonth").val(wmadvcprmonth);
				     	 
						 $("#wmcost").val(param.cost);
						 $("#wmshipment").val(param.shipment);
					     $("#wmcostcurrency").val(param.costcurrency);
						 $("#wmshipmentcurrency").val(param.shipmentcurrency);
				     	var wmcostmonth=fomatFloat(cost*parseInt(wmsalesmonth));
				     	$("#wmcostmonth").text(wmcostmonth);
				     	var wmshipmonth=fomatFloat(shipment*parseInt(wmsalesmonth));
				     	$("#wmshipmonth").text(wmshipmonth);
				     	var wmmprofitmonth=profit*parseInt(wmsalesmonth);
				     	$("#wmmprofitmonth").text(wmmprofitmonth);
			
				     	
				       	var wmadvclickmonth=0;
				    	if(wmadvcpcmonth&&wmadvcpcmonth!="0"){
				     		wmadvclickmonth=parseInt(parseFloat(wmadvordermonth)/(parseFloat(wmadvcrmonth)/100));
				     	}
				     	$("#wmadvclickmonth").text(wmadvclickmonth);
				     	
				    	var wmadvcostmonth=fomatFloat(wmadvclickmonth*parseFloat(wmadvcpcmonth));
				      	$("#wmadvcostmonth").text(wmadvcostmonth);
				  
				  
				      	var wmadvimpmonth=0;
				      	if(wmadvcprmonth&&wmadvcprmonth!="0"){
				      		wmadvimpmonth=parseInt(wmadvclickmonth/(parseFloat(wmadvcprmonth)/100));
				      	}
				     	$("#wmadvimpmonth").text(wmadvimpmonth);
				     	var wmprofitmonth=wmmprofitmonth-parseFloat(wmadvcostmonth);
				     	$("#wmprofitmonth").text(wmprofitmonth);
				     	var wmpoi=0;
				     	if(parseFloat(wmcostmonth)+parseFloat(wmshipmonth)+parseFloat(wmadvcostmonth)>0){
				     		wmpoi=fomatFloat(wmprofitmonth/(parseFloat(wmcostmonth)+parseFloat(wmshipmonth)+parseFloat(wmadvcostmonth))*100);
				     	}
		
				     	$("#wmpoi").text(wmpoi);
				   }
			}
        }
        sendResponse({message:request.data+'开始自动获取'});
    }
);
function getTemplate(style) {
	var template='<div   id="wmprofitpriceInsideBuyBox" style="'+style+'">' 
    +'<div class="sub" style="padding-top:0px;"><table style="margin-bottom:0px;"><tr>'
    +'<td class="small-box bg-aqua" style="padding:10px;">'
    +'  <div class="inner">'
    +'    <h3><span>售价：\${currency}</span><input  id="sellingprice" style="width:120px;"  value="\${sellingPrice}"></h3> '
    +'  </div>'
    +'</td>'
    +'<td class="small-box bg-green" style="padding:10px;">'
    +'<div class="inner">'
    +'<h3>利润率：\${margin}</h3>'
    +'</div>'
    +'</td>'
    +'<td class="small-box bg-red"  style="padding:10px;">'
    +'  <div class="inner">'
    +'    <h3><span>利润：\${currency}</span>\${profit }</h3>'
    +'  </div>'
    +'</td>'
    +'</tr></table>'
    +'<div class="box box-warning">'
    +'<input type="hidden" id="rankId" value="\${rankId }" >'
    +'<div class="row col-lg-12" style="margin-bottom:5px;"> <label class="col-lg-3" style="margin-top:5px;"><span style="float:left">利润计算方案：</span>&nbsp;&nbsp;&nbsp;<span style="font-size:12;color:#A8A8A8;float:left;font-weight:200">&nbsp;&nbsp;&nbsp;&nbsp;长宽高(\${inputDimensions.height.units}):\${inputDimensions.length.value}*\${inputDimensions.width.value}*\${inputDimensions.height.value}&nbsp;&nbsp;&nbsp;&nbsp;重(\${inputDimensions.weight.units})：\${inputDimensions.weight.value}</span>&nbsp;</label>'
    +'<div style="padding-bottom:10px;">'
    +'<select id = "profitCfg"  name = "profitCfg" class="form-control"  >'
    +'	{@each profitCfgList as profitCfg,index}'
    +'		<option value="\${profitCfg.id }" {@if profitCfg.id==profitCfgId} selected="selected" {@/if}>\${profitCfg.name }</option>'
    +'	{@/each}'
    +'</select>'
    +'<a target="_blank" href="https://erp.wimoor.com/page.do?parentid=c3edc4d9-ab14-11e6-bab5-00e04c023f0e&oldlocation=c3edc4d9-ab14-11e6-bab5-00e04c023f0e&oldparentid=94a71ce4-ab14-11e6-bab5-00e04c023f0e&breadcrumb=%E5%88%A9%E6%B6%A6%E8%AE%A1%E7%AE%97%E6%96%B9%E6%A1%88%E8%AE%BE%E7%BD%AE&location=2f74b1a7-c5de-11e6-bab5-00e04c023f0e">维护利润计算方案</a>'
    +' <button class="btn btn-md btn-default pull-right" style="margin-left:6px;margin-top:-10px;" id="wmrecalculate">重新计算</button> </div>'
    +'</div>'
    +'</div>'
    +'<div style="width:60%;padding-top:5px;padding-right:10px;float:left;border-right: 1px solid rgba(0,0,0,.1);"><ul class="mylist products-list">'
    +'<li><table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'     <td class="text-left">采购成本<span style="color:#A8A8A8;">Purchase cost</span></td>'
    +'     <td> <span class="pull-right">\${currency}\${purchase } <select id="wmcostcurrency" ><option value="RMB" selected>RMB</option><option value="\${currency}">\${currency}</option></select><input  id="wmcost" data-title="采购成本" style="width:70px;" name="cost" value=""/></span></td>'
    +'</tr></table>'
    +'</li>'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left"> 运费 <span style="color:#A8A8A8;">Shipment</span></td>'
    +'  <td><span class="pull-right">\${currency}\${shipment } <select id="wmshipmentcurrency" ><option value="RMB" selected>RMB</option><option value="\${currency}">\${currency}</option></select><input  id="wmshipment"   style="width:70px;" name="wmshipment" value="" /></span></td>'
    +'  </tr></table>'
    +'</li>'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left"> 平台佣金<span style="color:#A8A8A8;">Amazon Referral Fee</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${referralFee }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'<li>'
    +' <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +' <td class="text-left" title="针对商品销售收取的非固定交易手续费。"> 变动结算费<span style="color:#A8A8A8;">Variable Closing Fee</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${vcfee }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'	 <li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left" title="为了补偿亚马逊的配送成本而对每件商品收取的费用"> FBA处理费<span style="color:#A8A8A8;">FBA</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${fba }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'{@if country=="IN"}'
    +'<li>'
    +'<table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'<td class="text-left"> 固定结算费<span style="color:#A8A8A8;">Fixed Closing Fee</span></td>'
    +'<td><span class="pull-right"><span>\${currency}</span>\${closingFee }</span></td>'
    +'</tr></table>'
    +'</li>'
    +'{@/if}'
    +'{@if country=="CA"||country=="IN"}'
    +'<li>'
    +'<table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'<td class="text-left">FBA GST/HST 税率<span style="color:#A8A8A8;">FBA GST/HST Taxes</span></td>'
    +'<td><span class="pull-right"><span>\${currency}</span>\${fbaTaxFee }</span></td>'
    +'</tr></table>'
    +'</li>'
    +'{@/if}'
    +'<li>'
    +'<table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'<td class="text-left">仓储费<span style="color:#A8A8A8;">Inventory Storage Fee</span></td>'
    +'<td><span class="pull-right"><span>\${currency}</span>\${storageFee }</span></td>'
    +'</tr></table>'
    +'</li>'
    +'{@if country=="US"||country=="JP"}'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left">库存配置费<span style="color:#A8A8A8;">Inventory Placement Service Fee</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${inPlaceFee }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'{@/if}'
    +'<li>'
    +'<table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'<td class="text-left">标签费<span style="color:#A8A8A8;">Label Service Fee</span></td>'
    +'<td><span class="pull-right"><span>\${currency}</span>\${labelServiceFee }</span></td>'
    +'</tr></table>'
    +'</li>'
    +'{@if country=="US"}'
    +'<li>'
    +'<table width="100%">'
    +'<tr><td width="20px"><span class="fee-num">1</span></td>'
    +'<td class="text-left" title="入仓的时候亚马逊的仓库人员的拣货核对等的费用">人工处理费<span style="color:#A8A8A8;">Manual Processing Fee</span></td>'
    +'<td><span class="pull-right"><span>\${currency}</span>\${manualProcessingFee }</span></td>'
    +'</tr></table>'
    +'</li>'
    +'{@/if}'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left">进口关税<span style="color:#A8A8A8;">Import Duty Tax</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${tax }</span></td>'
    +' </tr></table>'
    +'</li>'
    +'{@if country=="IN"}'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +' <td class="text-left" >进口GST税率<span style="color:#A8A8A8;">Import GST Tax</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${import_GST }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'{@/if}'
    +'<li>'
    +'  <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
    +'  <td class="text-left" >汇率损耗<span style="color:#A8A8A8;">Currency Transport Fee</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${currencyTransportFee }</span></td>'
    +' </tr></table>'
    +'</li>'
    +'<li>'
    +'  <table width="100%">'
    +' <tr><td width="20px"><span class="fee-num">1</span></td>'
    +' <td class="text-left" >市场营销费用<span style="color:#A8A8A8;">Marketing</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${marketing }</span></td>'
    +'  </tr></table>'
    +'</li>'
    +'<li>'
    +' <table width="100%">'
    +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +'  <td class="text-left" >其它每单销售固定费用<span style="color:#A8A8A8;">Others</span></td>'
  +'  <td><span class="pull-right"><span>\${currency}</span>\${others }</span></td>'
  +'  </tr></table>'
  +'</li>'
+' <li>'
 +'  <table width="100%">'
  +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +'  <td class="text-left" >其它每单销售固定成本<span style="color:#A8A8A8;">Other rate Fee</span></td>'
  +'  <td><span class="pull-right"><span>\${currency}</span>\${othersFee }</span></td>'
  +'  </tr></table>'
  +'</li>'
+'<li>'
+'  <table width="100%">'
  +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +'  <td class="text-left" > 按件收费，适用个人版<span style="color:#A8A8A8;">Per-Item Fee</span></td>'
  +'  <td><span class="pull-right"><span>\${currency}</span>\${perItemFee }</span></td>'
  +'  </tr></table>'
  +'</li>'
+'{@if country=="UK"||country=="DE"||country=="FR"||country=="IT"||country=="ES"||country=="NL"||country=="PL"||country=="SE"}'
+'<li>'
+'  <table width="100%">'
  +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +' <td class="text-left" >欧洲增值税'
  +'	<i style="cursor:pointer;" data-toggle="tooltip"   class="icon-question" title="vat增值税费=售价*vat税率/(1+vat税率)"></i>'
    +'	<br><span style="color:#A8A8A8;">Vat Tax Fee</span></td>'
    +'  <td><span class="pull-right"><span>\${currency}</span>\${vatFee }</span></td>'
  +'  </tr></table>'
  +'</li>'
+'{@/if}'
+'{@if country=="IN"}'
+'<li>'
+'  <table width="100%">'
  +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +'  <td class="text-left">销售GST税率<br><span style="color:#A8A8A8;">Selling GST Tax</span></td>'
  +'  <td><span class="pull-right"><span>\${currency}</span>\${selling_GST }</span></td>'
  +'  </tr></table>'
  +'</li>'
+'<li>'
  +'  <table width="100%">'
  +'  <tr><td width="20px"><span class="fee-num">1</span></td>'
  +'  <td class="text-left"> 企业所得税率<br><span style="color:#A8A8A8;">Corporate Income Tax</span></td>'
  +'  <td><span class="pull-right"><span>\${currency}</span>\${corporateInFee }</span></td>'
  +'  </tr></table>'
  +'</li>'
+'{@/if}'
+'</ul>'
+'<div class="total-cost">总成本<span style="color:#A8A8A8;" style="font-size:8px;">Total cost</span>: <span class="pull-right"><span>\${currency}</span>\${totalCost }</span></div>'
+'</div><div style="width:40%;height:100%;padding-top:5px;padding-left:4px;float:right;background-color:#fafafa;"><ul class="mylist products-list">'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >1</span></td>'
+'  <td class="text-left"> 预计月销量&nbsp; </td>'
+'  <td><span class="pull-right"> <input id="wmsalesmonth" style="width:70px;"/>&nbsp;&nbsp;&nbsp;&nbsp;</span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span >2</span></td>'
+'  <td class="text-left"> 预计月广告订单&nbsp; </td>'
+'  <td><span class="pull-right">  <input  id="wmadvordermonth"  style="width:70px;"/>&nbsp;&nbsp;&nbsp;&nbsp;</span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >3</span></td>'
+'  <td class="text-left"> 预计广告单次点击扣费 &nbsp; </td>'
+'  <td><span class="pull-right"> \${currency}<input  id="wmadvcpcmonth"  style="width:70px;"/>&nbsp;&nbsp;&nbsp;&nbsp;</span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >4</span></td>'
+'  <td class="text-left"> 预计转化率&nbsp; </td>'
+'  <td><span class="pull-right"><input id="wmadvcrmonth" style="width:70px;"/>%</span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >5</span></td>'
+'  <td class="text-left"> 预计点击率&nbsp; </td>'
+'  <td><span class="pull-right"><input id="wmadvcprmonth" style="width:70px;"/>%</span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >6</span></td>'
+'  <td class="text-left"> 预计月广告花费&nbsp; </td>'
+'  <td><span class="pull-right">\${currency}<span id="wmadvcostmonth"> 0</span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >7</span></td>'
+'  <td class="text-left"> 预计月曝光量&nbsp; </td>'
+'  <td><span class="pull-right"><span id="wmadvimpmonth">0 </span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span >8</span></td>'
+'  <td class="text-left"> 预计月点击量&nbsp; </td>'
+'  <td><span class="pull-right"><span  id="wmadvclickmonth">0 </span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >9</span></td>'
+'  <td class="text-left"> 预计月采购成本&nbsp; </td>'
+'  <td><span class="pull-right">\${currency}<span  id="wmcostmonth">0</span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span >10</span></td>'
+'  <td class="text-left"> 预计月物流成本&nbsp; </td>'
+'  <td><span class="pull-right">\${currency}<span id="wmshipmonth">0.00</span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >11</span></td>'
+'  <td class="text-left"> 预计月毛利润 &nbsp;</td>'
+'  <td><span class="pull-right">\${currency}<span id="wmmprofitmonth">0.00</span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >12</span></td>'
+'  <td class="text-left"> 预计月净利润 &nbsp;</td>'
+'  <td><span class="pull-right">\${currency}<span id="wmprofitmonth">0.00</span></span></td>'
+'  </tr></table>'
+'</li>'
+'<li>'
+'  <table width="100%">'
+'  <tr><td width="20px"><span  >13</span></td>'
+'  <td class="text-left"> 预计月投入产出比&nbsp;</td>'
+'  <td><span class="pull-right"><span id="wmpoi">0.00</span>%</span></td>'
+'  </tr></table>'
+'</li>'
+'</div></div>'
+'</div>';
 return template;
}

 
 
