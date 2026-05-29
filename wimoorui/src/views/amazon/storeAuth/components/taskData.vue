<template>
	<el-dialog
	title="报表任务抓取详情"
	v-model="visible"
	width="70%"
	top="1vh"
	>
	<div style="padding-bottom:10px ">
	      <el-space>
				   <div>重新申请报表：</div>
	      <el-select v-model="reportType" placeholder="请选择报表类型">
	      	 			 <el-option v-for="item in reportTypeList" :key="item.code" :label="item.cname" :value="item.code">{{item.cname}}</el-option>
	      </el-select>
	      <Datepicker ref="datepickersRef" @changedate="changedate" />
	      <el-button class="button" @click="submitForm" type="primary">提交</el-button>
			   <div class="font-extraSmall text-red"><el-icon><InfoFilled></InfoFilled></el-icon>注意若无异常请勿手动申请报表</div>
		  </el-space>
	  </div>
	<el-tabs
	   v-model="activeName"
	   class="markets-tabs-dialog"
	   @tab-change="handleClick"
	   
	 >
	  <el-tab-pane  v-for = "(item,index) in groupData.countrysList" :label="item.countryname" :name="item.marketplaceid"  >
		  
	  </el-tab-pane>
	 </el-tabs>
	 <el-table :data="tableData" v-loading="dataloading" height="calc(100vh - 400px)" scrollbar-always-on>
		 <el-table-column prop="cname" label="报表名称">
			 <template #default="scope">
			 	  {{scope.row.cname}}
			 </template>
		 </el-table-column>
		 <el-table-column   label="报表申请状态" prop="report_processing_status" >
			<template #default="scope">
				  <el-tag v-if="scope.row.report_processing_status=='success'" type="success">成功</el-tag>
				  <el-tag v-if="scope.row.report_processing_status=='FATAL'" type="warning">创建失败</el-tag>
				  <el-tag v-if="scope.row.report_processing_status=='CANCELLED'" type="warning">取消</el-tag>
				  <el-tag v-if="scope.row.report_processing_status=='DONE'" type="warning">已申请待处理</el-tag>
				  <el-tag v-if="scope.row.report_processing_status=='error'" type="danger">失败</el-tag>
			</template>	
		 </el-table-column>
		 <el-table-column   label="报表最后更新时间" prop="lastupdate" >
					<template #default="scope">
		 			 	  {{dateTimesFormat(scope.row.lastupdate)}}
		 			 </template>
		 </el-table-column>
	 </el-table>
	 
	<template #footer>
		<el-button @click="visible=false">关闭</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,} from '@element-plus/icons-vue';
    import '@/assets/css/packbox_table.css';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import authApi from '@/api/amazon/auth/authApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import limitApi from '@/api/sys/admin/limitApi.js';
	import { ElMessage, ElMessageBox} from 'element-plus';
	const  datepickersRef=ref();
	const  state=reactive({
		isload:true,
		tableData: []  ,
		queryParams:{},
		visible:false,
		groupData:{},
		fromDate:"",
		endDate:"",
		dataloading:false,
		reportType:"",
		reportTypeList:[],
		activeName:null,
	})
	const{
		isload,
		tableData,
		queryParams,
		fromDate,
		endDate,
		reportType,
		dataloading,
		reportTypeList,
		visible,
		groupData,
		activeName,
	}=toRefs(state)
	
	 function changedate(dates){
	 	state.fromDate=dates.start;
	 	state.endDate=dates.end;
	 }
	 
	 function handleQuery(){
		 state.dataloading=true;
	 	 groupApi.selectTaskInfoList(state.queryParams).then(res=>{
			 state.dataloading=false;
			 if(res.data){
				 state.tableData=res.data;
			 }else{
				 state.tableData=[];
			 }
	 	 })
	 }
	 function getToday(){
	   const end = new Date()
	   const start = new Date()
	   return [start, end];
	 }
	 function handleClick(val){
		 state.queryParams.marketplaceid=val;
		 handleQuery();
	 }
	function show(data){
		state.visible=true;
		limitApi.reportTypeList().then((res)=>{
			state.reportTypeList=res.data;
			datepickersRef.value.setValue(getToday());
		});
		if(data && data.countrysList && data.countrysList.length>0){
			state.groupData=data;
			state.activeName=data.countrysList[0].marketplaceid;
			state.queryParams.sellerid=data.sellerid;
			state.queryParams.marketplaceid=data.countrysList[0].marketplaceid;
			handleQuery();
		}
	}
	function submitForm(){
		if(!state.groupData.authid){
			ElMessage.error("店铺不能为空");
			return;
		}
		if(!state.reportType){
			ElMessage.error("报表类型不能为空");
			return;
		}
		if(!state.fromDate || !state.endDate){
			ElMessage.error("开始结束日期不能为空");
			return;
		}
		authApi.requestReportParamAuth({"authid":state.groupData.authid,
		                             "type":state.reportType,
		                             "start":state.fromDate,
									 "end":state.endDate,
									 "ignore":"true"}).then((res)=>{
			    ElMessage.success("申请成功");
				handleQuery();
		});
	}
	 defineExpose({ show })
	 
</script>

<style scoped >
	 
</style>