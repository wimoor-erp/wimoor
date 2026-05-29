<template>
	<div class="main-sty">
		<div class="con-header">
	    	<Header @getdata="changedata" @deleterows="handleDelete" ref="headerRef"  />
		</div>
		<div class="con-body">
			<GlobalTable ref="globalTable" :defaultSort="defaultSort"  @selectionChange='selectChange' :tableData="tableData" height="calc(100vh - 220px)" :stripe="true"  @loadTable="loadTableData" style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column type="selection"></el-table-column>
				<el-table-column label="单据编码" prop="number" />
				<el-table-column label="调出SKU" prop="sku_from"/>
				<el-table-column label="调入SKU" prop="sku_to"/>
				<el-table-column label="调动数量" prop="amount"/>
				<el-table-column label="操作仓库" prop="warehouse"/>
				<el-table-column label="创建人" prop="creator"/>
				<el-table-column label="创建时间" prop="createdate">
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
    export default{ name:"代料单" };
</script>
<script setup>
	import Header from"./components/header.vue"
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne,} from '@icon-park/vue-next';
	import {Histogram} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import changeApi from '@/api/erp/inventory/changeApi.js';
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
			path:"/e/w/e/d",
			query:{
				title:'代料单详情',
				path:"/e/w/e/d",
				id:id
			},
		})
	}
	function handleDelete(){
		if(state.selectrows && state.selectrows.length>0){
					 ElMessageBox.confirm(
						'删除后库存将会撤销，确定删除吗？',
						'删除代料单',
						{
						  confirmButtonText: '确定',
						  cancelButtonText: '取消',
						  type: 'warning',
						}
					  )
						.then(() => {
						  var ids="";
						  state.selectrows.forEach(function(item){
							  ids+=(item.id+",");
						  });
						  changeApi.deleteData({"ids":ids}).then((res)=>{
									  if(res.data){
										  ElMessage.success( res.data);
										  headerRef.value.handleQuery();
									  }else{
										  ElMessage.error("操作失败");
									  }
								  });
						})
						.catch(() => {
						  ElMessage.info( '取消删除');
						})
		}else{
			 ElMessage.error("请选择至少一行数据！");
		}
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
