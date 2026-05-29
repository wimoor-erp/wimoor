<template>
	<div class="main-sty">
		<div class="con-header">
			<Header @getdata="changedata" ref="headerRef"  />
		</div>
		<div class="con-body">
		<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" height="calc(100vh - 252px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column label="调库单编码" prop="number" />
				<el-table-column label="调出仓库" prop="wfname"/>
				<el-table-column label="调入仓库" prop="wtname"/>
				<el-table-column label="状态" width="120">
					<template #default="scope">
						<el-tooltip>
							<template #content>
								<div style="padding-top:10px;padding-bottom:10px" v-if="scope.row.records" v-for="item in scope.row.records">{{item.operator}}-{{dateTimesFormat(item.opttime)}}-
								<span v-if="item.auditstatus==0" effect="plain" type="warning" >保存</span>
								<span v-if="item.auditstatus==1" effect="plain" type="info" >提交</span>
								<span v-if="item.auditstatus==2"  effect="plain" type="primary">审核</span>
								<span v-if="item.auditstatus==3" effect="plain" type="primary">发货</span>
								<span v-if="item.auditstatus==4" type="success" effect="plain">收货</span>
								<span v-if="item.auditstatus==5" type="danger" effect="plain">驳回</span>
								</div>
							</template>
						<el-tag v-if="scope.row.auditstatus==0" effect="plain" type="warning" >未提交</el-tag>
						<el-tag v-if="scope.row.auditstatus==1" effect="plain" type="info" >待审核</el-tag>
						<el-tag v-if="scope.row.auditstatus==2"  effect="plain" type="primary">配货中</el-tag>
						<el-tag v-if="scope.row.auditstatus==3" effect="plain" type="primary">已发货</el-tag>
						<el-tag v-if="scope.row.auditstatus==4" type="success" effect="plain">已完成</el-tag>
						<el-tag v-if="scope.row.auditstatus==5" type="danger" effect="plain">已驳回</el-tag>
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column label="申请人" prop="creator"/>
				<el-table-column label="申请时间" prop="createdate">
					<template #default="scope">
						<span>{{dateTimesFormat(scope.row.createdate)}}</span>
					</template>
				</el-table-column>
				<el-table-column label="审核人" prop="auditor"/>
				<el-table-column label="审核时间" prop="audittime">
					<template #default="scope">
						<span>{{dateTimesFormat(scope.row.audittime)}}</span>
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
	import dispatchApi from '@/api/erp/inventory/dispatchApi.js';
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
			path:"/e/w/t/d",
			query:{
				title:'调库单详情',
				path:"/e/w/t/d",
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
		if(params.auditstatus=='1' || params.auditstatus=='2' || params.auditstatus=='3'){
			params.fromDate=undefined;
			params.toDate=undefined;
		}
		dispatchApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	onMounted(() => {
	   
	});
</script>

<style>
</style>
