<template >
	 
	<GlobalTable
	 :tableData="tableData"
	 show-summary
	 :summary-method="getSummaries"
	   @loadTable="loadTableData" 
	 ref="globalTable"
	 
	 >
	 <template #field>
		<el-table-column label="平台SKU" header-align="center"  min-width="270px" fixed="left"  show-overflow-tooltip  prop="sku" >
			<template #header>
				       	平台SKU<el-icon class="change-icon" @click="handleChangeTable()"><Switch /></el-icon>
			</template>	
			<template #default="scope">
				<div class="flex-center">
				<img v-if="scope.row.image" :src="scope.row.image" class="img-80"  width="80" height="80"  />
				<img v-else :src="$require('empty/noimage40.png')"  class="img-80"  width="80" height="80"  />
					<div style="margin-left:5px;">
					 <div class="sku">{{scope.row.sku}}</div>
					 <div class="font-extraSmall">{{scope.row.pname}}</div>
					 <div> <span class="font-extraSmall">ASIN:{{scope.row.asin}} </span></div>
					 
					 <div><span class="font-extraSmall"> 店铺：{{scope.row.groupname}} 站点：{{scope.row.marketname}}</span></div>
					 <div><span class="font-extraSmall"> 负责人： {{scope.row.ownername}}</span></div>
					 </div>
				 </div>
			</template>
		</el-table-column>
		<el-table-column label="销售情况"  header-align="center"   width="150"  sortable="custom"   prop="principal" >
			<template #default="scope">
				 <div><div class="text-green pull-left text-right width60"> 销售额：</div> {{scope.row.principal}}</div>
				 <div><div class="font-extraSmall pull-left text-right width60"> 销量：</div> {{scope.row.salenum}}</div>
				 <div><div class="font-extraSmall pull-left text-right width60"> 订单：</div> {{scope.row.ordernum}}</div>
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
			<el-table-column label="店铺结算"  header-align="center"   width="100"  sortable="custom"   prop="income" >
			 </el-table-column>
			 <el-table-column label="VAT税费"  header-align="center"   width="100"  sortable="custom"   prop="profit_vat" >
			  </el-table-column>
			 <el-table-column label="本地成本预估"   header-align="center"  width="145"  sortable="custom"   prop="profit_local_shipmentfee" >
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
			  <el-table-column label="本地采购费用"  header-align="center"   width="145"  sortable="custom"   prop="local_price" >
			  	<template #default="scope">
			  		 <div><div class="font-extraSmall pull-left text-right width60"> 采购成本：</div> {{scope.row.local_price}}</div>
			  		 <div  v-if="scope.row.local_other_cost!='0'"><div class="font-extraSmall pull-left text-right width60"> 其他成本：</div> {{scope.row.local_other_cost}}</div>
			  		 <div  v-if="scope.row.local_return_tax!='0'"><div class="font-extraSmall pull-left text-right width60"> 退税：</div> {{scope.row.local_return_tax}}</div>
			  	</template>
			   </el-table-column>
		<el-table-column label="利润"  	fixed="right" header-align="center"  width="135" sortable="custom"   prop="profit" >
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
				 <div><div class="font-extraSmall pull-left text-right width60"> 利润：</div>{{scope.row.profit}}</div>
				 <div><div class="font-extraSmall pull-left text-right width60"> 利润率：</div>{{scope.row.profitrate}}</div>
		    </template>
		</el-table-column>	
		 </template>
	</GlobalTable> 
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,Switch} from '@element-plus/icons-vue';
    import '@/assets/css/packbox_table.css';
	import {dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	import settlementSkuRptApi from '@/api/amazon/finances/settlementSkuRptApi.js';
	const globalTable=ref();
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
		tableData,
		queryParams,
		summary,
		downloading,
	}=toRefs(state)
	
	 function recalculation(){
	 	refreshDataDialogRef.value.show();
	 }
	 const emit = defineEmits(['changeTable' ]);
	 function handleChangeTable(){
	 	emit("changeTable","horizontal");
	 }
	 function show(param){
	 		 state.queryParams=param;
			 handleQuery();
	 }
	 	 defineExpose({ show });
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
		  
	 /* 合计行数据 */
	 function getSummaries({columns,data}){
	 	var arr = ["合计"];
	 	columns.forEach((item,index)=>{
	 		if(index>=1){
			   var fee=0.0;
			   console.log(index,item.property);
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
				 }else{
					 fee=state.summary[item.property];
				 }
	 			 arr[index]=outputmoney(fee);
	 		}
	 	})
	 	return  arr
	 }
</script>
<style>
	.settlementTable .el-table__footer .cell {
		text-align:center!important; 
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
	 .pull-left{
		 float:left;
	 }
</style>
 