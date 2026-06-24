	<template>
		<el-dialog v-model="dialog.visible" title="产品公告" :destroy-on-close='true' width="800px"  >
				<el-row :gutter="32">
					<el-col :span="12" class="border-right">
						<div style="padding-bottom:10px;"><span>修改时间:{{dateTimesFormat(opttime)}}</span> <span style="float:right;padding-right:5px;">操作人:{{operator}}</span></div>
						<el-input v-model="remarks" placeholder="输入公告" :rows="8"  type="textarea"  />
						<el-button class="m-t-8" type="primary" @click="submitForm" >提交</el-button>
					</el-col>
					<el-col :span="12">
						<el-scrollbar class="remark-history">
						<h4>公告日志</h4>
						 <el-timeline>
						    <el-timeline-item
						      v-for="(activity, index) in activities"
						      :key="index"
						      :timestamp="activity.timestamp"
							  :hollow="true"
							   type='success'
						    >
						      {{ activity.content }}
						    </el-timeline-item>
						  </el-timeline>
						  </el-scrollbar>
					</el-col>
				</el-row>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="dialog.visible = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
	<script setup>
		import {ref,reactive,onMounted,toRefs} from "vue";
		import markApi from '@/api/erp/material/markApi.js';
		import {dateFormat,dateTimesFormat,decodeText} from "@/utils/index";
		import { ElMessage, ElMessageBox } from 'element-plus';
		const emit = defineEmits(['confirm']);
		const state=reactive({
			  dialog:{visible:false},
			  remarks:"",
			  materialid:"",
			  activities: [],
			  opttime:null,
			  operator:null,
		})
		const {
		   dialog,remarks,activities,opttime,operator
		} = toRefs(state);
		function submitForm(){
			var formdata={'materialid':state.materialid,"mark":state.remarks};
			markApi.saveNotice(formdata).then(res=>{
				ElMessage.success( "保存成功");
				state.dialog.visible=false;
				emit("confirm",formdata);
			});
		}
				function show(materialid){
					 state.dialog.visible=true;
					 state.activities= [];
					 state.materialid=materialid;
					 markApi.showNotice({'materialid':materialid}).then(res=>{
						 if(res.data){
							 state.remarks=res.data.mark;
							 state.opttime=res.data.opttime;
							 state.operator=res.data.operator;
							 if(res.data.hisList){
								 res.data.hisList.forEach(item=>{
									 var row={content:item.mark,timestamp:""};
									 row.timestamp=dateTimesFormat(item.opttime)+" | 操作人："+item.operator;
									 state.activities.push(row);
								 })
							 }
						 }else{
							 state.remarks="";
						 }
						 
					 })
				}
			   defineExpose({
			      show
			   })
	</script>
	
	<style>
		.border-right{
			border-right:1px solid #e4e7ed;
		}
		.remark-history h4{
			margin-bottom:16px;
		}
		.el-timeline-item__node--normal{
			width: 10px;
			height:10px;
			left: 0px;
		}
		.el-timeline-item__timestamp{
			color: #bcbec3;
		}
	</style>
	
