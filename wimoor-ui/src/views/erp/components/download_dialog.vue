<template>
		<el-dialog
		    v-model="dialog.visible"
		    width="40%"
			title="下载报告"
			class="downRptDialog"
		    :before-close="handleClose"
		  >
       <el-table :data="filelist" v-loading="loading">
            <el-table-column  label="创建时间">
          			 <template #default="scope"> 
          			  <div>{{dateTimesFormat(scope.row.createtime)}}</div>
          			 </template>
          </el-table-column>
          <el-table-column label="状态" >
          	<template #default="scope">
				<el-space>
          		 <el-tag v-if="scope.row.isrun">生成中...</el-tag>
				 <el-tag v-else type="success">已完成</el-tag>
				 <div v-if="scope.row.log" :title="scope.row.log" class="text-red">报告生成异常</div>
				</el-space>
          	</template>
          </el-table-column>
		  <el-table-column label="状态" >
		  	<template #default="scope">
				<el-space>
		  		 <el-button  v-if="!scope.row.isrun" type="primary" @click="downReport(scope.row)">下载</el-button>
				  <el-button   type="danger" @click="deleteReport(scope.row)">删除</el-button>
				</el-space>
		  	</template>
		  </el-table-column>
       </el-table>
	   <div style="padding-top:10px" class="text-right">
		    <el-space ><el-button @click="handleQuery">刷新</el-button><el-button @click="handleReport">生成报告</el-button></el-space>  
	   </div>
	  
       </el-dialog>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,unref} from 'vue'
	import downloadFileApi from '@/api/erp/common/downloadTemplateFileApi.js';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	let props = defineProps({  ftype:undefined  });
	const {  ftype} = toRefs(props);
	const state = reactive({
	   // 弹窗属性
	   filelist:[],
	   loading:false,
	   dialog: { visible: false }  ,
	 });
	 const {
	   dialog,filelist,loading
	 } = toRefs(state);
	function handleQuery(){
		downloadFileApi.downloadlist({"ftype":props.ftype}).then(res=>{
			state.filelist=res.data;
			state.loading=false;
		})
		
	}
	function deleteReport(row){
		var data={id:row.id};
		downloadFileApi.deletedownload(data).then(res=>{
			handleQuery();
		});
	}
	function downReport(row){
		var data={id:row.id,ftype:row.ftype};
		downloadFileApi.downExcel(data);
	}
	function handleReport(){
		emit("change");
	}
	function show(){
		var ftype=props.ftype?props.ftype:"purchase_time";
		state.loading=true;
		state.dialog.visible=true;
		handleQuery();
	}
	 defineExpose({ show})
</script>

<style >
 .downRptDialog .el-dialog__body{
	 padding-top:0px !important;
 }
</style>