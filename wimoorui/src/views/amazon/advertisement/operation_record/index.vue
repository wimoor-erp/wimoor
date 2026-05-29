<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
	          <AdGroup :border="true"  @change="changeGroup" :isAll="true"/>
			  <Datepicker ref="datepickers" @changedate="changedate" />
			  <el-input  v-model="queryParams.search" clearable @input="handleQuery" placeholder="搜索广告活动/广告组/商品" class="input-with-select" > </el-input>
			</el-space >
		  </el-row>
		</div>
		<el-row>
		<GlobalTable  ref="globalTable" 
		              :tableData="tableData"  
					  :defaultSort="{ prop: 'opttime', order: 'descending' }" 
		              height="calc(100vh - 246px)" 
					  @loadTable="loadTableData" 
					  :stripe="true"  
					  style="width: 100%;margin-bottom:16px;">
			 <template #field>
			  <el-table-column prop="opttime"  label="操作时间" width="100"  >
				  <template #default="scope">
					  {{dateTimesFormat(scope.row.opttime)}}
				  </template>
				  </el-table-column>
			  <el-table-column prop="campaignName" label="广告活动(店铺/站点)"    show-overflow-tooltip>
				<template #default="scope">
					<div>{{scope.row.campaignName}}</div>
					<div class="font-extraSmall">{{scope.row.groupname}}-{{scope.row.maketname}}</div>
				</template>  
			  </el-table-column>
			   <el-table-column prop="adGroupName" label="广告组"   show-overflow-tooltip>
			      <template #default="scope">
			            <div>{{scope.row.adGroupName}}</div>
				   </template>
				</el-table-column>
		      <el-table-column prop="beanclasz" label="日志记录" width="260" >
		       <template #default="scope">
		          <div>
					  <span @click.stop='showOperateDetailModel(scope.row)' class='text-blue pointer'> 
					      {{scope.row.opt}}
					  </span>
				  </div>
		      </template>
		    </el-table-column>
		    <el-table-column prop="username" label="操作人员" width='180'>
		    </el-table-column>
		    <el-table-column prop="remark" label="备注" width="120"   >
				<template #default="scope">
				   <span class="table-edit-flex" >
				   	<span class="font-small" v-html="scope.row.htmlremark"></span>
				   	<el-icon @click.stop="editRemarks(scope.row)"><Edit/></el-icon>
				   </span>
				</template>
			</el-table-column>	
		  </template>
		 </GlobalTable>
		</el-row>
	</div>
	<detailDialog ref="detailDialogRef"></detailDialog>
	<RemarksDialog ref="remarksRef" @confirm="remarkConfirm"/>
</template>
<script>
    export default{ name:"广告操作记录" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	import AdGroup from '@/components/header/ad_group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import operationApi from '@/api/amazon/advertisement/operation/operationApi.js';
	import detailDialog from './components/detail.vue';
	import RemarksDialog from "./components/remarks_dialog.vue"
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {decodeText} from '@/utils/index.js';
	import {Edit} from '@element-plus/icons-vue';
	const globalTable=ref();
	const detailDialogRef=ref();
	const remarksRef=ref();
	const state=reactive({  queryParams:{fromDate:"",endDate:"",groupid:"",profileid:"",search:""},
							tableData:{records:[],total:0}
	                      });
	const{
		queryParams,tableData
		}=toRefs(state);
	function changeGroup(data){
			state.queryParams.groupid=data.groupid;
			state.queryParams.profileid=data.profileid;
			state.queryParams.marketplaceid=data.marketplaceid;
			state.queryParams.profile=data.profile;
			handleQuery();
	}
	//日期改变
	function changedate(dates,value,opt){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(opt!="load"){
			handleQuery();
		}
	}
	function showOperateDetailModel(row){
		detailDialogRef.value.show(row);
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParams);
	}
	/* 编辑公告 */
	function editRemarks(row){
		   remarksRef.value.show(row);
	}
	function remarkConfirm(item){
		 if(item.remark){
		 	 item.htmlremark=decodeText(item.remark);
		  }else{
		 	 item.htmlremark="--"
		  }
	}
	function hisTypeFormatter(value, row, index) {
		var html = "";
		var type = "";
		if (row.beforeobject) {
			type = "更改";
		} else {
			type = "新增";
		}
		if ("AmzAdvCampaigns" == value||"AmzAdvCampaignsSD" == value||"AmzAdvCampaignsHsa" == value) {
			html = type + "广告活动";
		} else if ("AmzAdvAdgroups" == value||"AmzAdvAdgroupsSD" == value||"AmzAdvAdgroupsHsa" == value) {
			html = type + "广告组";
		} else if ("AmzAdvProductads" == value||"AmzAdvProductadsSD" == value) {
			html = type + "商品广告";
		} else if ("AmzAdvAds" == value) {
			html = type + "广告";
		} else if ("AmzAdvKeywords" == value||"AmzAdvKeywordsHsa" == value) {
			html = type + "关键词";
		} else if ("AmzAdvKeywordsNegativa" == value||"AmzAdvKeywordsNegativaHsa" == value) {
			html = type + "否定关键词";
		} else if ("AmzAdvProductTarge" == value||"AmzAdvProductTargeSD" == value||"AmzAdvProductTargeHsa" == value) {
			html = type + "商品投放";
		} else if ("AmzAdvProductTargeNegativa" == value||"AmzAdvProductTargeNegativaSD" == value||"AmzAdvProductTargeNegativaHsa" == value) {
			html = type + "否定商品投放";
		}
		return html;
	}
	
	
	
	function loadTableData(param){
		operationApi.getOperateLogList(param).then(res=>{
			if(res.data.records&&res.data.records.length>0){
				res.data.records.forEach((item,index)=>{
					if(item.remark){
						 item.htmlremark=decodeText(item.remark);
					 }else{
						 item.htmlremark="--"
					 }
					item.opt=hisTypeFormatter(item.beanclasz,item,index);
				})
			}
			state.tableData.records =  res.data.records;
			state.tableData.total = res.data.total;
			
		})
	}
</script>

<style>
</style>