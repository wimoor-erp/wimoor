<template>
	<el-row>
	   <GlobalTable ref="globalTable" :tableData="tableData" @selectionChange="selectionChange"  height="calc(100vh - 250px)" @loadTable="loadtableData" :defaultSort="{ prop: 'buydate', order: 'descending' }"  :stripe="true"  style="margin-bottom:16px;">
		   <template #field>
	  		 <el-table-column type="selection" width="38" />
			 <el-table-column prop="image" label="图片" width="66" >
			    <template #default="scope">
			     <img :src="scope.row.image"  width="40" height="40"/>
			   </template>
			 </el-table-column>
			 <el-table-column prop="name" label="商品信息"  show-overflow-tooltip>
			    <template #default="scope">
			       <div class='name'>{{scope.row.name}}</div>
			       <div class='sku'>{{scope.row.sku}}
			         <span class="font-extraSmall">店铺:{{scope.row.groupname}}-{{scope.row.marketname}}</span>
			       </div>
			   </template>
			 </el-table-column>
	  		   <el-table-column prop="orderid" label="订单号" width='170' />
	  	    <el-table-column prop="orderprice" label="订单总金额" width='110'  sortable="custom"/>
	  	    <el-table-column prop="quantity" label="数量/单价" width="105"   sortable="custom">
	  			<template #default="scope">
	  			<div class='mname'>{{scope.row.quantity}}</div>
	  			<div class='sku'>{{scope.row.itemprice}}  </div>
	  			</template>
	  		</el-table-column>
	  	    <el-table-column prop="buydate" label="订购时间" width="100"   sortable="custom">
	  				<template #default="scope">
	  				<span >{{dateTimesFormat(scope.row.buydate)}}</span>
	  				</template>
	  			</el-table-column>
	  	     <el-table-column prop="orderstatus" label="状态" width="120"   sortable="custom">
	  	        <template #default="scope">
	  	            <el-tag  :type="statusFunc(scope.row.orderstatus)" effect="plain">{{scope.row.orderstatus}}</el-tag>
	  	        </template>
	  	    </el-table-column>  
			<el-table-column prop="feedstatus" v-if="showVatField" label="发票上传" width='100' >
							 <template #default="scope">
								  <el-tag  effect="success" v-if="scope.row.feedstatus=='已上传'">{{scope.row.feedstatus}}</el-tag>
								  <el-tag  effect="info" v-else>{{scope.row.feedstatus}}</el-tag>
							 </template>
		   </el-table-column>
		   <el-table-column prop="feedstatus"   label="邀评状态" width='100' >
				 <template #default="scope">
					  <el-tag  effect="warning" v-if="scope.row.reviewstatus=='loading'">邀评中...</el-tag>
					  <el-tooltip v-else-if="scope.row.reviewstatus">
						    <template #content>邀评时间<br />{{dateTimesFormat(scope.row.reviewSendTime)}} </template>
						  <el-tag  effect="success"  >已邀评</el-tag>
					  </el-tooltip>
					  
					  <el-tag  effect="info" v-else>未邀评</el-tag>
				 </template>
		   </el-table-column>
		   <el-table-column prop="remark"   label="备注" width='100' >
		   </el-table-column>
	  	    <el-table-column prop="operate" label="操作"  width="140"   sortable="custom">
	  	        <template #default="scope">
	  	          <el-space>
	  	            <el-button class='el-button--blue' @click="showOrderModal(scope.row)">详情</el-button>
	  	            <el-button class='el-button--blue' @click="showVatModal(scope.row)">上传发票</el-button>
	  	           
	  	            <el-dropdown :hide-on-click="false">
	  	              <span class="el-dropdown-link">
	  	                <more-one theme="outline" size="20" fill="#333" :strokeWidth="3"/>
	  	              </span>
	  	               <template #dropdown>
	  	                <el-dropdown-menu>
	  	                  <el-dropdown-item>邀请评论</el-dropdown-item>
	  	                  <el-dropdown-item>标记推广</el-dropdown-item>
	  	                </el-dropdown-menu>
	  	            </template>
	            </el-dropdown>
	          </el-space>
	        </template>
	    </el-table-column>
		</template>
	  </GlobalTable>
	</el-row>
	
	<OrderDetail ref="orderDetailRef" @change="orderDetailChangeHandle"></OrderDetail>
	<Invoice ref="invoiceRef"></Invoice>
</template>

<script setup>
	import {ref ,watch,reactive,onMounted,onUpdated} from 'vue';
	import orderListApi from '@/api/amazon/order/orderListApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage,ElLoading} from 'element-plus';
	import {View,Refresh} from '@element-plus/icons-vue'
    import myUtil from "@/hooks/amazon/order/financial.js";
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import FeedStatus from "@/components/feedstatus/index.vue";
	import OrderDetail from "./order_detail.vue";
	import Invoice from "./invoice.vue";
	        const invoiceRef=ref();
	        const orderDetailRef=ref();
			const globalTable=ref();
			let fromDate =ref()
			let toDate =ref()
            let showVatField =ref(false);
			let params=ref({})
			let tableData=reactive({records:[],total:0})
			onMounted(()=>{
				toDate.value = new Date().format("yyyy-MM-dd");
				var oidtime = new Date().getTime() - 3600 * 1000 * 24 * 7
				fromDate.value = new Date(oidtime).format("yyyy-MM-dd")
			}) ;
			const {statusFunc,countryFunc,currencyFunc,totalRowFunc}=myUtil();
			function showOrderModal(row){
				orderDetailRef.value.show(row);
			}
			function showVatModal(row){
				invoiceRef.value.show(row);
			}
			function orderDetailChangeHandle(data){
				tableData.records.forEach(item=>{
					if(item.orderid==data.orderid){
						item.remark=data.remark;
					}
				})
			}
			function getDataParams(rows){
				//隐藏字段
				if(rows.pointname=='Amazon.de'||rows.pointname=='Amazon.it'||rows.pointname=='Amazon.co.uk'||rows.pointname=='Amazon.fr'||rows.pointname=='Amazon.es'){
					showVatField.value=true
				}else{
					showVatField.value=false
				}
				if(rows.dates[1]){
					fromDate.value = rows.dates[0].format("yyyy-MM-dd")
					toDate.value = rows.dates[1].format("yyyy-MM-dd")
				}
				params.value=rows;
				globalTable.value.loadTable();
			}
		 
			const successMsg = () => {
			    ElMessage.success('操作成功！');
			}
			const errorMsg = () => {
				ElMessage.error('操作失败！')
			}
			const loadtableData =function(queryParam){
							   queryParam.groupid=params.value.groupid;
							   queryParam.search=params.value.search;
							   queryParam.searchtype=params.value.searchtype;
							   queryParam.startDate=fromDate.value;
							   queryParam.endDate=toDate.value;
							   queryParam.channel=params.value.saleschannel?params.value.saleschannel:null;
							   queryParam.pointname=params.value.pointname?params.value.pointname:null;
							   queryParam.status=params.value.orderstatus?params.value.orderstatus:null;
							   queryParam.color=params.value.color?params.value.color:null;
							   queryParam.isbusiness=params.value.isbusiness?params.value.isbusiness:null;
							   queryParam.datatype=params.value.datatype;
							if(params.value.datatype&&params.value.datatype=="2"){
								orderListApi.getOrderInvoinceList(queryParam).then(function(res){
									tableData.records = res.data.records
									tableData.total = res.data.total
								})
							}else{
								orderListApi.getOrderList(queryParam).then(function(res){
									tableData.records = res.data.records
									tableData.total = res.data.total
								})
							}
							
			}
		
			
			
		
			
			function downloadOrderXlsx(rows){
				params.value=rows;
				if(params.value.search==undefined){
					params.value.search="";
				}
				if(params.value.saleschannel==""){
					params.value.saleschannel="all";
				}
				if(params.value.orderstatus==""){
					params.value.orderstatus="all";
				}
				if(params.value.pointname==""){
					params.value.pointname="all";
				}
				if(params.value.color==""){
					params.value.color="all";
				}
				if(params.value.isbusiness==""){
					params.value.isbusiness="all";
				}
				orderListApi.downloadOrderList({
					"groupid":params.value.groupid,
					"search":params.value.search,
					"searchtype":params.value.searchtype,
					"startDate":fromDate.value,
					"endDate":toDate.value,
					"channel":params.value.saleschannel?params.value.saleschannel:null,
					"pointname":params.value.pointname?params.value.pointname:null,
					"status":params.value.orderstatus?params.value.orderstatus:null,
					"color":params.value.color?params.value.color:null,
					"isbusiness":params.value.isbusiness?params.value.isbusiness:null
				}); 
			}
			
			function backLookInfo(){
				lookvatVisible.value=false;
				vatVisible.value=true;
				
			}
			function backEditInfo(){
				editvatVisible.value=false;
				vatVisible.value=true;
			}
		
			
			function callbackReview(){
				if(selectRows&&selectRows.length>0){
					selectRows.forEach(row=>{
						if(!row.reviewSendTime){
							var param={};
							param.orderid=row.orderid;
							param.marketplaceid=row.marketplaceId;
							param.amazonauthid=row.authid;
							row.reviewstatus="loading";
							orderListApi.sendReviewMessage(param).then(res=>{
								row.reviewstatus="ok";
							});
						}else{
							ElMessage.warning(row.orderid+'已经邀评，自动忽略');
						}
					})
				}
				
			}
			let selectRows=[];
			function selectionChange(rows){
				selectRows=rows;
			}
            defineExpose({ getDataParams,downloadOrderXlsx,callbackReview })
</script>

<style>
	.od-table{
		margin-top:16px;
	}
	.od-sum{
		margin-top:16px;
		font-size: 14px;
		display: flex;
		align-items: center;
		justify-content: flex-end;
	}
	.od-sum span{
		font-size: 16px;
		font-weight: 600;
		color:var(--el-color-primary)
	}
	.od-label{
		width:120px;
		text-align: left;
	}
	.textright{
		text-align: right;
	}
	.btn-betwn{
		display: flex;
		justify-content: space-between;
	}
</style>
