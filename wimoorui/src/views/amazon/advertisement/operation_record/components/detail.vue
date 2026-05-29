<template>
	<el-dialog v-model="dialogVisable" :title="row.opt" >
		<div v-html="html">
			
		</div>
		<template #footer>
			<el-button @click="dialogVisable=false">关闭</el-button>
		</template>
	
	</el-dialog>
</template>

<script setup>
	import {reactive,toRefs}from"vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import {deleteFrom } from '@/api/erp/assembly/assemblyApi.js'
	const state = reactive({
		dialogVisable:false,
		row:{},
		html:"",
	})
	const {
		dialogVisable,row,html
		 
	}=toRefs(state)
	function loadOldData(olddata, nowdata) {
		if(!olddata&&!nowdata){
			return "";
		}else if(olddata&&!nowdata){
			return olddata;
		}else if(!olddata&&nowdata){
			return "";
		} else if (olddata == nowdata) {
			return "";
		} else {
			return olddata;
		}
	}
	function showOperateDetail(row) {
		var html = "";
		var samehtml = "";
		var afterjson;
		var beforejson;
		var keywordstbody = "";
		var protargetbody = "";
		var producttbody = "";
		var KeywordsNegativatbody = "";
		var protargetNegativatbody = "";
		var skus = "";
		var bidding;
		var matchtype = "";
		var targettype ;
		var enddate;
		var typename =row.opt;
		var mystate="";
		// //////
		samehtml += "<div class='form-group col-lg-12'>"
				+ "<span class='light-font'>操作类型：</span><span>"
				+ typename
				+ "</span>"
				+ "</div>"
				+ "<div class='form-group col-lg-12'>"
				+ "<span class='light-font'>操作日期：</span><span>"
				+ dateFormat(row.opttime)
				+ "</span>"
				+ "</div>"
				+ "<div class='form-group col-lg-12'>"
				+ "<div class='col-lg-5 padding0'><span class='light-font'>店铺：</span><span>"
				+ row.groupname + "</span></div>"
				+ "<div class='col-lg-7'><span class='light-font'>站点：</span><span>"
				+ row.maketname + "</span></div>" + "</div>";
		if (row["beforeobject"]) {
			var beforeobject = "";
			if (row.beforeobject.substring(0, 1) == "[") {
				beforeobject = row.beforeobject;
			} else {
				beforeobject = "[" + row.beforeobject + "]";
			}
			beforejson = eval(beforeobject);
			for (i = 0; i < beforejson.length; i++) {
				bejson = beforejson[i];
			}
		}
		var json=null;
		if (row["afterobject"]) {
			var afterobject = "";
			if (row.afterobject.substring(0, 1) == "[") {
				afterobject = row.afterobject;
			} else {
				afterobject = "[" + row.afterobject + "]";
			}
			afterjson = eval(afterobject);
			for (i = 0; i < afterjson.length; i++) {
				json = afterjson[i];
				skus += "，" + json.sku;
				bidding = bidplusFormatter2(json.bidding);
			}
			skus = skus.substring(1);
		}
		var oldname = "", oldstate = "", oldenddate = "", olddailybudget = "", oldbidstate = "", olddefaultbid = "", oldbidding = "", oldbid = "";
		for (var i = 0; i < afterjson.length; i++) {
			var bejson;
			json = afterjson[i];
			var oldname = "";
			var oldstate = "";
			var oldenddate = "";
			var olddailybudget = "";
			var olddefaultbid = "";
			var olddidding = "";
			if (row["beforeobject"]) {
				bejson = beforejson[i];
				oldname = loadOldData(bejson.name, json.name);
				oldstate = loadOldData(bejson.state, json.state);
				oldenddate = loadOldData(bejson.enddate, json.enddate);
				olddailybudget = loadOldData(bejson.dailybudget, json.dailybudget);
				oldbid = loadOldBidData(bejson.bid, json.bid, row.defaultBid);
				oldbidding = bidplusFormatter2(bejson.bidding);
				olddefaultbid = loadOldData(bejson.defaultbid, json.defaultbid);
			}  
			// //////字段翻译
			if (oldstate == "enabled"||oldstate == "ENABLED") {
				oldstate = "<span class='text-green'>启用</span>"
			} else if (oldstate == "paused"||oldstate == "PAUSED") {
				oldstate = "<span class='text-red'>禁用</span>"
			} else if (oldstate == "archived"||oldstate == "ARCHIVED") {
				oldstate = "<span>归档</span>"
			} else if (oldstate == "pending"||oldstate == "PENDING") {
				oldstate = "<span>审核</span>"
			}
			if(oldstate){
			     oldstate = "<span class='light-font'> ( " + oldstate
					+ " <span class='box-gray'>old</span> )</span>";
			}
			// //////字段翻译
			if (json.state == "enabled"||json.state == "ENABLED") {
				mystate = "<span class='text-green'>启用</span>"
			} else if (json.state == "paused"||json.state == "PAUSED") {
				mystate = "<span class='text-red'>禁用</span>"
			} else if (json.state == "archived"||json.state == "ARCHIVED") {
				mystate = "<span>归档</span>"
			} else if (json.state == "pending"||json.state == "PENDING") {
				mystate = "<span>审核</span>"
			}
			if (json.targetingtype == "manual") {
				targettype = "手动";
			} else {
				targettype = "自动";
			}
			if (json.enddate == null || json.enddate == undefined) {
				enddate = "-";
			} else {
				enddate = dateFormat(json.enddate);
			}
			if (json.matchtype == "broad" || json.matchtype == "BROAD") {
				matchtype = "广泛匹配";
			} else if (json.matchtype == "exact" || json.matchtype == "EXACT"
					|| json.matchtype == "negativeExact") {
				matchtype = "精准匹配";
			} else if (json.matchtype == "phrase" || json.matchtype == "PHRASE"
					|| json.matchtype == "negativePhrase") {
				matchtype = "词组匹配";
			}
			if (oldstate != "") {
				if (oldstate == "enabled") {
					oldstate = "<span class='light-font'> ( 启用 <span class='box-gray'>old</span> )</span>"
				} else if (oldstate == "paused") {
					oldstate = "<span class='light-font'> ( 禁用 <span class='box-gray'>old</span> )</span>"
				} else if (oldstate == "archived") {
					oldstate = "<span class='light-font'> ( 归档 <span class='box-gray'>old</span> )</span>"
				} else if (oldstate == "pending") {
					oldstate = "<span class='light-font'> ( 审核 <span class='box-gray'>old</span> )</span>"
				}
			}
			if (oldenddate != "") {
				if (oldenddate == null || oldenddate == undefined) {
					oldenddate = "<span class='light-font'> ( - <span class='box-gray'>old</span> )</span>";
				} else {
					oldenddate = "<span class='light-font'> ( "
							+ dateFormat(oldenddate)
							+ " <span class='box-gray'>old</span> )</span>";
				}
			}
			if (oldname != "") {
				oldname = "<span class='light-font'> ( " + oldname
						+ " <span class='box-gray'>old</span> )</span>";
			}
			if (olddailybudget != "") {
				olddailybudget = "<span class='light-font'> ( " + olddailybudget
						+ " <span class='box-gray'>old</span> )</span>";
			}
			if (oldbidstate != "") {
				if (oldbidstate == "false") {
					oldbidstate = "<span class='light-font'> ( 已关闭 <span class='box-gray'>old</span> )</span>";
				} else if (oldbidstate == "true") {
					oldbidstate = "<span class='light-font'> ( 已开启 <span class='box-gray'>old</span> )</span>";
				}
			}
			if (oldbid != "") {
				oldbid = "<span class='light-font'> ( " + oldbid
						+ " <span class='box-gray'>old</span> )</span>";
			}
			if (olddefaultbid != "") {
				olddefaultbid = "<span class='light-font'> ( " + olddefaultbid
						+ " <span class='box-gray'>old</span> )</span>";
			}
			if (oldbidding != bidding) {
				oldbidding = "<span class='light-font'> ( " + oldbidding
						+ " <span class='box-gray'>old</span> )</span>";
			}else{
				oldbidding="";
			}
			// ////字段翻译end
			if (row.beanclasz == "AmzAdvCampaigns") {
				html += "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告活动：</span><span>"
						+ json.name
						+ oldname
						+ "</span>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告活动状态：</span><span>"
						+ mystate
						+ oldstate
						+ "</span>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<div class='col-lg-5 padding0'><span class='light-font'>类型：</span>"
						+ json.compaignTypePrefix
						+ "</div>"
						+ "<div class='col-lg-7 '><span class='light-font'>投放：</span>"
						+ targettype
						+ "</div>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<div class='col-lg-5 padding0'><span class='light-font'>开始日期：</span>"
						+ dateFormat(json.startdate)
						+ "</div>"
						+ "<div class='col-lg-7 '><span class='light-font'>结束日期：</span>"
						+ enddate
						+ oldenddate
						+ "</div>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>预算：</span><span>"
						+ json.dailybudget
						+ olddailybudget
						+ "</span>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>竞价：</span><span>"
						+ bidding
						+ "<span class='texts'>"
						+ oldbidding
						+ "</span></span>"
						+ "</div>";
			} else if (row.beanclasz == "AmzAdvCampaignsHsa"||row.beanclasz == "AmzAdvCampaignsSD") {
				var type="sb";
				if(row.beanclasz == "AmzAdvCampaignsSD") {
					type="sd";
				}
				html += "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告活动：</span><span>"
						+ json.name
						+ oldname
						+ "</span>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告活动状态：</span><span>"
						+ mystate
						+ oldstate
						+ "</span>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<div class='col-lg-5 padding0'><span class='light-font'>类型：</span>"+type+"</div>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<div class='col-lg-5 padding0'><span class='light-font'>开始日期：</span>"
						+ dateFormat(json.startdate)
						+ "</div>"
						+ "<div class='col-lg-7 '><span class='light-font'>结束日期：</span>"
						+ enddate
						+ oldenddate
						+ "</div>"
						+ "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>预算：</span><span>"
						+ json.budget
						+ olddailybudget + "</span>" + "</div>";
			} else if (row.beanclasz == "AmzAdvAdgroups"||row.beanclasz == "AmzAdvAdgroupsSD") {
				html += "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告活动：</span><span>"
						+ row.campaignName + "</span>" + "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告组：</span><span>" + json.name
						+ oldname + "</span>" + "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>广告组状态：</span><span>" + mystate
						+ oldstate + "</span>" + "</div>"
						+ "<div class='form-group col-lg-12'>"
						+ "<span class='light-font'>默认竞价：</span><span>"
						+ json.defaultbid + olddefaultbid + "</span>" + "</div>";
			} else if (row.beanclasz == "AmzAdvProductads"||row.beanclasz == "AmzAdvProductadsSD") {
				producttbody += "<tr><td>" + json.sku + "</td><td>" + mystate + oldstate + "</td></tr>";
			} else if (row.beanclasz == "AmzAdvKeywords") {
				if (json.bid) {
					keywordstbody += "<tr><td>" + json.keywordtext + "</td><td>"
							+ matchtype + "</td><td>" + json.bid + oldbid
							+ "</td><td>" + mystate + oldstate + "</td></tr>";
				} else {
					keywordstbody += "<tr><td>" + json.keywordtext + "</td><td>"
							+ matchtype + "</td><td>" + row.defaultBid
							+ "</td><td>" + mystate + oldstate + "</td></tr>";
				}
			} else if (row.beanclasz == "AmzAdvKeywordsNegativa") {
				KeywordsNegativatbody += "<tr><td>" + json.keywordtext + "</td><td>" + matchtype + "</td></tr>";
			} else if (row.beanclasz == "AmzAdvProductTarge"||row.beanclasz == "AmzAdvProductTargeSD") {
				protargetbody += "<tr><td style='width:200px;'>" + json.expression
						+ "</td><td>" + json.bid + "</td><td>" + mystate + oldstate + "</td></tr>";
			} else if (row.beanclasz == "AmzAdvProductTargeNegativa"||row.beanclasz == "AmzAdvProductTargeNegativaSD") {
				protargetNegativatbody += "<tr><td style='width:200px;'>"
						+ json.expression + "</td><td>" + mystate + oldstate + "</td></tr>";
			}
		}
		if (row.beanclasz == "AmzAdvProductTarge"||row.beanclasz == "AmzAdvProductTargeSD") {
			html += "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告活动：</span><span>"
					+ row.campaignName
					+ "</span>"
					+ "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告组：</span><span>"
					+ row.adGroupName
					+ "</span>"
					+ "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<table class='table table-bordered'>"
					+ "<thead><tr><td>商品投放</td><td>关键词竞价</td><td>状态</td></tr></thead>"
					+ "<tbody>" + protargetbody + "</tbody>" + "</table>"
					+ "</div>";
		}
		if (row.beanclasz == "AmzAdvProductTargeNegativa"||row.beanclasz == "AmzAdvProductTargeNegativaSD") {
			html += "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告活动：</span><span>"
					+ row.campaignName + "</span>" + "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告组：</span><span>"
					+ row.adGroupName + "</span>" + "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<table class='table table-bordered'>"
					+ "<thead><tr><td>否定商品投放</td><td>状态</td></tr></thead>"
					+ "<tbody>" + protargetNegativatbody + "</tbody>" + "</table>"
					+ "</div>";
		}
		if (row.beanclasz == "AmzAdvProductads"||row.beanclasz == "AmzAdvProductadsSD") {
			html += "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告活动：</span><span>"
					+ row.campaignName + "</span>" + "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告组：</span><span>"
					+ row.adGroupName + "</span>" + "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<table class='table table-bordered'>"
					+ "<thead><tr><td>SKU</td><td>状态</td></tr></thead>" + "<tbody>"
					+ producttbody + "</tbody>" + "</table>" + "</div>";
		}
		if (row.beanclasz == "AmzAdvKeywords") {
			var name = "";
			if (row.adGroupName) {
				name = row.adGroupName;
			} else {
				name = "-";
			}
			html += "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告活动：</span><span>"
					+ row.campaignName
					+ "</span>"
					+ "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告组：</span><span>"
					+ name
					+ "</span>"
					+ "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<table class='table table-bordered'>"
					+ "<thead><tr><td>关键词</td><td>匹配类型</td><td>关键词竞价</td><td>状态</td></tr></thead>"
					+ "<tbody>" + keywordstbody + "</tbody>" + "</table>"
					+ "</div>";
		}
		if (row.beanclasz == "AmzAdvKeywordsNegativa") {
			var name = "";
			if (row.adGroupName) {
				name = row.adGroupName;
			} else {
				name = "-";
			}
			html += "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告活动：</span><span>"
					+ row.campaignName + "</span>" + "</div>"
					+ "<div class='form-group col-lg-12'>"
					+ "<span class='light-font'>广告组：</span><span>" + name
					+ "</span>" + "</div>" + "<div class='form-group col-lg-12'>"
					+ "<table class='table table-bordered'>"
					+ "<thead><tr><td>否定关键词</td><td>匹配类型</td></tr></thead>"
					+ "<tbody>" + KeywordsNegativatbody + "</tbody>" + "</table>"
					+ "</div>";
		}
		return samehtml + html;
	}
	
 function getValue(value,prefix,suffix){
 	
 	if(value==null||value==""||typeof(value)=="undefined"||(typeof(value) === "number" && isNaN(value))){
 		return "--";
 	}
 	else {
 		if(prefix){
 			value=prefix+value;
 		}
 	    if(suffix){
 	    	value=value+suffix;
 		} 
 	  return value;
 		 
 	}
 }
 
	function bidplusFormatter2(value) {
		var addhtml = "";
		if (value) {
			var data = JSON.parse(value);
			if (data["strategy"]) {
				value = data.strategy;
				if ("autoForSales" == value)
					value = "提高和降低";
				if ("legacyForSales" == value)
					value = "只降低";
				if ("manual" == value)
					value = "固定竞价";
				var just = data.adjustments;
				if (just &&just.length > 0 ) {
					var tophtml = "";
					var prohtml = "";
					for (var i = 0; i < just.length; i++) {
						var predicate = just[i].predicate;
						if (just.length == 2) {
							if (predicate == "placementTop") {
								tophtml += "<span class='light-font'>首页:</span>" + getValue(just[i].percentage) + "%";
							} else {
								prohtml += "<span class='light-font'>商品页面:</span>" + getValue(just[i].percentage) + "%";
							}
						}
						if (just.length == 1) {
							if (predicate == "placementTop") {
								tophtml += "<span class='light-font'>首页:</span>" + getValue(just[i].percentage) + "%";
								prohtml += "<span class='light-font'>商品页面:0%<span>";
							} else {
								prohtml += "<span class='light-font'>商品页面:</span>" + getValue(just[i].percentage) + "%";
								tophtml += "<span class='light-font'>首页:0%</span>";
							}
						}
					}
					addhtml = "<span class='text-sm'>(" + tophtml + " | " + prohtml + ")</span>";
				} else {
					addhtml = "<span  class='text-sm light-font'>首页:0%  |  商品页面:0%</span>";
				}
			}
		}
		var html = "<span>" + getValue(value) + addhtml + "</span>";
		return html;
	}
	
	function loadOldBidData(olddata, nowdata, defaultbid) {
		if (!olddata) {
			olddata = defaultbid;
		}
		if (!nowdata) {
			nowdata = defaultbid;
		}
		if (olddata == nowdata) {
			return "";
		} else {
			return olddata;
		}
	}
	
	function show(row,ftype){
	  state.dialogVisable=true;
	  state.row=row;
	  state.html=showOperateDetail(row);
	}
 
	defineExpose({
		show,
	})
</script>
<style>
	.box-gray{
	    background: #a0a0a0;
	    padding: 0px 4px;
	    border-radius: 2px;
	    color: #fff;
	}
	.light-font {
	    color: #999;
	    font-weight: 400 !important;
	}
	.form-group {
	    margin-bottom: 15px;
	}
	.row {
	  margin-left: -15px;
	  margin-right: -15px;
	}
	.col-xs-1, .col-sm-1, .col-md-1, .col-lg-1, .col-xs-2, .col-sm-2, .col-md-2, .col-lg-2, .col-xs-3, .col-sm-3, .col-md-3, .col-lg-3, .col-xs-4, .col-sm-4, .col-md-4, .col-lg-4, .col-xs-5, .col-sm-5, .col-md-5, .col-lg-5, .col-xs-6, .col-sm-6, .col-md-6, .col-lg-6, .col-xs-7, .col-sm-7, .col-md-7, .col-lg-7, .col-xs-8, .col-sm-8, .col-md-8, .col-lg-8, .col-xs-9, .col-sm-9, .col-md-9, .col-lg-9, .col-xs-10, .col-sm-10, .col-md-10, .col-lg-10, .col-xs-11, .col-sm-11, .col-md-11, .col-lg-11, .col-xs-12, .col-sm-12, .col-md-12, .col-lg-12 {
	  position: relative;
	  min-height: 1px;
	  padding-left: 15px;
	  padding-right: 15px;
	}
	.col-xs-1, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .col-xs-10, .col-xs-11, .col-xs-12 {
	  float: left;
	}
	.col-xs-12 {
	  width: 100%;
	}
	.col-xs-11 {
	  width: 91.66666667%;
	}
	.col-xs-10 {
	  width: 83.33333333%;
	}
	.col-xs-9 {
	  width: 75%;
	}
	.col-xs-8 {
	  width: 66.66666667%;
	}
	.col-xs-7 {
	  width: 58.33333333%;
	}
	.col-xs-6 {
	  width: 50%;
	}
	.col-xs-5 {
	  width: 41.66666667%;
	}
	.col-xs-4 {
	  width: 33.33333333%;
	}
	.col-xs-3 {
	  width: 25%;
	}
	.col-xs-2 {
	  width: 16.66666667%;
	}
	.col-xs-1 {
	  width: 8.33333333%;
	}
	 
	@media (min-width: 768px) {
	  .col-sm-1, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-sm-10, .col-sm-11, .col-sm-12 {
	    float: left;
	  }
	  .col-sm-12 {
	    width: 100%;
	  }
	  .col-sm-11 {
	    width: 91.66666667%;
	  }
	  .col-sm-10 {
	    width: 83.33333333%;
	  }
	  .col-sm-9 {
	    width: 75%;
	  }
	  .col-sm-8 {
	    width: 66.66666667%;
	  }
	  .col-sm-7 {
	    width: 58.33333333%;
	  }
	  .col-sm-6 {
	    width: 50%;
	  }
	  .col-sm-5 {
	    width: 41.66666667%;
	  }
	  .col-sm-4 {
	    width: 33.33333333%;
	  }
	  .col-sm-3 {
	    width: 25%;
	  }
	  .col-sm-2 {
	    width: 16.66666667%;
	  }
	  .col-sm-1 {
	    width: 8.33333333%;
	  }
	}
	@media (min-width: 992px) {
	  .col-md-1, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-md-10, .col-md-11, .col-md-12 {
	    float: left;
	  }
	  .col-md-12 {
	    width: 100%;
	  }
	  .col-md-11 {
	    width: 91.66666667%;
	  }
	  .col-md-10 {
	    width: 83.33333333%;
	  }
	  .col-md-9 {
	    width: 75%;
	  }
	  .col-md-8 {
	    width: 66.66666667%;
	  }
	  .col-md-7 {
	    width: 58.33333333%;
	  }
	  .col-md-6 {
	    width: 50%;
	  }
	  .col-md-5 {
	    width: 41.66666667%;
	  }
	  .col-md-4 {
	    width: 33.33333333%;
	  }
	  .col-md-3 {
	    width: 25%;
	  }
	  .col-md-2 {
	    width: 16.66666667%;
	  }
	  .col-md-1 {
	    width: 8.33333333%;
	  }
	}
	@media (min-width: 1200px) {
	  .col-lg-1, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-lg-10, .col-lg-11, .col-lg-12 {
	    float: left;
	  }
	  .col-lg-12 {
	    width: 100%;
	  }
	  .col-lg-11 {
	    width: 91.66666667%;
	  }
	  .col-lg-10 {
	    width: 83.33333333%;
	  }
	  .col-lg-9 {
	    width: 75%;
	  }
	  .col-lg-8 {
	    width: 66.66666667%;
	  }
	  .col-lg-7 {
	    width: 58.33333333%;
	  }
	  .col-lg-6 {
	    width: 50%;
	  }
	  .col-lg-5 {
	    width: 41.66666667%;
	  }
	  .col-lg-4 {
	    width: 33.33333333%;
	  }
	  .col-lg-3 {
	    width: 25%;
	  }
	  .col-lg-2 {
	    width: 16.66666667%;
	  }
	  .col-lg-1 {
	    width: 8.33333333%;
	  }
	}
	</style>
<style scoped="scoped">
	 .text-red {
	     color: #dd4b39 !important;
	 }
	 .box-gray{
	     background: #a0a0a0;
	     padding: 0px 4px;
	     border-radius: 2px;
	     color: #fff;
	 }
	 .text-sm {
	     font-size: 12px;
	 }
	 
	 .padding0 {
	     padding: 0;
	 }
	
</style>