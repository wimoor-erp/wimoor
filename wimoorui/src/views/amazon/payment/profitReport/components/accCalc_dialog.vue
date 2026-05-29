<template>
	<el-dialog
	title="自定义利润计算公式"
	v-model="visible"
	width="60%"
	>
	<div>可用于计算的项：</div>	
	 <div class="fielditem">
	<el-row :gutter="16" class="m-b-16">
				 <el-col :span="6">
					 <el-card shadow="never" class="yellow-top-border">
						<template #header>
						        <span class="font-bold">亚马逊SKU费用(账期)</span>
						    </template>
						<ul >
							<li v-for="(item,index) in list.skufee" >
							<el-tag :type="item.color">{{item.name}}</el-tag>
							</li>
						</ul>	
					 </el-card>
				 </el-col>
				 <el-col :span="6" >
					 <el-card shadow="never" class="green-top-border">
						<template #header>
						        <span class="font-bold ">亚马逊店铺分摊(账期)</span>
						    </template>
						<ul >
							<li v-for="(item,index) in list.incom" >
							<el-tag :type="item.color">{{item.name}}</el-tag>
							</li>
						</ul>		
					 </el-card>
				 </el-col>
				 <el-col :span="6">
				 					 <el-card shadow="never"  class="blue-top-border" >
				 						<template #header>
				 						        <span class="font-bold">预估费用(利润计算配置)</span>
				 						    </template>
				 							<ul >
				 								<li v-for="(item,index) in list.profit" >
				 								<el-tag :type="item.color" >{{item.name}}</el-tag>
				 								</li>
				 							</ul>		
				 					 </el-card>
				 </el-col>
				 <el-col :span="6">
					 <el-card shadow="never"  class="blue-top-border" >
						<template #header>
						        <span class="font-bold">非账期费用(报表)</span>
						    </template>
							<ul >
								<li v-for="(item,index) in list.other" >
								<el-tag :type="item.color">{{item.name}}</el-tag>
								</li>
							</ul>		
					 </el-card>
				 </el-col>
	</el-row>
	</div>
	<div style="margin-top: 15px;">手动录入计算公式:</div>
	<el-space>
		利润 = SKU结算收入 -
		<el-input v-model="localData.formulaData" style="width: 600px;" clearable></el-input>
	</el-space>
	<div style="margin-top: 15px;">
		<el-space>
			采购成本=(销售数量-退货数量)*采购成本
		</el-space>
	</div>
	 <div class="font-extraSmall" style="margin-top: 15px;">
		 <p>计算方案推荐:</p>
		 <p>利润 = SKU结算收入 - (运费预估 + 其它成本 + 采购成本+店铺费用 )</p>
		 <p>利润 = SKU结算收入 - (运费预估 + 其它成本 + 广告费用 +采购成本 +店铺费用) (信用卡直接支付广告费,如果是账户扣款广告费引入广告费用 会造成重复扣款)</p>
		 <p>利润 = SKU结算收入 - (运费预估 + 其它成本 + 广告费用+仓储费+长期仓储费+采购成本 ) (查看SKU利润情况用此公式更加合适)</p>
	 </div>
	<template #footer>
		<el-button @click="visible=false">取消</el-button>
		<el-button type="primary" @click="submitForm" :loading="downloading">提交</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs} from"vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import settlementFormulaApi from '@/api/amazon/finances/settlementFormulaApi.js';
	import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
	const globalTable=ref();
	const state = reactive({
		visible:false,
		localData:{},
		downloading:false,
		itemlist:[],
		list:{
			skufee:[
				{name:'销售额',value:"principal",check:true,disabled:true,color:'warning'},
				{name:'FBA费用',value:"fbafee",check:true,disabled:true,color:'warning'},
				{name:'销售佣金',value:"commission",check:true,disabled:true,color:'warning'},
				{name:'退款金额',value:"refund",check:true,disabled:true,color:'warning'},
				{name:'促销',value:"promotion",check:true,disabled:true,color:'warning'},
				{name:'买家运费',value:"shipping",check:true,disabled:true,color:'warning'},
				{name:'其它收支',value:"otherfee",check:true,disabled:true,color:'warning'},
				{name:'订单数量',value:"ordernum",check:true,disabled:true,color:'warning'},
				{name:'销售数量',value:"salenum",check:true,disabled:true,color:'warning'},
				{name:'退款数量',value:"refundnum",check:true,disabled:true,color:'warning'},
				{name:'平均售价',value:"avgprice",check:true,disabled:true,color:'warning'},
				{name:'SKU结算',value:"setincome",check:true,disabled:true,color:'warning'},
				{name:'VAT税费',value:"profit_vat",check:true,disabled:false,color:'warning'},
			],
			incom:[
				{name:'店铺分摊仓储费',value:"share_storagefee",check:true,disabled:true,color:'danger'},
				{name:'店铺分摊长期仓储费',value:"share_longstoragefee",check:true,disabled:true,color:'danger'},
				{name:'店铺分摊广告费',value:"share_advspendfee",check:true,disabled:true,color:'danger'},
				{name:'店铺分摊折扣券',value:"share_couponredemptionfee",check:true,disabled:false,color:'danger'},
				{name:'店铺分摊预留金',value:"share_reservefee",check:true,disabled:false,color:'danger'},
				{name:'店铺分摊赔偿',value:"share_reimbursementfee",check:true,disabled:false,color:'danger'},
				{name:'店铺分摊其它收支',value:"share_shopotherfee",check:true,disabled:false,color:'danger'},
				{name:'店铺分摊移除费',value:"share_disposalfee",check:true,disabled:false,color:'danger'},
			],
			profit:[
				{name:'运费预估',value:"shipmentfee",check:true,disabled:true,color:'success'},
				{name:'预估所得税',value:"profit_companytax",check:true,disabled:true,color:'success'},
				{name:'预估关税',value:"profit_customstax",check:true,disabled:true,color:'success'},
				{name:'预估汇率损耗',value:"profit_exchangelost",check:true,disabled:true,color:'success'},
				{name:'预估其他成本',value:"profit_costrate",check:true,disabled:false,color:'success'},
				{name:'预估市场费用',value:"profit_marketfee",check:true,disabled:false,color:'success'},
				{name:'预估其他',value:"profit_otherfee",check:true,disabled:false,color:'success'},
			],
			other:[
				{name:'采购成本',value:"price",check:true,disabled:false,color:'success'},
				{name:'货件头程运费',value:"firstShipment",check:true,disabled:false,color:'success'},
				{name:'长期仓储费',value:"longTermFee",check:false,disabled:false,color:'success'},
				{name:'仓储费',value:"storagefee",check:false,disabled:false,color:'success'},
				{name:'广告费用',value:"spend",check:false,disabled:false,color:'success'},
				{name:'赔偿费用',value:"reimbursementsFee",check:false,disabled:false,color:'success'},
				{name:'退税',value:"returntax",check:false,disabled:false,color:'success'},
				{name:'先进先出运费成本',value:"fifo_shipmentFee",check:false,disabled:false,color:'success'},
				{name:'先进先出采购成本',value:"fifo_cost",check:false,disabled:false,color:'success'},
				{name:'其它成本',value:"othersfee",check:false,disabled:false,color:'success'},
			],
			}
	})
	const{
		visible,
		tableData,
		localData,
		downloading,
		itemlist,
		list
	}=toRefs(state)
 
	function submitForm(){
		state.downloading=true;
		if(state.localData.formulaData.indexOf("运费预估")>=0&&state.localData.formulaData.indexOf("货件头程运费")>=0){
			ElMessage.error("【运费预估】和【货件头程运费】属于同一费用，只是计算方式不一样，不能同时加入计算公式。");
			state.downloading=false;
			return;
		}
		settlementFormulaApi.formulaSave({"formuladata":state.localData.formulaData,"pricetype":state.localData.pricetype}).then((res)=>{
			if(res.data){
				ElMessage.success("操作成功");
			}
			state.downloading=false;
		});
	}
	 
	function loadData(){
		settlementFormulaApi.loadformula().then((res)=>{
			state.localData=res.data;
		});
		
		financesItemApi.getFinListNoPage().then((res)=>{
			state.itemlist=res.data;
		});
	}
	 
	 		
	
	function show(queryParams){
		state.visible = true;
		 loadData();
	}
	defineExpose({
		show,
	})
</script>
<style scoped="scoped">
	.m-b-16{
		margin-bottom:16px;
	}
	.yellow-top-border{
		border-top: 2px solid #e6a23c;
	}
	.green-top-border{
		border-top: 2px solid #67c23a;
	}
	.blue-top-border{
		border-top: 2px solid #409eff;
	}
	.fielditem li{
		padding:3px;
	}
	ul {
		list-style-type:none;
	}
	.m-t-b-8{
		margin-top: 8px;
		margin-bottom: 8px;
	}
</style>
<style>
	.have-ass-text-radio .el-radio{
		height:inherit!important;
		line-height:24px;
		white-space: inherit;
	}
	.have-ass-text-radio p{
		font-size:12px;
		color:#acb0b9;
	}
</style>