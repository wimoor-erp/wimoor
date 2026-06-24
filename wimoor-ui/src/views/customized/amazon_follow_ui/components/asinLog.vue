<template>
	<el-dialog
	title="操作日志"
	v-model="visable"
	top="5vh"
	width="70%"
	>
	<el-space>
		<el-select placeholder="操作类型" v-model="opttype">
			<el-option key="" value="" label="全部"></el-option>
			<el-option key="upload" value="upload" label="发布"></el-option>
			<el-option key="sales" value="sales" label="销售"></el-option>
			<el-option key="unsales" value="unsales" label="停售"></el-option>
			<el-option key="delete" value="delete" label="删除"></el-option>
		</el-select>
		<el-button type="primary" @click="handleQuery">查询</el-button>
	</el-space>
	<div class=" m-t-8">
	<el-table ref="globalTable" :data="tableData"  height="calc(60vh)" 
	 :stripe="true"
	>
			<el-table-column label="说明">
				<template #default="scope">
					站点：{{scope.row.groupname}}{{scope.row.marketname}}，{{scope.row.opttype}}
					，{{scope.row.remark}}
					<p class="font-extraSmall">时间设置编号:{{scope.row.timename}}</p>
				</template>
			</el-table-column>
			<el-table-column label="时间" >
				<template #default="scope">
					{{dateTimesFormat(scope.row.opttime) }}
				   </template>
			</el-table-column>
			<el-table-column label="状态" width="80" prop="opttypestatus">
			</el-table-column>
	</el-table>
	</div>
	<template #footer>
		<el-button @click="visable=false">关闭</el-button>
	</template>
	</el-dialog>
</template>


<script setup>
	import {ref,reactive,toRefs,onMounted,}from"vue";
	import {dateFormat,dateTimesFormat,} from '@/utils/index.js';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	const state=reactive({
		visable:false,
		opttype:"",
		pid:null,
		tableData:[],
	})
	const {
		visable,
		tableData,
		opttype,
		pid,
	}=toRefs(state)
	
	function handleQuery(){
		followListApi.findRecordList({"pid":state.pid,"opttype":state.opttype}).then((res)=>{
			state.tableData=res.data;
		});
	}
	function show(pid){
		state.visable = true;
		state.pid=pid;
		handleQuery();
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>