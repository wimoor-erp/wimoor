<template>
	<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" :height="inForm?'calc(100vh - 600px)':'calc(100vh - 200px)'"  :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column fixed type="selection" width="38" />
		<el-table-column label="入库单编码" prop="number"></el-table-column>
		<el-table-column label="入库仓库" prop="wname"></el-table-column>
		<el-table-column label="申请人" prop="creator"></el-table-column>
		<el-table-column label="备注" prop="remark"></el-table-column>
		<el-table-column label="创建时间" prop="opttime">
			 <template #default="scope">
				 {{dateTimesFormat(scope.row.opttime)}}
			 </template>
		</el-table-column>
		<el-table-column label="操作">
			<template #default='scope'>
			<el-space>
				<el-button @click="gotoDetails(scope.row.id)" type="primary" link>详情</el-button>
			  </el-space>	
			</template>
		</el-table-column>
		</template>
	</GlobalTable>
</template>

<script setup>
	import {h,ref,reactive,toRefs} from 'vue';
	import {Download,Edit} from '@element-plus/icons-vue';
	import {MoreOne} from '@icon-park/vue-next';
	import inApi from '@/api/erp/inventory/inApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {useRouter } from 'vue-router';
	const emit = defineEmits(['selectrow']);
	let props = defineProps({inForm:null});
	const {inForm} = toRefs(props);
	const router = useRouter()
	let globalTable=ref();
	let state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		defaultSort:{"prop": 'opttime', "order": 'descending' }
	})
	let {
		tableData,
		queryParams,
		defaultSort,
	} = toRefs(state)
	function gotoDetails(id){
		router.push({
			path:"/e/w/i/d",
			query:{
				title:'入库单详情',
				path:"/e/w/i/d",
				id:id
			},
		})
	}
	function load(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	function loadTableData(params){
		inApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	function selectChange(selection) {
		 emit('selectrow',selection);
	}
	defineExpose({
	  load,
	})
</script>

<style>

</style>
