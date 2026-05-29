<template >
 
	<GlobalTable
	 :tableData="tableData"
	 show-summary
	 :summary-method="getSummaries"
	   @loadTable="loadTableData" 
	 ref="globalTable"
	 
	 >
	 <template #field>
		<el-table-column label="商品信息"  width="270px" fixed="left"  show-overflow-tooltip sortable="custom"   prop="sku" >
			 <template #header>
			 	       	商品信息<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
			 </template>	
			<template #default="scope">
				<div class="flex-center">
				<img v-if="scope.row.image" :src="scope.row.image" class="product-img" />
				<img v-else :src="$require('empty/noimage40.png')"   class="product-img"/>
					<div style="margin-left:5px;">
					 <div class="sku">{{scope.row.sku}}</div>
					 <div class="font-extraSmall">{{scope.row.pname}}</div>
					 <el-space> <span class="font-extraSmall">ASIN:{{scope.row.asin}} </span>
					 <span class="font-extraSmall">{{scope.row.groupname}}-{{scope.row.marketname}}</span></el-space>
					 </div>
				 </div>
			</template>
		</el-table-column>
		<el-table-column label="销售额"  width="120"  sortable="custom"   prop="principal" />
		<el-table-column label="销量"  width="80"  sortable="custom"   prop="salenum" />
		<el-table-column label="订单量"  width="90"  sortable="custom"   prop="ordernum" />
		<el-table-column label="平均售价"  width="80"     prop="avgprice" />
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
					<p class="l-h-none">其它费用</p>
			  </template>	
			</el-table-column>	
			<el-table-column label="店铺结算"  header-align="center"   width="100"  sortable="custom"   prop="income" />
			<el-table-column label="VAT税费"   width="100"  prop="profit_vat" >
			</el-table-column>	
			<el-table-column label="市场费用"   label-class-name="bg-group-y"    width="100"  prop="profit_marketfee">
			<template #header>
					<p class="l-h-none ">预估费用</p>
					<p class="l-h-none">市场费用</p>
			  </template>	
			</el-table-column>	
			<el-table-column label="所得税"   label-class-name="bg-group-y" width="100"   prop="profit_companytax" >
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
			<el-table-column label="其它费用"  label-class-name="bg-group-y"  width="100"   prop="profit_otherfee" >	 
			<template #header>
					<p class="l-h-none ">&nbsp;</p>
					<p class="l-h-none">其它费用</p>
			  </template>	
			</el-table-column>
            <el-table-column label=" 预估运费"    width="100" sortable="custom"   prop="profit_local_shipmentfee" />  
			<el-table-column label="采购成本"  sortable="custom"    width="100"  prop="local_price" />
			<el-table-column label="自定义费用"    width="100"   prop="local_other_cost" />
			<el-table-column label="退税"    width="60"   prop="local_return_tax" />
		   <el-table-column label="利润"  	fixed="right"   width="110" sortable="custom"   prop="profit" >
			<template #header>
			<el-space :size="4">
			  <span>利润</span>
			   <el-tooltip
			          effect="dark"
			          content=""
			          placement="top-start"
			        >
					 <template #content> 
					 <div>利润=店铺结算-本地成本预估-采购成本-采购其他成本+退税</div>
					  <div>本地成本预估=预估运费+预估市场费用+预估VAT+预估所得税+预估关税+预估汇率损耗+预估固定费用+预估其他费用</div>
					  <div>店铺结算=销售额-退款-平台费用-店铺费用</div>
					   <div>店铺费用（含分摊广告费，仓储费，长期仓储费，赔偿费，预留金，其他）</div>
					   <div>平台费用（含分FBA费用，佣金，运费，促销，其他）</div>
					   <div>所有平台费用与分摊费用，减项目自动显示为负数</div>
					  </template>
			          <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
			        </el-tooltip>
			</el-space>
			</template>
			<template #default="scope">
				 <span :class="scope.row.profit<0?'text-red':''">{{scope.row.profit}}</span>
		    </template>
		</el-table-column>	
		<el-table-column label="毛利润率"  fixed="right" width="100"   prop="profitrate" >
			<template #default="scope">
				<div v-if="scope.row.profitrate">
					{{formatPercent(scope.row.profitrate)}}%
				</div>
				<div v-else>-</div>
			</template>
		</el-table-column>		
		 </template>
	</GlobalTable> 
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,Switch} from '@element-plus/icons-vue';
    import '@/assets/css/packbox_table.css';
	import {dateFormat,dateTimesFormat,outputmoney,formatPercent} from '@/utils/index.js';
	import settlementSkuRptApi from '@/api/amazon/finances/settlementSkuRptApi.js';
	const globalTable=ref();
	const ownerRef=ref();
    const accDialogRef=ref();
	const calcDialogRef=ref();
	const refreshDataDialogRef=ref();
	const  state=reactive({
		isload:true,
		loading:false,
		tableData: {records:[],total:0}  ,
		queryParams:{datetype:"acc",currency:"CNY"},
		summary:{},
		downloading:false,
	})
	const{
		isload,
		loading,
		dateOptions,
		tableData,
		queryParams,
		summary,
		downloading,
	}=toRefs(state)
	
	 function show(param){
		 state.queryParams=param;
		 handleQuery();
	 }
	 const emit = defineEmits(['changeTable' ]);
	 function handleChangeTable(){
	 	emit("changeTable","vertical");
	 }
	 function handleQuery(){
	 	globalTable.value.loadTable(state.queryParams);
		state.isload=false;
	 }
	 function formatData(row){
		 row.principa=outputmoney(row.principa);
		 row.avgprice=outputmoney(row.avgprice);
 	     row.principal=outputmoney(row.principal);
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
		 row.local_price=outputmoney(row.local_price);
		 row.local_other_cost=outputmoney(row.local_other_cost);
		 row.local_return_tax=outputmoney(row.local_return_tax);
		 row.profit=outputmoney(row.profit);
		 	 
	 }
	 function loadTableData(params){
	 	 settlementSkuRptApi.proCommodity(params).then(res=>{
			 if(res.data){
				 res.data.records.forEach(row=>{
					 formatData(row);
				 })
				 state.tableData.records=res.data.records;
				 state.tableData.total=res.data.total;
				 if(params.currentpage==1){
				 	if(res.data.records&&res.data.records.length>0){
				 		state.summary=res.data.records[0].summary;
				 	}else{
				 		state.summary={};
				 	}
				 }
			 }else{
				 state.tableData.records=[];
				 state.tableData.total=0;
				 state.summary={};
			 }
	 	 })
	 }
 
     function parseFloatValue(value){
		 if(value)return parseFloat(value);
		 else return 0.0;
	 }
	 defineExpose({ show })
	 /* 合计行数据 */
	 function getSummaries({columns,data}){
	 	var arr = ["合计"];
	 	columns.forEach((item,index)=>{
	 		if(index>=1){
	 			 var fee=0.0;
	 				 fee=state.summary[item.property];
	 				 if(item.property=='salenum'||item.property=='ordernum'||item.property=='refundsales'){
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
</script>
<style>
	.bg-group-y{
		background-color:#efefef !important;
	}
	.dark .bg-group-y{
		background-color:#434343 !important;
	}
</style>
<style scoped >
	.change-icon{
	padding-top: 3px;
	font-size: 11px;
	margin-top: 3px;
	cursor: pointer;
	line-height: unset;
	}
	 .l-h-none{
	 	line-height:14px;
	 }

</style>