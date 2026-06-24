<template >
	<div class="payment-profit-report">
	<el-table :data="extendData" >
			<el-table-column  label="商品信息"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #default="scope">
					<div class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="product-img"    />
					<img v-else :src="$require('empty/noimage40.png')"  class="product-img"    />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.asin}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						<div><span class="font-extraSmall">{{scope.row.groupname}}-{{scope.row.marketname}}</span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
            <el-table-column label="销售额"  width="120"  sortable="custom"   prop="principal" />
			<el-table-column label="销量"  width="80"  sortable="custom"   prop="sales" />
			<el-table-column label="订单量"  width="90"  sortable="custom"   prop="order_amount" />
			<el-table-column label="平均售价"  width="80"     prop="avgprice" />
			<el-table-column label="广告销售额"  width="120"  sortable="custom"    prop="rpt_adv_sales" />
			<el-table-column label="广告销量"  width="100"    sortable="custom"  prop="rpt_adv_units" />
			<el-table-column label="广告销量"  width="100"    sortable="custom"  prop="rpt_adv_units" />
			<el-table-column label="退款数量"  width="100"    sortable="custom"  prop="refundsales" />
			<el-table-column label="退款金额"  width="100"    sortable="custom"  prop="refund" />
			<el-table-column label="退款率"  width="90"    sortable="custom"  prop="refundrate" />
			<el-table-column label="FBA费用"   prop="fbafee" width="100"/>
			<el-table-column label="销售佣金"    prop="commission" width="100"/>
			<el-table-column label="买家运费"   prop="shipping" width="90"/>
			<el-table-column label="促销费"    prop="promotion" width="100"/>
			<el-table-column label="其它收支"    prop="otherfee" width="90"/>
			<el-table-column label="SKU结算" header-align="center"   width="100"  sortable="custom"   prop="setincome" />
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
			<el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_disposal_fee">
			  <template #header>
					<p class="l-h-none ">&nbsp;</p>
					<p class="l-h-none">移除费</p>
			  </template>	
			</el-table-column>	
			<el-table-column label="店铺分摊" label-class-name="bg-group-y" width="100" prop="share_shop_other_fee">
			  <template #header>
					<p class="l-h-none ">&nbsp;</p>
					<p class="l-h-none">其它收支</p>
			  </template>	
			</el-table-column>	
			<el-table-column label="店铺结算"  header-align="center"   width="100"  sortable="custom"   prop="income" />
			<el-table-column label="VAT税费"   width="100"  prop="profit_vat" ></el-table-column>
	       <el-table-column label="市场费用" label-class-name="bg-group-y"   width="100"  prop="profit_marketfee" >
		   <template #header>
		   		<p class="l-h-none ">预估费用</p>
		   		<p class="l-h-none">市场费用</p>
		     </template>	
		   </el-table-column>	
	       <el-table-column label="所得税" label-class-name="bg-group-y"   width="100"   prop="profit_companytax" >
		   <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">所得税</p>
		     </template>	
		   </el-table-column>
	       <el-table-column label="关税"  label-class-name="bg-group-y"  width="100"   prop="profit_customstax" >
		   <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">关税</p>
		     </template>	
		   </el-table-column>
	       <el-table-column label="汇率损耗"  label-class-name="bg-group-y"  width="100"  prop="profit_exchangelost" >
		   <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">汇率损耗</p>
		     </template>	
		   </el-table-column>
	       <el-table-column label="固定费用"  label-class-name="bg-group-y"  width="100"  prop="profit_lostrate" >
		   <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">固定费用</p>
		     </template>	
		   </el-table-column>
	       <el-table-column label="其它费用"  label-class-name="bg-group-y"  width="100"   prop="profit_otherfee">
		   <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">其它成本</p>
		     </template>	
		   </el-table-column>
		    <el-table-column label=" 预估运费"    width="100" sortable="custom"   prop="profit_local_shipmentfee" />
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
		   <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_disposal_fee">
		     <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">移除费</p>
		     </template>	
		   </el-table-column>	
		   <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_disposal_units">
		     <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">移除数</p>
		     </template>	
		   </el-table-column>
	       <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="fin_sum_fee">
	         <template #header>
	       		<p class="l-h-none ">&nbsp;</p>
	       		<p class="l-h-none">自定义费用</p>
	         </template>	
	       </el-table-column>	
	      
	       <el-table-column label="采购成本"  sortable="custom"    width="100"  prop="local_price" />
	       <el-table-column label="其它采购成本"    width="100"   prop="local_other_cost" />
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
	</el-table>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDownBold,Download,Upload} from '@element-plus/icons-vue';
	import Child from"./child.vue"
	import {dateFormat,getCurrencyMark,outputmoney,formatFloat} from '@/utils/index.js';
	import settlementMonthSummaryApi from "@/api/amazon/finances/settlementMonthSummaryApi.js";
	let props = defineProps({  extendData:[], });
	const { extendData} = toRefs(props);
</script>

<style >
	.payment-profit-report th.yellow-column {
		background-color: var(--el-color-warning-light-9)!important;
	}
	.payment-profit-report th.green-column{
		background-color: var(--el-color-success-light-9)!important;
	}
	.payment-profit-report .text-gray{ 
		color: #999999;
	}
	.payment-profit-report .list-item p{
		line-height: 32px;
	}

	.payment-profit-report .expand-table .el-table__expanded-cell{
		padding:0px;
	}
	.payment-profit-report .expand-table .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.payment-profit-report .el-table__expanded-cell .table-edit-flex{
		text-align:center;
		display: block;
	}
	.payment-profit-report .expand-table .el-table__expanded-cell .el-table__inner-wrapper::before{background-color:var(--el-fill-color-lighter)}
</style>
 