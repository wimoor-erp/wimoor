<template>
	<div class="main-sty" >
	<div class="con-header">
		<el-row>
			<el-space>
				   <Group  @change="groupChange"   defaultValue="only" ></Group>
					<Datepicker ref="datepickers" :shortIndex="2" @changedate="changedate" />
					<!-- <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或平台SKU"></el-input> -->
		    </el-space>
			<div class='rt-btn-group' style="margin-bottom:10px;">
			  <el-button style="float:right;" :loading="downloading" @click="downloadList">导出</el-button>  
			</div>
		 </el-row>
		 <GlobalTable ref="globalTable"
		  :tableData="tableData"  height="calc(100vh - 270px)" 
		  :defaultSort="{ prop: 'posted_date', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
		  style="width: 100%;margin-bottom:16px;">
		 	<template #field>
		   <el-table-column label="平台商品信息" prop="sku"   sortable="custom" show-overflow-tooltip >
		   	<template #default="scope">
		   	<div class="flex-center">
		   		   <img v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  /> 
		   		   <img v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  />
		   		   <div >
		   			   <div  >{{scope.row.pname}}</div>
		   			   <p class="sku">{{scope.row.sku}} 
		 				  <span class="font-extraSmall"> ASIN:{{scope.row.asin}} </span>
						  <span class="font-extraSmall"> 站点: {{scope.row.mname}}</span>
		   			   </p>
		   		   </div>
		   	</div>
		   	</template>
		   </el-table-column>
		    <el-table-column label="费用时间" prop="posted_date"   sortable="custom" width="210" >
					<template #default="scope">
						{{dateTimesFormat(scope.row.posted_date)}}
						<div class="font-extraSmall"><span >订单号:</span>{{scope.row.amazon_order_id}}</div>
					</template>
			</el-table-column>
			<el-table-column label="数量" prop="quantity"  sortable="custom"   width="100"   >
			</el-table-column>
			<el-table-column label="费用" prop="amount"  sortable="custom"   width="100"   >
			</el-table-column>
			<el-table-column label="费用币种" prop="currency"   width="100"  sortable="custom"  >
			</el-table-column>
			<el-table-column label="费用类型" prop="ftype"   width="200"  sortable="custom"  >
				<template #default="scope">
					{{scope.row.ftype}}
					<div v-if="scope.row.ftypename">{{scope.row.ftypename}}</div>
					</template>
			</el-table-column>
			<el-table-column label="事件类型" prop="event_type"   width="120" sortable="custom"  >
			</el-table-column>
		</template>
		</GlobalTable>
	</div>
	</div>
</template>
<script>
    export default{ name:"账期未结算" };
</script>
<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import Group from '@/components/header/group.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import {dateFormat,dateTimesFormat} from '@/utils/index.js';
		import { ElMessage, ElMessageBox} from 'element-plus';
		import settlementSkuRptApi from '@/api/amazon/finances/settlementSkuRptApi.js';
		const globalTable=ref();
		let state=reactive({
			queryParams: {search:""},
			tableData:{records:[],total:0},
			isload:true,
			downloading:false,
		})
		const{
			dateOptions,
			queryParams,
			tableData,
			isload,
			downloading,
		}=toRefs(state)
		
		//日期改变
		function changedate(dates){
			state.queryParams.fromDate=dates.start;
			state.queryParams.endDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			handleQuery();
		}
		
		function handleQuery(){
			 globalTable.value.loadTable(state.queryParams);
		}
		function loadTableData(params){
			  settlementSkuRptApi.selectSettlementOpen(params).then(res=>{
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
					 state.isload=false;
			  })
		}
		
		function downloadList(){
			state.downloading=true;
			settlementSkuRptApi.downloadSettlementOpen(state.queryParams,()=>{
				state.downloading=false;
			})
		}
		onMounted(()=>{
			 
		});
	</script>
	
	<style scoped="scoped">
		.app-container{
			padding:20px;
		}
		.img-40{width: 40px;
		height: 40px;flex: none;
		margin-right: 8px;}
	</style>