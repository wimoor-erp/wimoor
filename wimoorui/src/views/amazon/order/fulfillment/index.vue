<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
			 <el-space>
              <Group  defaultValue="only" @change="groupChange"    isproduct="ok"></Group>
			   <Datepicker :shortIndex="1" ref="datepickers" @changedate="changedate" />
			  <el-select v-model="queryParams.status" placeholder="订单状态" @change="handleQuery">
				  <el-option label="全部订单状态" key="" value=""></el-option>
				  <el-option label="订单已完成" key="Complete" value="Complete"></el-option>
				  <el-option label="订单处理中" key="Processing" value="Processing"></el-option>
			  </el-select>
			  <el-input  v-model="queryParams.orderid" @input="handleQuery" placeholder="亚马逊订单号" style="width: 250px;" class="input-with-select" >
			  	<template #suffix>
			  	  <el-icon style="font-size:16px;" class="el-input__icon pointer" @click="handleQuery"><search/></el-icon>
			     </template> 
			  </el-input>
			 </el-space>
			</el-row>
        </div>
		<div class="con-body">
			 <GlobalTable ref="globalTable"
			   :tableData="tableData"  height="calc(100vh - 210px)" 
			   @selectionChange='handleSelect' 
			   :defaultSort="{ prop: 'displayableOrderDate', order: 'descending' }"  
			   @loadTable="loadTableData" 
			   :stripe="true"  
			   style="width: 100%;margin-bottom:16px;">
			  	<template #field>
				<el-table-column label="亚马逊订单号" prop="sellerFulfillmentOrderId"   width="180"    sortable="custom" >
				</el-table-column>
				<el-table-column prop="displayableOrderId" label="其它平台订单号"   width="180"    sortable="custom">
				</el-table-column>
				<el-table-column prop="displayableOrderDate" label="购买时间"  width="180"  sortable="custom">
					<template #default="scope">
					    {{dateTimesFormat(scope.row.displayableOrderDate)}} 
					</template>
				</el-table-column>
				<el-table-column prop="displayableOrderComment" label="订单评论"   width="150"    sortable="custom">
				</el-table-column>
				<el-table-column prop="fulfillmentOrderStatus" label="订单状态"  width="100"  sortable="custom">
					<template #default="scope">
					   <el-tag v-if="scope.row.fulfillmentOrderStatus=='Complete'" type="success">已完成</el-tag>
					   <el-tag v-if="scope.row.fulfillmentOrderStatus=='Processing'" type="warning">处理中</el-tag>
					</template>
				</el-table-column>
				
				<el-table-column prop="destinationAddress" label="订单联系信息"     sortable="custom">
					<template #default="scope">
					   <span v-if="scope.row.address">
						   <el-space>姓名:{{scope.row.address.name}}</el-space>
						   <el-space>地址:{{scope.row.address.addressLine1}}</el-space>
						   <el-space>城市:{{scope.row.address.city}}</el-space>
						   <el-space>州/区:{{scope.row.address.stateOrRegion}}</el-space>
						   <el-space>国家:{{scope.row.address.countryCode}}</el-space>
						   <el-space>邮政编码:{{scope.row.address.postalCode}}</el-space>
						   <el-space>国家:{{scope.row.address.phone}}</el-space>
					   </span>
					</template>
				</el-table-column>
				<el-table-column prop="sellerFulfillmentOrderId" label="操作"    width="120" >
					<template #default="scope">
					    <el-link type="primary" @click.stop="showInfo(scope.row)">查看</el-link>
					</template>
				</el-table-column>
			 		 
					 
			  	</template>
			  </GlobalTable>
		</div>
</div>

<el-dialog
	    v-model="dialogVisible"
	    title="查看订单详情"
	    width="70%" 
	  >
	      666666
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button type="primary" @click="dialogVisible = false"  >关闭</el-button>
	      </span>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {Search,} from '@element-plus/icons-vue';
	import {MoreOne} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import fulfillmentOrderApi from "@/api/amazon/order/fulfillmentOrderApi.js";
	
	let globalTable=ref();
	let datepickers=ref();

	let state = reactive({
		tableData: {records:[],total:0},queryParams:{status:""},
		isload:true,
		dialogVisible:false,
	})
	let {
		tableData,queryParams,isload,dialogVisible
		 } =toRefs(state);
	
	function groupChange(obj){
				state.queryParams.groupid=obj.groupid;
			    state.queryParams.marketplaceid=obj.marketplaceid;
				handleQuery();
	}
	
	//日期改变
	function changedate(dates){
		state.queryParams.startDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
		   handleQuery();
		}
	}
	
	function handleQuery(){
		if(state.queryParams.groupid){
			globalTable.value.loadTable(state.queryParams);
			state.isload=false;
		}
	}
	
	function loadTableData(params){
		fulfillmentOrderApi.list(params).then((res)=>{
			if(res.data.total>0){
				res.data.records.forEach(item=>{
					if(item.destinationAddress){
						var addre=JSON.parse(item.destinationAddress);
						item.address=addre;
					}
				});
			}
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	
	function showInfo(){
		state.dialogVisible=true;
	}
	
	
</script>

<style scoped>
	
</style>