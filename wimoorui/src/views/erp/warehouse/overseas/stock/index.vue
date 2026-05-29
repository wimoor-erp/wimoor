<template>
	<div class="main-sty">
		<div class="con-header">
			<Header @getdata="changedata" ref="headerRef"  />
		</div>
		<div class="con-body">
		<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" height="calc(100vh - 252px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column label="备货单号" width="140" prop="number" />
				<el-table-column label="发货仓库" prop="wfname"/>
				<el-table-column label="海外仓库" prop="wtname" >
					<template #default="scope">
					   <div>{{scope.row.wtname}}</div>
					   <div class="font-extraSmall">
						   <span v-if="scope.row.groupname">{{scope.row.groupname}}/</span>
						   <span>{{scope.row.country}}</span></div>
					</template>
				</el-table-column>
				<el-table-column label="创建日期" width="140" prop="arrivalTime">
					<template #default="scope">
						<div>{{dateFormat(scope.row.createdate)}}</div>
						<div class="font-extraSmall">到货日期:{{dateFormat(scope.row.arrivalTime)}}</div>
					</template>
				</el-table-column>
				<el-table-column label="SKU个数" width="100" prop="skunum"/>
				<el-table-column label="发货量" width="100" prop="amount"/>
				<el-table-column label="物流渠道" prop="company">
					<template #default="scope">
						<span>{{scope.row.company}}</span>
						<div class="font-extraSmall">{{scope.row.channame}}</div>
					</template>
				</el-table-column>
				<el-table-column label="申请人" width="100" prop="creator"/>
				<el-table-column label="状态" width="120">
					<template #default="scope">
						<el-tag v-if="scope.row.auditstatus==1" effect="plain" type="info" >待审核</el-tag>
						<el-tag v-if="scope.row.auditstatus==2"  effect="plain" type="primary">配货中</el-tag>
						<el-tag v-if="scope.row.auditstatus==3" effect="plain" type="success">已发货</el-tag>
						<el-tag v-if="scope.row.auditstatus==4" type="success" effect="plain">已完成</el-tag>
						<el-tag v-if="scope.row.auditstatus==5" type="danger" effect="plain">已驳回</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="备注" prop="remark"/>
				<el-table-column label="操作" width="70">
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
    export default{ name:"海外仓备货单" };
</script>
<script setup>
	import Header from"./components/header.vue"
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import dispatchApi from '@/api/erp/inventory/dispatchOverseaApi.js';
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
			path:"/e/w/os/d",
			query:{
				title:'海外仓备货单详情',
				path:"/e/w/os/d",
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
