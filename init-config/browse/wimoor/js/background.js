 
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse)
		{
	     if(request&&request.opttype=="getHoliday"){
	         $.ajax({
	             url: 'https://photo.wimoor.com/storeImg/extensions/'+request.country+'-holidday.json',
	             type: 'POST',
	             dataType: 'text',
	         }).then(function(data){
                 return  chrome.tabs.sendMessage(sender.tab.id,{jsontext:data} );
	         }, function(data){
	        	 return    chrome.tabs.sendMessage(sender.tab.id,{jsontext:data} );
	         });
	         return sendResponse(request);
	     }
	     
	     if(request&&request.opttype=="getProfitConfig"){
	         $.ajax({
	             url: 'https://erp.wimoor.com/calculateCost/showCost.do',
	             type: 'POST',
	             data:request,
	             dataType: 'text',
	         }).then(function(data){
                  return chrome.tabs.sendMessage(sender.tab.id,{result:data} );
	         }, function(data){
	        	 return chrome.tabs.sendMessage(sender.tab.id,{result:data} );
	         });
	         return sendResponse(request);
	     }
	     if(request&&request.opttype=="updateFile"){
	    	 
	   	  var xhr = new XMLHttpRequest();
	          xhr.timeout = 3000;
	      var obj = {operationName: "reportDataQuery",
	                 query: "query reportDataQuery($input: GetReportDataInput) {\n  getReportData(input: $input) {\n    granularity\n    hadPrevious\n    hasNext\n    size\n    startDate\n    endDate\n    page\n    columns {\n      label\n      valueFormat\n      isGraphable\n      translationKey\n      isDefaultSortAscending\n      isDefaultGraphed\n      isDefaultSelected\n      isDefaultSortColumn\n      __typename\n    }\n    rows\n    __typename\n  }\n}\n",
	                 variables:{input:{endDate:request.myEndDate,startDate:request.myStartDate,legacyReportId: "102:DetailSalesTrafficBySKU"}}
	                };
		    xhr.open('POST',"https://"+request.host+"/business-reports/api");
		    xhr.setRequestHeader('content-type', 'application/json');
		    xhr.send(JSON.stringify(obj));
		    xhr.onreadystatechange = function() {
		    	 if (xhr.readyState == 4) {
		    		 request.data=xhr.responseText;
			         $.ajax({
			             url: 'http://localhost:8099/amazon/api/v1/report/product/amzProductPageviews/auth/uploadSessionFile',
			             type: 'POST',
			             data:request,
			             dataType: 'text',
			         }).then(function(data){
			        	   var status=404;
			        	   if(data.indexOf("ISOK")>0){   status=1;  }
		                   return chrome.tabs.sendMessage(sender.tab.id,{result:data,status:status} );
			         }, function(data){
			        	   var status=404;
			        	   return chrome.tabs.sendMessage(sender.tab.id,{result:data,status:status} );
			         });
		    	 }
		      }
	         request.status=0;
	         return sendResponse(request);
	     }
          return true;
		});