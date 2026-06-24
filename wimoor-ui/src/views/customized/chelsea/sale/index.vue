<template>
	<div class="main-sty">
		<div class="con-header week-range-sty">
			<el-row>
			<el-space>
			<Group ref="groups" @change="changeGroup" />
			 	<Datepicker ref="datepickersRef" :days="1"  @changedate="changedate" />
				<el-radio-group v-model="times" @change="changeTimes">
				      <el-radio-button label="昨日" />
				      <el-radio-button label="近7天" />
				      <el-radio-button label="近15天" />
				    </el-radio-group>
					<el-input  v-model="searchKeywords" placeholder="请输入SKU"
					 clearable @input="handleQuery"  style="min-width:200px;" >
					   <template #append>
					     <el-button @click="handleQuery">
					         <el-icon style="font-size: 16px;align-itmes:center">
					         <search />
					      </el-icon>
					     </el-button>
					   </template>
					 </el-input>
			 </el-space>
			 <div class='rt-btn-group'>
			 	<el-space>
					<el-button @click="handleReport" :loading="btnloading">下载</el-button>
			 <el-dropdown :hide-on-click="false" trigger="click"  @command="handleCommand">
			     <el-button class='ic-btn'  title='排序'>
			        <sort-one theme="outline" size="16"  :strokeWidth="3"/>
			     </el-button>
			     <template #dropdown>
			 	<ul class="sortdropdowntitle">
			 		  <li class="font-extraSmall">排序依据</li>
			 	 </ul>
			     <div  style="height:300px;overflow-y:scroll; ">
			 	
			       <ul class="sortdropdown">
			         <li v-for="(item,index) in rankData"  @click="rankChange(item.value)"
			 		 :class="{r_active:currentRank==item.value}" 
			 		>{{item.name}}</li>
			     </ul>
			 	    </div>
			 		<ul class="sortdropdowntitle" >
			 		<li divided  class="font-extraSmall">排序循序</li>
			 		</ul>
			 		 <ul class="sortdropdown">
			 		<li v-for="item in soltData" @click="soltChange(item.value)" 
			 		:class="{r_active:currentSolt==item.value}">{{item.name}}</li>
			 		</ul>
			     </template>
			   </el-dropdown>
			   	 </el-space>
				 </div>
				 </el-row>
	    </div>
		<div class="con-body">
			<GlobalTable  ref="globalTable" 
			:tableData="tableData"
			  @loadTable="loadtableData"
			  :show-header="true"
			  @row-click="rowClick"
			  @expand-change = "expandChange"
			  height="calc(100vh - 198px)"
			   border >
			   <template #field>
				   <el-table-column type="expand" width="40">
					    <template #default="props">
							<el-row>
								<el-col :span="8">
									<div  :id="'orderSales'+props.row.id"  style='height:240px;width:100%'></div>
								</el-col>
								<el-col :span="8">
									<div :id="'adChart'+props.row.id"  style='height:240px;width:100%'></div>
								</el-col>
								<el-col :span="8">
									<div :id="'orderProfit'+props.row.id"   style='height:240px;width:100%'></div>
								</el-col>
							</el-row>
						</template>
				   </el-table-column>
                   <el-table-column label="图片" width="110">
					   <template #default="scope">
							  <el-image
							  class="img-size"
							   v-if="scope.row.image"
							   :src="scope.row.image"></el-image>
							   <el-image v-else 
							  class="img-size"
							   :src="$require('empty/noimage40.png')"
							   ></el-image>
					   </template>
				   </el-table-column>
                   <el-table-column label="产品信息" min-width="240">
					   <template #default="scope">
	                        <el-space 
							class="table-edit-flex"
							direction="vertical" alignment="start" :size="4">
								<p>{{scope.row.name}}</p>
								<div class="light-font">{{scope.row.sku}}</div>
								<div><span>Asin: </span> 
								<el-link type="primary" :underline="false">{{scope.row.asin}}</el-link></div>
								<el-tag v-if="scope.row.flownnumber"
								@click.stop="followSeller(scope.row)"
								type="danger"><span >{{parseInt(scope.row.flownnumber)-1}}</span>
								人跟卖</el-tag>
								<el-space >
									<div v-if="scope.row.fulfillchannel=='DEFAULT'" class="table-edit-flex">
											  <span v-if="scope.row.landedAmount">
											  {{scope.row.landedCurrency}}{{scope.row.landedAmount}} </span>
											  <span v-else>0 </span>
											  <el-icon @click.stop="changeFBMPrice(scope.row)"><Edit/></el-icon>
									</div>
									<div v-else class="table-edit-flex" >
										<el-popover placement="right" :width="435" trigger="click" @show.stop="showPrice(scope.row.id)">
										      <template #reference>
										       <span class="font-num font-bold">
										       	{{scope.row.landedCurrency}}{{scope.row.landedAmount}}
										       </span>
										      </template>
										      <el-table  :data="priceList">
										        <el-table-column width="100" prop="ptype" label="类型" >
													<template #default="scopesub">
														<span v-if="scopesub.row.ptype=='BUYP'">您的售价</span>
														<span v-if="scopesub.row.ptype=='BOXP'">购物车售价</span>
														<span v-if="scopesub.row.ptype=='LOWP'">最低售价</span>
													</template>
											    </el-table-column>
										        <el-table-column width="120" prop="landedAmount"
												   label="价格" >
												   <template #header>
													   <el-space><div>价格</div> <el-button size="small" @click.stop="updatePrice(scope.row.id)">更新</el-button></el-space>
												   </template>
													<template #default="scopesub">
														{{scopesub.row.landedCurrency}} {{scopesub.row.landedAmount}}
													</template>
												</el-table-column>	
												<el-table-column width="95" prop="listingAmount"  label="listing价格" >
													<template #default="scopesub">
														{{scopesub.row.listingCurrency}} {{scopesub.row.listingAmount}}
													</template>
												</el-table-column>	
												<el-table-column width="90" prop="shippingAmount"
												   label="运费" >
													<template #default="scopesub">
														{{scopesub.row.shippingCurrency}} {{scopesub.row.shippingAmount}}
													</template>
												</el-table-column>	
										      </el-table>
										    </el-popover>
									       <el-icon @click.stop="modifyPrice(scope.row)" ><Edit/></el-icon>
									</div>
								</el-space>
							</el-space>
					   </template>
				   </el-table-column>
				   <el-table-column width="300">
					   <template #default="scope"> 
						   <el-row>
							   <el-col :span="12">
								   <el-space direction="vertical" alignment="start">
									   <p><span class="light-font">销量</span>
											<p v-if="scope.row.ordersales">{{scope.row.ordersales}}</p>
											<p v-else>0</p>
									   </p>
									   <p><span class="light-font">销售额</span><p>{{scope.row.landedCurrency}}
										 <span v-if="scope.row.orderprice">{{scope.row.orderprice}}</span>
										 <span v-else>0</span>
									   </p></p>
									   <p><span class="light-font">赔偿金</span><p>
									   <span v-if="scope.row.reimbursements">
									      {{scope.row.reimbursements}}
									   </span>
									   <span v-else>0</span>
									   </p></p>
								   </el-space>
							   </el-col>
							   <el-col :span="12">
								   <el-space direction="vertical" alignment="start">
									   <p><span class="light-font">退货率</span><p>
									 <span v-if="scope.row.refundrate"> {{formatFloat(scope.row.refundrate*100)}}%</span>
									  <span v-else> 0%</span>
									   </p></p>
									   <p><span class="light-font">退货量</span><p>{{NullTransform(scope.row.returnqty)}}</p></p>
									   <p><span class="light-font" title="扣除了退款金额对应的VAT,80%的佣金,数字服务费">退货金额成本</span>
									   <el-tooltip :content="'实际退款金额'+scope.row.returnprice">
									   <p>{{scope.row.landedCurrency}}
									   
									   <span v-if="scope.row.costReturn">{{formatFloat(scope.row.costReturn)}}</span>
									   <span v-else>0</span>
									   </p>
									   </el-tooltip>
									   </p>
								   </el-space>
							   </el-col>
						   </el-row>
					   </template>
				   </el-table-column>
				   <el-table-column width="600">
					   <template #default="scope">
						   <el-row>
							   <el-col :span="6">
								   <el-space class="table-edit-flex" direction="vertical" alignment="start">
									   <p ><span class="light-font">采购成本</span>
									   <el-popover 
									   trigger="click"
									   placement="top"
									   >
									   <template #reference>
										  <p>
											  <span v-if="scope.row.param && scope.row.param.costDetailMap&&scope.row.param.costDetailMap.purchase"
										 >{{formatFloat(scope.row.param.costDetailMap.purchase*scope.row.ordersales)}}</span>
											  <span @click.stop="editPrice()" class="font-extraSmall" v-if="scope.row.mprice"> | {{formatFloat(scope.row.mprice)}}</span>
										  <el-icon><edit></edit>
										  </el-icon>
										  </p>
									   </template>
			                             <el-input 
										 v-if="scope.row.mprice"
										 v-model="scope.row.mprice"
										 type="number">
										 <template #suffix>￥</template>
										 </el-input>
										 <div v-else >
											  请匹配本地SKU!
										 </div>
										 <el-button style="margin-top: 5px;"  v-if="scope.row.mprice" size="small" type="primary" @click.stop="submitPrice(scope.row)">提交</el-button> 
									   </el-popover>
									   </p>
									   <p><span class="light-font">仓储费</span><p>{{scope.row.landedCurrency}}
									  <span  v-if="scope.row.param && scope.row.param.costDetailMap
										 && scope.row.param.costDetailMap.storageFee">{{formatFloat(scope.row.param.costDetailMap.storageFee*scope.row.ordersales)}}</span>
									   </p></p>
								   </el-space>
							   </el-col>
							   <el-col :span="6">
								   <el-space direction="vertical" alignment="start">
									   <p><span class="light-font">广告花费</span>
										   <p v-if="scope.row.advspend">{{formatFloat(scope.row.advspend)}}</p>
										   <p v-else>0</p>
									   </p>
									   <p><span class="light-font">FBA费</span><p>
									 
									   <el-space
									   class="pointer"
									    :ref="el=>getFbaRef(el,scope.row)"
											@mouseenter="e=>setFbaRef(e,scope.row)"
									   @click.stop="exceedFee(scope.row)"
									   >
									   <span v-if="scope.row.fbafee">
											  <span>
												   {{formatFloat(scope.row.fbafee)}}
											  </span>
											  <el-icon title="FBA费用超收"  v-if="checkFBA(scope.row.fba,scope.row.param.costDetailMap.fba)" :size="20"  class="text-red"><WarnTriangleFilled />
											  </el-icon>
										 </span>
									   <span v-else>0</span>
									   </el-space>
									   </p></p>
									   <p><span class="light-font">佣金</span>
									   <p v-if="scope.row.referralfeeRate">
									   <el-tooltip :content="'佣金比例'+(scope.row.referralfeeRate*100)+'%'">
										   {{formatFloat(scope.row.referralFee)}}
									   </el-tooltip>
									   </p>
									    <p v-else>0</p>
									   </p>
								   </el-space>
							   </el-col>
							   <el-col :span="6">
								   <el-space direction="vertical" alignment="start">
									  <!-- <p><span class="light-font">VAT税</span>
									   <p v-if="scope.row.param && scope.row.param.costDetailMap
									   		&& scope.row.param.costDetailMap.vatFee">{{scope.row.param.costDetailMap.vatFee*scope.row.orderprice}}</p>
									   <p v-else>0</p>
									   </p> -->
									   <p><span class="light-font">数字服务费</span>
									   <p >{{formatFloat(scope.row.digitalFee)}}</p>
									   </p>
									   <p><span class="light-font">Coupon</span>
									   <p v-if="scope.row.budget_spend" >
									   <span>{{scope.row.budget_spend}}</span><span v-if="scope.row.budget_percentage_used">/{{formatFloat(scope.row.budget_percentage_used)}}%</span>
									   </p>
									   <p v-else>0/0</p>
									   
									   </p>
								   </el-space>
							   </el-col>
							   <el-col :span="6">
								   <el-space direction="vertical" alignment="start">
									   <p><span class="light-font" :title="scope.row.param && scope.row.param.costDetailMap
									   		&& scope.row.param.costDetailMap.vatRate?'VAT增值税比例'+scope.row.param.costDetailMap.vatRate:'' ">欧洲增值税</span>
									   <p v-if="scope.row.vatFee">{{formatFloat(scope.row.vatFee)}}</p>
									   <p v-else >0</p>
									   </p>
									   <p><span class="light-font">广告费/销量</span>
									   <p v-if="scope.row.advspend">{{formatFloat(scope.row.advspend/(scope.row.ordersales*1.00))}}</p>
									   <p v-else>0</p>
									   </p>
								   </el-space>
							   </el-col>
						   </el-row>
					   </template>
				   </el-table-column>
				   <el-table-column width="200">
					   <template #default="scope">
						   <el-row>
								   <el-col :span="12">
									   <el-space direction="vertical" alignment="start">
										   <p><p class="light-font">毛利润</p>
										   <el-space><div>{{scope.row.profit}}</div>
											<el-tooltip
										          effect="dark"
										          content="毛利润=销售额-采购成本-仓储费-广告花费-FBA费-佣金-数字服务费-Coupon-欧洲增值税-退款"
										          placement="top-start"
										        >
										          <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
										        </el-tooltip>
										   </el-space>
										   </p>
										   <p><span class="light-font">毛利率</span><p>{{formatFloat(scope.row.profitRate*100)}}%</p></p>
									   </el-space>
								   </el-col>
								   <el-col :span="12">
									  <!-- <el-space direction="vertical" alignment="start">
										   <p><span class="light-font">可售库存</span><p>{{scope.row.afnFulfillableQuantity}}</p></p>
									   </el-space> -->
									   <div>
									     <div class="font-extraSmall">可售库存<el-tooltip content="查看历史"><el-icon class="pointer"  @click.stop="handleShowFbaHistory(scope.row)" ><Histogram /></el-icon></el-tooltip> </div>
									     <div v-if="scope.row.fulfillchannel=='DEFAULT'" class="table-edit-flex">
									   	  <span v-if="scope.row.fulfillmentAvailability">{{scope.row.fulfillmentAvailability}} </span>
									   	  <span v-else>0 </span>
									   	  <el-icon @click.stop="changeFBMInventory(scope.row)"><Edit/></el-icon>
									   	  <span class="font-extraSmall"> FBM</span>
									     </div>
									     <el-popover title="库存详情" v-else placement="bottom" trigger="click" :width="600">
									   		   <el-row :gutter="8">
									   					 <el-col :span="7">
									   						 <div class="li-wrap">
									   									<ul class="list-style-type li-be" v-if="FBAInvData">
									   										<li><span class="text-orange">总库存</span><el-tag type="primary">{{FBAInvData.afnTotalQuantity}}</el-tag></li>
									   										<li><span>可用库存</span><el-tag type="success">{{FBAInvData.afnFulfillableQuantity}}</el-tag></li>
									   										<li><span>预留</span><el-tag>{{FBAInvData.afnReservedQuantity}}</el-tag></li>
									   										<li><span>正在发货</span><el-tag>{{FBAInvData.afnInboundWorkingQuantity}}</el-tag></li>
									   										<li><span>待接收</span><el-tag>{{FBAInvData.afnInboundShippedQuantity}}</el-tag></li>	 
									   										<li><span>已接收</span><el-tag>{{FBAInvData.afnInboundReceivingQuantity}}</el-tag></li>
									   										<li><span>不可用</span><el-tag type="info">{{FBAInvData.afnUnsellableQuantity}}</el-tag></li>
									   										<li><span>异常</span><el-tag type="danger">{{FBAInvData.afnResearchingQuantity}}</el-tag></li>
									   										
									   										<li v-if="scope.row.mregion=='EU'"><span>站点库存</span><el-tag type="warning">{{scope.row.countryinv}}</el-tag></li>
									   									</ul>
									   									<el-button  class="m-t-8 btn-block" type="primary" @click.stop="refreshInventory(scope.row)">更新</el-button>
									   							</div>
									   					</el-col>
									   								<el-col :span="17">
									   									<el-row :gutter="8"   v-if="FBAInvPlanData">
									   										<el-col :span="9">
									   											<div class="gary-wrap-card">
									   													<span class="font-bold text-black">-</span>
									   													<div class="font-small ">低量库存费
									   													<span class="font-extraSmall ">(周更)</span>
									   													</div>
									   											</div>
									   											<div class="gary-wrap-card">
									   													<span class="font-bold text-black" v-if="FBAInvPlanData.historicalDaysOfSupply">{{FBAInvPlanData.historicalDaysOfSupply}}</span>
									   													<span class="font-bold text-black" v-else>-</span>
									   													<div class="font-small ">历史供货天数</div>
									   											</div>
									   										</el-col>
									   										<el-col :span="15">
									   											<div class="his-wrap-card">
									   												<div class="el-h5">库存状况和建议
									   												<span class="font-extraSmall">(件)</span>
									   												</div>
									   												<div class="inv-card " 
									   												:class="FBAInvPlanData.fbaInventoryLevelHealthStatus==='Healthy'?'bg-green':'bg-red'">
									   													<span>FBA</span>
									   													<span class="font-medium font-bold "
									   													:class="FBAInvPlanData.fbaInventoryLevelHealthStatus==='Healthy'?'text-green':'text-red'"
									   													>{{FBAInvPlanData.fbaInventoryLevelHealthStatus}}</span>
									   												</div>
									   												<div class="inv-footer">
									   													<div class="m-r-16">
									   														<span class=" ">
									   															<span class="text-orange" v-if="RecommendedAction[FBAInvPlanData.recommendedAction]">
									   																{{RecommendedAction[FBAInvPlanData.recommendedAction]}}
									   															</span>
									   															<span class="text-orange" v-else>{{FBAInvPlanData.recommendedAction}}</span>
									   															<span v-if="FBAInvPlanData.alert" class="text-red">/
									   															   <span v-if="FBAInvPlanData.alert=='Low conversion'">转化低</span>
									   															   <span v-else-if="FBAInvPlanData.alert=='Low traffic'">流量低</span>
									   															   <span v-else></span>
									   															</span>
									   														</span>
									   														<div class="font-small m-t-8">建议</div>
									   													</div>
									   													<div>
									   														<span class="font-bold " style="color:#333" v-if="FBAInvPlanData.fbaMinimumInventoryLevel">{{FBAInvPlanData.fbaMinimumInventoryLevel}}</span>
									   														<span class="font-bold " style="color:#333" v-else>-</span>
									   														<div class="font-small m-t-8">建议最低水平</div>
									   													</div>
									   												</div>
									   											</div>
									   										</el-col>
									   									</el-row>
									   				
									   									
									   									
									   									<table class="sd-table m-t-16"    border="0" cellpadding="0" cellspacing="0">
									   										<thead>
									   											<tr>
									   												<th  width="130">仓库名称</th>
									   												<th  width="50">可用</th>
									   												<th width="60">待出库</th>
									   												<th width="60">待入库</th>
									   											</tr>
									   										</thead>
									   										<tbody>
									   											<tr v-for = "(item,index) in localInvData" :key="index" >
									   												<td>{{item.warehousename}}</td>
									   												<td>{{item.fulfillable}}</td>
									   												<td>{{item.outbound}}</td>
									   												<td>{{item.inbound}}</td>
									   										    </tr>
									   											<tr v-if="localInvData.length<=0">
									   												<td colspan="4"> <el-empty :image-size="80" description="暂无数据" /></td>
									   											</tr>
									   										</tbody>
									   										</table>
									   								</el-col>
									   								</el-row>
									   						 	<template #reference>
									   								<span class="pointer" @click.tsop="loadInventory(scope.row)">
									   									{{NullTransform(scope.row.afnFulfillableQuantity)}}  
									   								</span>
									   								 <el-tag
									   								      effect="plain"
									   								      round
									   									  type="warning"
									   									  v-if="scope.row.fulfillchannel=='DEFAULT'"
									   								    >
									   								    自发货
									   								    </el-tag>
									   						 	</template>
									   </el-popover>
									   &nbsp; 
									   <el-popover v-if="scope.row.mregion=='EU'" title="EU库存详情"  placement="bottom" trigger="click" :width="226">
									   	<el-table :data="FBAEUlist">
									   		<el-table-column>
									   			<el-table-column prop="country" label="国家" width="100" >
									   				<template #default="scope">
									   					<span v-if="scope.row.country=='GB'">UK</span>
									   					<span v-else>{{scope.row.country}}</span>
									   				</template>
									   			</el-table-column>
									   			<el-table-column prop="quantity" label="库存数量" width="100" />
									   		</el-table-column>
									   	</el-table>
									   	<template #reference>
									   		<span class="font-extraSmall pointer" @click.stop="loadEUInventory(scope.row)">
									   			<span v-if="scope.row.country=='DE' || scope.row.country=='PL'">
									   				(DE+PL+CZ:{{NullTransform(scope.row.czinv)}})  
									   			</span>
									   			<span v-else>({{scope.row.country}}:{{NullTransform(scope.row.countryinv)}})</span>
									   		</span>
									   	</template>
									   </el-popover>
									     </div>
								   </el-col>
						   </el-row>
				       </template>
				   </el-table-column>
				</template>
			   </GlobalTable>
		</div>
	</div>
	<ModifyPriceDialog ref="modifypriceRef" @change="handleAfterModifyPrice"/>
	<FbaPopover ref="poporef"/>
	<FollowDialog ref='followRef'/>
	 <FbaInvHistoryDialog ref="fbaInvHistoryDialogRef"></FbaInvHistoryDialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs, computed, nextTick, watch,} from 'vue';
	import Group from '@/components/header/group.vue';
	import {Help,Plus,MenuUnfold,ChartHistogram,Slide,ChartLine,DownTwo,UpTwo,SortOne} from '@icon-park/vue-next';
	import {ElMessage,ElMessageBox,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Edit,MoreFilled,CaretTop,InfoFilled,Avatar,Histogram,WarnTriangleFilled} from '@element-plus/icons-vue';
	import * as echarts from 'echarts';
	import {useRoute}from"vue-router";
	import {formatFloat,formatPercent,formatInteger,decodeText,getCurrencyMark} from '@/utils/index.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import ModifyPriceDialog from "@/views/amazon/listing/products/components/modifypriceDialog.vue"
	import FollowDialog from "@/views/amazon/listing/products/components/follow_dialog.vue"
	import FbaPopover from"./components/fbafee_popover.vue";
	import FbaInvHistoryDialog from "@/views/amazon/listing/products/components/fbainvhistory_dialog.vue";
	import NullTransform from"@/utils/null-transform";
	import RecommendedAction from '@/model/amazon/ship/RecommendedAction.json';
	import productinoptApi from '@/api/amazon/product/productinoptApi.js';
	import inventoryApi from "@/api/erp/inventory/inventoryApi.js";
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import materialApi from '@/api/erp/material/materialApi.js';
	import productinfoApi from "@/views/customized/chelsea/api/productionform/productinfoApi.js";
	const datepickersRef =ref();
	const followRef =ref();
	const globalTable =ref();
	const poporef =ref(null);
	const modifypriceRef =ref();
	const popoverRefs = ref([]);
	const fbaInvHistoryDialogRef=ref();
    const state = reactive({
		tableData:{
			records:[{
				image:'',
				sku:"abc",
			}],
			total:1,
		},
		searchKeywords:"",
		times:"近7天",
		priceList:[],
		currentSolt:"desc",
		btnloading:false,
		currentRank:"sku",
		queryParams:{
			fromDate:null,
			endDate:null,
		},
		localInvData:[],
		FBAInvData:{},
		FBAInvPlanData:{},
		rankData:[
			{
				name:'SKU',
				value:'sku',
			},
		{
			name:'日均销量',
			value:'averageSalesDay',
		},
		
		{
			name:'库存',
			value:'afn_fulfillable_quantity',
		},{
			name:'跟卖数',
			value:'notread,followcount',
		},
		{
			name:'可售天数',
			value:'dayfulfilla',
		},
		{
			name:'利润',
			value:'profits',
		},
		{
			name:'价格',
			value:'landed_amount',
		},
		{
			name:'访问量',
			value:'sessions',
		},
		{
			name:'点击量',
			value:'advclick',
		},
		{
			name:'广告花费',
			value:'advspend',
		},
		{
			name:'净利润',
			value:'profitall',
		},
		{
			name:'产品利润率',
			value:'newprorate',
		},
		{
			name:'上架日期',
			value:'opendate',
		},
		{
			name:'Review数量',
			value:'feedback_count',
		},
		{
			name:'Review评分',
			value:'positive_feedback_rating',
		},
		{
			name:'父ASIN',
			value:'parentAsin',
		},
		],
		soltData:[{name:'从高到低',value:'desc'},{name:'从低到高',value:"asc"}],
	})
    const {
		times,
		queryParams,
		searchKeywords,
		rankData,
		soltData,
		currentSolt,
		currentRank,
		btnloading,
		priceList,
		tableData,
		localInvData,
		FBAInvData,
		FBAInvPlanData,
	} = toRefs(state)
	
	
    function changedate(dates){
    	state.queryParams.fromDate=dates.start;
    	state.queryParams.endDate=dates.end;
		handleQuery();
    }
	function changeTimes(val){
		var end = new Date();
		var start = new Date();
		var beforedays=0;
		end.setTime(end.getTime() - 3600 * 1000 * 24 * (beforedays+1));
		var array=[start, end];
		if(val=="昨日"){
			 start.setTime(start.getTime() - 3600 * 1000 * 24 * (1+beforedays))
		}
		if(val=="近7天"){
			 start.setTime(start.getTime() - 3600 * 1000 * 24 * (7+beforedays))
		}
		if(val=="近15天"){
			start.setTime(start.getTime() - 3600 * 1000 * 24 * (15+beforedays))
		}
		datepickersRef.value.dateValue=array;
		datepickersRef.value.dateChange(array);
	}
	function handleReport(){
		state.queryParams.searchtype="sku";
		state.queryParams.search=state.searchKeywords;
		state.btnloading=true;
		productinfoApi.downloadReport(state.queryParams,()=>{
			state.btnloading=false;
		}) 
	}
  function  expandChange(row,expand){
	   var isExpand=false;
	   if(expand && expand.length>0){
		   expand.forEach(item=>{
			   if(item.id==row.id){
				   isExpand=true;
			   }
		   });
	   }
	   var param=JSON.parse(JSON.stringify(row));
	   param.fromDate=state.queryParams.fromDate;
	   param.endDate=state.queryParams.endDate;
	   if(isExpand==true){
		  productinfoApi.chart(param).then((res)=>{
			   const labels =res.data.labels;
			   const series1 = [{
				data: res.data.orderitems,
				color:'#ff7315',
				lineStyle:{
					color:'#ff7315',
				},
				itemStyle:{
					color:'#ff7315',
				},
				label:{
					show:true,
				},
				type: 'line',
				showSymbol: false,
				smooth: 0.5,
				areaStyle: {
				opacity: 0.1 ,
				 },
			   }];
			   const series2 = [{
				data: res.data.advSpend,
				lineStyle:{
					color:'#17C565',
				},
				itemStyle:{
					color:'#17C565',
				},
				color:'#17C565',
				label:{
					show:true,
				},
				type: 'line',
				showSymbol: false,
				smooth: 0.5,
				areaStyle: {
				opacity: 0.1 ,
				 },
			   }];
			   const series3 = [{
					data: res.data.orderProfit,
					lineStyle:{
						color:'#409EFF',
					},
					itemStyle:{
						color:'#409EFF',
					},
					color:'#409EFF',
					label:{
						show:true,
					},
					type: 'line',
					showSymbol: false,
					smooth: 0.5,
					areaStyle: {
					opacity: 0.1 ,
					 },
			   }];
			   
			   nextTick(()=>{
				getchart(labels,series1,'orderSales'+row.id);
				getchart(labels,series2,'adChart'+row.id);
				getchart(labels,series3,'orderProfit'+row.id);
				
			   })
		   });
	   }
	  
   }
	 function updatePrice(id){
		 productinoptApi.refreshPrice(id).then(res=>{
			  ElMessage.success('操作成功！');
		 });
	 }
	 function showPrice(pid){
	 			 productinoptApi.findPriceById({"pid":pid}).then((res)=>{
	 				 if(res.data){
	 					 state.priceList=res.data;  
	 				 }
	 			 });
	 }
	 //排序
	 function rankChange(val){
	 	state.currentRank= val;
	 	if(val=='sku'){
	 		state.currentSolt="asc";
	 	}
	 	state.queryParams.sort=val;
	 	state.queryParams.order=state.currentSolt;
	 	handleQuery();
	 }
	 function soltChange(val){
	 	state.currentSolt =val;
	 	state.queryParams.sort=state.currentRank;
	 	state.queryParams.order=val;
	 	handleQuery();
	 }
	 function handleAfterModifyPrice(data){
	 			   if(data&&data.ftype=="1"){
	 				   var paramitem=null;
	 				   state.tableData.records.forEach(item=>{
	 				  	 if(item.id==data.pid){
	 				  	    item.landedAmount=data.price;
	 						paramitem=item;
	 				  	 }
	 				   });
	 				   if(paramitem!=null){
	 					   var data={"sku":paramitem.sku,"marketplace":paramitem.marketplaceid,"groupid":paramitem.groupid}
	 					   productinfoApi.list(data).then((res)=>{
	 					   	if(res.data.records && res.data.total>0){
	 					   		 res.data.records.forEach(item=>{
	 								 if(item.id==paramitem.id){
	 									 paramitem.newprorate=item.newprorate;
	 								 }
	 							 })
	 					   	}
	 					   });	
	 				   }
	 			   }
	 }
	 // function getDigital(row){
		//   var referral=row.fba.estimated_referral_fee_per_unit;
		//   var fba=row.fba.estimated_fee_total-row.fba.estimated_referral_fee_per_unit;
		//   var digit=0;
		//   if(row.country=="UK"){
		// 	digit=referral*0.02+fba*0.02;
		//   }
		//   if(row.country=="IT"){
		//   	digit=referral*0.02;  
		//   }
		//   if(row.country=="FR"){
		//   	digit=referral*0.02;  
		//   }
		//   if(row.country=="ES"){
		//   	digit=referral*0.02;  
		//   }
		//  return digit*row.ordersales;
	 // }
	 function changeGroup(obj){
	 	state.queryParams.groupid=obj.groupid;
	 	state.queryParams.marketplace=obj.marketplaceid;
	 	handleQuery();
	 }
	 function modifyPrice(row){
		 modifypriceRef.value.ismore=false;
		 modifypriceRef.value.localData=row;
		 modifypriceRef.value.modifyPriceVisable = true
	 }
	function rowClick(row){
		globalTable.value.toggleRowExpansion(row)
	}
	
   function getchart(labels,series,id){
	  var myChart = echarts.init(document.getElementById(id));
	  var title="";
	  if(id.indexOf('adChart')>=0){
		  title="广告花费"
	  }else if(id.indexOf("orderProfit")>=0){
		  title="广告花费/销量"
	  }else{
		  title="销量"
	  }
	  var option = {
		  title:{
			  text:title,
			  textStyle:{
				  fontSize:14,
				  fontWeight:400,
			  },
		  },
		   tooltip : { trigger : 'axis', },
		  xAxis: {
		  	type: 'category',
		  	data: labels,
		  	boundaryGap:false,
		  	axisLine:{
		  	  show: false
		  	},
		  	axisTick:{
		  	show: false
		  	},
		  	axisLabel:{
		  	   color:"#acb0b9"
		  	},
		    },
		  		
		  yAxis: {
		  	axisLabel:{
		       color:"#acb0b9"
		  	},
		  	splitLine:{
		  		lineStyle:{
		  		color:"rgba(170,170,170,0.2)"
		  		}
		  	}
		  },
		  series:series,
	  }
	  myChart.setOption(option);
	  window.addEventListener('resize',()=>{
	  	  		   myChart.resize();
	  })
  }
 
 
   function adjustPrice(){
	   modifypriceRef.value.modifyPriceVisable = true;
   }
   
 //   function  getProfit(row,ftype){
 //   	var value=0;
	// if(row.orderprice){
	// 	value=row.orderprice;
	// }else{
	// 	return 0;
	// }
	// if(row.param&&row.param.costDetailMap &&row.param.costDetailMap.storageFee){
	// 	value=value-formatFloat(row.param.costDetailMap.storageFee*row.ordersales);
	// }
	// if(row.param&&row.param.costDetailMap &&row.param.costDetailMap.purchase){
	// 	value=value-formatFloat(row.param.costDetailMap.purchase*row.ordersales);
	// }
	// if(row.advspend){
	// 	value=value-formatFloat(row.advspend);
	// }
	// // if(row.othercost){
	// // 	value=value-formatFloat(row.othercost*row.ordersales);
	// // }
	// if(row.param && row.param.costDetailMap&& row.param.costDetailMap.fba){
	// 	value=value-formatFloat(row.param.costDetailMap.fba*row.ordersales);
	// }
	// if(row.param && row.param.costDetailMap&& row.param.costDetailMap.vatRate){
	// 	value=value-formatFloat(row.param.costDetailMap.vatRate*row.orderprice/(1+row.param.costDetailMap.vatRate));
	// }
	// if(row.fba&&row.fba.estimated_referral_fee_per_unit){
	// 	value=value-formatFloat(row.fba.estimated_referral_fee_per_unit*row.ordersales);
	// }else if(row.param && row.param.costDetailMap&& row.param.costDetailMap.referralFee){
	// 		value=value-formatFloat(row.param.costDetailMap.referralFee*row.ordersales);
	// }
	// if(row.fba){
	// 	value=value-formatFloat(getDigital(row));
	// }
	// if(row.param && row.param.costDetailMap&& row.param.costDetailMap.refundprice){
	// 	value=value-formatFloat(row.param.costDetailMap.refundprice);
	// }
	// if(row.budget_spend){
	// 	value=value-formatFloat(row.budget_spend);
	// }
	// if(ftype=='rate'){
	// 	return formatFloat(value/row.orderprice);
	// }else{
	// 	return formatFloat(value);
	// }
 //   }

   function getFbaRef(el,row){
	   if(el){
		   popoverRefs.value[`popover_${row.id}`] = el
	   }
   }
   function setFbaRef(el,row){
	    poporef.value.buttonRef =  popoverRefs.value[`popover_${row.id}`];
		var timer= setTimeout(function(){
			if(poporef.value){
				 poporef.value.show(row);
			}
		  clearTimeout(timer);
		 },100)
   }
   function handleQuery(){
	   state.queryParams.searchtype="sku";
	   state.queryParams.search=state.searchKeywords;
	   globalTable.value.loadTable(state.queryParams);
   }
   function loadtableData(data){
   			 productinfoApi.list(data).then((res)=>{
   				 if(res.data&&res.data.records&&res.data.records.length>0){
   				 	res.data.records.forEach(item=>{
   						if(item.remark){
   						    item.htmlremark=decodeText(item.remark);
   						}else{
   							item.htmlremark="";
   						}
   					});
   				 }
   				 if(res.data){
   					state.tableData.records = res.data.records;
   					state.tableData.total =res.data.total 
   				 }else{
   					 state.tableData.records = [];
   					 state.tableData.total =0; 
   				 }
   			 });
   }
   function exceedFee(row){
	  var timer= setTimeout(function(){
	    poporef.value.show(row);
	  clearTimeout(timer);
	   },100)
   }
   
   function checkFBA(fbafeerow,localfbafee){
	   var diff=0;
	   if(fbafeerow && localfbafee){
		   if(fbafeerow&&fbafeerow.estimated_fee_total){
				 diff = formatFloat(fbafeerow.estimated_fee_total - fbafeerow.estimated_referral_fee_per_unit);
				 if (fbafeerow.estimated_fee_total==fbafeerow.estimated_referral_fee_per_unit) {
							   if(fbafeerow.expected_efn_fulfilment_fee_per_unit_uk){
								   diff=formatFloat(fbafeerow.expected_efn_fulfilment_fee_per_unit_uk);
							   }else if(fbafeerow.expected_efn_fulfilment_fee_per_unit_de){
								   diff=formatFloat(fbafeerow.expected_efn_fulfilment_fee_per_unit_de);
							   }else if(fbafeerow.expected_efn_fulfilment_fee_per_unit_it){
								   diff=formatFloat(fbafeerow.expected_efn_fulfilment_fee_per_unit_it);
							   }
							 }
		     }
		   if(localfbafee){
		   	   diff=diff-formatFloat(localfbafee) ;
		   }
		   if(diff<0){
		   		   return true;
		   }else{
		   		   return false;
		   }
	   }else{
		    return false; 
	   }
	   
	   
   }
   
   function submitPrice(localrow){
	    var ftype="cost";
	    var data=[];
		var row={};
		row.id=localrow.mid;
		if(localrow.mprice){
			row.price=parseFloat(localrow.mprice);
			data.push(row);
			materialApi.updateMaterial(ftype,data).then((res)=>{
				ElMessage.success('操作成功！');
			})
		}
   }
   
   function followSeller(row){
	   followRef.value.show(row);
   }
   function loadInventory(rows){
   			 var msku=rows.sku;
   			 if(rows.msku){
   				 msku=rows.msku;
   			 }
   			 if(rows.itemshow==false){
   				 //本地库存
   				 inventoryApi.getInventoryByMaterialSKU({"sku":msku}).then((ress)=>{
   				 	state.localInvData=ress.data;
   				 })
   				 //fba库存
   				 inventoryRptApi.findFBA({"sku":rows.sku,"groupid":rows.groupid,"marketplaceid":rows.marketplaceid}).then((res)=>{
   						 state.FBAInvData=res.data.fbainv;
   						 state.FBAInvPlanData=res.data.invplandata;
   				 })
   			 }
   }
   function loadEUInventory(rows){
   			 inventoryRptApi.findEUFBA({"sku":rows.sku,"authid":rows.amazonAuthId}).then((res)=>{
   			 	if(res.data && res.data.length>0){
   					FBAEUlist.value=res.data;
   				}else{
   					FBAEUlist.value=[];
   				}
   			 })
   }
   function refreshInventory(rows){
   			 //刷新fba库存
   			 inventoryRptApi.syncInventorySupply({"skus":rows.sku,"groupid":rows.groupid,"marketplaceid":rows.marketplaceid}).then((res)=>{
   				 if(res.data){
   					 rows.afnFulfillableQuantity=res.data.afnFulfillableQuantity;
   					 ElMessage.success('更新成功！');
   					rows.itemshow=false;
   				 }else{
   					 ElMessage.error('更新失败！');
   				 }
   			 })
   			 
   }
   
   function handleShowFbaHistory(row){
   			 fbaInvHistoryDialogRef.value.show(row.groupid,row.marketplaceid,row.sku);
   }
   
  onMounted(()=>{

  })
</script>


<style scoped>
	.con-body{
		margin-top: 16px;
	}
.img-size{
	width: 80px;
	height: 80px;
}
.font-num{
	color: var(--el-color-primary);
}
.his-wrap-card{
		border:1px solid var(--el-border-color-light);
		border-radius:4px;
	}
	.his-wrap-card .el-h5{
        padding:8px 16px;
		font-weight: 600;
		color:#333;
		border-bottom:1px solid var(--el-border-color-light);
	}
	.his-wrap-card .inv-card{
		padding:8px 16px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.inv-footer{
		padding:8px 16px;
		display: flex;
	}
	.m-r-16{
		margin-right:16px;
	}
	.list-style-type{
		list-style-type:none;
	}
	.dark .li-wrap{
		border:1px solid var(--el-border-color-light);
		padding:8px 16px;
		border-radius:4px;
		background-color:#060606;
	}
	.li-wrap{
		border:1px solid var(--el-border-color-light);
		padding:8px 16px;
		border-radius:4px;
		background-color:#fafafa;
	}
	.li-be li{
		display: flex;
		align-items: center;
		justify-content: space-between;
		line-height: 32px;
		border-bottom: 1px solid var(--el-border-color-extra-light);
	}
</style>
<style>
	.r_active{
		background-color: var(--el-dropdown-menuItem-hover-fill);
	    color: var(--el-dropdown-menuItem-hover-color);
		}
	.sortdropdowntitle li{
		padding:5px;
		border-bottom:solid 1px #dedede;
	}
	.sortdropdown li{
		padding:10px;
		cursor: pointer;
	}
	.sortdropdown li:hover{
		color: var(--el-dropdown-menuItem-hover-color);
	}
	.radioWrapper .el-radio-group{
		flex-wrap: nowrap;
	}
	</style>