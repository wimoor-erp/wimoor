<template >
	<el-dialog
	v-model="visable"
	width="90%"
	top="1vh"
	title="月度业绩">
		<div class="con-header">
			<el-row>
				<el-space>
				 <Datepicker type="monthrange"   :shortIndex="1"  ref="datepickers" @changedate="changedate" />
				</el-space>
			</el-row>
		</div>
		<div class="con-body">
 	 <!-- height="calc(100vh - 348px)"  -->
	 <div class="settlementTable">
	<GlobalTable
	:defaultSort="{ prop: 'order_amount', order: 'descending' }" 
	 show-summary
	 :summary-method="getSummaries"
	 height="calc(60vh)"
	 ref="globalTableRef"
	 @selectionChange='selectChange'
	 :tableData="tableData"
	 @loadTable="loadTableData"
	  >
	<template #field>
			 
			<el-table-column   align="center" label="月份" min-width="200px" header-align="center"   fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #default="scope">
						<span  >	{{dateMonthFormat(scope.row.posted_date)}}</span>
				</template>
			</el-table-column>
	 <el-table-column label="销售额"  sortable="custom"  prop="principal" width="110">
	 	<template #default="scope">
	 		<span v-if="scope.row.principal">{{scope.row.principal}}</span><span v-else>0</span>
	 	</template>
	 </el-table-column>
	 <el-table-column label="销量" sortable="custom"  prop="sales" width="75"/>
	 <el-table-column label="订单" sortable="custom"  prop="order_amount" width="75"/>
	 <el-table-column label="平均售价"  prop="avgprice" width="80"/>
	 <el-table-column label="广告销售额" sortable="custom"    prop="rpt_adv_sales" width="115"/>
	 <el-table-column label="广告销量" sortable="custom"  prop="rpt_adv_units" width="100"/>
	 <el-table-column label="退款数量" sortable="custom" prop="refundsales" width="100"/>
	 <el-table-column label="退款金额" sortable="custom"   prop="refund" width="100"/>
	 <el-table-column label="退款率" sortable="custom"  prop="refundrate" width="85"/>
	 <el-table-column label="FBA费用"   prop="fbafee" width="100"/>
	 <el-table-column label="销售佣金"    prop="commission" width="100"/>
	 <el-table-column label="运费"   prop="shipping" width="90"/>
	 <el-table-column label="促销费"    prop="promotion" width="100"/>
	 <el-table-column label="其它收支"    prop="otherfee" width="90"/>
	 <el-table-column label="SKU结算"    width="100"  sortable="custom"   prop="setincome" />
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_storage_fee">
	   <template #header>
	 		<p class="l-h-none ">店铺分摊</p>
	 		<p class="l-h-none">仓储费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_long_storage_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">长期仓储费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_coupon_redemption_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">折扣券</p>
	   </template>	
	 </el-table-column>			
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_adv_spend_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">广告花费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_reserve_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">预留金</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_reimbursement_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">赔偿金</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_shop_other_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">其它费用</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="店铺结算"   width="100"  sortable="custom"   prop="income" />
	 <el-table-column label="市场费用"    width="100"  prop="profit_marketfee" />
	 <el-table-column label="VAT税费"    width="100"  prop="profit_vat" />
	 <el-table-column label="所得税"    width="100"   prop="profit_companytax" />
	 <el-table-column label="关税"    width="100"   prop="profit_customstax" />
	 <el-table-column label="汇率损耗"    width="100"  prop="profit_exchangelost" />
	 <el-table-column label="固定费用"    width="100"  prop="profit_lostrate" />
	 <el-table-column label="其它费用"    width="100"   prop="profit_otherfee" />
	 <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_storage_fee">
	   <template #header>
	 		<p class="l-h-none ">SKU报表</p>
	 		<p class="l-h-none">仓储费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_long_storage_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">长期仓储费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_adv_spend_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">广告花费</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_reimbursements_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">赔偿金</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="fin_sum_fee">
	   <template #header>
	 		<p class="l-h-none ">&nbsp;</p>
	 		<p class="l-h-none">其它费用</p>
	   </template>	
	 </el-table-column>	
	 <el-table-column label=" 预估运费"    width="100" sortable="custom"   prop="profit_local_shipmentfee" />
	 <el-table-column label="采购成本"  sortable="custom"    width="100"  prop="local_price" />
	 <el-table-column label="自定义费用"    width="100"   prop="local_other_cost" />
	 <el-table-column label="退税"    width="60"   prop="local_return_tax" />
	 <el-table-column label="毛利润" fixed="right"   width="110" sortable="custom"   prop="profit" >
	 	<template #header>
	 	<el-space :size="4">
	 	  <span>毛利润</span>
	 	   <el-tooltip
	 	          effect="dark"
	 	          content=""
	 	          placement="top-start"
	 	        >
	 			 <template #content> 
	 			 <div>利润 = SKU结算收入 -{{profittooltip}}</div>
	 			  </template>
	 	          <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
	 	        </el-tooltip>
	 	</el-space>
	 	</template>
	 	<template #default="scope">
	 	 <span :class="scope.row.profit<0?'text-red':''">{{scope.row.profit}}</span>
	     </template>
	 </el-table-column>	
	 <el-table-column label="毛利润率"  fixed="right" width="100"   prop="profitrate" />
			 </template>
	</GlobalTable>
	</div>
	</div>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,Histogram,InfoFilled} from '@element-plus/icons-vue';
	import {dateMonthFormat,getCurrencyMark,outputmoney,formatFloat} from '@/utils/index.js';
	import settlementFormulaApi from '@/api/amazon/finances/settlementFormulaApi.js';
	import settlementMonthSummaryApi from "@/api/amazon/finances/settlementMonthSummaryApi.js";
    import Owner from '@/components/header/owner.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import Currency from '@/components/header/currency.vue';
	let props = defineProps({  activeName:"", });
	const { activeName} = toRefs(props);
	const state = reactive({
		tableData:{ records:[ { } ],total:1},
		param:{},
		visable:false,
		profittooltip:"",
		summary:{},
	})
	const globalTableRef=ref();
	const mrRef=ref();
	const ownerRef=ref();
	const{
		tableData,summary,profittooltip,visable
	}=toRefs(state)
	function parseFloatValue(value){
			 if(value)return parseFloat(value);
			 else return 0.0;
	}
		function monthSales(){
			mrRef.value.show() 
		}
		onMounted(()=>{
			settlementFormulaApi.loadformula().then((res)=>{
				state.profittooltip=res.data.formulaData;
			});
		})
	function formatData(row){
			 row.principal=outputmoney(row.principal);
			 row.avgprice=outputmoney(row.avgprice);
		     row.refund=outputmoney(row.refund);
			 row.fbafee=outputmoney(row.fbafee);
			 row.commission=outputmoney(row.commission);
			 row.shipping=outputmoney(row.shipping);
	         row.promotion=outputmoney(row.promotion);
			 row.otherfee=outputmoney(row.otherfee);
	         row.setincome=outputmoney(row.setincome);
			 row.share_storage_fee=outputmoney(row.share_storage_fee);
			 row.share_long_storage_fee=outputmoney(row.share_long_storage_fee);
			 row.share_adv_spend_fee=outputmoney(row.share_adv_spend_fee);
			 row.share_coupon_redemption_fee=outputmoney(row.share_coupon_redemption_fee);
			 row.share_reserve_fee=outputmoney(row.share_reserve_fee);
			 row.share_shop_other_fee=outputmoney(row.share_shop_other_fee);
			 row.income=outputmoney(row.income);
			 row.profit_local_shipmentfee=outputmoney(row.profit_local_shipmentfee);
			 row.profit_marketfee=outputmoney(row.profit_marketfee);
			 row.profit_vat=outputmoney(row.profit_vat);
			 row.profit_companytax=outputmoney(row.profit_companytax);
			 row.profit_customstax=outputmoney(row.profit_customstax);
			 row.profit_exchangelost=outputmoney(row.profit_exchangelost);
			 row.profit_lostrate=outputmoney(row.profit_lostrate);
			 row.profit_otherfee=outputmoney(row.profit_otherfee);
			 row.local_price=outputmoney(row.local_price);
			 row.local_other_cost=outputmoney(row.local_other_cost);
			 row.local_return_tax=outputmoney(row.local_return_tax);
			 row.profit=outputmoney(row.profit);
			 	 
	}
	function loadTableData(param){
		if(props.activeName=="parentasin"){
			if(!param.sort){
				 param.sort="subnum";
				 param.order="desc";
			}
		}else{
			if(param.sort=="subnum"){
				param.sort="principal";
			}
		}
		settlementMonthSummaryApi.list(param).then(res=>{
			if(res.data&&res.data.records){
				res.data.records.forEach(row=>{
					row.avgprice=outputmoney(parseFloat(row.principal)/parseFloat(row.sales));
					if(parseFloat(row.principal)>0.000001){
					row.profitrate=formatFloat(parseFloat(row.profit)*100/parseFloat(row.principal))+"%";
					}else{
						row.profitrate="0.0%";
					}
					 formatData(row);
				})
			}
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
			if(param.currentpage==1){
				if(res.data.records&&res.data.records.length>0){
					state.summary=res.data.records[0].summary;
				}else{
					state.summary={};
				}
			}
		})
	}
	function show(param){
		param.fromDate=state.param.fromDate;
		param.endDate=state.param.endDate;
		state.param=param;
		if(!ownerRef.value){
			var timer=setTimeout(function(){
				handleQuery();
			    clearTimeout(timer);
			},200);
		}else{
			handleQuery();
		}
		
		state.visable=true;
	}
	function changeOwner(owner){
		state.param.owner=owner;
		handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.param.fromDate=dates.start;
		state.param.endDate=dates.end;
		handleQuery();
	}
	function handleQuery(){
			globalTableRef.value.loadTable(state.param);
	}
	 defineExpose({show})
	 function getSummaries({columns,data}){
	 	var arr = ["合计"];
		var startindex=1;
	 	columns.forEach((item,index)=>{
	 		if(index>=startindex){
	 			 var fee=0.0;
	 				 fee=state.summary[item.property];
	 				 if(item.property=='sales'||item.property=='order_amount'||item.property=='rpt_adv_units'||item.property=='refundsales'){
	 					 arr[index]=fee;
	 				 }else if(item.property=='avgprice'||item.property=='refundrate'||item.property=='profitrate'){
	 					  arr[index]='-';
	 				 }else{
	 					arr[index]=outputmoney(fee);
	 				 }
	 		}
	 	})
	 	return  arr
	 }
	 function handleExpandChange(row,expandedRows){
		 var param={pagesize:1000000,currentpage:1,
		 charttype:"month",parentasin:row.parentasin?row.parentasin:"#",
		 endDate:state.param.endDate,fromDate:state.param.fromDate,
		 currency:state.param.currency,
		 groupid:row.groupid,marketplaceid:row.marketplaceid};
		 settlementMonthSummaryApi.list(param).then(res=>{
		 	if(res.data&&res.data.records){
		 		res.data.records.forEach(row=>{
		 			row.avgprice=outputmoney(parseFloat(row.principal)/parseFloat(row.sales));
		 			if(parseFloat(row.principal)>0.000001){
		 			row.profitrate=formatFloat(parseFloat(row.profit)*100/parseFloat(row.principal))+"%";
		 			}else{
		 				row.profitrate="0.0%";
		 			}
		 			 formatData(row);
		 		})
		 	}
		 	row.extendData=res.data.records;
			});
	}
	function rowClassName({ row, rowIndex }){
		var clasz=""
		if(row.subnum>0||row.subnum>"0"){
			clasz= "hasChildren";
		}else{
			clasz= "NonChildren";
		}
		if(row.isrepeat){
			clasz= "isrepeat "+clasz;
		}else{
			clasz= "norepeat "+clasz;
		}
		return clasz;
	}
</script>

<style>
	th.yellow-column {
		background-color: var(--el-color-warning-light-9)!important;
	}
	th.green-column{
		background-color: var(--el-color-success-light-9)!important;
	}
	.text-gray{
		color: #999999;
	}
	.list-item p{
		line-height: 32px;
	}
	.img80{
			 width:80px;
			 length:80px;
	}
	.width60{
			 width:60px;
	}
	.width50{
			 width:50px;
	}
	.width40{
			 width:40px;
	}
	.pull-left{
			 float:left;
	}
	.NonChildren .el-table__expand-icon{
		display: none !important;
	}
</style>
  