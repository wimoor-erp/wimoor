import {getValue} from '@/utils/index.js';
export function faltngatargetFormatter(row) {
		var value=row.expression;
		var html = "";
		var iconlight = "icon-lightbulb-o";
		var region = "(" + row.groupname + "/" + row.market + ")";
		var txtval = getValue(value);
		var expressionType = row.expressionType;
		if (expressionType) {
			if (expressionType == "manual") {
				if (value) {
					value = value.replace("[", "").replace("]", "").replaceAll("{","").replaceAll("}", "").replaceAll("\"", "")
							.replaceAll("type:", "").replaceAll("value", "").replaceAll(",:", "=").toUpperCase().replaceAll("ASINSAMEAS", "asin");
					txtval = txtval.replace("[", "").replace("]", "").replaceAll("{", "").replaceAll("}", "").replaceAll("\"", "")
							.replaceAll("type:", "").replaceAll("value", "").replaceAll(",:", "=").toUpperCase().replaceAll("ASINSAMEAS", "asin");
					if (value.length > 40) {
						txtval = value.substring(0, 35) + "...";
					}
				}
			} else {
				if (value) {
					value = value.replace("]", "").replace("}", "").split(":")[1].replaceAll("\"", "").toUpperCase().replaceAll("ASINSAMEAS", "asin");
					txtval = txtval.replace("]", "").replace("}", "").split(":")[1].replaceAll("\"", "").toUpperCase().replaceAll("ASINSAMEAS", "asin");
					if (value.indexOf("queryHighRelMatches")>=0) {
						value = "Close-match"
					}
					if (value.indexOf("queryBroadRelMatches")>=0) {
						value = "Loose-match"
					}
					if (value.indexOf("asinSubstituteRelated")>=0) {
						value = "Substitutes"
					}
					if (value.indexOf("asinAccessoryRelated")>=0) {
						value = "Complements"
					}
					txtval = value;
					if (value.length > 40) {
						txtval = value.substring(0, 35) + "...";
					}
				}
			}
		}
		if (txtval.indexOf("queryHighRelMatches")>=0) {
			txtval = "Close-match"
		}
		if (txtval.indexOf("queryBroadRelMatches")>=0) {
			txtval = "Loose-match"
		}
		if (txtval.indexOf("asinSubstituteRelated")>=0) {
			txtval = "Substitutes"
		}
		if (txtval.indexOf("asinAccessoryRelated")>=0) {
			txtval = "Complements"
		}
		html = "<div style='width:300px;margin:0;' class='row'><div "
				+ " class='col-lg-9 padding0'>"
				+ "<span targetid='"
				+ row.id
				+ "'   class='pointer'   title='"
				+ getValue(value)
				+ "'>"
				+ txtval
				+ "</span>"
				+ "&nbsp;&nbsp; "
				+ "</div><div class='padding0' style='float:right;margin-right:20px;'><span  class='" + iconlight
				+ " text-red'></span></div></div>"
				+ "<div class='light-font' style='font-size:10px;' >" + region
				+ "</div>";
		return html;
	}
	

export	function targetnameFormatter(row) {
	var value=row.expressionname;
	var html = "";
	var iconlight = "icon-lightbulb-o";
	var region = "(" + row.groupname + "/" + row.market + ")";
	var expressionType = row.expressionType;
	var targetingText = row.targetingText;
	var txtval = getValue(value);
	if (targetingText != null && targetingText != "" && targetingText != undefined&&targetingText.indexOf("{")<0) {
		value = targetingText;
		txtval = getValue(value);
		// if (value.length > 40) {
		// 	txtval = value.substring(0, 35) + "...";
		// }
	} else {
		if (expressionType) {
			if (expressionType.toUpperCase() == "AUTO") {
				if (value) {
					value = value.replace("]", "").replace("}", "").split(":")[1].replaceAll("\"", "").replaceAll("asinSameAs", "asin").replaceAll("ASIN_SAME_AS", "asin");
					txtval = txtval.replace("]", "").replace("}", "").split(":")[1].replaceAll("\"", "").replaceAll("asinSameAs", "asin").replaceAll("ASIN_SAME_AS", "asin");
					if (value.indexOf("queryHighRelMatches")>=0) {
						value = "Close-match"
					}
					if (value.indexOf("queryBroadRelMatches")>=0) {
						value = "Loose-match"
					}
					if (value.indexOf("asinSubstituteRelated")>=0) {
						value = "Substitutes"
					}
					if (value.indexOf("asinAccessoryRelated")>=0) {
						value = "Complements"
					}
					txtval = value;
					// if (value.length > 40) {
					// 	txtval = value.substring(0, 35) + "...";
					// }
				}
			} else {
				if (value) {
					value = value.replace("[", "").replace("]", "").replaceAll("{", "").replaceAll("}", "").replaceAll("\"", "")
							.replaceAll("type:", "").replaceAll("value", "").replaceAll(",:", "=").replaceAll("asinSameAs", "asin").replaceAll("ASIN_SAME_AS", "asin");
					txtval = txtval.replace("[", "").replace("]", "").replaceAll("{", "").replaceAll("}", "").replaceAll("\"", "").replaceAll("type:", "")
							.replaceAll("value", "").replaceAll(",:", "=").replaceAll("asinSameAs", "asin").replaceAll("ASIN_SAME_AS", "asin");
					// if (value.length > 40) {
					// 	txtval = value.substring(0, 35) + "...";
					// }
					if (value.indexOf("queryHighRelMatches")>=0) {
						value = "Close-match"
					}
					if (value.indexOf("queryBroadRelMatches")>=0) {
						value = "Loose-match"
					}
					if (value.indexOf("asinSubstituteRelated")>=0) {
						value = "Substitutes"
					}
					if (value.indexOf("asinAccessoryRelated")>=0) {
						value = "Complements"
					}
					txtval = value;
					// if (value.length > 40) {
					// 	txtval = value.substring(0, 35) + "...";
					// }
				}
			}
		}
	}
	 
	if (txtval.indexOf("queryHighRelMatches")>=0) {
		txtval = "Close-match"
	}
	if (txtval.indexOf("queryBroadRelMatches")>=0) {
		txtval = "Loose-match"
	}
	if (txtval.indexOf("asinSubstituteRelated")>=0) {
		txtval = "Substitutes"
	}
	if (txtval.indexOf("asinAccessoryRelated")>=0) {
		txtval = "Complements"
	}
	html = "<div style='width:300px;margin:0;' class='row'><div "
			+ " class='col-lg-9 padding0'>"
			+ "<span targetid='"
			+ row.id
			+ "'   class='pointer'   title='"
			+ getValue(value)
			+ "'>"
			+ txtval
			+ "</span>"
			+ "&nbsp;&nbsp; "
			+ "</div><div class='padding0'  style='float:right;margin-right:20px;'><span onclick='showAdgroupRemaindModal(\""
			+ row.id + "\");' class='" + iconlight
			+ " text-red'></span></div></div>"
			+ "<div class='font-extraSmall' style='font-size:10px;' >" + region
			+ "</div>";
	return html;
}



export function targetsuggestFormatter(row) {
	var suggestedbid = "";
	var rangeEnd = "";
	var rangeStart = "";
	if (row["suggestedbid"])
		suggestedbid = row.suggestedbid;
	if (row["rangeEnd"])
		rangeEnd = row.rangeEnd;
	if (row["rangeStart"])
		rangeStart = row.rangeStart;
	if (row.expressionType != "manual"){
		return "<span title='自动类型,无建议竞价'>-</span>";
	}else{
		return "<div id='suggests" + row.id + "'>" + suggestedbid
				+ "<div class='light-font text-sm'>" + rangeStart + "-" + rangeEnd + "</div><div>";
	}
}
	
export	function adgroupsuggestFormatter(row) {
		var suggestedbid = "";
		var rangeEnd = "";
		var rangeStart = "";
		if (row["targetingtype"] != "auto") {
			return "<span class='light-font' style='font-size:10px;' title='非自动广告活动下的广告组无建议竞价'>无建议竞价</span>";
		}
		if (row["suggestedbid"]) {
			suggestedbid = row.suggestedbid;
		}
		if (row["rangeEnd"]) {
			rangeEnd = row.rangeEnd;
		}
		if (row["rangeStart"]) {
			rangeStart = row.rangeStart;
		}
	    return  suggestedbid + "<div class='light-font text-sm'>" + rangeStart + "-" + rangeEnd + "</div>" ;
		  
	}
	
export	function keysuggestFormatter(row) {
		var suggestedbid = "";
		var rangeEnd = "";
		var rangeStart = "";
		if (row["suggestedbid"]) {
			suggestedbid = row.suggestedbid;
		}
		if (row["rangeEnd"]) {
			rangeEnd = row.rangeEnd;
		}
		if (row["rangeStart"]) {
			rangeStart = row.rangeStart;
		}
		if (row.targetingType != "manual") {
			return "<span title='自动类型,无建议竞价'>无建议竞价</span>";
		}
		 
		return "<div id='suggest" + row.id + "'>" + suggestedbid
				+ "<div class='light-font text-sm'>" + rangeStart + "-" + rangeEnd + "</div><div>";
		 
	
	}
	
	
export function sbAdsFormatter(row){
	var html="";
	if(row && row.creative){
		var data=JSON.parse(row.creative);
		if(data.asins && data.asins.length>0){
			data.asins.forEach(item=>{
				html=html+" ["+item+"] ";
			})
		   return html;
		} 
	}
	if(row && row.landingPage&&html==""){
		var data=JSON.parse(row.landingPage);
		if(data.asins && data.asins.length>0){
			data.asins.forEach(item=>{
				html=html+" ["+item+"] ";
			})
		  return html;
		} 
	}
	return "--";
}
	
