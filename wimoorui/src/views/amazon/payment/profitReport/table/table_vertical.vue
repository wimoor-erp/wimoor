<template >
	<!-- 	 height="calc(100vh - 348px)"  -->
	<div class="settlementTable">
	<GlobalTable
	 v-if="activeName=='parentasin'"
	 show-summary
	 :summary-method="getSummaries"
	 ref="globalTableRef1"
	 @selectionChange='selectChange'
	 :tableData="tableData"

	 @loadTable="loadTableData" 
	 rowKey="parentasin"
	 :row-class-name="rowClassName" 
	 @expandChange="handleExpandChange" 
	 >
	<template #field>
			   
			 <el-table-column type="expand"   min-width="30px" fixed="left"  show-overflow-tooltip  >
			 	<template #default="scope">
			 		 <Child :extendData="scope.row.extendData" v-if="scope.row.subnum>0"/>
			 	</template>
			 </el-table-column>
			<el-table-column   label="父ASIN" header-align="center"  min-width="270px" fixed="left" sortable="custom"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	父ASIN<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div  class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="img-80"  width="80" height="80"  />
					<img v-else :src="$require('empty/noimage40.png')"  class="img-80"  width="80" height="80"  />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.parentasin}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						 <div><span class="font-extraSmall"> 店铺：{{scope.row.groupname}} 站点：{{scope.row.marketname}}</span></div>
						 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}} </span></div>
						 </div>
					 </div>
				</template>
			 
			</el-table-column>
			
			<el-table-column label="销售情况"  header-align="center"   width="150"  sortable="custom"   prop="principal" >
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
					 <div ><div class="font-extraSmall pull-left text-right width50"> 其它费用：</div> {{scope.row.otherfee}}</div>
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
					 <div v-if="scope.row.share_reimbursement_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿金：</div> {{scope.row.share_reimbursement_fee}}</div>
					 <div v-if="scope.row.share_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除费：</div> {{scope.row.share_disposal_fee}}</div>
					 <div v-if="scope.row.share_shop_other_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它费用：</div> {{scope.row.share_shop_other_fee}}</div>
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
						  <div v-if="scope.row.rpt_reimbursements_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿金：</div> {{scope.row.rpt_reimbursements_fee}}</div>
				  		 <div v-if="scope.row.rpt_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除：</div> {{scope.row.rpt_disposal_fee}}</div>
				  		 <div v-if="scope.row.rpt_disposal_units!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除个数：</div> {{scope.row.scope.row.rpt_disposal_units}}</div>
						 <div v-if="scope.row.fin_sum_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它自定义：</div> {{scope.row.fin_sum_fee}}</div>
				  	</template>
				   </el-table-column>
				  <el-table-column label="本地费用"  header-align="center"   width="145"  sortable="custom"   prop="local_price" >
				  	<template #default="scope">
				  		 <div title="采购成本"><div class="font-extraSmall pull-left text-right width40" > 采购：</div> {{scope.row.local_price}}</div>
				  		 <div  v-if="scope.row.local_other_cost!='0'"><div class="font-extraSmall pull-left text-right width40"> 其它费用：</div> {{scope.row.local_other_cost}}</div>
				  		 <div  v-if="scope.row.local_return_tax!='0'"><div class="font-extraSmall pull-left text-right width40"> 退税：</div> {{scope.row.local_return_tax}}</div>
				  	</template>
				   </el-table-column>
			<el-table-column label="利润"  fixed="right"  header-align="center"  width="150" sortable="custom"   prop="profit" >
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
	 
	<!-- 	 height="calc(100vh - 348px)"  -->
	<GlobalTable
	 v-else-if="activeName=='groupid' || activeName=='categoryid'"
	:defaultSort="{ prop: 'order_amount', order: 'descending' }" 
	 show-summary
	 :summary-method="getSummaries"
	 ref="globalTableRef2"
	 @selectionChange='selectChange'
	 :tableData="tableData"

	 @loadTable="loadTableData"
	  >
	<template #field>
			 
			<el-table-column v-if="activeName=='categoryid'" align="center" label="本地品类名称" min-width="200px" header-align="center"   fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	本地品类名称<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
						<span v-if="scope.row.categoryname">	{{scope.row.categoryname}}</span>
						<span v-else>未对应本地产品分类</span>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='groupid'" align="center" label="店铺名称" min-width="100px" header-align="center"   fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	店铺名称<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
						 {{scope.row.groupname}}
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
					<div v-if="scope.row.share_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除费：</div> {{scope.row.share_disposal_fee}}</div>
					 <div v-if="scope.row.share_shop_other_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它费用：</div> {{scope.row.share_shop_other_fee}}</div>
			   	</template>
			    </el-table-column>
				<el-table-column label="店铺结算"   header-align="center"   width="120"   sortable="custom"   prop="income" ></el-table-column>
				<el-table-column label="VAT税费"    header-align="center"   width="120"   sortable="custom"   prop="profit_vat" ></el-table-column>
				<el-table-column label="本地预估"   header-align="center"    width="160"  sortable="custom"    prop="profit_local_shipmentfee" >
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
						 <div v-if="scope.row.rpt_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除：</div> {{scope.row.rpt_disposal_fee}}</div>
						 <div v-if="scope.row.rpt_disposal_units!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除个数：</div> {{scope.row.scope.row.rpt_disposal_units}}</div>
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
			<el-table-column label="利润" fixed="right"   header-align="center"  width="170" sortable="custom"   prop="profit" >
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
	<!-- 	 height="calc(100vh - 348px)"  -->
	<GlobalTable
	v-else
	:defaultSort="defaultSort" 
	 show-summary
	 :summary-method="getSummaries"
	 ref="globalTableRef3"
	 @selectionChange='selectChange'
	 :tableData="tableData"

	 @loadTable="loadTableData" 
	 >
	<template #field>
			<el-table-column v-if="activeName=='sku'" label="平台SKU" header-align="center"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	平台SKU<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div  @click.stop="handleToMaterial(scope.row)" class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="img-80"  width="80" height="80"  />
					<img v-else :src="$require('empty/noimage40.png')"  class="img-80"  width="80" height="80"  />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.sku}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						 <div> <span class="font-extraSmall">ASIN:{{scope.row.asin}} </span></div>
						 
						 <div><span class="font-extraSmall"> 店铺：{{scope.row.groupname}} 站点：{{scope.row.marketname}}</span></div>
						 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}} </span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='msku'" label="本地SKU" header-align="center"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	本地SKU<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div   class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="img-80" @click.stop="handleToMaterial(scope.row)" width="80" height="80"  />
					<img v-else :src="$require('empty/noimage40.png')" @click.stop="handleToMaterial(scope.row)" class="img-80"  width="80" height="80"  />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.msku}}</div>
						 <div class="font-extraSmall">{{scope.row.mname}}</div>
						 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}} </span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='asin'" label="平台ASIN" header-align="center"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	平台ASIN<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="img-80" @click.stop="handleToMaterial(scope.row)" width="80" height="80"  />
					<img v-else :src="$require('empty/noimage40.png')" @click.stop="handleToMaterial(scope.row)" class="img-80"  width="80" height="80"  />
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
					<div><div class="text-green pull-left text-right width60"> 销售额：</div>
						 <span v-if="scope.row.principal">{{scope.row.principal}}</span><span v-else>0</span></div>
						 <div v-if="scope.row.rpt_adv_sales"><div class="font-extraSmall pull-left text-right width60"> 广告销售：</div> {{scope.row.rpt_adv_sales}}</div>
						 <div v-if="scope.row.sales"><div class="font-extraSmall pull-left text-right width60"> 销量：</div> {{scope.row.sales}}</div>
						 <div v-if="scope.row.rpt_adv_units"><div class="font-extraSmall pull-left text-right width60"> 广告销量：</div> {{scope.row.rpt_adv_units}}</div>
						 <div v-if="scope.row.order_amount"><div class="font-extraSmall pull-left text-right width60"> 订单：</div> {{scope.row.order_amount}}</div>
						 <div v-if="scope.row.avgprice"><div class="font-extraSmall pull-left text-right width60"> 平均售价：</div> {{scope.row.avgprice}}</div>
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
			  		 <div v-if="scope.row.shipping!='0'"><div class="font-extraSmall pull-left text-right width50"> 运费：</div> {{scope.row.shipping}}</div>
					 <div v-if="scope.row.promotion!='0'"><div class="font-extraSmall pull-left text-right width50"> 促销：</div> {{scope.row.promotion}}</div>
					 <div ><div class="font-extraSmall pull-left text-right width50"> 其它费用：</div> {{scope.row.otherfee}}</div>
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
					  <div v-if="scope.row.share_reimbursement_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿金：</div> {{scope.row.share_reimbursement_fee}}</div>
					 <div v-if="scope.row.share_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除费：</div> {{scope.row.share_disposal_fee}}</div>
					 <div v-if="scope.row.share_shop_other_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 其它费用：</div> {{scope.row.share_shop_other_fee}}</div>
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
				  	     <div v-if="scope.row.rpt_reimbursements_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 赔偿：</div> {{scope.row.rpt_reimbursements_fee}}</div>
						 <div v-if="scope.row.rpt_disposal_fee!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除：</div> {{scope.row.rpt_disposal_fee}}</div>
						 <div v-if="scope.row.rpt_disposal_units!='0'"><div class="font-extraSmall pull-left text-right width60"> 移除个数：</div> {{scope.row.scope.row.rpt_disposal_units}}</div>
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
			<el-table-column label="利润" fixed="right"  header-align="center"  width="150" sortable="custom"   prop="profit" >
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
					 <div><div class="font-extraSmall pull-left text-right width50"> 利润：</div>{{scope.row.profit}}</div>
					 <div><div class="font-extraSmall pull-left text-right width50"> 利润率：</div>{{scope.row.profitrate}}</div>
			    </template>
			</el-table-column>	
			 </template>
	</GlobalTable>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,Switch} from '@element-plus/icons-vue';
	import Child from"./child_vertical.vue"
	import {dateFormat,getCurrencyMark,outputmoney,formatFloat} from '@/utils/index.js';
	import settlementFormulaApi from '@/api/amazon/finances/settlementFormulaApi.js';
	import settlementMonthSummaryApi from "@/api/amazon/finances/settlementMonthSummaryApi.js";
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	let props = defineProps({  activeName:"", });
	const { activeName} = toRefs(props);
	const state = reactive({
		tableData:{
			records:[ { } ],total:1},
		param:{},
		profittooltip:"",
		summary:{},
	})
	const globalTableRef1=ref();
	const globalTableRef2=ref();
	const globalTableRef3=ref();
	const{
		tableData,summary,profittooltip
	}=toRefs(state)
	function parseFloatValue(value){
			 if(value)return parseFloat(value);
			 else return 0.0;
	}
	onMounted(()=>{
		settlementFormulaApi.loadformula().then((res)=>{
			state.profittooltip=res.data.formulaData;
		});
	})
	const emit = defineEmits(['changeTable' ]);
	function handleChangeTable(){
		emit("changeTable","horizontal");
	}
	function handleToMaterial(row){
		 router.push({
			path:'/material/details',
			query:{
			  title:"产品详情",
			  path:'/material/details',
			  details:row.mid,
			}
		 })
	}
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
			 row.share_reimbursement_fee=outputmoney(row.share_reimbursement_fee);
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
			 row.rpt_storage_fee=outputmoney(row.rpt_storage_fee);
			 row.rpt_long_storage_fee=outputmoney(row.rpt_long_storage_fee);
			 row.rpt_adv_spend_fee=outputmoney(row.rpt_adv_spend_fee);
			 row.rpt_reimbursements_fee=outputmoney(row.rpt_reimbursements_fee);
			 row.fin_sum_fee=outputmoney(row.fin_sum_fee);
			 row.local_price=outputmoney(row.local_price);
			 row.local_other_cost=outputmoney(row.local_other_cost); 
			 row.local_return_tax=outputmoney(row.local_return_tax);
			 row.rpt_adv_sales=outputmoney(row.rpt_adv_sales);
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
		state.param=param;
		handleQuery();
	}
	function handleQuery(){
		if(props.activeName=="parentasin"){
			globalTableRef1.value.loadTable(state.param);
		}else if(props.activeName=='groupid' || props.activeName=='categoryid'){
			globalTableRef2.value.loadTable(state.param);
		}else{
			globalTableRef3.value.loadTable(state.param);
		}
		
	}
	 defineExpose({show})
	 function getSummaries({columns,data}){
	 	var arr = ["合计"];
		var startindex=1;
		if(props.activeName=="parentasin"){
			startindex=2;
		}
	 	columns.forEach((item,index)=>{
	 		if(index>=startindex){
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
						 fee=fee+parseFloatValue(state.summary["share_reimbursement_fee"]);
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
		 charttype:"asin",parentasin:row.parentasin?row.parentasin:"#",
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
	.change-icon{
	padding-top: 3px;
	font-size: 11px;
	margin-top: 3px;
	cursor: pointer;
	line-height: unset;
	}
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
 