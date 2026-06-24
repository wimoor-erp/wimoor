<!-- 添加产品时，共用的产品弹窗 -->
<template>
  <div>
  	<el-dialog v-model="dialog.visible"  title="账期详情"  width="80%" top="6vh" :before-close="handleClose">
		<el-row :gutter="24">
			<el-col :span="shopfee.length>0?16:24">
				<el-card class="m-t-16 bg-card-orange">
						 <h2 class="m-b-16">销售汇总{{currency}}</h2>
								 <el-row>
								 <el-col :span="4">
						<el-space direction="vertical" alignment="left">
								  <span class="text-gray">销售额</span>
								  <span class="font-bold font-large">{{outputmoney(skusum.in_principal)}}</span>
							    </el-space>
								</el-col>
								 <el-col :span="4" >
						<el-space  direction="vertical" alignment="left">
								  <span class="text-gray">销量</span>
								  <span class="font-bold font-large">{{skusum.sales}}</span>
								</el-space>
								</el-col>
								<el-col :span="4">
						<el-space direction="vertical" alignment="left"><span class="text-gray">订单量</span>
								<span class="font-bold font-large">{{skusum.order_amount}}</span>
								</el-space>
								</el-col>
								<el-col :span="4">
						         <el-space direction="vertical" alignment="left"><span class="text-gray">SKU结算</span>
								<span class="font-bold font-large">{{outputmoney(skusum.amount)}}</span>
								</el-space>
								</el-col>
							
								<el-col :span="4">
								 <el-space direction="vertical" alignment="left"><span class="text-gray">店铺费用</span>
								<span class="font-bold font-large">	{{outputmoney(summaryshopfee)}}</span>
								</el-space>
								</el-col>
								<el-col :span="4">
								 <el-space direction="vertical" alignment="left"><span class="text-gray">总结算费用</span>
								<span class="font-bold font-large">{{outputmoney(totalsummary)}}</span>
								</el-space>
								</el-col>
								
								
								</el-row>
				</el-card>
				 <el-divider content-position="left" class="del-divider-po" style="padding-bottom:10px;">账期详情</el-divider>
				  			<el-row :gutter="24"   >
								    <el-col   :span="size" v-for="(tvalue, tkey) in data"  >
										<el-card  v-if="tvalue"  shadow="never">
											<h3  v-if="tvalue.cname" >{{tvalue.cname}}
											<div style="float:right;padding-left:10px;padding-top:2px;" title="占比总结算费用" class="font-extraSmall">
												 {{formatFloat(parseFloat(tvalue.summary)*100/parseFloat(totalsummary))}}%
											</div>
											<div style="float:right" title="项目汇总">{{outputmoney(tvalue.summary)}}</div>
											</h3>
											<h3  v-else>{{tvalue.name}} 
											<div style="float:right;padding-left:10px;padding-top:2px; " title="占比总结算费用" class="font-extraSmall">
												{{formatFloat(parseFloat(tvalue.summary)*100/parseFloat(totalsummary))}}%
											</div>
											<div style="float:right" title="项目汇总">{{outputmoney(tvalue.summary)}}
											</div>
											</h3>
											<div v-for="(avalue,akey) in tvalue"> 
												<div  v-if="akey&&akey!='cname'&&akey!='name'&&avalue&&avalue.item&&avalue.item.length>0">
												<div class="avalue-name" v-if="avalue.cname">
												{{avalue.cname}}</div>
												<div class="avalue-name" v-else>{{avalue.name}}</div>
												<ul class="ul-list" v-if="avalue&&avalue.item&&avalue.item.length>0" >
													<li   v-for="(value,key) in avalue.item"> 
														<span class="font-extraSmall" v-if="value.cname">{{value.cname}}:</span>
														<span class="font-extraSmall" v-else>{{value.name}}:</span>
														{{outputmoney(value.amount)}}
													</li>
												</ul>
												</div>
											</div>
										</el-card>
								    </el-col>
				  			</el-row>
			</el-col>
			<el-col :span="8" v-if="shopfee.length>0">
				<el-card shadow="never" >
								 		  		 <h2 style="margin-bottom:8px;" title="账期中无法在SKU上面体现的费用项目">店铺费用
												 <div style="float:right;padding-left:10px;padding-top:6px; " 
												   title="占比总结算费用" class="font-extraSmall">
												 	{{formatFloat(parseFloat(summaryshopfee)*100/parseFloat(totalsummary))}}%
												 </div>
												 <div style="float:right" title="店铺费用汇总">{{outputmoney(summaryshopfee)}}</div>
												 </h2>
								 				 <ul class="ul-list">
								 		  			<li   v-for="value in shopfee" > 
								 					<span>
								 		  				<span   v-if="value.ctransaction_type">
								 						<span v-if="value.ctransaction_type!=value.camount_type">{{value.ctransaction_type}}-</span>	
								 						</span>
								 		  				<span   v-else>{{value.transaction_type}}-</span>
								 		  				<span   v-if="value.camount_type">{{value.camount_type}}</span>
								 		  				<span   v-else>{{value.amount_type}}</span>
								 		  				<span   v-if="value.camount_description">-{{value.camount_description}}</span>
								 		  				<span   v-else>-{{value.amount_description}}</span>
								 		  				</span ><span class="font-bold">{{outputmoney(value.amount)}}</span>
								 		  			</li>
								 					</ul>
				</el-card> 
			</el-col>
		</el-row>
  		 
  		<template #footer>
  			<span class="dialog-footer">
  				<el-button @click="dialog.visible = false">关闭</el-button>
  			</span>
  		</template>
  	</el-dialog>
  </div>    
</template>

<script setup> 
	import { ref, reactive, onMounted,toRefs ,watch} from 'vue'
	import { Search, ArrowDown, } from '@element-plus/icons-vue'
	import { ElDivider, ElMessageBox, ElMessage } from 'element-plus'
		import {dateFormat,outputmoney,formatFloat} from '@/utils/index.js';
	import settlementAccRptApi from '@/api/amazon/finances/settlementAccRptApi.js';
	const state=reactive({
		dialog:{visible:false},
		loading:false,
		activeName:"detail",
		data:{},
		skusum:{},
		currency:"",
		shopfee:[],
		summaryshopfee:0.0,
		totalsummary:0.0,
		size:24,
		queryParams:{}
	});
	const { loading,
	         data,
			 size,
			 shopfee,
			 summaryshopfee,
			 currency,
			 totalsummary,
			 activeName,
			queryParams,
			skusum,
			dialog,
		  } = toRefs(state);
	let props = defineProps({ type:""});
	const {  type } = toRefs(props);
	const emit = defineEmits(['change']);
	function handleClose(){
		state.dialog.visible=false;
	}
	function show(params,queryParams){
		state.dialog.visible=true;
		state.summaryshopfee=0.0;
		state.totalsummary=0.0;
		var data={settlement_id:params.settlement_id};
		data.currency=queryParams.currency;
		state.currency=queryParams.currency;
		data.amazonAuthId=params.amazonauthid;
		//data.fromDate=params.startDate;
		//data.endDate=params.endDate;
		settlementAccRptApi.getSettmentDetail(data).then(res=>{
			state.data=res.data.detail;
			state.totalsummary=res.data.totalsummary;
			var i=0;
			for(let item in res.data.detail){
                    i++;
			}
			state.size=parseInt(24/i);
			if(state.size<12){
				state.size=12;
			}
			if(res.data.list){
				state.shopfee=[];
				res.data.list.forEach(item=>{
					if(item.transaction_type=='sku'){
						state.skusum=item;
					}else{
						state.shopfee.push(item);
						state.summaryshopfee=state.summaryshopfee+parseFloat(item.amount);
					}
				})
			}
		})
	}
	defineExpose({
	  show,
	})
</script>

<style scoped>
	 h3{
		 margin-bottom:16px;
	 }
	 .el-card{
		 margin-bottom:24px;
	 }
	 .m-b-16{
		 margin-bottom:16px;
	 }
	 .text-gray{
		opacity:0.8;
	 }
	 .ul-list li{
		 border-bottom: 1px solid #eee;
		 padding:8px 0px;
	 }
	.dark .ul-list li{
	 		 border-bottom: 1px solid #555555;
	 		 padding:8px 0px;
	 }
	 .avalue-name{
		 padding:8px 0px;
		 border-top:1px solid #eee;
	 }
	 .dark .avalue-name{
	 		 padding:8px 0px;
	 		 border-top:1px solid #464646;
	 }
    .del-divider-po{
		background:#fff;
	}
	.dark .del-divider-po{
		background:#1e1e1e;
	}
</style>
<style>
	.el-divider__text {
	    background-color: var(--el-color-white) !important;
	}
	.dark .el-divider__text {
	    background-color:var(--el-bg-color) !important;
	}
	.bg-card-orange .el-card__body{
			background-image: linear-gradient(270deg, #ffa400 0, #ff7315 97%);
			color:#fff;
	}
 
</style>