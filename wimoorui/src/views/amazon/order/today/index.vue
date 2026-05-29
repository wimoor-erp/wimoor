<template>
  <div class="main-sty">
	  <div class="con-header" >
	     <MyHeader @getdata="loadData"></MyHeader>
			 <el-row>
				   <el-space >
					  <el-button @click="downloadList">导出</el-button>
				   </el-space>
				      <div class='rt-btn-group'>
				   <el-button class='ic-btn'  title='列配置'>
					  <setting-two theme="outline" size="16"  :strokeWidth="3"/>
				   </el-button>
					<el-button   class='ic-btn' title='帮助文档'>
					 <help theme="outline" size="16" :strokeWidth="3"/>
				   </el-button>
				   </div>
				</el-row>
		</div>
	<el-row :gutter="16">
			<el-col class="m-t-16" :span="12" v-if="summaryData">
			     <el-card  :body-style="{ padding: '16px' }">
				<div class="font-extraSmall dot-before">{{summaryData.counrtyname}}:{{summaryData.counrtynow}}</div>
				<div class="today-data-box">
					<div >
						<div class="data">{{summaryData.nowtotalOrder}}</div>
						<div class="title">订单数量</div>
					</div>
					<div >
						<div class="data">{{summaryData.nowtotalquantity}}</div>
						<div class="title">销量</div>
					</div>
					<div >
						<div class="data">{{summaryData.currency}}{{summaryData.nowtotalprice}}</div>
						<div class="title">订单金额</div>
					</div>
					<div >
						<div class="data">{{summaryData.nowavgsales}}</div>
						<div class="title">每单平均销量</div>
					</div>	
					<div >
						<div class="data">{{summaryData.currency}}{{summaryData.nowavgprice}}</div>
						<div class="title">平均单品净销售额</div>
					</div>
				</div>
				</el-card>
			</el-col>
			
			<el-col class="m-t-16" :span="12" v-if="summaryData ">
			 <el-card  :body-style="{ padding: '16px' }">
				<div class="font-extraSmall dot-before">{{summaryData.counrtyname}}:{{summaryData.counrtyyes}}</div>
				<div class="today-data-box">
					<div >
						<div class="data">{{summaryData.yesstotalOrder}}</div>
						<div class="title">订单数量</div>
					</div>
					<div >
						<div class="data">{{summaryData.yesstotalquantity}}</div>
						<div class="title">销量</div>
					</div>
					<div >
						<div class="data">{{summaryData.currency}}{{summaryData.yesstotalprice}}</div>
						<div class="title">订单金额</div>
					</div>
					<div >
						<div class="data">{{summaryData.yessavgsales}}</div>
						<div class="title">每单平均销量</div>
					</div>	
					<div >
						<div class="data">{{summaryData.currency}}{{summaryData.yessavgprice}}</div>
						<div class="title">平均单品净销售额</div>
					</div>
				</div>
				</el-card>
			</el-col>
			</el-row>	
	<el-row>
	  <GlobalTable ref="globalTable" 
	    :tableData="tableData" 
		@loadTable="loadTableData"
		 height="calc(100vh - 120px)"
		 :stripe="true"  style="width: 100%;margin-bottom:16px;">
		  <template #field>
			    <el-table-column prop="img"    label="图片" width="60" >
			  	       <template #default="scope">
			  	        <el-image :src="scope.row.image"   width="40" height="40"  ></el-image>
			  	      </template>
			  	    </el-table-column>
	    <el-table-column prop="product"  label="商品信息"    show-overflow-tooltip>
	       <template #default="scope">
			  
				 <div class='name'>{{scope.row.name}}</div>
				 <div class='sku'>{{scope.row.sku}}
				  <span  class="font-extraSmall"> ({{scope.row.groupname}}-{{marketFunc(scope.row.market)}})</span> 
				 </div>  
			   
	      </template>
	    </el-table-column>
		 <el-table-column label="今日">
		<el-table-column prop="QuantityOrdered" label="销量" width='75' sortable />
		<el-table-column prop="total_price" label="净销售额" width="105"   sortable>
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.total_price)}}</span>
			 </template>
		 </el-table-column>
	    <el-table-column prop="price" label="单价" width="75"   sortable>
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.price)}}</span>
			 </template>
		 </el-table-column>
		 </el-table-column>
		  <el-table-column label="昨日">
	    <el-table-column prop="QuantityOrdered_yy" label="销量" width="75"   sortable/>
		<el-table-column prop="totalprice_yy" label="净销售额"  width="105" >
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.totalprice_yy)}}</span>
			 </template>
		 </el-table-column>
		<el-table-column prop="price_yy" label="单价"  width="75" >
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.price_yy)}}</span>
			 </template>
		 </el-table-column>
		  </el-table-column>
		    <el-table-column label="上周同日">
		<el-table-column prop="QuantityOrdered_lw" label="销量"  width="75" />
		<el-table-column prop="totalprice_lw" label="净销售额"  width="105" >
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.totalprice_lw)}}</span>
			 </template>
		 </el-table-column>
		<el-table-column prop="price_lw" label="单价"  width="75" >
			 <template #default="scope">
				 <span>{{formatFloat(scope.row.price_lw)}}</span>
			 </template>
		 </el-table-column>
		  </el-table-column>
	   <el-table-column label="库存">
		<el-table-column prop="fulfillable" label="可售"  width="75"  >
			 <template #default="scope">
				 <span  v-if="scope.row.fulfillable!=undefined">{{scope.row.fulfillable}}</span>
				  <span class="font-extraSmall" v-else>加载中...</span>
			 </template>
		 </el-table-column>
		<el-table-column prop="inbound" label="待收货"  width="80"  >
			 <template #default="scope">
				<span  v-if="scope.row.inbound!=undefined">{{scope.row.inbound}}</span>
				 <span class="font-extraSmall" v-else>加载中...</span>
			 </template>
		 </el-table-column>
		<el-table-column prop="reserved" label="预留"  width="70"  >
			 <template #default="scope">
				 <span v-if="scope.row.reserved!=undefined">{{scope.row.reserved}}</span>
				  <span  class="font-extraSmall" v-else>加载中...</span>
			 </template>
		 </el-table-column>
		  </el-table-column>
		  </template>
	  </GlobalTable>
	</el-row>
	</div>
</template>
<script>
    export default{ name:"今日订单" };
</script>
<script setup>
	import { ref ,watch,reactive,onMounted,onUpdated,getCurrentInstance, toRefs} from 'vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import orderListApi from "@/api/amazon/order/orderListApi.js";
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import MyHeader from "./components/header.vue"
	import {dateFormat,formatFloat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	        const globalTable=ref(GlobalTable);
		 
			let state=reactive({
				tableData:{records:[],total:0},
				summaryData:{},
				queryParams:{},
			});
			defineExpose({
			  loadData,
			})
			
			 const {
			   tableData,
			   summaryData,
			 } = toRefs(state);
			 
			onMounted(()=>{
				 
			})
		 
			function loadData(params){
				globalTable.value.loadTable(params);
				loadTimesData();
			}
			
			function loadTableData(params){
				state.queryParams=params;
				orderListApi.todaylist(params).then((res)=>{
					state.tableData.records = res.data.records
					state.tableData.total =res.data.total
					var myparams={};
					myparams.groupid=params.groupid;
					myparams.marketplaceid=params.marketplaceid;
					myparams.skuStr="";
					if(res.data.total>0){
						state.tableData.records.forEach(item=>{
							if(myparams.skuStr==""){
								myparams.skuStr=item.sku;
							}else{
								myparams.skuStr=myparams.skuStr+","+item.sku;
							}
						})
						inventoryRptApi.getInventorySupply(myparams).then(res=>{
							state.tableData.records.forEach(item=>{
								var inbound=0;
								inbound+=res.data[item.sku].inventoryDetails.inboundReceivingQuantity;
								inbound+=res.data[item.sku].inventoryDetails.inboundWorkingQuantity;
								inbound+=res.data[item.sku].inventoryDetails.inboundShippedQuantity;
								item.inbound=inbound;
								item.fulfillable=res.data[item.sku].inventoryDetails.fulfillableQuantity;
								item.reserved=res.data[item.sku].inventoryDetails.reservedQuantity.totalReservedQuantity;
							})
						})
					}
					
				})
			}
			function loadTimesData(){
				state.summaryData={};
				orderListApi.getParamOfTodayOrder(state.queryParams).then((res)=>{
					var datas={};
					if(res.data){
						state.summaryData=res.data;
						state.summaryData.counrtynow=res.data.counrtynow;
						state.summaryData.counrtyyes=res.data.counrtyyes;
						state.summaryData.currency=res.data.currency;
						if(res.data.list.length>0&&res.data.list[0]){
							state.summaryData.nowtotalOrder=res.data.list[0].total_order;
							state.summaryData.nowtotalprice=formatFloat(res.data.list[0].total_price);
							state.summaryData.nowtotalquantity=res.data.list[0].total_quantity;
							state.summaryData.nowavgsales=formatFloat(res.data.list[0].total_quantity/res.data.list[0].total_order);
							state.summaryData.nowavgprice=formatFloat(res.data.list[0].total_price/res.data.list[0].total_quantity);
						}else{
							state.summaryData.nowtotalOrder=0;
							state.summaryData.nowtotalprice=0.0;
							state.summaryData.nowtotalquantity=0;
							state.summaryData.nowavgsales=0.0;
							state.summaryData.nowavgprice=0.0;
						}
						if(res.data.list.length>1&&res.data.list[1]){
							state.summaryData.yesstotalOrder=res.data.list[1].total_order;
							state.summaryData.yesstotalprice=formatFloat(res.data.list[1].total_price);
							state.summaryData.yesstotalquantity=res.data.list[1].total_quantity;
							state.summaryData.yessavgsales=formatFloat(res.data.list[1].total_quantity/res.data.list[1].total_order);
							state.summaryData.yessavgprice=formatFloat(res.data.list[1].total_price/res.data.list[1].total_quantity);
						}else{
							state.summaryData.yesstotalOrder=0;
							state.summaryData.yesstotalprice=0.0;
							state.summaryData.yesstotalquantity=0;
							state.summaryData.yessavgsales=0.0;
							state.summaryData.yessavgprice=0.0;
						}
					} 
					
				})
			}
			function downloadList(){
				orderListApi.downExcelTodayOrdersData(state.queryParams); 
			} 
			function marketFunc(value){
				if (value == "Amazon.fr")
					return "法国";
				if (value == "Amazon.co.uk")
					return  "英国";
				if (value == "Amazon.de")
					return "德国";
				if (value == "Amazon.es")
					return "西班牙";
				if (value == "Amazon.co.jp")
					return "日本";
				if (value == "Amazon.in")
					return "印度";
				if (value == "Amazon.ca")
					return "加拿大";
				if (value == "Amazon.com.au")
					return "澳洲";
				if (value == "Amazon.it")
					return "意大利";
				if (value == "Amazon.com")
					return "美国";
				if (value == "Amazon.se")
					return "瑞典";	
				if (value == "Amazon.nl")
					return "荷兰";
				if (value == "Amazon.pl")
					return "波兰";
				if (value == "Amazon.ae")
					return "阿联酋";	
				if (value == "Amazon.com.br")
					return "巴西";	
				if (value == "Amazon.sa")
					return "沙特";	
				if (value == "Amazon.com.mx")
					return "墨西哥";		
				if (value == "Egypt.souq.com")
					return "埃及";			
					
			}
	 
</script>

<style scoped="scoped">
.today-data-box{
		display: flex;
		justify-content: space-between;
	}
	.today-data-box .title{
		font-size: 12px;
		color: var(--el-text-color-placeholder);
		margin-top:4px;
	}
	.today-data-box .data{
		font-weight: 600;
		font-size: 12px;
		margin-top:4px;
		margin-top:16px;
	}
	.dot-before::before{
		background-color: var(--el-text-color-placeholder);
	}
	.m-t-16{
		margin-bottom:16px;
	}
</style>

