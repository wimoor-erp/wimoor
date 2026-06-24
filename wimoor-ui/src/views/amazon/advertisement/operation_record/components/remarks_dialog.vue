	<template>
		<el-dialog v-model="dialog.visible" title="备注" :destroy-on-close='true' width="800px"  >
				<el-row :gutter="32">
					<el-col :span="24">
						<el-input v-model="remark" placeholder="输入备注" :rows="8"  type="textarea"  />
						<el-button class="m-t-8" type="primary" @click="submitForm" >提交</el-button>
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
		import operationApi from '@/api/amazon/advertisement/operation/operationApi.js';
		import {dateFormat,dateTimesFormat,decodeText} from "@/utils/index";
		import { ElMessage, ElMessageBox } from 'element-plus';
		const emit = defineEmits(['confirm']);
		const state=reactive({
			  dialog:{visible:false},
			  remark:"",
			  row:{},
			  id:"",
		})
		const {
		   dialog,remark,
		} = toRefs(state);
		function submitForm(){
			var formdata={'id':state.id,"remark":state.remark};
			operationApi.updateOperateLogRemark(formdata).then(res=>{
				ElMessage.success("保存成功");
				state.dialog.visible=false;
				state.row.remark=state.remark;
				emit("confirm",state.row);
			});
		}
				function show(row){
					 state.dialog.visible=true;
					 state.id=row.id;
					 state.row=row;
					 state.remark=row.remark;
				}
			   defineExpose({
			      show
			   })
	</script>
	
	<style>
		 
	</style>
	
