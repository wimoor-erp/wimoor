<template>
	<el-dialog
	    v-model="warningVisible"
	    title="指标设置"
	    width="600px"
	  >
	  <span class="font-extraSmall">下降比率范围在5% ~ 100%之间,默认20%,下降值必须大于0,默认为0,超出下方设定的值即会预警。</span>
	  <el-table :data="tableData"
	  class="m-t-8"
	  >
		  <el-table-column label="预警字段" prop="name">
		  </el-table-column>
		  <el-table-column label="下降比率超">
			  <template #default="scope">
				  <el-input v-model="scope.row.num">
					 <template #append>%</template>
				  </el-input>
			  </template>
		  </el-table-column>
		  <el-table-column label="且下降值超">
			  <template #default="scope">
					  <el-input v-model="scope.row.absoluteNum">
					  </el-input>
			  </template>
		  </el-table-column>
	  </el-table>
	    <template #footer>
	        <el-button @click="warningVisible = false">取消</el-button>
	        <el-button type="primary" @click="handleSubmit">
	          保存
	        </el-button>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs} from'vue';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import { ElMessage } from 'element-plus';
	const state = reactive({
		warningVisible:false,
		tableData:[
			{num:'',absoluteNum:"",field:"Impressions",name:'曝光量'},
			{num:'',absoluteNum:"",field:"Clicks",name:'点击量'},
			{num:'',absoluteNum:"",field:"CSRT",name:'转化率'},
			{num:'',absoluteNum:"",field:"ACOS",name:'ACOS'},
			],
		data:{},
		waringType:"",
		wardatatype:"",
	})
	const{
		 warningVisible,
		 tableData,
		 data,
	}=toRefs(state);
	function show(waringType,wardatatype){
		state.warningVisible=true;
		state.waringType=waringType;
		state.wardatatype=wardatatype;
		summaryApi.getAdvWrningData({ftype:state.waringType,recordType:state.wardatatype}).then(res=>{
			if(res.data){
			   state.data=res.data;
			}else{
				state.data={
							 numImpressions:20,
							 numClicks:20,
							 numCSRT:20,
							 numACOS:20,
							 absoluteNumImpressions:0,
							 absoluteNumClicks:0,
							 absoluteNumCSRT:0,
							 absoluteNumACOS:0
						}
			} 
			state.tableData[0].num=state.data.numImpressions;
			state.tableData[0].absoluteNum=state.data.absoluteNumImpressions;
			state.tableData[1].num=state.data.numClicks;
			state.tableData[1].absoluteNum=state.data.absoluteNumClicks;
			state.tableData[2].num=state.data.numCSRT;
			state.tableData[2].absoluteNum=state.data.absoluteNumCSRT;
			state.tableData[3].num=state.data.numACOS;
			state.tableData[3].absoluteNum=state.data.absoluteNumACOS;
		})
	}
	function handleSubmit(){
		var data={};
		data.numImpressions=state.tableData[0].num;
		data.absoluteNumImpressions=state.tableData[0].absoluteNum;
		data.numClicks=state.tableData[1].num;
		data.absoluteNumClicks=state.tableData[1].absoluteNum;
		data.numCSRT=state.tableData[2].num;
		data.absoluteNumCSRT=state.tableData[2].absoluteNum;
		data.numACOS=state.tableData[3].num;
	    data.absoluteNumACOS=state.tableData[3].absoluteNum;
		data.ftype=state.waringType;
		data.recordtype=state.wardatatype;
		summaryApi.saveAdvWrningData(data).then(res=>{
			 ElMessage.success("保存成功");
			 show(state.waringType,state.wardatatype);
		     state.warningVisible = false
		})
	}
	defineExpose({show});
</script>

<style>
</style>