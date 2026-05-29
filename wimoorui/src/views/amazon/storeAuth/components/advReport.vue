<template>
	<el-dialog
	   v-model="dialogVisible"
	   title="广告报告申请"
	   width="60%"
	   :before-close="handleClose"
	 >
	 <el-space style="margin-bottom: 10px;">
	 	<div>重新申请报表：</div>
	    <Datepicker ref="datepickersRef" @changedate="changedate" />
		<el-button class="button" @click="submitForm" type="primary">提交</el-button>
					   <div class="font-extraSmall text-red"><el-icon><InfoFilled></InfoFilled></el-icon>注意若无异常请勿手动申请报表</div>
		</el-space>
		<el-table :data="tableData" height="calc(100vh - 500px)"  :default-sort="{ prop: 'opttime', order: 'descending' }">
			<el-table-column label="区域-国家" prop="name"  width="100"  sortable>
				<template #default="scope">
					<div>
						{{scope.row.region_name}}-{{scope.row.name}}
				    </div>
				</template>
			</el-table-column>
			<el-table-column label="报表类型" prop="recordType" width="100" sortable>
				<template #default="scope">
						<div>
							{{scope.row.recordType}}
					    </div>{{scope.row.campaignType}}
					</template>
				</el-table-column>
			<el-table-column label="开始日期" prop="startDate" width="100"  sortable>
				<template #default="scope">
					<div>
						{{dateFormat(scope.row.startDate)}}
				    </div>
				</template>
			</el-table-column>
			<el-table-column label="结束日期" prop="endDate" width="100"   sortable>
				<template #default="scope">
					<div>
						{{dateFormat(scope.row.endDate)}}
				    </div>
				</template>
			</el-table-column>
			<el-table-column label="申请时间" prop="requesttime" width="100"  sortable>
			</el-table-column>
			<el-table-column label="操作时间" prop="opttime" width="100" sortable>
			</el-table-column>
			<el-table-column label="状态" prop="status"  sortable>
			</el-table-column>
			<el-table-column label="大小" prop="fileSize"  sortable>
			</el-table-column>
			<el-table-column label="处理状态" prop="treat_status" sortable>
			</el-table-column>
		</el-table>
	   <template #footer>
	     <div class="dialog-footer">
	       <el-button @click="dialogVisible = false">关闭</el-button>
	     </div>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ref,reactive,onMounted,toRefs} from "vue";
	import {Edit,ArrowDown,Delete} from '@element-plus/icons-vue';
	import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import authApi from '@/api/amazon/auth/authApi.js';
	import authAdvApi from '@/api/amazon/advertisement/auth/authApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import AuthStore from "./authstore.vue"
	import AuthAdv from "./authadv.vue"
	const  state=reactive({
		dialogVisible:false,
		authid:"",
		fromDate:null,
		endDate:null,
		submitloading:false,
		tableData:[],
	})
	const{
		dialogVisible,authid,fromDate,endDate,tableData,submitloading
	}=toRefs(state);
	function show(auth){
		state.authid=auth;
		state.dialogVisible=true;
		handleQuery();
	}
	function changedate(dates){
		state.fromDate=dates.start;
		state.endDate=dates.end;
	}
	function handleQuery(){
		authAdvApi.listReport({"authid":state.authid}).then(res=>{
			 state.tableData=res.data;
		})
	}
	function submitForm(){
		state.submitloading=true;
		authAdvApi.requestReportByDate({"authid":state.authid,"start":state.fromDate,"end":state.endDate}).then(res=>{
			ElMessage.success("操作成功");
			state.submitloading=false;
			handleQuery();
		}).catch(e=>{
			state.submitloading=false;
		})
	}
	
	 defineExpose({ show })
</script>

<style>
</style>