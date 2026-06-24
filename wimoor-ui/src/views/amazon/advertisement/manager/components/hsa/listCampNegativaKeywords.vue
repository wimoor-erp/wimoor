<template>
	<el-row>
		<el-space>
			<el-select v-model="adstate" placeholder="广告状态" @change="handleQuery"  >
				<el-option v-for="item in adStatesOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
			 <Datepicker ref="datepickersRef" :days="1" class="date-picker-width"   @changedate="changedate" />
			   <el-radio-group class="el-radio-group-button" v-model="times" @change="changeTimes(datepickersRef,times)">
			      <el-radio-button label="昨天" />
			      <el-radio-button label="近7天" />
			      <el-radio-button label="近30天" />
			    </el-radio-group>
			<el-input v-model="queryParams.search" @input="handleQuery" placeholder="搜索否定关键词">
					 <template #suffix>
					  <el-icon @click.stop="handleQuery"><Search /></el-icon>
					 </template>
			</el-input>	
			<el-button @click="clearSearch">重置</el-button>
			<DataFilter :activeName="activeName"  @change="changeDataFilter"/>
		</el-space>
	</el-row>
	<el-row class="flex-center-between">
		<el-space>
		<el-button    type="primary"  class="im-but-one" @click="handleAdd()">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>添加否定关键词</span>
		</el-button>
		</el-space>
	</el-row>
 <div class="summary-top expend-table"  >
	 <GlobalTable  ref="globalTable" :tableData="tableData"
	   @loadTable="loadTableData" :stripe="false" 
	   :defaultSort="defaultSort"
	   :lazy="false"
	   >
		  <template #field>
		  <el-table-column   label="否定关键词" width="200 " sortable="custom"   prop="keywordText">
		  	   <template #default="scope">
		  	  <div >{{scope.row.keywordText}} </div>
		  	  <div><span class="font-extraSmall"> {{scope.row.groupname}}-{{scope.row.market}}</span></div>
		  		 </template>
		  </el-table-column>
		  <el-table-column    label="状态"  >
		  			   <template #default="scope">
		  				   <div >
		  				    <el-switch   v-if="scope.row.status?.toUpperCase()=='ARCHIVED'" disabled
		  				       v-model="scope.row.checkstatus"
		  					    inline-prompt
		  						width="45px"
		  					   active-text="开启"
		  					   inactive-text="归档"
		  				       size="small"
		  				     />
		  					 <el-switch :loading="scope.row.statusloading" v-else-if="scope.row.status?.toUpperCase()=='ENABLED' || scope.row.status?.toUpperCase()=='PAUSED'"  
		  					    v-model="scope.row.checkstatus"
		  						 inline-prompt
		  						 width="45px"
		  						active-text="开启"
		  						inactive-text="暂停"
		  					    size="small"
		  						 @change="handleSubmitUpdate(state,'status',scope.row)"
		  					  />
		  					  <span v-else></span>
		  					  </div>
		  					   <el-button link  size="small" v-if="scope.row.servingStatus&&scope.row.servingStatus=='loading'" :loading="true"></el-button>
		  					   <div v-else>
		  						    <span  style="font-size:10px;" v-if="scope.row.servingStatus&&scope.row?.servingStatus?.show" :class="'text-'+scope.row?.servingStatus?.color" :title="scope.row?.servingStatus?.title">{{scope.row?.servingStatus?.name}}</span>
		  					   </div>
		  					   
		  			   </template>
		  	</el-table-column>
			<el-table-column    label="广告活动"    >
				   <template #default="scope">
						   <el-link   :underline="false" type="primary">
						   <span @click.stop="bindAdvCams(scope.row.campaignId,scope.row.campaignName)" v-if="scope.row.campaignName">
							   {{scope.row.campaignName}}
						   </span>
						   </el-link>
						  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
				   </template>
			 </el-table-column>
			 <el-table-column     label="广告组"   >
			 	   <template #default="scope">
			 				   <el-link   :underline="false" type="primary">
			 				   <span v-if="scope.row.adGroupName"  
			 							   @click.stop="bindAdvGpds(scope.row.adGroupId,scope.row.adGroupName,scope.row.campaignId,scope.row.campaignName)">
			 					   {{scope.row.adGroupName}}
			 				   </span>
			 				   </el-link>
								<copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
			 	   </template>
			 				   
			  </el-table-column>
			
			<el-table-column     label="匹配类型"  width="200" >
				   <template #default="scope">
						<span v-if="scope.row?.matchType?.toUpperCase()=='NEGATIVE_BROAD' || scope.row.matchType=='negativeBroad'">广泛匹配</span>
						<span v-else-if="scope.row?.matchType?.toUpperCase()=='NEGATIVE_EXACT' || scope.row.matchType=='negativeExact'">精确匹配</span>
						<span v-else-if="scope.row?.matchType?.toUpperCase()=='NEGATIVE_PHRASE' || scope.row.matchType=='negativePhrase'">词组匹配</span>
				   </template>
			 </el-table-column>
			 <el-table-column     label="备注" prop="remark"  />
	 </template>
	 </GlobalTable>
 </div>
 
	<AdChart ref="adchartRef" :summary="summary" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Menu,Edit,Histogram,WarningFilled,Close} from '@element-plus/icons-vue';
	import {Copy,AddOne,ReduceOne} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
	import {loadList,handleSubmitUpdate} from '../javascript/campNeKeyword.js';
	import {changeTimes} from '../javascript/common.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import {adgroupsuggestFormatter,} from '../javascript/formatter.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const globalTable =ref()
	const datepickersRef=ref();
	const adchartRef=ref();
	let props = defineProps({ activeName:"" })
	const { activeName } = toRefs(props);
	const state = reactive({
		tableData:{records:[
		],total:0},
		summary:{},
		times:'近7天',
		queryParams:{},
		emits:null,
		defaultSort:{ prop: 'keywordText', order: 'ascending' },
		bindParams:{},
		adStatesOptions:[
				{name:'全部广告状态',value:'all',},
				{name:'已启动',value:'enabled',},
				{name:'已暂停',value:'paused',},
				{name:'已归档',value:'archived',},
				// {name:'运行中',value:'runing',},
				// {name:'停止中',value:'stoping',},
			],
		adstate:'all',
	})
	const{
		tableData,
		batch,
		times,
		summary,
		queryParams,
		defaultSort,
		bindParams,
		adStatesOptions,
		adstate,
	}=toRefs(state)
	const emits = defineEmits(['selectRow','change','bindData']);
	state.emits=emits;
	
	 
	function changedate(dates,val,type){
			state.queryParams.fromDate=dates.start;
			state.queryParams.endDate=dates.end;
			if(type!="load"){
				handleQuery();
			}
	}
	 
	function bindAdvCams(id,name){
		state.bindParams.campaignid=id;
		state.bindParams.camname=name;
		state.bindParams.adgroupid=null;
		state.bindParams.adgroupname=null;
		emits("bindData",state.bindParams);
	}
	function bindAdvGpds(adid,adname,camid,camname){
		state.bindParams.campaignid=camid;
		state.bindParams.camname=camname;
		state.bindParams.adgroupid=adid;
		state.bindParams.adgroupname=adname;
		emits("bindData",state.bindParams);
	}
	 
	function loadTableData(params){
		if(params&&params.profileid){
			state.queryParams=params;
			loadList(state);
		}
	}
	function handleQuery(){
		if(state.queryParams.profileid){
			if(state.adstate=='all'){
				state.queryParams.state=null;
			}else{
				state.queryParams.state=state.adstate;
			}
		    state.queryParams.name=state.queryParams.search;
			state.queryParams.matchType=null;
			 globalTable.value.loadTable(state.queryParams);
		}
	}
	 function show(params){
		 state.queryParams=Object.assign(state.queryParams, params);
		 state.activeName=props.activeName;
		var timer=setTimeout(function(){
					 handleQuery();
					 clearTimeout(timer);
		},100);
	 }
	 function clearSearch(){
	 	state.adstate='all';
	 	state.adType='all';
	 	state.queryParams.search='';
	 	state.bindParams={};
	 	state.queryParams.changeRate='';
	 	state.queryParams.categoryid='';
	 	state.queryParams.skuStr=null;
	 	handleQuery();
	 } 
	 function handleAdd(){
	 	router.push({
	 		path:"/a/a/sb",
	 		query:{
	 			title:'添加活动-否定关键词',
	 			path:"/a/a/sb",
	 			type:state.activeName,
	 			campaignid:state.queryParams.campaignid,
	 			adGroupid:state.queryParams.adGroupid
	 		},
	 	})
	 }
	defineExpose({
		handleQuery,show,
	})
</script>

<style>
.popover-footer{
	margin-top:16px;
}
 .summary-top .el-table__body-wrapper {
  order: 1;
}
</style>