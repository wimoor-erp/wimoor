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
	 
			<el-table-column label="销售情况"  header-align="center"   width="170"  sortable="custom"   prop="principal" >
				<template #default="scope">
					<div><div class="text-green pull-left text-right width60"> 销售额：</div>
					 <span v-if="scope.row.principal">{{scope.row.principal}}</span><span v-else>0</span></div>
					 <div v-if="scope.row.rpt_adv_sales"><div class="font-extraSmall pull-left text-right width60"> 广告销售：</div> {{scope.row.rpt_adv_sales}}</div>
					 <div v-if="scope.row.sales"><div class="font-extraSmall pull-left text-right width60"> 销量：</div> {{scope.row.sales}}</div>
					 <div v-if="scope.row.rpt_adv_units"><div class="font-extraSmall pull-left text-right width60"> 广告销量：</div> {{scope.row.rpt_adv_units}}</div>
					 <div v-if="scope.row.order_amount"><div class="font-extraSmall pull-left text-right width60"> 订单：</div> {{scope.row.order_amount}}</div>
					 <div v-if="scope.row.avgprice"><div class="font-extraSmall pull-left text-right width60"> 平均售价：</div> {{scope.row.avgprice}}</div>
				</template>
			 </el-table-column>
			 <el-table-column label="退款情况" header-align="center"    width="140"  sortable="custom"   prop="refund" >
			 	<template #default="scope">
			 		 <div><div class="font-extraSmall pull-left text-right width50"> 退款：</div> {{scope.row.refund}}</div>
			 		 <div v-if="scope.row.refundsales"><div class="font-extraSmall pull-left text-right width50"> 数量：</div> {{scope.row.refundsales}}</div>
			 		 <div v-if="scope.row.refundrate"><div class="font-extraSmall pull-left text-right width50"> 退款率：</div> {{scope.row.refundrate}}</div>
			 	</template>
			  </el-table-column>
			  <el-table-column label="平台费用"  header-align="center"   width="160"  sortable="custom"   prop="fbafee" >
			  	<template #default="scope">
			  		 <div><div class="font-extraSmall pull-left text-right width50"> FBA费：</div> {{scope.row.fbafee}}</div>
			  		 <div><div class="font-extraSmall pull-left text-right width50"> 佣金：</div> {{scope.row.commission}}</div>
			  		 <div v-if="scope.row.shipping!='0'"><div class="font-extraSmall pull-left text-right width50"> 运费：</div> {{scope.row.shipping}}</div>
					 <div v-if="scope.row.promotion!='0'"><div class="font-extraSmall pull-left text-right width50"> 促销：</div> {{scope.row.promotion}}</div>
					 <div ><div class="font-extraSmall pull-left text-right width50"> 其它费用：</div> {{scope.row.otherfee}}</div>
			  	</template>
			   </el-table-column>
			   <el-table-column label="SKU结算" header-align="center"   width="110"  sortable="custom"   prop="setincome" />
			   <el-table-column label="店铺分摊"   header-align="center"  width="170"  sortable="custom"   prop="share_storage_fee" >
			   	<template #default="scope">
			   		 <div><div class="font-extraSmall pull-left text-right width60"> 仓储费：</div> {{scope.row.share_storage_fee}}</div>
			   		 <div v-if="scope.row.share_long_storage_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 长期仓储：</div> {{scope.row.share_long_storage_fee}}</div>
			   		 <div v-if="scope.row.share_adv_spend_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 广告：</div> {{scope.row.share_adv_spend_fee}}</div>
					 <div v-if="scope.row.share_coupon_redemption_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 折扣券：</div> {{scope.row.share_coupon_redemption_fee}}</div>
					 <div v-if="scope.row.share_reserve_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 预留金：</div> {{scope.row.share_reserve_fee}}</div>
					 <div v-if="scope.row.share_reimbursement_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿金：</div> {{scope.row.share_reimbursement_fee}}</div>
					 <div v-if="scope.row.share_shop_other_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它费用：</div> {{scope.row.share_shop_other_fee}}</div>
			   	</template>
			    </el-table-column>
				<el-table-column label="店铺结算"  header-align="center"   width="120"  sortable="custom"   prop="income" >
				 </el-table-column>
				 <el-table-column label="VAT税费"  header-align="center"   width="120"  sortable="custom"   prop="profit_vat" >
				  </el-table-column>
				 <el-table-column label="本地预估"   header-align="center"  width="160"  sortable="custom"   prop="profit_local_shipmentfee" >
				 	<template #default="scope">
				 		 <div><div class="font-extraSmall pull-left text-right width60"> 运费：</div> {{scope.row.profit_local_shipmentfee}}</div>
				 		 <div v-if="scope.row.profit_marketfee!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 市场费用：</div> 
							 {{scope.row.profit_marketfee}}
					      </div>
						 <div v-if="scope.row.profit_companytax!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 所得税：</div> 
							 {{scope.row.profit_companytax}}
						  </div>
						 <div v-if="scope.row.profit_customstax!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 关税：</div> 
							 {{scope.row.profit_customstax}}
						  </div>
						 <div v-if="scope.row.profit_exchangelost!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 汇率损耗：</div> 
							 {{scope.row.profit_exchangelost}}
						 </div>
						 <div v-if="scope.row.profit_lostrate!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 固定费用：</div> 
							 {{scope.row.profit_lostrate}}
						 </div>
						 <div v-if="scope.row.profit_otherfee!='0'">
							 <div class="font-extraSmall pull-left text-right width60"> 其它费用：</div> 
							 {{scope.row.profit_otherfee}}
						</div>
				 	</template>
				  </el-table-column>
				  <el-table-column label="报表费用"   header-align="center"  width="160"  sortable="custom"   prop="rpt_storage_fee" >
				  	<template #default="scope">
				  		 <div><div class="font-extraSmall pull-left text-right width60"> 仓储费：</div> {{scope.row.rpt_storage_fee}}</div>
				  		 <div v-if="scope.row.rpt_long_storage_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 长期仓储：</div> {{scope.row.rpt_long_storage_fee}}</div>
				  		 <div v-if="scope.row.rpt_adv_spend_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 广告：</div> {{scope.row.rpt_adv_spend_fee}}</div>
						 <div v-if="scope.row.rpt_reimbursements_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿：</div> {{scope.row.rpt_reimbursements_fee}}</div>
				  		 <div v-if="scope.row.fin_sum_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它自定义：</div> {{scope.row.fin_sum_fee}}</div>
				  	</template>
				   </el-table-column>
				  <el-table-column label="本地费用"  header-align="center"   width="170"  sortable="custom"   prop="local_price" >
				  	<template #default="scope">
				  		 <div><div class="font-extraSmall pull-left text-right width40"> 采购：</div> {{scope.row.local_price}}</div>
				  		 <div  v-if="scope.row.local_other_cost!='0'"><div class="font-extraSmall pull-left text-right width40"> 其它：</div> {{scope.row.local_other_cost}}</div>
				  		 <div  v-if="scope.row.local_return_tax!='0'"><div class="font-extraSmall pull-left text-right width40"> 退税：</div> {{scope.row.local_return_tax}}</div>
				  	</template>
				   </el-table-column>
			<el-table-column label="利润"   fixed="right"  header-align="center"  width="170" sortable="custom"   prop="profit" >
				<template #header>
				<el-space :size="4">
				  <span>利润</span>
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
					 <div><div class="font-extraSmall pull-left text-right width60"> 利润：</div>{{scope.row.profit}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 利润率：</div>{{scope.row.profitrate}}</div>
			    </template>
			</el-table-column>	
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
	 		if(index>=startindex&&index<columns.length-1){
	 			   var fee=0.0;
	 				 if(item.property=='fbafee'){
	 					 fee=fee+parseFloatValue(state.summary["commission"]);
	 					 fee=fee+parseFloatValue(state.summary["fbafee"]);
	 					 fee=fee+parseFloatValue(state.summary["promotion"]);
	 					 fee=fee+parseFloatValue(state.summary["shipping"]);
	 					 fee=fee+parseFloatValue(state.summary["otherfee"]);
	 				 }else if(item.property.indexOf('share')==0){
	 					 fee=fee+parseFloatValue(state.summary["share_storage_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["share_long_storage_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["share_adv_spend_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["share_coupon_redemption_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["share_reserve_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["share_shop_other_fee"]);
	 				 }else if(item.property=='profit_local_shipmentfee'){
	 					 fee=fee+parseFloatValue(state.summary["profit_local_shipmentfee"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_marketfee"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_vat"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_companytax"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_customstax"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_exchangelost"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_lostrate"]);
	 					 fee=fee+parseFloatValue(state.summary["profit_otherfee"]);
	 				 }else if(item.property=='rpt_storage_fee'){
	 					 fee=fee+parseFloatValue(state.summary["rpt_storage_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["rpt_long_storage_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["rpt_reimbursements_fee"]);
	 					 fee=fee+parseFloatValue(state.summary["fin_sum_fee"]);
	 				 }else{
	 					 fee=state.summary[item.property];
	 				 }
	 			 arr[index]=outputmoney(fee);
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
	.settlementTable .el-table__footer .cell {
		text-align:center!important;
	}
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
  