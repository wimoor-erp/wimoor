<template>
	<div v-loading="loading">
	<el-row  :gutter="16" v-if="state.LogisticsResult.length>0">
		<el-col  :span="10">
			<el-card shadow="never"  v-for="logistics in state.LogisticsResult">
			<el-descriptions title="物流单" size="small" :column="1">
			   <el-descriptions-item label="物流编号">{{logistics.logisticsId}}</el-descriptions-item>
			   <el-descriptions-item label="物流公司">{{logistics.logisticsCompanyName}}</el-descriptions-item>
			   <el-descriptions-item label="运单号码">{{logistics.logisticsBillNo}}</el-descriptions-item>
			   <el-descriptions-item label="订单编号">{{logistics.orderEntryIds}}</el-descriptions-item>
			 </el-descriptions>
			 <el-divider></el-divider>
			 <h4 class="m-b-16 m-t-16">商品清单</h4>
			 <div class="flex-center m-t-8" v-for="product in orderResult.productItems">
				 <el-image class="m-r-16" :src="product.productImgUrl[0]"></el-image>
			     <div class="font-small">
					 <p class="name">{{product.name}}</p>
					 <div class="m-t-8">
						 <p><span class="font-extraSmall">单位重量: </span> -</p>
						 <p><span class="font-extraSmall">数量: </span>  {{product.quantity}}{{product.unit}}</p>
					 </div>
				 </div>
			 </div>
			 </el-card>
			 <el-button v-if="orderResult.baseInfo" type="primary" @click="dialog.visible=true" class="m-t-16">打印订单详情</el-button>
		</el-col>
	<el-col  :span="14">
		<el-card class="gray-bg" shadow="never" v-for="trace in logisticsTrace">
		    <template #header>
				<div> <span>物流跟踪</span></div>
				<el-space>
					<div class="font-extraSmall">运单号码:{{trace.logisticsBillNo}}</div>
				</el-space>
		    </template>
		    <el-timeline>
		       <el-timeline-item
		         v-for="(item, index) in trace.logisticsSteps"
		         :key="index"
		         :timestamp="item.acceptTime"
				 :color="index==trace.logisticsSteps.length-1?'#ff6700':''"
		       >
		         {{ item.remark }}
		       </el-timeline-item>
		     </el-timeline>
		  </el-card>
	</el-col>
	</el-row>
	
	<div v-else>
		  <div class="m-t-16">手动保存运单号：</div>	
		  <div class="font-extraSmall">系统不会同步运单号对应物流信息，仅用于查询订单使用</div>
		  <div class="m-t-16">
		  <el-input v-model="logisticsId"></el-input> 
		  </div>	
		  <div class="m-t-16">
		  <el-button type="primary" plain @click="handleBindLogisticsId">确定</el-button> 
		  </div>	
	</div>
	</div>
	<el-dialog
	    v-model="dialog.visible"
	    title="打印订单"
	    width="70%"
	    :before-close="handleClose"
	  >
	      <div v-if="orderResult.baseInfo" class="text-center" id="printPage"  style="padding: 20px;border:1px dashed  #eee;">
	      		<div id="emsType" class="col-lg-12 text-center" style="border-bottom: 1px dashed  #eee; ">
	      			<p class="deviation">
						<span style="font-weight: bolder;font-size: 20px;">订单详情单</span>&nbsp;--
						<span id="orderid" >{{orderResult.baseInfo.id}}</span></p>
	      		</div>
	      
	      		<div id="buyerInfo" class="text-left">
	      			<p class="deviation boldfont" >买家信息</p>
	      			<table  class="table table-bordered">
	      				<tbody id="buyerInfoTbody" >
	      					<tr>
	      						<td class="boldtxt">买家</td>
	      						<td><span id="buyerName" >{{orderResult.baseInfo.buyerContact.name}}</span></td>
	      						<td class="boldtxt">买家会员名</td>
	      						<td><span id="buyerVip" >{{orderResult.baseInfo.buyerContact.imInPlatform}}</span></td>
	      					</tr>
	      					<tr>
	      						<td class="boldtxt">收货地址</td>
	      						<td colspan="3" ><span id="buyerAddress">
								{{orderResult.baseInfo.receiverInfo.toArea}}
								</span></td>
	      					</tr>
	      					<tr>
	      						<td class="boldtxt">手机号码</td>
	      						<td><span id="buyerMobile" >{{orderResult.baseInfo.buyerContact.mobile}}</span></td>
	      						<td class="boldtxt">电话号码</td>
	      						<td><span id="buyerPhone" >{{orderResult.baseInfo.buyerContact.phone}}</span></td>
	      					</tr>
	      					<tr>
	      						<td class="boldtxt">下单公司主体</td>
	      						<td colspan="3" ><span id="buyerCompany">{{orderResult.baseInfo.buyerContact.companyName}}</span></td>
	      					</tr>
	      				</tbody>
	      			</table>
	      		</div>
	      		
	      		<div class="text-left">
	      			<p class="deviation boldfont" >卖家信息</p>
	      			<table  class="table table-bordered">
	      				<tbody id="sellerInfoTbody" >
	      					<tr>
	      						<td class="boldtxt">卖家</td>
	      						<td><span id="sellerName" >{{orderResult.baseInfo.sellerContact.name}}</span></td>
	      						<td class="boldtxt">卖家会员名</td>
	      						<td><span id="sellerVip" >{{orderResult.baseInfo.sellerContact.imInPlatform}}</span></td>
	      					</tr>
	      					<tr>
	      						<td class="boldtxt">手机号码</td>
	      						<td><span id="sellerMobile" >{{orderResult.baseInfo.sellerContact.toMobile}}</span></td>
	      						<td class="boldtxt">电话号码</td>
	      						<td><span id="sellerPhone" >{{orderResult.baseInfo.sellerContact.phone}}</span></td>
	      					</tr>
	      				</tbody>
	      			</table>
	      		</div>
	      
	      		<el-row class="text-left">
					<el-col :span="24"   >
	      			<p class="deviation boldfont" >订单信息</p>
					</el-col>
					<el-col :span="24"   >
	      			     <p class="deviation" style="margin-top: 10px">
						  <span class="boldtxt">交易订单号:</span>
						  <span> {{orderResult.baseInfo.id}}</span>
						  </p> 
	      			</el-col>
					<el-col :span="12"   >
	      			  <div style="margin-top: 10px">
						  <span class="boldtxt">下单时间:</span>
						  <span > {{orderResult.baseInfo.createTime}}</span>
					  </div>	
	      			</el-col>
	      			<el-col :span="12"  >
						 <div style="margin-top: 10px">
	      			   	<span class="boldtxt">发货时间:</span>
						<span>{{orderResult.baseInfo.allDeliveredTime}}</span>
	      		        </div>	
					</el-col>
	      		</el-row>
	      		<div  style="margin-top: 10px" class="text-left">
				<div shadow="never"  v-for="logistics in state.LogisticsResult">
					<p class="deviation boldfont" >收货和物流信息</p>
					<p class="deviation" style="margin-top: 10px">
					 <span class="boldtxt">收货地址:</span>
					 <span> {{logistics.receiver.receiverAddress}}</span>
					 </p> 
					 <p class="deviation" style="margin-top: 10px">
					  <span class="boldtxt">物流编号:</span>
					  <span> {{logistics.logisticsId}}</span>
					  </p> 
					  <p class="deviation" style="margin-top: 10px">
					   <span class="boldtxt">物流公司:</span>
					   <span> {{logistics.logisticsCompanyName}}</span>
					   </p> 
					   <p class="deviation" style="margin-top: 10px">
					    <span class="boldtxt">运单号码:</span>
					    <span>{{logistics.logisticsBillNo}}</span>
					    </p> 
			    </div>
				 <div class="flex-center product-list" >
					 <table  class="table table-bordered">
					 	<tbody id="sellerInfoTbody" >
					 		<tr>
								
					 			<td class="boldtxt">序号</td>
								<td class="boldtxt">图片</td>
					 			<td style="width:300px" class="boldtxt">货品名称</td>
					 			<td class="boldtxt">规格</td>
					 			<td class="boldtxt">数量</td>
								<td class="boldtxt">单价</td>
								<td class="boldtxt">优惠(元)	</td>
								<td class="boldtxt">金额(元)</td>
					 		</tr>
					 		<tr v-for="(product,index) in orderResult.productItems">
								<td > {{index+1}}</td>
					 			<td style="width:40px;height:40px;" > <el-image  :src="product.productImgUrl[0]"></el-image></td>
					 			<td style="width:300px"> {{product.name}}</td>
					 			<td ><div v-for="item in product.skuInfos">{{item.name}}:{{item.value}}</div></td>
					 			<td>{{product.quantity}}</td>
								<td>{{product.price}}/{{product.unit}}</td>
								<td>   <div >{{product.entryDiscount/100}}</div></td>
								<td>   <div >{{product.itemAmount}}</div></td>
					 		</tr>
					 	</tbody>
					 </table>
					 </div>
				 </div>
			<p class="deviation boldfont text-left" > 买家留言</p>
			<p  class="text-left"> {{orderResult.baseInfo.buyerFeedback}}</p>
		  
	      	</div>
	      	<!--endPrint-->
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="dialog.visible = false">取消</el-button>
	   
					 <el-button type="primary" v-print="content">
					   打印
					 </el-button>
	      </span>
	    </template>
	  </el-dialog> 
	
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs} from 'vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {Edit} from '@element-plus/icons-vue';
	import purchasealibabaApi from '@/api/erp/purchase/open1688/purchasealibabaApi.js';
	import OrderStatus from "@/model/erp/purchase/open1688/order_status.json";
 	const emit = defineEmits(['change']);
	const state =reactive({
		LogisticsResult:[],
		logisticsTrace:[],
		orderResult:{},
		entryid:"",
		loading:false,
		logisticsId:"",
		content:{id:"printPage",popTitle:""},
		dialog:{visible:false},
	})
	const {
		loading,LogisticsResult,logisticsTrace,orderResult,dialog,content,logisticsId
	}=toRefs(state)
	function loadLogisticsNumber(entryid){
		purchasealibabaApi.getLogisticsId({'purchaseEntryid':entryid}).then(res=>{
			
		});
	}
	function handleBindLogisticsId(){
		purchasealibabaApi.bindLogisticsId({'purchaseEntryid':state.entryid,'logisticsId':state.logisticsId}).then(res=>{
			loadLogisticsNumber(state.entryid);
			ElMessage.success('绑定成功');
		});
	}
	function show(entryid){
		state.LogisticsResult=[];
		state.logisticsTrace=[];
		state.orderResult={};
		state.entryid=entryid;
		state.loading=true;
		purchasealibabaApi.catchLogisticsInfo({'purchaseEntryid':entryid}).then(res=>{
			state.loading=false;
			var hasdata=false;
			if(res.data){
				if(res.data.LogisticsResult){
					state.LogisticsResult=res.data.LogisticsResult.result;
					if(state.LogisticsResult.length>0){
						 hasdata=true;
					}
				}else{
					loadLogisticsNumber(entryid);
				}
				if(res.data.TraceResult){
					if(res.data.TraceResult.errorMessage){
						state.logisticsTrace=[]; 
						state.logisticsTrace.push({logisticsBillNo:res.data.TraceResult.errorMessage,
						                           logisticsSteps:[{acceptTime:new Date(),remark:res.data.TraceResult.errorMessage}]});
					}else{
						state.logisticsTrace=res.data.TraceResult.logisticsTrace;
					}
				}
				if(res.data.orderResult){
					state.orderResult=res.data.orderResult.result;
				}
			}else{
				loadLogisticsNumber(entryid);
			}
			 emit("change",hasdata);
		}).catch(e=>{
			state.loading=false;
		}) 
	}
	defineExpose({
		show
	})
</script>

<style scoped="scoped">
	.boldfont{
	font-weight: bolder;font-size: 17px;
	}
	.boldtxt{
		font-weight: bolder;font-size: 14px;
	}
	.text-left{
		text-align: left;
	}
	.table {
	    width: 100%;
	    max-width: 100%;
	    margin-bottom: 20px;
	}
	.table-bordered {
	    border: 1px solid #ddd;
		    border-spacing: 0;
		    border-collapse: collapse;
	}
	.table-bordered > thead > tr > th, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > tbody > tr > td, .table-bordered > tfoot > tr > td {
	    border: 1px solid #ddd;
		padding: 8px;
		    line-height: 1.42857143;
		    vertical-align: top;
		    border-top: 1px solid #ddd;
	}
	
	.gray-bg{
		background-color: var(--el-bg-color);
	}
	.m-t-16{
		margin-top:16px;
	}
	.el-timeline-item__node--normal{
		width: 8px;
		height: 8px;
		left:1px;
	}
</style>
