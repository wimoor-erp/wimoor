<template >
	<div class="payment-cost-sharing expand-table">
	<GlobalTable
	ref="globalTable"
	 :tableData="tableData"
	  @expandChange="handleExpandChange"
	  @row-click="tableRowClick"
	  rowKey="id"
	 height="calc(100vh - 248px)"  @loadTable="loadTableData" >
		<template #field>
			<el-table-column type="expand" width="65" >
				 <template #default="props">
					<el-table :data="props.row.expandTable">
						<el-table-column label="分摊产品"  prop="sku" show-overflow-tooltip>
							<template #default="scope">
								<div class="flex-center">
									<el-image class="  product-img" :src="scope.row.image"></el-image>
									<div class="m-l-8">
										<div class="name">{{scope.row.pname}}</div>
										<div class="sku">{{scope.row.sku}}</div>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column label="金额" prop="amount">
							<template #default="scope">
								<span>{{scope.row.amount}}</span>
							</template>
						</el-table-column>
						<el-table-column label="日期" prop="byday">
							<template #default="scope">
							<span>{{dateFormat(scope.row.byday)}}</span>
							</template>
						</el-table-column>
					</el-table>
				 </template>	 
			</el-table-column>
		<!-- <el-table-column label="单据号" width="150" prop="code" />  
		    <el-table-column label="分摊日期" prop="date"></el-table-column> -->
		<el-table-column label="店铺"   prop="groupname" />
		<el-table-column label="国家"   prop="marketname" />
		<el-table-column label="费用类型"  prop="itemname"></el-table-column>
		<el-table-column label="货币"  prop="currency"></el-table-column>
		<el-table-column label="金额" prop="amount">
			<template #default="scope">
				<span>{{scope.row.amount}}</span>
			</template>
		</el-table-column>
		<!-- <el-table-column label="创建人" prop="operator">	</el-table-column>
		<el-table-column label="创建日期" prop="createDate">	</el-table-column>
		<el-table-column label="操作" width="140">
			<template #default="scope">
				<el-button type="primary" link @click="handleDetail(scope.row)">编辑</el-button>
				    <el-popover :visible="scope.row.desVisible" placement="left"   trigger="click" >
				      <p >确认要删除吗？删除后分摊的费用将退回！</p>
				      <div class="m-t-8">
				        <el-button size="small"  @click="scope.row.desVisible = false">取消</el-button>
				        <el-button size="small" type="primary" @click="scope.row.desVisible = false"
				          >确认</el-button
				        >
				      </div>
				      <template #reference>
						  <el-button type="primary" link @click="scope.row.desVisible=true">删除</el-button>
				      </template>
				    </el-popover>
			</template>
		</el-table-column> -->
		</template>
	</GlobalTable>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs} from 'vue';
	import {useRouter } from 'vue-router';
	import financesItemDataApi from '@/api/amazon/finances/financesItemDataApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	const router = useRouter()
	let globalTable=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
	})
	const{
		tableData,queryParams,
	}=toRefs(state)
	
	function handleDetail(){
		router.push({
			path:"/f/c/c",
			query:{
				title:'编辑分摊费用',
				path:"/f/c/c",
			},
		})
	}
	function handleQuery(){
	    globalTable.value.loadTable(state.queryParams);
	}
	function loadTableData(params){
		financesItemDataApi.getFinDataMonthList(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(res.data.total>0){
				state.tableData.records.forEach(item=>{
					item.expandTable=[];
				});
			}
		});
	}
	function show(params){
		state.queryParams=params;
		handleQuery();
	}
	 
	function handleExpandChange(row,expandedRows){
		//查询detail
		state.expendRows=expandedRows;
		if(row.expandTable&&row.expandTable.length==0){
			var data={};
			data.itemid=row.itemid;
			data.groupid=row.groupid,
			data.marketplaceid=row.marketplaceid;
			data.fromDate=state.queryParams.fromDate;
			data.endDate=state.queryParams.endDate;
			data.currency=row.currency;
			financesItemDataApi.getFinDataList(data).then((res)=>{
				res.data.forEach(item=>{
					row.expandTable.push(item);
				})
			});
		}
		
	}
	function tableRowClick(row){
		   globalTable.value.toggleRowExpansion(row);
	}
	defineExpose({
		show,
	})
</script>

<style >
	.payment-profit-report .expand-table .el-table__expanded-cell{
		padding:0px;
	}
	.payment-cost-sharing .expand-table .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.m-l-8{
		margin-left: 8px;
	}
</style>
 