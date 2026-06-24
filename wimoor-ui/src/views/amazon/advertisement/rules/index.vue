<template>
	<div class="main-sty">
		<div class="con-header" >
			<el-tabs  v-model="activeName" class="demo-tabs">
			    <el-tab-pane label="预算规则" name="budget"></el-tab-pane>
			    <el-tab-pane label="竞价规则" name="bidding"></el-tab-pane>
			  </el-tabs>
		  <el-row>
		    <el-space >
	          <AdGroup :border="true"  @change="changeGroup"  />
			 <el-radio-group v-model="adverttype" @change="handleQuery"  >
			        <el-radio-button label="sp" >商品推广</el-radio-button>
			        <el-radio-button label="sb" >品牌推广</el-radio-button>
			        <el-radio-button label="sd" >展示推广</el-radio-button>
			   </el-radio-group>
			</el-space >
				
		  </el-row>
			<el-row>	<div>
			<el-button type="primary" @click="handeAdd">新建</el-button>
			<el-space>
				<el-input style="margin-left:15px;"  v-model="queryParams.search" @input="handleQuery" placeholder="请输入规则名称"></el-input>
			</el-space>
		</div></el-row>
		</div>
	
		<el-row>
	
		<GlobalTable  ref="globalTable" :tableData="tableData"   height="calc(100vh - 300px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			 <template #field>
			  <el-table-column prop="created_date"  label="创建时间" width="100"  >
				  <template #default="scope">
					  {{dateTimesFormat(scope.row.created_date)}}
				  </template>
			  </el-table-column>
			  <el-table-column prop="name" label="名称"    show-overflow-tooltip>
			  </el-table-column>
			  <el-table-column prop="rule_state" label="状态"   ></el-table-column>
			 <el-table-column prop="budget_increase_by" label="预算增加依据"   >
				 <template #default="scope">
				 	 {{scope.row.budget_increase_by}}
				 </template>
			 </el-table-column>	
			 <el-table-column prop="duration" label="区间"   >
				 <template #default="scope">
					 {{scope.row.duration}}
				 </template>
			 </el-table-column>	
			 <el-table-column prop="recurrence" label="执行频率"   >
					 <template #default="scope">
						 {{scope.row.recurrence}}
					 </template>
			 </el-table-column>	
			 <el-table-column prop="performance_measure_condition" label="指标"   >
							 <template #default="scope">
								 {{scope.row.performance_measure_condition}}
							 </template>
			 </el-table-column>	
		  </template>
		 </GlobalTable>
		</el-row>
	</div>
	 <SchedulePlan  ref="scheduleRef" />
</template>
 
<script>
    export default{ name:"广告规则" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	import AdGroup from '@/components/header/ad_group.vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {decodeText} from '@/utils/index.js';
	import {Edit} from '@element-plus/icons-vue';
	import SchedulePlan from '@/views/amazon/advertisement/manager/components/common/schedule_plan.vue';
	import advBudgetRuleApi from '@/api/amazon/advertisement/report/advBudgetRuleApi.js';
	const globalTable=ref();
	const scheduleRef=ref();
	const detailDialogRef=ref();
	const remarksRef=ref();
	const state=reactive({  queryParams:{fromDate:"",endDate:"",groupid:"",profileid:"",search:""},
							tableData:{records:[],total:0},
							adverttype:"sp",
							activeName:"budget",
	                      });
	const{
		queryParams,tableData,adverttype,activeName
		}=toRefs(state);
	function changeGroup(data){
			state.queryParams.groupid=data.groupid;
			state.queryParams.profileid=data.profileid;
			state.queryParams.marketplaceid=data.marketplaceid;
			state.queryParams.profile=data.profile;
			handleQuery();
	}
	function handleQuery(){
		state.queryParams.ftype='add';
		globalTable.value.loadTable(state.queryParams);
	}
	function handeAdd(){
		scheduleRef.value.show(state.queryParams);
	}
	function loadTableData(param){
		advBudgetRuleApi.list(state.queryParams.profileid,state.adverttype,param).then((res)=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
		});
	}
</script>

<style>
</style>