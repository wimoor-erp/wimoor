<template >
	<el-table :data="extendData">
			<el-table-column  label="平台ASIN" header-align="center"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #default="scope">
					<div class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="img-80"  width="80" height="80"  />
					<img v-else :src="$require('empty/noimage40.png')"  class="img-80"  width="80" height="80"  />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.asin}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						 <div><span class="font-extraSmall"> 店铺：{{scope.row.groupname}} 站点：{{scope.row.marketname}}</span></div>
						 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}} </span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column label="销售情况"  header-align="center"   width="150"  sortable="custom"   prop="principal" >
				<template #default="scope">
					 <div><div class="text-green pull-left text-right width60"> 销售额：</div> {{scope.row.principal}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 广告销售：</div> {{scope.row.rpt_adv_sales}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 销量：</div> {{scope.row.sales}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 订单：</div> {{scope.row.order_amount}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 平均售价：</div> {{scope.row.avgprice}}</div>
				</template>
			 </el-table-column>
			 <el-table-column label="退款情况" header-align="center"    width="125"  sortable="custom"   prop="refund" >
			 	<template #default="scope">
			 		 <div><div class="font-extraSmall pull-left text-right width50"> 退款：</div> {{scope.row.refund}}</div>
			 		 <div v-if="scope.row.refundsales"><div class="font-extraSmall pull-left text-right width50"> 数量：</div> {{scope.row.refundsales}}</div>
			 		 <div v-if="scope.row.refundrate"><div class="font-extraSmall pull-left text-right width50"> 退款率：</div> {{scope.row.refundrate}}</div>
			 	</template>
			  </el-table-column>
			  <el-table-column label="平台费用"  header-align="center"   width="145"  sortable="custom"   prop="fbafee" >
			  	<template #default="scope">
			  		 <div><div class="font-extraSmall pull-left text-right width50"> FBA费：</div> {{scope.row.fbafee}}</div>
			  		 <div><div class="font-extraSmall pull-left text-right width50"> 佣金：</div> {{scope.row.commission}}</div>
			  		 <div v-if="scope.row.shipping!='0'"><div class="font-extraSmall pull-left text-right width50"> 买家运费：</div> {{scope.row.shipping}}</div>
					 <div v-if="scope.row.promotion!='0'"><div class="font-extraSmall pull-left text-right width50"> 促销费：</div> {{scope.row.promotion}}</div>
					 <div ><div class="font-extraSmall pull-left text-right width50"> 其它：</div> {{scope.row.otherfee}}</div>
			  	</template>
			   </el-table-column>
			   <el-table-column label="SKU结算" header-align="center"   width="100"  sortable="custom"   prop="setincome" />
			   <el-table-column label="店铺分摊"   header-align="center"  width="140"  sortable="custom"   prop="share_storage_fee" >
			   	<template #default="scope">
			   		 <div><div class="font-extraSmall pull-left text-right width60"> 仓储费：</div> {{scope.row.share_storage_fee}}</div>
			   		 <div v-if="scope.row.share_long_storage_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 长期仓储：</div> {{scope.row.share_long_storage_fee}}</div>
			   		 <div v-if="scope.row.share_adv_spend_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 广告：</div> {{scope.row.share_adv_spend_fee}}</div>
					 <div v-if="scope.row.share_coupon_redemption_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 折扣券：</div> {{scope.row.share_coupon_redemption_fee}}</div>
					 <div v-if="scope.row.share_reserve_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 预留金：</div> {{scope.row.share_reserve_fee}}</div>
					 <div v-if="scope.row.share_shop_other_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它：</div> {{scope.row.share_shop_other_fee}}</div>
			   	</template>
			    </el-table-column>
				<el-table-column label="店铺结算"  header-align="center"   width="100"  sortable="custom"   prop="income" ></el-table-column>
				<el-table-column label="VAT税费"  header-align="center"   width="100"  sortable="custom"   prop="profit_vat" ></el-table-column>
				<el-table-column label="本地预估"   header-align="center"  width="145"  sortable="custom"   prop="profit_local_shipmentfee" >
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
				  <el-table-column label="报表费用"   header-align="center"  width="140"  sortable="custom"   prop="rpt_storage_fee" >
				  	<template #default="scope">
				  		 <div><div class="font-extraSmall pull-left text-right width60"> 仓储费：</div> {{scope.row.rpt_storage_fee}}</div>
				  		 <div v-if="scope.row.rpt_long_storage_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 长期仓储：</div> {{scope.row.rpt_long_storage_fee}}</div>
				  		 <div v-if="scope.row.rpt_adv_spend_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 广告：</div> {{scope.row.rpt_adv_spend_fee}}</div>
				  		 <div v-if="scope.row.fin_sum_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它自定义：</div> {{scope.row.fin_sum_fee}}</div>
				  	</template>
				   </el-table-column>
				  <el-table-column label="本地费用"  header-align="center"   width="145"  sortable="custom"   prop="local_price" >
				  	<template #default="scope">
				  		 <div><div class="font-extraSmall pull-left text-right width40"> 采购：</div> {{scope.row.local_price}}</div>
				  		 <div  v-if="scope.row.local_other_cost!='0'"><div class="font-extraSmall pull-left text-right width40"> 其它：</div> {{scope.row.local_other_cost}}</div>
				  		 <div  v-if="scope.row.local_return_tax!='0'"><div class="font-extraSmall pull-left text-right width40"> 退税：</div> {{scope.row.local_return_tax}}</div>
				  	</template>
				   </el-table-column>
			<el-table-column label="利润"   header-align="center"  width="150" sortable="custom"   prop="profit" >
				<template #default="scope">
					 <div><div class="font-extraSmall pull-left text-right width60"> 利润：</div>{{scope.row.profit}}</div>
					 <div><div class="font-extraSmall pull-left text-right width60"> 利润率：</div>{{scope.row.profitrate}}</div>
			    </template>
			</el-table-column>	
	</el-table>
	
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
</style>
 