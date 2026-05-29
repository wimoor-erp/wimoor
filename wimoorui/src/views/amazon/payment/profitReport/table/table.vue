<template >
	<!-- 	 height="calc(100vh - 348px)"  -->
	<div class="settlementTable expand-table">
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
			   
			 <el-table-column type="expand" width="60px" fixed="left"  show-overflow-tooltip  >
			 	<template #default="scope">
			 		 <Child :extendData="scope.row.extendData" v-if="scope.row.subnum>0"/>
			 	</template>
			 </el-table-column>
			<el-table-column   label="父ASIN" width="260px" fixed="left"  show-overflow-tooltip sortable="custom"   prop="sku" >
				<template #header>
					       	父ASIN<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div  class="flex-center">
					<img v-if="scope.row.image"  :src="scope.row.image" class="product-img"   />
					<img v-else :src="$require('empty/noimage40.png')"  class="product-img"    />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.parentasin}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						<div><span class="font-extraSmall"> {{scope.row.groupname}}-{{scope.row.marketname}}</span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column label="销售额"  width="140"  sortable="custom"   prop="principal" />
			<el-table-column label="销量"  width="80"  sortable="custom"   prop="sales" />
			<el-table-column label="订单量"  width="90"  sortable="custom"   prop="order_amount" />
			<el-table-column label="平均售价"  width="80"     prop="avgprice" />
			<el-table-column label="广告销售额"  width="120"  sortable="custom"    prop="rpt_adv_sales" />
			<el-table-column label="广告销量"  width="100"    sortable="custom"  prop="rpt_adv_units" />
			<el-table-column label="广告销量"  width="100"    sortable="custom"  prop="rpt_adv_units" />
			<el-table-column label="退款数量"  width="100"    sortable="custom"  prop="refundsales" />
			<el-table-column label="退款金额"  width="100"    sortable="custom"  prop="refund" />
			<el-table-column label="退款率" sortable="custom"  prop="refundrate" width="85">
				<template #default="scope">
					<span v-if="scope.row.refundrate" 
					:title="'退货订单：'+scope.row.refundorder+'订单量：'+scope.row.order_amount">
					{{scope.row.refundrate}}</span>
					<span v-else>0</span>
				</template>
			</el-table-column>
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
			<el-table-column label="VAT税费"  width="100"  prop="profit_vat" > </el-table-column>	
	       <el-table-column label="仓储费"   label-class-name="bg-group-y"  width="100"  prop="profit_local_shipmentfee" >
	       			   <template #header>
	       			   	       		<p class="l-h-none ">预估费用</p>
	       			   	       		<p class="l-h-none">仓储费</p>
	       			   </template>	
	       			</el-table-column>	
	       <el-table-column label="市场费" label-class-name="bg-group-y"    width="100"  prop="profit_marketfee" >
	       <template #header>
	          	       		<p class="l-h-none ">&nbsp;</p>
	          	       		<p class="l-h-none">市场费用</p>
	          </template>	
	       </el-table-column>				
	       <el-table-column label="所得税"  label-class-name="bg-group-y"   width="100"   prop="profit_companytax" >
		   <template #header>
		      	       		<p class="l-h-none ">&nbsp;</p>
		      	       		<p class="l-h-none">所得税</p>
		      </template>	
		   </el-table-column>	
	       <el-table-column label="关税"  label-class-name="bg-group-y"   width="100"   prop="profit_customstax" >
		   <template #header>
		      	       		<p class="l-h-none ">&nbsp;</p>
		      	       		<p class="l-h-none">关税</p>
		      </template>	
		   </el-table-column>	
	       <el-table-column label="汇率损耗"   label-class-name="bg-group-y"  width="100"  prop="profit_exchangelost" >
		   <template #header>
		      	       		<p class="l-h-none ">&nbsp;</p>
		      	       		<p class="l-h-none">汇率损耗</p>
		      </template>	
		   </el-table-column>	
	       <el-table-column label="固定费用"  label-class-name="bg-group-y"   width="100"  prop="profit_lostrate" >
		   <template #header>
		      	       		<p class="l-h-none ">&nbsp;</p>
		      	       		<p class="l-h-none">固定费用</p>
		      </template>	
		   </el-table-column>	
	       <el-table-column label="其它费用"  label-class-name="bg-group-y"   width="100"   prop="profit_otherfee" >
		   <template #header>
		      	       		<p class="l-h-none ">&nbsp;</p>
		      	       		<p class="l-h-none">其它成本</p>
		      </template>	
		   </el-table-column>	
		   <el-table-column label=" 预估运费"    width="100" sortable="custom"   prop="profit_local_shipmentfee" />
	       <el-table-column label="SKU报表" label-class-name="bg-group-y" width="100" prop="rpt_storage_fee">
	         <template #header>
	       		<p class="l-h-none">SKU报表</p>
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
		   <el-table-column label="SKU移除费" label-class-name="bg-group-y" width="100" prop="rpt_disposal_fee">
		     <template #header>
		   		<p class="l-h-none ">&nbsp;</p>
		   		<p class="l-h-none">移除费</p>
		     </template>	
		   </el-table-column>	
		   <el-table-column label="SKU移除数" label-class-name="bg-group-y" width="100" prop="rpt_disposal_units">
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
	       <el-table-column label="其它成本成本"    width="100"   prop="local_other_cost" />
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
	 
	<!-- 	 height="calc(100vh - 348px)"  -->
	<GlobalTable
	 v-else-if="activeName=='groupid' || activeName=='categoryid'"
	:defaultSort="{ prop: 'order_amount', order: 'descending' }" 
	 show-summary
	 :summary-method="getSummaries"
	 ref="globalTableRef2"
	 @selectionChange='selectChange'
	 :tableData="tableData"
     height="calc(100vh - 348px)"
	 @loadTable="loadTableData"
	  >
	<template #field>
			 
			<el-table-column v-if="activeName=='categoryid'" align="center" label="本地品类名称" min-width="200px"  fixed="left" prop="categoryname" sortable="custom"  show-overflow-tooltip  >
				<template #header>
					       	本地品类名称<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
						<span v-if="scope.row.categoryname">	{{scope.row.categoryname}}</span>
						<span v-else>未对应本地产品分类</span>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='groupid'" align="center" label="店铺名称" min-width="100px"   fixed="left"   prop="groupname" sortable="custom"  show-overflow-tooltip   >
				<template #header>
					       	店铺名称<el-icon @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
						 {{scope.row.groupname}}
				</template>
			</el-table-column>
            <el-table-column label="销售额"  sortable="custom"  prop="principal" width="140">
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
			<el-table-column label="退款率" sortable="custom"  prop="refundrate" width="85">
				<template #default="scope">
					<span v-if="scope.row.refundrate" 
					:title="'退货订单：'+scope.row.refundorder+'订单量：'+scope.row.order_amount">
					{{scope.row.refundrate}}</span>
					<span v-else>0</span>
				</template>
			</el-table-column>
			<el-table-column label="FBA费用"   prop="fbafee" width="100"/>
			<el-table-column label="销售佣金"    prop="commission" width="100"/>
			<el-table-column label="买家运费"   prop="shipping" width="90"/>
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
			<el-table-column label="店铺结算"   width="100"  sortable="custom"   prop="income" />
			<el-table-column label="VAT税费"   width="100"  prop="profit_vat" >
			</el-table-column>	
			<el-table-column label="仓储费"   label-class-name="bg-group-y"  width="100"  prop="profit_local_shipmentfee" >
						   <template #header>
						   	       		<p class="l-h-none ">预估费用</p>
						   	       		<p class="l-h-none">仓储费</p>
						   </template>	
						</el-table-column>	
			<el-table-column label="市场费" label-class-name="bg-group-y"    width="100"  prop="profit_marketfee" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">市场费用</p>
			   </template>	
			</el-table-column>			
			<el-table-column label="所得税" label-class-name="bg-group-y"    width="100"   prop="profit_companytax" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">所得税</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="关税"  label-class-name="bg-group-y"   width="100"   prop="profit_customstax" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">关税</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="汇率损耗"  label-class-name="bg-group-y"   width="100"  prop="profit_exchangelost" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">汇率损耗</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="固定费用" label-class-name="bg-group-y"    width="100"  prop="profit_lostrate" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">固定费用</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="其它费用" label-class-name="bg-group-y"    width="100"   prop="profit_otherfee" >
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
			<el-table-column label="SKU移除费" label-class-name="bg-group-y" width="100" prop="rpt_disposal_fee">
			  <template #header>
					<p class="l-h-none ">&nbsp;</p>
					<p class="l-h-none">移除费</p>
			  </template>	
			</el-table-column>	
			<el-table-column label="SKU移除数" label-class-name="bg-group-y" width="100" prop="rpt_disposal_units">
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
			<el-table-column label="其它成本成本"    width="100"   prop="local_other_cost" />
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
			<el-table-column v-if="activeName=='sku'" label="商品信息"  width="260" fixed="left"   sortable="custom" show-overflow-tooltip  prop="sku" >
				<template #header>
					       	商品信息<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>	
				<template #default="scope">
					<div  class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image"  @click.stop="handleToMaterial(scope.row)" class="product-img"    />
					<img v-else :src="$require('empty/noimage40.png')"   @click.stop="handleToMaterial(scope.row)" class="product-img"    />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.sku}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						 <el-space class="font-extraSmall"> <span> ASIN:{{scope.row.asin}} </span>
						<span>{{scope.row.groupname}}-{{scope.row.marketname}}</span> 
						 </el-space>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='msku'" label="本地SKU"   min-width="270px" fixed="left"  sortable="custom"  show-overflow-tooltip  prop="sku" >
				<template #header>
					       	本地SKU<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>
				<template #default="scope">
					<div class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image"  @click.stop="handleToMaterial(scope.row)" class="product-img"  />
					<img v-else :src="$require('empty/noimage40.png')"  @click.stop="handleToMaterial(scope.row)" class="product-img"   />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.msku}}</div>
						 <div class="font-extraSmall">{{scope.row.mname}}</div>
						 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}} </span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column v-if="activeName=='asin'" label="平台ASIN"  min-width="270px" fixed="left"  show-overflow-tooltip   prop="sku" >
				<template #header>
					       	平台ASIN<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
				</template>
				<template #default="scope">
					<div class="flex-center">
					<img v-if="scope.row.image" :src="scope.row.image" class="product-img"   />
					<img v-else :src="$require('empty/noimage40.png')"  class="product-img"    />
						<div style="margin-left:5px;">
						 <div class="sku">{{scope.row.asin}}</div>
						 <div class="font-extraSmall">{{scope.row.pname}}</div>
						 <div><span class="font-extraSmall">{{scope.row.groupname}}-{{scope.row.marketname}}</span></div>
						 </div>
					 </div>
				</template>
			</el-table-column>
			<el-table-column label="销售额"  sortable="custom"  prop="principal" width="140">
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
			<el-table-column label="退款率" sortable="custom"  prop="refundrate" width="85">
				<template #default="scope">
					<span v-if="scope.row.refundrate" 
					:title="'退货订单：'+scope.row.refundorder+'订单量：'+scope.row.order_amount">
					{{scope.row.refundrate}}</span>
					<span v-else>0</span>
				</template>
			</el-table-column>
			<el-table-column label="FBA费用"   prop="fbafee" width="100"/>
			<el-table-column label="销售佣金"    prop="commission" width="100"/>
			<el-table-column label="买家运费"   prop="shipping" width="90"/>
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
			<el-table-column label="店铺结算"   width="100"  sortable="custom"   prop="income" />
			<el-table-column label="VAT税费"     width="100"  prop="profit_vat" > </el-table-column>
			<el-table-column label="仓储费"   label-class-name="bg-group-y"  width="100"  prop="profit_local_shipmentfee" >
						   <template #header>
						   	       		<p class="l-h-none ">预估费用</p>
						   	       		<p class="l-h-none">仓储费</p>
						   </template>	
						</el-table-column>	
			<el-table-column label="市场费用" label-class-name="bg-group-y"    width="100"  prop="profit_marketfee" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">市场费用</p>
			   </template>	
			</el-table-column>				
			<el-table-column label="所得税"  label-class-name="bg-group-y"   width="100"   prop="profit_companytax" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">所得税</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="关税"   label-class-name="bg-group-y"  width="100"   prop="profit_customstax" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">关税</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="汇率损耗"  label-class-name="bg-group-y"   width="100"  prop="profit_exchangelost" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">汇率损耗</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="固定费用" label-class-name="bg-group-y"    width="100"  prop="profit_lostrate" >
			<template #header>
			   	       		<p class="l-h-none ">&nbsp;</p>
			   	       		<p class="l-h-none">固定费用</p>
			   </template>	
			</el-table-column>	
			<el-table-column label="其它费用" label-class-name="bg-group-y"    width="100"   prop="profit_otherfee" >
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
			<el-table-column label="SKU移除费" label-class-name="bg-group-y" width="100" prop="rpt_disposal_fee">
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
			<el-table-column label="货件头程运费"  sortable="custom"    width="130"  prop="local_shipment_item_fee" >
				<template #header>
					<div style="float:left">
					<p class="l-h-none " > 
										<el-space>货件
										<el-tooltip
											  effect="dark"
											  content=""
											  placement="top-start"
											>
											 <template #content> 
											 <div>计算发货模块-货件跟踪-的物流费用，根据每个货件商品上的接收日期计算</div>
											  </template>
											  <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
											</el-tooltip>
											</el-space>
					</p>
					<p class="l-h-none">
						头程运费
					</p>
					</div>
				</template>	
			</el-table-column>		
			<el-table-column label="采购成本"  sortable="custom"    width="100"  prop="local_price" />
			<el-table-column label="其它采购成本"    width="120"   prop="local_other_cost" />
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
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,HomeFilled,Switch} from '@element-plus/icons-vue';
	import Child from"./child.vue"
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
			 row.share_disposal_fee=outputmoney(row.share_disposal_fee);
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
			 row.rpt_disposal_fee=outputmoney(row.rpt_disposal_fee);
			 row.rpt_reimbursements_fee=outputmoney(row.rpt_reimbursements_fee);
			 row.fin_sum_fee=outputmoney(row.fin_sum_fee);
			 row.local_price=outputmoney(row.local_price);
			 row.local_other_cost=outputmoney(row.local_other_cost); 
			 row.local_return_tax=outputmoney(row.local_return_tax);
			 row.rpt_adv_sales=outputmoney(row.rpt_adv_sales);
			 row.profit=outputmoney(row.profit);
			 	 
	}
	const emit = defineEmits(['changeTable' ]);
	function handleChangeTable(){
		emit("changeTable","vertical");
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
	.settlementTable .el-table__footer-wrapper .el-table__cell div{
		float:left;
	}
	.change-icon{
    padding-top: 3px;
    font-size: 11px;
    margin-top: 3px;
    cursor: pointer;
    line-height: unset;
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

	.pull-left{
			 float:left;
	}
	.NonChildren .el-table__expand-icon{
		display: none !important;
	}
	.l-h-none{
		line-height:14px;
	}
	.bg-group-y{
		background-color:#efefef !important;
	}
	.dark .bg-group-y{
		background-color:#434343 !important;
	}
</style>
 