<template >
	<div class="main-sty con-header"> 
	<el-row>
		<Group @change="getGroup"    ref="groupRef" />
	 <el-space>
		 <el-select v-model="queryParams.currency" @change="handleQuery">
		 	<el-option label="人民币" value="CNY"></el-option>
		 	<el-option label="美元" value="USD"></el-option>
		 	<el-option label="站点币种" value="market"></el-option>
		 </el-select>
		 <div class="date-picker-group">
		 	<el-select v-model="queryParams.datetype" style="width:100px" placeholder="结算日期" @change="handleQuery">
		 		<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
		 	</el-select>
			<Datepicker  type="monthrange"   ref="datepickersMonth" @changedate="changedateMonth" />
		 </div>
	  </el-space>
	 	<div class='rt-btn-group'>
		  <el-button @click="exportToExcel">导出 </el-button>
	  </div>
	  </el-row>
	
	<table class="sd-table" ref="tableRef"  style="width: 100%;" v-loading="loading" cellspacing="0px;">
				<thead>
					<tr id="theadtr" v-html="theadtr" class="gary-font " style="font-size: 12px !important">
					</tr>
				</thead>
				<tbody>
					<tr id="in_sum" v-html="in_sum" class="sumRow"> </tr>
					<tr id="in_principal" v-html="in_principal" style="border-left: 3px solid #ffa400;"> </tr>
					<tr id="in_other" v-html="in_other" style="border-left: 3px solid #ffa400;"> </tr>
					<tr id="in_principalRate" v-html="in_principalRate"> </tr>
					<tr id="out_sum" v-html="out_sum" class="sumRow"> </tr>
					<tr id="out_adv" v-html="out_adv" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_adv_invoice" v-html="out_adv_invoice" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_advRate" v-html="out_advRate" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_commission" v-html="out_commission" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_fba" v-html="out_fba" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_refund" v-html="out_refund" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="out_other" v-html="out_other" style="border-left: 3px solid #25D769;"> </tr>
					<tr id="total" v-html="total" style="border-left: 3px solid #F84040;"> </tr>
					<tr id="converted_total" v-html="converted_total" style="border-left: 3px solid #F84040;"> </tr>
					<tr id="accountconverted_total" v-html="accountconverted_total" style="border-left: 3px solid #dedede;"> </tr>
					<tr id="linetr"> </tr>
				</tbody>
				 
				 <tbody id="localname" class="localFee" >
					 <tr  ><td class="font-bold ">本地费用</td>
					 						 <td :colspan="times.length">（除头程费用其他费用与店铺无关） </td>
					 						 
					 </tr>
					 
					 <tr v-for="name,index in localName">
						 <td >{{name}}</td>
						 <template v-if="localmaps[name]"  v-for="time,index in times" >
							 <td class='text-right' v-if="index!=0">
							 							 {{localmaps[name][time]}}
							 </td>
							 <td class='text-right' v-if="index==times.length-1">{{localmaps[name]["all"]}}</td>
						 </template>
					 </tr>
				 </tbody> 
	</table>
	</div>
</template>
<script>
	export default{ name:"月度结算" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
    import '@/assets/css/packbox_table.css';
	import Group from '@/views/amazon/listing/common/group.vue';
	import settlementAccRptApi from '@/api/amazon/finances/settlementAccRptApi.js';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import * as XLSX from 'xlsx';
	const tableRef=ref();
	const groupRef=ref();
	const  state=reactive({
		isload:true,
		dateOptions:[{label:'结算日期',value:''},{label:'转账日期',value:'acc'}],
		loading:false,
	    columnHeaders:[],
		 tableData:[],
		 addhtml:"",
		 queryParams:{datetype:"",currency:"CNY"},
		 headerData:[],
		 localName:[],
		 localmaps:{},
		 times:[],
		 linetr:"",
		 total:"",
		 theadtr:"",
		 in_sum:"", 
		 in_principal:"", 
		 in_other:"", 
		 in_principalRate:"", 
		 out_sum:"", 
		 out_adv:"", 
		 out_adv_invoice:"",
		 out_advRate:"", 
		 out_commission:"", 
		 out_fba:"", 
		 out_refund:"", 
		 out_other:"", 
		 converted_total:"", 
		 accountconverted_total:"",
		 invoiceData:null,
	})
 
	const{
		isload,
		loading,
		dateOptions,
		localName,
		localmaps,
		times,
		tableData,
		columnHeaders,
		queryParams,
		headerData,
		linetr,
		total,
		theadtr,
		in_sum, 
		in_principal, 
		in_other, 
		in_principalRate, 
		out_sum, 
		out_adv, 
		out_adv_invoice,
		out_advRate, 
		out_commission, 
		out_fba, 
		out_refund, 
		out_other, 
		converted_total, 
		accountconverted_total,
		invoiceData,
	}=toRefs(state)
	function handleQuery(){
		//查询表格数据
		state.loading=true;
		// summaryApi.getInvoicesSummary({"groupid":state.queryParams.groupid,
		// 		"marketplaceid":state.queryParams.marketplaceid,
		// 		"fromDate":state.queryParams.fromDate,"toDate":state.queryParams.endDate}).then((ress)=>{
		// 			if(ress.data && ress.data.length>0){
		// 				state.invoiceData=ress.data;
		// 			}
		// });
		settlementAccRptApi.getSummaryMonth(state.queryParams).then((res)=>{
			    state.loading=false;
				state.linetr="";
				state.total="";
				state.theadtr="";
				state.in_sum=""; 
				state.in_principal=""; 
				state.in_other=""; 
				state.in_principalRate=""; 
				state.out_sum=""; 
				state.out_adv=""; 
				state.out_adv_invoice="";
				state.out_advRate=""; 
				state.out_commission=""; 
				state.out_fba=""; 
				state.out_refund=""; 
				state.out_other=""; 
				state.converted_total=""; 
				state.accountconverted_total="";
			    var data=res.data;
				var headhtml = "", insumhtml = "", inprincipalhtml = "", inotherhtml = "", inprincipalRatehtml = "", outsumhtml = "",
						outadvhtml = "",outadvinvoicehtml="", outadvhtmlRate = "", outcommissionhtml = "", outfbahtml = "", outrefundhtml = "", outotherhtml = "",
						convertedtotalhtml = "", convertedtotalRatehtml = "",accountconvertedtotalhtml="", linetrhtml = "", totalhtml = "";
						var maps = data.maps;
						state.localName=data.localName;
						state.localmaps=data.localmaps;
						state.times=data.times;

						if (data.times && data.times.length > 0) {
							for (var i = 1; i < data.times.length; i++) {
								var temp = data.times[i];
								headhtml += "<th class='text-right'>20" + temp + "</th>";
								insumhtml += initSummayMonthHtml("in_sum", temp, maps);
								inprincipalhtml += initSummayMonthHtml("in_principal", temp, maps);
								inotherhtml += initSummayMonthHtml("in_other", temp, maps);
								inprincipalRatehtml += initSummayMonthHtml("in_principalRate", temp, maps);
								outsumhtml += initSummayMonthHtml("out_sum", temp, maps);
								outadvhtml += initSummayMonthHtml("out_adv", temp, maps);
								outadvinvoicehtml += initSummayMonthHtml("out_adv_invoice", temp, maps);
								outadvhtmlRate += initSummayMonthHtml("out_advRate", temp, maps);
								outcommissionhtml += initSummayMonthHtml("out_commission", temp, maps);
								outfbahtml += initSummayMonthHtml("out_fba", temp, maps);
								outrefundhtml += initSummayMonthHtml("out_refund", temp, maps);
								outotherhtml += initSummayMonthHtml("out_other", temp, maps);
								convertedtotalhtml += initSummayMonthHtml("converted_total", temp, maps);
								accountconvertedtotalhtml += initSummayMonthHtml("accountconverted_total", temp, maps);
								totalhtml += "<td >" + " " + "</td>";
								linetrhtml += "<td>" + " " + "</td>";
							}
						}
						state.theadtr="<th width='20%' style='text-align:left'>收支项目</th>" + headhtml + "<th class='text-right' >合计</th>";
						loadSummaryHtml(maps, "in_sum", insumhtml, "收入", "bigtxt", "text-red bloder ");
						loadSummaryHtml(maps, "in_principal", inprincipalhtml, "销售额", "", "text-red");
						loadSummaryHtml(maps, "in_other", inotherhtml, "其它收入", "", "text-red");
						loadSummaryHtml(maps, "in_principalRate", inprincipalRatehtml, "销售额同比增长率", "", "text-black");
						loadSummaryHtml(maps, "out_sum", outsumhtml, "支出", "bigtxt", "text-green bloder");
						loadSummaryHtml(maps, "out_adv", outadvhtml, "广告费用(账期)", "", "text-green");
						loadSummaryHtml(maps, "out_adv_invoice", outadvinvoicehtml, "广告费用(发票)", "", "text-green");
						loadSummaryHtml(maps, "out_advRate", outadvhtmlRate, "广告费用占销售额比率", "", "text-green text-black");
						loadSummaryHtml(maps, "out_commission", outcommissionhtml, "FBA订单佣金", "", "text-green");
						loadSummaryHtml(maps, "out_fba", outfbahtml, "FBA交易费用", "", "text-green");
						loadSummaryHtml(maps, "out_refund", outrefundhtml, "订单退款", "", "text-green");
						loadSummaryHtml(maps, "out_other", outotherhtml, "其它支出", "", "text-green");
						loadSummaryHtml(maps, "converted_total", convertedtotalhtml, "结算汇总", "", "text-red text-black");
						loadSummaryHtml(maps, "accountconverted_total", accountconvertedtotalhtml, "期间转账 <span class='fa fa-exclamation-circle light-font' data-toggle='tooltip' data-placement='right' data-original-title='入账时间在该月的账期汇总'></span>", "", "text-green text-black");
						if(maps.in_principal==undefined){
							state.linetr=" <td class='text-center '  colspan='4'><div class='text-center 'style='padding:30;'><img src='./images/systempicture/defaultpage/emptysearch.png'><br><p class='light-font'>没有找到相关数据哦！</p></div></td>";
						}else{
							state.linetr=" <td><span > </span></td>" + linetrhtml + "<td><span  >  &nbsp;</span></td>";
						}
					    state.total="<td class='text-left' ><span class='bigtxt text-red text-black'> 结算</span></td>" + totalhtml + "<td><span> &nbsp;</span></td>";
						state.isload=false;
		}).catch(e=>{
			state.loading=false;
		});
	}
	function initSummayMonthHtml(ftype, temp, maps) {
		var addhtml = "";
		if (maps[ftype] && maps) {
			//$("#" + ftype).show();
			addhtml = "<td class='text-right'>" + outputmoney(maps[ftype][temp]) + "</td>";
			if (ftype == "advfee" || ftype == "itemtax" || ftype == "shiptax" || ftype == "tax") {
				addhtml = "<td class='text-right'>-" + outputmoney(maps[ftype][temp]) + "</td>";
			}
			if (ftype == "in_principalRate" || ftype == "converted_totalRate" || ftype == "out_advRate") {
				if (maps && maps[ftype] && maps[ftype][temp] || maps[ftype][temp] == 0) {
					addhtml = "<td class='text-right'>" + outputmoney(maps[ftype][temp]) + "%</td>";
				} else {
					addhtml = "<td class='text-right'>--</td>";
				}
			}
			if (ftype == "in_other") {
				addhtml = "<td class='text-right'><span id='showOtherin' "
						+ "onclick='showother(this,\"20" + temp
						+ "\");' style='cursor:pointer;'"
						+ "class='icon-otherseyes' ></span>"
						+ outputmoney(maps[ftype][temp]) + "</td>";
			} else if (ftype == "out_other") {
				// var invoiceAmount=0;
				//  state.invoiceData.forEach(items=>{
				// 	 if(items.times.indexOf(temp)>=0){
				// 		 invoiceAmount=items.amount*-1;
				// 	 }
				//  });
				addhtml = "<td class='text-right'><span id='showOtherout' "
						+ "onclick='showother(this,\"20" + temp
						+ "\");' style='cursor:pointer;'"
						+ "class='icon-otherseyes' ></span>"
						+ outputmoney(maps[ftype][temp]) + "</td>";
						//<p>广告发票费:"+outputmoney(invoiceAmount)+"</p>
			}
			return addhtml;
		} else {
	        //$("#out_advRate,#total").hide();
			return "";
		}
	}
	function loadSummaryHtml(maps, ftype, addhtml, trname, trclass, lastclass) {
		if (maps[ftype]) {
			if (maps[ftype]["all"]) {
				if (ftype == "advfee" || ftype == "itemtax" || ftype == "shiptax" || ftype == "tax") {
					addhtml += "<td class='text-right'><span class='" + lastclass + "'>-" + outputmoney(maps[ftype]["all"]) + "</span></td>";
				} else if (ftype == "in_principalRate" || ftype == "converted_totalRate" || ftype == "out_advRate") {
					addhtml += "<td class='text-right'><span class='" + lastclass + "'>" + outputmoney(maps[ftype]["all"]) + "%</span></td>";
				} else {
					addhtml += "<td class='text-right'><span class='" + lastclass + "'>" + outputmoney(maps[ftype]["all"]) + "</span></td>";
				}
			}
			var htmls="<td class='text-left' ><span class='" + trclass + "' >" + trname + "</span></td>" + addhtml;
			if(ftype=="in_sum"){
				state.in_sum=htmls;
			}
			if(ftype=="in_principal"){
				state.in_principal=htmls;
			}
			if(ftype=="in_other"){
				state.in_other=htmls;
			}
			if(ftype=="in_principalRate"){
				state.in_principalRate=htmls;
			}
			if(ftype=="out_sum"){
				state.out_sum=htmls;
			}
			if(ftype=="out_adv"){
				state.out_adv=htmls;
			}
			if(ftype=="out_adv_invoice"){
				state.out_adv_invoice=htmls;
			}
			if(ftype=="out_advRate"){
				state.out_advRate=htmls;
			}
			if(ftype=="out_commission"){
				state.out_commission=htmls;
			}
			if(ftype=="out_fba"){
				state.out_fba=htmls;
			}
			if(ftype=="out_refund"){
				state.out_refund=htmls;
			}
			if(ftype=="out_other"){
				state.out_other=htmls;
			}
			if(ftype=="converted_total"){
				state.converted_total=htmls;
			}
			if(ftype=="accountconverted_total"){
				state.accountconverted_total=htmls;
			}
		}
	}
	function outputmoneyF(value,row,index){
		value=parseFloat(value)*-1;
		result=outputmoney(value);
		return result;
	}
	function outputmoney(number) {  
	   if(number==0)return "0";
	   if (isNaN(number) || number == "") return "";  
	   number = Math.round(number * 100) / 100;  
	   if (number < 0)  
	       return '-' + outputdollars(Math.floor(Math.abs(number) - 0) + '') + outputcents(Math.abs(number) - 0);  
	   else  
	       return outputdollars(Math.floor(number - 0) + '') + outputcents(number - 0);  
	} 
	
	//格式化金额   
	function outputdollars(number) {  
	   if (number==undefined||isNaN(number) || number == "") return "";  
	   if ( number == 0) return "0";  
	   number=number+"";
	   if (number.length <= 3)  
	       return (number == '' ? '0' : number);  
	   else {  
	       var mod = number.length % 3;  
	       var output = (mod == 0 ? '' : (number.substring(0, mod)));  
		   var i=0;
	       for (i = 0; i < Math.floor(number.length / 3); i++) {  
	           if ((mod == 0) && (i == 0))  
	               output += number.substring(mod + 3 * i, mod + 3 * i + 3);  
	           else  
	               output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);  
	       }  
	       return (output);  
	   }  
	} 
	function outputcents(amount) {  
	   amount = Math.round(((amount) - Math.floor(amount)) * 100);  
	   return (amount < 10 ? '.0' + amount : '.' + amount);  
	}  
	 
	function changedateMonth(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	const exportToExcel = () => {
	  // 从表格导出
	  var data=groupRef.value.getData();
	  var groupname=data.group&&data.group!=''&&data.group.name?data.group.name:'全部店铺';
	  var marketname=data.market&&data.market!=''&&data.market.name?data.market.name:'全部站点';
	  var fromDate=state.queryParams.fromDate;
	  var toDate=state.queryParams.endDate;
	  const worksheet = XLSX.utils.table_to_sheet(tableRef.value)
	  const workbook = XLSX.utils.book_new()
	  XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1")
	  XLSX.writeFile(workbook, groupname+"-"+marketname+"-"+fromDate+"到"+toDate+"月度结算.xlsx")
	}

	function getGroup(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		handleQuery();
	}  
</script>

<style scoped >
	.localFee{
		background:#f9fbff;
	}
	.dark .localFee{
		background:#343434;
	}
	.main {
		-ms-transform: translate(0, 0);
		transform: translate(0, 0);
		-ms-transform: translate(0, 0); /* IE 9 */
		-moz-transform: translate(0, 0); /* Firefox */
		-webkit-transform: translate(0, 0); /* Safari 和 Chrome */
		-o-transform: translate(0, 0); /* Opera */
	}

	.lgfont {
		font-size: 24px;
	}
	
	.menu-td-color {
		background-color: #F9F9F9;
		font-size: 12px;
	}
	.text-left{
		text-align: left !important;
	}
	 .text-right{
		 text-align: right !important;
	 }
	.header_foot {
		vertical-align: middle;
		border: 1px solid #f4f4f4;
		padding: 12 10 12 6;
		margin-left: 15px;
		margin-right: 15px;
		margin-top: 15px;
		background-color: #F9F9F9;
	}
	.theadercolor{
	color: #666;
	}
	.mycol {
		cursor: pointer;
		float: left;
		margin: 8 5% 8 0;
		display: inline-block;
		font-size: 12px;
	}
	
	.line-btn {
		border: 1px solid #dcdcdc;
		padding: 3px 7px;
		margin: 4px 15px;
	}
	
	.activeStatus {
		color: #ffa400 !important;
	}
	
	.regions {
		margin-left: 8px;
		border-radius: 10px;
		border: 1px solid #bbb;
		padding: 1px 4px;
		font-size: 12px;
		color: #bbb;
	}
	
	.alldata {
		padding: 15;
	}
	
	.allpad {
		height: 100px;
		border-radius: 4px;
		border: 1px solid #eee;
		padding: 20px;
		text-align: center;
		cursor: pointer;
	}
	
	.allpad:hover {
		box-shadow: 0px 2px 12px rgba(0, 0, 0, 0.1);
		-webkit-transition: box-shadow .30s ease;
		transition: box-shadow .30s ease;
	}
	
	.num-align {
		display: inline-block;
		margin-left: 6px;
		text-align: left;
	}
	
	.allprice {
		font-size: 24px;
		font-weight: 500;
	}
	
	#salesChart>.activechart {
		border-bottom: 3px solid #ffa400;
	}
	
	#refundChart>.activechart {
		border-bottom: 3px solid #31CF82;
	}
	
	#salenumChart>.activechart {
		border-bottom: 3px solid #7aa5da;
	}
	
	#ordernumChart>.activechart {
		border-bottom: 3px solid #FF979F;
	}
	
	td>.th-inner {
		background: #fafafa;
		color: #333 !important;
		font-size: 14px !important;
	}
	
	.bigtxt {
		font-weight: bold;
		font-size: 16px;
	}
	
	.mgn10 {
		margin-right: 10px;
		margin-bottom: 10;
		font-size: 12px
	}
	
	.bloder {
		font-weight: bold;
	}
	
	.smallbox {
		position: relative;
	}
	
 
	
	.smallbox {
		font-size: 12px;
		background: #fff;
		margin-left: 15px;
		padding: 16px 28px 10px 30px;
		min-width: 200px;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
		border-radius: 4px;
		color: #fff;
	}
	
	.cardbox {
		font-size: 12px;
		background-color: #fff;
		padding: 16px 28px 10px 30px;
		min-width: 300px;
		cursor:pointer;
		border-radius: 4px;
		margin-bottom:15px;
	    box-shadow:  0px 1px 10px rgba(159,159,159,0.4);
	}
 	.finsum{
	    background: url(@/assets/image/pages/finance/107.png) no-repeat 96% 50%;
	}
	.finacc{
	    background: url(@/assets/image/pages/finance/104.png) no-repeat 96% 50%;
	}
	.finsett{
	    background: url(@/assets/image/pages/finance/103.png) no-repeat 96% 50%;
	}
	.finsett.active{
	     color:#fff; 
	    background:#ff9300 url(@/assets/image/pages/finance/106.png) no-repeat 96% 50%;
	}
	.finacc.active{
	    color:#fff; 
	    background:#ff9300 url(@/assets/image/pages/finance/105.png) no-repeat 96% 50%;
	}  
	
	
	.cardbox.active .light-font{
	background:#ff9300;
	color:#fff; 
	}
  	i.icon-down-order, span.icon-down-order {
		background: url(@/assets/image/pages/purchase/down_out.png) no-repeat
			center center !important;
		padding: 8px 12px 3px 12px !important;
	}
	 
	.fileinput {
		display: inline-block;
		cursor: pointer !important;
	}
	
	.file {
		position: relative;
		display: inline-block;
		background: #fff;
		border-radius: 4px;
		border: 2px dashed #eee;
		width: 100%;
		overflow: hidden;
		color: #666;
		float: left;
		text-decoration: none;
		text-align: center;
		padding: 20px;
	}
	
	.file input {
		position: absolute;
		font-size: 100px;
		right: 0;
		top: 0;
		opacity: 0;
	}
	
	.file:hover {
		background: #fafafa;
		text-decoration: none;
	}
	
	.fileerrorTip {
		color: #dd4b39;
		padding-top: 6px;
		vertical-align: middle;
		padding-left: 30px;
	}
	
	.showFileName {
		color: #333;
		padding-top: 10px;
		vertical-align: middle;
		text-align: center;
	}
	
	.precompent {
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
		float: left;
		width: 115px;
		border-right: none;
	}
	
	#select-col li {
		position: relative;
		min-width: 110px;
		font-size: 12px;
		text-align: left;
		line-height: 30px;
		cursor: pointer;
		color: #333;
		padding-left: 20px;
	}
	
	#select-col li:hover {
		background: #eee !important;
	}
	
	.mysettable thead tr th {
		border-bottom: 1px solid #eee !important;
		border-top: 1px solid #eee !important;
	}
	
	#select-col li span {
		color: #666;
		position: absolute;
		left: 16;
		bottom: 10;
	}
	
	.mysettable {
		text-align: right;
	}
	.mysettable tr td,.mysettable tr th{
		border-bottom:1px solid #dedede;
	}
	.overallTable td,.overallTable th{ text-align: right;}
	.overallTable tr>td:first-child,.overallTable tr>th:first-child{ text-align: center;}
	.overallTable tr>td:last-child,.overallTable tr>th:last-child{ padding-right:24px !important;}
	.overallTable tr>th:last-child .th-inner{ padding-right:0 !important;}
	#overallTable{ color:#666;}
	.trstore{color:#333;border-left:4px solid #ffa900;background:#f3f3f3 !important}
	.no-records-found td{ text-align: center;}
	.has-footer{padding-bottom:0px !important; }
	.tooltip-inner {
	  max-width:300px;
	  text-align:left;
	}
</style>
 <style>
	 .sumRow td{
	 	font-weight:700;
	    background-color:#f5f5f5;
	 }
	 .dark .sumRow td{
	 	font-weight:700;
	    background-color:#434343;
	 }
	 .text-right{
		 text-align: right!important;
	 }
 </style>