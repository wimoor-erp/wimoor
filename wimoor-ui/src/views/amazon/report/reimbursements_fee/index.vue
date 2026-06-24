<template>
	<div class="main-sty" >
	<div class="con-header">
		<el-row>
			<el-space>
				   <Group  @change="groupChange"   defaultValue="only" ></Group>
					<Datepicker ref="datepickers" :shortIndex="2" @changedate="changedate" />
					 <el-input class='ic-btn' v-model="queryParams.search" @input="handleQuery" placeholder="搜索ASIN或平台SKU"></el-input>
		    </el-space>
			<div class='rt-btn-group' style="margin-bottom:10px;">
			  <el-button style="float:right;" :loading="downloading" @click="downloadList">导出</el-button>  
			</div>
		 </el-row>
		 <GlobalTable ref="globalTable"
		  :tableData="tableData"  height="calc(100vh - 270px)" 
		  :defaultSort="{ prop: 'approval_date', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
		  style="width: 100%;margin-bottom:16px;">
		 	<template #field>
		   <el-table-column label="平台商品信息" prop="sku"   sortable="custom" show-overflow-tooltip >
		   	<template #default="scope">
		   	<div class="flex-center">
		   		   <img v-if="scope.row.image" :src="scope.row.image" class="img-40"  width="40" height="40"  /> 
		   		   <img v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  />
		   		   <div >
		   			   <div  >{{scope.row.name}}</div>
		   			   <p class="sku">{{scope.row.sku}} 
		 				  <span class="font-extraSmall"> ASIN:{{scope.row.asin}}</span>
		   			   </p>
		   		   </div>
		   	</div>
		   	</template>
		   </el-table-column>
		    <el-table-column label="费用时间" prop="approval_date"   sortable="custom" width="210" >
					<template #default="scope">
						{{dateTimesFormat(scope.row.approval_date)}}
						<div class="font-extraSmall"><span >订单号:</span>{{scope.row.amazon_order_id}}</div>
					</template>
			</el-table-column>
			<el-table-column label="报销原因" prop="reason"  sortable="custom"   width="220"   >
				<template #default="scope">
					{{scope.row.reason}}
					<div class="font-extraSmall"><span >报销号:</span>{{scope.row.reimbursement_id}}</div>
				</template>
			</el-table-column>
			<el-table-column label="每个金额" prop="amount_per_unit"   width="100"  sortable="custom"  >
			</el-table-column>
			<el-table-column label="总金额" prop="amount_total"   width="100"  sortable="custom"  >
			</el-table-column>
			<el-table-column label="币种" prop="currency_unit"    width="80"   >
			</el-table-column>
			<el-table-column label="已偿还数量" prop="quantity_reimbursed_cash"   width="120" sortable="custom"  >
			</el-table-column>
			<el-table-column label="已报销数量" prop="quantity_reimbursed_inventory"  width="120"  sortable="custom"  >
			</el-table-column>
			<el-table-column label="已报数量总计" prop="quantity_reimbursed_total"   width="140" sortable="custom"   >
			</el-table-column>
		</template>
		</GlobalTable>
	</div>
	</div>
</template>
<script>
    export default{ name:"赔偿费用" };
</script>
<script setup>
		import { ref,reactive,onMounted,toRefs} from 'vue';
		import Group from '@/components/header/group.vue';
		import Datepicker from '@/components/header/datepicker.vue';
		import {dateFormat,dateTimesFormat} from '@/utils/index.js';
		import { ElMessage, ElMessageBox} from 'element-plus';
		import reimbursementsFeeApi from '@/api/amazon/finances/reimbursementsFeeApi.js';
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
			state.queryParams.toDate=dates.end;
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
			  reimbursementsFeeApi.list(params).then(res=>{
					 state.tableData.records=res.data.records;
					 state.tableData.total=res.data.total;
					 state.isload=false;
			  })
		}
		
		function downloadList(){
			state.downloading=true;
			reimbursementsFeeApi.downloadList(state.queryParams,()=>{
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