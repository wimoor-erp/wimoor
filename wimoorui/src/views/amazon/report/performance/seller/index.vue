<template>
<div class="main-sty">
<div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
		</div>
	<el-row :gutter="16">
	     <el-col :span="8">
			 <el-card shadow="never">
				 <template #header>
					 <el-space>
						 <el-icon class="icon-bg-blue icon-bg" :size="12"><UserFilled /></el-icon>
						 <h3>客户服务绩效</h3>
					 </el-space>
				 </template>
				 
				 <div class="flex-center-between">
					 <el-space direction="vertical" alignment="normal">
					  <span class="be-point flex-center font-bold">订单缺陷率</span>
					  <div class="font-extraSmall">
						  订单日期：{{dataList.orderDefectRate?.afn.reportingDateRange.reportingDateFrom.substring(0,10)}}
						   -
							{{dataList.orderDefectRate?.afn.reportingDateRange.reportingDateTo.substring(0,10)}}
						</div>
					 </el-space>
					 <el-space direction="vertical">
						 <span>低于 <b>{{dataList.orderDefectRate?.afn.targetValue*100}}%</b></span>
						 <span class="font-extraSmall">目标</span>
					 </el-space>
				 </div>
				 <el-divider></el-divider>
				 <el-row :gutter="16">
					 <el-col :span="12">
						 <el-space direction="vertical" alignment="normal" fill style="width: 100%;">
						 <p class="font-base">卖家自配送</p>
						 <h3>{{dataList.orderDefectRate?.mfn.rate.toFixed(2)}}%</h3>
						 <div>
							 <span class="font-extraSmall">订单总数:</span>
							 <span class="font-small">{{dataList.orderDefectRate?.mfn.orderCount}}</span>
						 </div>
						 <div class="dark-bg-direct">
							 <span >存在缺陷的订单:</span>
							 <span >{{dataList.orderDefectRate?.mfn.orderWithDefects.count}}</span>
						 </div>
						 <div class="font-small line-under flex-center-between">
						 	 <span>Feedback差评</span>
						 	 <span>{{dataList.orderDefectRate?.mfn.negativeFeedback.count}}</span>
						   </div>
						  <div class="font-small line-under flex-center-between">
						 	 <span>亚马逊商城交易保障索赔</span>
						 	 <span>{{dataList.orderDefectRate?.mfn.claims.count}}</span>
						 	 </div>
						  <div class="font-small line-under flex-center-between">
						 	 <span>信用卡拒付索赔</span>
						 	 <span>{{dataList.orderDefectRate?.mfn.chargebacks.count}}</span>
						 </div>
						 </el-space>
					 </el-col>
					 <el-col :span="12">
						 <div>
						 <el-space direction="vertical" alignment="normal" fill style="width: 100%;">
						 <p class="font-base">FBA配送</p>
						 <h3>{{dataList.orderDefectRate?.afn.rate.toFixed(2)}}%</h3>
						 <div>
							 <span class="font-extraSmall">订单总数:</span>
							 <span class="font-small">{{dataList.orderDefectRate?.afn.orderCount}}</span>
						 </div>
						 <div class="dark-bg-direct">
							 <span>存在缺陷的订单:</span>
							 <span >{{dataList.orderDefectRate?.afn.orderWithDefects.count}}</span>
						 </div>
						 
						 <div class="font-small line-under flex-center-between">
							 <span>Feedback差评</span>
							 <span>{{dataList.orderDefectRate?.afn.negativeFeedback.count}}</span>
						  </div>
						 <div class="font-small line-under flex-center-between">
							 <span>亚马逊商城交易保障索赔</span>
							 <span>{{dataList.orderDefectRate?.afn.claims.count}}</span>
							 </div>
						 <div class="font-small line-under flex-center-between">
							 <span>信用卡拒付索赔</span>
							 <span>{{dataList.orderDefectRate?.afn.chargebacks.count}}</span>
						</div>
						 </el-space>
						 </div>
					 </el-col>
				 </el-row>
				 <div class="flex-center-between cus-item">
						 <el-space direction="vertical" alignment="normal">
						  <span class="be-point flex-center font-bold">发票缺陷率</span>
						<div class="font-extraSmall">
										订单日期：{{dataList.invoiceDefectRate?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										 -
										{{dataList.invoiceDefectRate?.reportingDateRange.reportingDateTo.substring(0,10)}}
									</div>
						 </el-space>
						 <el-space direction="vertical">
							 <span>低于 <b>{{dataList.invoiceDefectRate?.targetValue*100}}%</b></span>
							 <span class="font-extraSmall">目标</span>
						 </el-space>
				 </div>
				 <el-divider></el-divider>
				 <el-row :gutter="16">
				 					 <el-col :span="12">
				 						 <el-space direction="vertical" alignment="normal" fill style="width: 100%;">
				 						 <p class="font-base">缺陷率</p>
				 						 <h3>{{dataList.invoiceDefectRate?.rate.toFixed(2)}}%</h3>
				 						 <div>
				 							 <span class="font-extraSmall">订单总数:</span>
				 							 <span class="font-small">{{dataList.invoiceDefectRate?.orderCount}}</span>
				 						 </div>

				 						 <div class="font-small line-under flex-center-between">
				 						 	 <span>逾期发票订单</span>
				 						 	 <span>{{dataList.invoiceDefectRate?.lateInvoice.count}}</span>
				 						   </div>
				 						  <div class="font-small line-under flex-center-between">
				 						 	 <span>遗失发票订单</span>
				 						 	 <span>{{dataList.invoiceDefectRate?.missingInvoice.count}}</span>
				 						 	 </div>
				 						  <div class="font-small line-under flex-center-between">
				 						 	 <span>缺陷发票订单</span>
				 						 	 <span>{{dataList.invoiceDefectRate?.invoiceDefect.count}}</span>
				 						 </div>
				 						 </el-space>
				 					 </el-col>
				 </el-row>
			 </el-card>
		 </el-col>
	     <el-col :span="8">
			<el-card shadow="never">
				<template #header>
						 <el-space>
							<el-icon class="icon-bg-orange icon-bg" :size="14"><StarFilled /></el-icon>
							<h3>商品政策合规性</h3>
						</el-space>
				</template>
				<div class="flex-center-between">
									 <el-space direction="vertical" alignment="normal">
									  <span class="be-point flex-center font-bold">账号健康评级</span>
									  <div class="font-extraSmall">
										  订单日期：{{dataList.accountHealthRating?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										   -
										  	{{dataList.accountHealthRating?.reportingDateRange.reportingDateTo.substring(0,10)}}
									  </div>
									 </el-space>
									 <el-space direction="vertical" alignment="normal" :size="2">
										 <div class="pro-wrap">
											 <div class="pro-text "
											 :class="accountHealth(dataList.accountHealthRating?.ahrScore)"
											 >{{dataList.accountHealthRating?.ahrStatus}}</div>
											 <div class="pro-bar"></div>
										 </div>
										 <span class="font-extraSmall">得分：{{dataList.accountHealthRating?.ahrScore}}</span>
									 </el-space>
				</div>
				<el-divider></el-divider>
				<el-space direction="vertical" alignment="normal" :size="4" style="width: 100%;">
				<div class="flex-center-between font-base">
					<span>商品政策违规</span>
					<span class="font-bold">{{dataList.listingPolicyViolations?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>商品真实性投诉</span>
					<span class="font-bold">{{dataList.productAuthenticityCustomerComplaints?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>商品状况买家投诉</span>
					<span class="font-bold">{{dataList.productConditionCustomerComplaints?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>商品安全买家投诉</span>
					<span class="font-bold">{{dataList.productSafetyCustomerComplaints?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>受限商品政策违规</span>
					<span class="font-bold">{{dataList.restrictedProductPolicyViolations?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>知识产权投诉</span>
					<span class="font-bold">{{dataList.receivedIntellectualPropertyComplaints?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>涉嫌侵犯知识产权</span>
					<span class="font-bold">{{dataList.suspectedIntellectualPropertyViolations?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>食品和产品安全问题</span>
					<span class="font-bold">{{dataList.foodAndProductSafetyIssues?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>商品评论政策违规</span>
					<span class="font-bold">{{dataList.customerProductReviewsPolicyViolations?.defectsCount}}</span>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between font-base">
					<span>其他政策违规</span>
					<span class="font-bold">{{dataList.otherPolicyViolations?.defectsCount}}</span>
				</div>
				</el-space>
			</el-card>
		 </el-col>
	     <el-col :span="8">
			 <el-card shadow="never">
			 	<template #header>
			 			 <el-space>
			 				<el-icon class="icon-bg-green icon-bg" :size="13"><Promotion /></el-icon>
			 				<h3>配送绩效</h3>
			 			</el-space>
			 	</template>
				<div class="flex-center-between">
									 <el-space direction="vertical" alignment="normal">
									  <span class="be-point flex-center font-bold">迟发率</span>
									  <div class="font-extraSmall">
										  订单日期：{{dataList.lateShipmentRate?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										   -
										  	{{dataList.lateShipmentRate?.reportingDateRange.reportingDateTo.substring(0,10)}}
									  </div>
									 </el-space>
									 <el-space direction="vertical">
										 <span class="font-bold">{{dataList.lateShipmentRate?.rate.toFixed(2)}}%</span>
										 <span class="font-extraSmall">目标:低于{{dataList.lateShipmentRate?.targetValue*100}}%</span>
									 </el-space>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between">
									 <el-space direction="vertical" alignment="normal">
									  <span class="be-point flex-center font-bold">准时交货率</span>
									  <div class="font-extraSmall">
										  订单日期：{{dataList.onTimeDeliveryRate?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										   -
										  	{{dataList.onTimeDeliveryRate?.reportingDateRange.reportingDateTo.substring(0,10)}}
									  </div>
									 </el-space>
									 <el-space direction="vertical">
										 <span class="font-bold">{{(100-dataList.onTimeDeliveryRate?.rate).toFixed(2)}}%</span>
										 <span class="font-extraSmall">目标:高于{{dataList.onTimeDeliveryRate?.targetValue*100}}%</span>
									 </el-space>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between">
									 <el-space direction="vertical" alignment="normal">
									  <span class="be-point flex-center font-bold">有效追踪率</span>
									  <div class="font-extraSmall">
										  订单日期：{{dataList.validTrackingRate?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										   -
										  	{{dataList.validTrackingRate?.reportingDateRange.reportingDateTo.substring(0,10)}}
									  </div>
									 </el-space>
									 <el-space direction="vertical">
										 <span class="font-bold">{{(100-dataList.validTrackingRate?.rate).toFixed(2)}}%</span>
										 <span class="font-extraSmall">高于:低于{{dataList.validTrackingRate?.targetValue*100}}%</span>
									 </el-space>
				</div>
				<el-divider></el-divider>
				<div class="flex-center-between">
									 <el-space direction="vertical" alignment="normal">
									  <span class="be-point flex-center font-bold">预配送取消率</span>
									  <div class="font-extraSmall">
										  订单日期：{{dataList.preFulfillmentCancellationRate?.reportingDateRange.reportingDateFrom.substring(0,10)}}
										   -
										  	{{dataList.preFulfillmentCancellationRate?.reportingDateRange.reportingDateTo.substring(0,10)}}
									  </div>
									 </el-space>
									 <el-space direction="vertical">
										 <span class="font-bold">{{dataList.preFulfillmentCancellationRate?.rate.toFixed(2)}}%</span>
										 <span class="font-extraSmall">目标:低于{{dataList.preFulfillmentCancellationRate?.targetValue*100}}%</span>
									 </el-space>
				</div>
			 </el-card>
		 </el-col>
	</el-row>
 </div>
</template>
<script>
    export default{ name:"店铺绩效" };
</script>
<script setup>
	import {UserFilled,Promotion,StarFilled}from '@element-plus/icons-vue';
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import authApi from "@/api/amazon/auth/authApi.js";
	import MyHeader from "./header.vue"
	import {dateFormat,formatFloat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
			let state=reactive({
				dataList:{},
				queryParams:{},
			});
			defineExpose({
			  loadData,
			})
			
			 const {
			   dataList,
			 } = toRefs(state);
			 
			onMounted(()=>{
				 
			})
		   function accountHealth(val){
			   if(val<100){
				   return "pro-danger"
			   }else if(val<200){
				   return "pro-warning" 
			   }else{
				   return "pro-great"
			   }
		   }
			function loadData(params){
				 authApi.getPerformance(params).then((res)=>{
				 	 console.log(res);
					 if(res&&res.data){
						 state.dataList =res.data.performanceJson;
					 }
				 })
			}
	 
</script>

<style scoped>
	.pro-bar{
		 width:120px;
		 height:12px;
		 background: linear-gradient(to right, #df3a3a 0%, #df3a3a 33.33%, #ffc941 33.33%, #ffc941 66.66%, #67c23a 66.66%, #67c23a 100%);
		 margin-top:4px;
	   }
.icon-bg{
	color: #fff;
	width: 20px;
	height: 20px;
	border-radius: 4px;
}
.icon-bg-blue{
	background-color:#3c8bfa;
}
.icon-bg-orange{
	background-color:#ff7315;
}
.icon-bg-green{
	background-color:#67c23a;
}
.be-point::before{
	content: "";
	width: 6px;
	height: 6px;
	background-color: #333;
	border-radius: 6px;
	margin-right: 8px;
}
.dark-bg-direct::before{
	content: "";
	width: 12px;
	height: 12px;
	display: inline-block;
	border-radius: 2px;
	transform: rotate(45deg);
	background-color: #dcdcdc;
	position: absolute;
	bottom: -6px;
	left:48%;
}
.dark-bg-direct{
	position: relative;
	background-color: #dcdcdc;
	color:#666;
	font-size: 12px;
	padding:8px;
	border-radius: 4px;
}
.line-under{
	padding-bottom:8px;
	border-bottom:1px solid var(--el-border-color-light);
}
.cus-item{
	margin-top:32px;
}
.pro-text{
	font-size: 12px;
	font-weight:1000;
	position: relative;
	color: #fff;
	display: inline-block;
	padding:2px 4px;
	border-radius: 4px;
}
.pro-text::before{
	content: "";
	width:8px;
	height:8px;
	display: inline-block;
	border-radius: 2px;
	transform: rotate(45deg);
	position: absolute;
	bottom: -3px;
	left:48%;
}
.pro-danger{
	background-color: #f13527;
	left:0%;
}
.pro-danger::before{
	background-color: #f13527;
	
}
.pro-warning{
	background-color: #ffc941;
	left:28%;
}
.pro-warning::before{
	background-color: #ffc941;
}
.pro-great{
	background-color: #67c23a;
	left:60%;
}
.pro-great::before{
	background-color: #67c23a;
}
</style>

