<template>
	<div class="main-sty">
		<div class="con-header">
			<Header @getdata="changedata" ref="headerRef"  />
		</div>
		<div class="con-body">
		<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" height="calc(100vh - 252px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column label="换货单编码" prop="number" />
				<el-table-column label="采购单编码" prop="pnumber" />
				<el-table-column label="操作仓库" prop="wname"/>
				<el-table-column label="状态" width="120">
					<template #default="scope">
						<el-tag v-if="scope.row.auditstatus==1" effect="plain" type="warning" >采购待确认</el-tag>
						<el-tag v-if="scope.row.auditstatus==2" effect="plain" type="warning" >仓库待收货</el-tag>
						<el-tag v-if="scope.row.auditstatus==3" effect="plain" type="success" >已完成</el-tag>
						<el-tag v-if="scope.row.auditstatus==4" effect="plain" type="success" >已取消</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="SKU" prop="sku"/>
				<el-table-column label="数量" prop="amount"/>
				<el-table-column label="申请人" prop="creator"/>
				<el-table-column label="申请时间" prop="createdate">
					<template #default="scope">
						<span>{{dateTimesFormat(scope.row.createdate)}}</span>
					</template>
				</el-table-column>
				<el-table-column label="备注" prop="remark"/>
				<el-table-column label="操作">
					<template #default='scope'>
					<el-space>
						<el-button @click="handleDetail(scope.row.id)" type="primary" link>详情</el-button>
					  </el-space>	
					</template>
				</el-table-column>
				</template>
			</GlobalTable>
		</div>
	</div>
</template>
<script>
    export default{ name:"调库单" };
</script>
<script setup>
	import Header from"./components/header.vue"
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import changeApi from '@/api/erp/purchase/change/changeApi.js';
	const router = useRouter();
	let globalTable=ref();
	let headerRef=ref();
	

	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'desc' },
		selectrows:[]
	})
	const {
		tableData,
		queryParams,
		defaultSort,
		selectrows
	}=toRefs(state)
	 
	function handleDetail(id){
		router.push({
			path:"/e/p/c/d",
			query:{
				title:'查看换货单',
				path:"/e/p/c/d",
				id:id
			},
		})
	}
	function changedata(params){
		state.queryParams=params;
		state.queryParams.sort=state.defaultSort.prop;
		state.queryParams.order=state.defaultSort.order;
		globalTable.value.loadTable(state.queryParams);
	}
	function selectChange(selection) {
		state.selectrows=selection;
	}
	function loadTableData(params){
		// if(params.auditstatus=='1' || params.auditstatus=='2' || params.auditstatus=='3'){
		// 	params.fromDate=undefined;
		// 	params.toDate=undefined;
		// }
		changeApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	onMounted(() => {
	   
	});
</script>

<style>
</style>
