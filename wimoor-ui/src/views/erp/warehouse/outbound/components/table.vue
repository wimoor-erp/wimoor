<template>
	<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" :height="inForm?'calc(100vh - 600px)':'calc(100vh - 200px)'" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column fixed type="selection" width="38" />
		<el-table-column label="出库单编码" prop="number"></el-table-column>
		<el-table-column label="出库仓库" prop="wtoname"></el-table-column>
		<el-table-column label="收件人" prop="customer"></el-table-column>
		<el-table-column label="物流快递" prop="express"></el-table-column>
		<el-table-column label="快递单号" prop="expressno"></el-table-column>
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
	import outApi from '@/api/erp/inventory/outApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {useRouter } from 'vue-router';
	const emit = defineEmits(['selectrow']);
	const router = useRouter()
	let props = defineProps({inForm:null});
	const {inForm} = toRefs(props);
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
			path:"/e/w/o/d",
			query:{
				title:'出库单详情',
				path:"/e/w/o/d",
				id:id
			},
		})
	}
	function load(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	function loadTableData(params){
		outApi.list(params).then((res)=>{
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
