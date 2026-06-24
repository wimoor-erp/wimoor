<template>
	<el-row>
		<el-space>
			<el-select v-model="adstate" placeholder="广告状态" @change="handleQuery"  >
				<el-option v-for="item in adStatesOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
			<el-select v-model="admatch" placeholder="匹配类型" @change="handleQuery"  >
				<el-option v-for="item in admatchOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
			 <Datepicker ref="datepickersRef" :days="1" class="date-picker-width"   @changedate="changedate" />
			   <el-radio-group class="el-radio-group-button" v-model="times" @change="changeTimes(datepickersRef,times)">
			      <el-radio-button label="昨天" />
			      <el-radio-button label="近7天" />
			      <el-radio-button label="近30天" />
			    </el-radio-group>
			<el-input v-model="queryParams.search" @input="handleQuery" placeholder="搜索客户关键词">
					 <template #suffix>
					  <el-icon @click.stop="handleQuery"><Search /></el-icon>
					 </template>
			</el-input>	
			<el-button @click="clearSearch">重置</el-button>
			<DataFilter :activeName="activeName"  @change="changeDataFilter"/>
		</el-space>
	</el-row>
	<el-row class="flex-center-between" style="float:right">
		<el-space>
		</el-space>
		<div class='icon-group'>
			<el-space>
			  <el-button   @click="showAdChart(adchartRef,state)" class='ic-btn'  title='展示图表'>
			   <el-icon><Histogram /></el-icon>
			  </el-button>
			  <el-tooltip content="自定义列"   placement="top">
			  	<el-button   @click="showColumnSet(columnSetRef,'advsearchterms')" class='ic-btn'>
			  <el-icon><Tools /></el-icon>
			  </el-button>
			  </el-tooltip>
			</el-space>
		</div>
	</el-row>
	<el-row class="flex-center-between">
		<el-space>
		 
		<el-button   :disabled="!queryParams.adGroupid" title="请先锁定广告组再添加"  type="primary"  class="im-but-one" @click="handleAddKeyword()">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>添加关键词</span>
		</el-button>
		 <el-button   :disabled="!queryParams.adGroupid" title="请先锁定广告组再添加"  class="im-but-one" @click="handleAddNeKeyword()">
		   <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		   <span>添加否定关键词</span>
		 </el-button>
		 <el-button     class="im-but-one" @click="copySelectRows">
				复制到粘贴板 
		 </el-button>
		</el-space>
		 
	</el-row>
	
	
	
 <div class="summary-top expend-table"  >
	 <GlobalTable  ref="globalTable" :tableData="tableData"
	   @loadTable="loadTableData" :stripe="false"
		  @selectionChange = "selectChange"
	   :defaultSort="defaultSort"
	   :lazy="false"
	   fieldType="advsearchterms"
	   >
		  <template v-slot:field='columns'>
		  <el-table-column  fixed type="selection" width="60" />
		 <el-table-column fixed    label="操作" prop="query"  width="120"  >
			   <template #default="scope">
				   
			  <add-one v-if="scope.row.keywordFlag" class="text-gray"  theme="outline" size="24" />
			   <add-one v-else class="text-green" @click.stop="addQuery(scope.row)"  theme="outline" size="24" />
			   <reduce-one v-if="scope.row.keywordNegativaFlag"  class="text-gray" theme="outline" size="24"   style="margin-left: 10px;" />
				<reduce-one v-else class="text-danger" @click.stop="delQuery(scope.row)" theme="outline" size="24"   style="margin-left: 10px;" />
			   </template>
		  </el-table-column>
		  <el-table-column fixed    label="Search Terms" prop="query"  width="200"  >
			  <template #default="scope">
				  <div>{{scope.row.query}}</div>
			   <copy class="" @click.stop="CopyText(scope.row.query)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
			</template>
			</el-table-column>
			
			<template v-if="columns.list" v-for="column in columns.list">
				<el-table-column   :label="column.label" :width="column.width " :sortable="column.sortable?'custom':false" :prop="column.prop">
					<template #default="scope">
						   <div v-if="column.prop=='campaignName'">
							 <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
							   <span  ></span>
							 </span>
							   <el-link v-else :underline="false" type="primary">
							   <span @click.stop="bindAdvCams(scope.row.campaignId,scope.row.campaignName)" v-if="scope.row.campaignName">
								   {{scope.row.campaignName}}
							   </span>
							   <span v-else>
								   <span >{{scope.row.name}}</span>
							   </span>
							   </el-link>
							  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
						   </div>
							<div v-else-if="column.prop=='adGroupName'">
								<span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
										  <span  ></span>
									   </span>
								   <el-link v-else :underline="false" type="primary">
								   <span v-if="scope.row.adGroupName"  
											   @click.stop="bindAdvGpds(scope.row.adGroupId,scope.row.adGroupName,scope.row.campaignId,scope.row.campaignName)">
									   {{scope.row.adGroupName}}
								   </span>
								   <span v-else>
										 <span >{{scope.row.name}}</span>
								   </span>
								   </el-link>
									<copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>							
							</div>
							<div v-else-if="column.prop=='keywordText'">
							  <div >{{scope.row.keywordText}} </div>
							  <div><span class="font-extraSmall"> {{scope.row.groupname}}-{{scope.row.market}}</span></div>
							</div>
							<div v-else-if="column.prop=='matchType'">
							  <span v-if="scope.row.matchType=='broad'">广泛匹配</span>
							  <span v-else-if="scope.row.matchType=='phrase'">词组匹配</span>
							  <span v-else-if="scope.row.matchType=='exact'">精确匹配</span>
							  <span v-else-if="scope.row.matchType=='negativeExact'">精确匹配</span>
							  <span v-else-if="scope.row.matchType=='negativePhrase'">词组匹配</span>
							</div>
							<div v-else>
								  {{scope.row[column.prop]}}
							</div>
					  </template>
				</el-table-column>
			</template>
			
			<!--<el-table-column    label="广告活动"  width="200" >
				   <template #default="scope">
					   <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
					     <span  ></span>
					   </span>
						   <el-link v-else :underline="false" type="primary">
						   <span @click.stop="bindAdvCams(scope.row.campaignId,scope.row.campaignName)" v-if="scope.row.campaignName">
							   {{scope.row.campaignName}}
						   </span>
						   <span v-else>
							   <span >{{scope.row.name}}</span>
						   </span>
						   </el-link>
						  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
				   </template>
			 </el-table-column>
			 <el-table-column     label="广告组"  width="200" >
			 	   <template #default="scope">
			 		   <span v-if="scope.row.status=='none'&& !scope.row.statustype"  style="font-size: 12px;"  >
			 						  <span  ></span>
			 					   </span>
			 				   <el-link v-else :underline="false" type="primary">
			 				   <span v-if="scope.row.adGroupName"  
			 							   @click.stop="bindAdvGpds(scope.row.adGroupId,scope.row.adGroupName,scope.row.campaignId,scope.row.campaignName)">
			 					   {{scope.row.adGroupName}}
			 				   </span>
							   <span v-else>
							   		 <span >{{scope.row.name}}</span>
							   </span>
			 				   </el-link>
								<copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
			 	   </template>
			 				   
			  </el-table-column>
			  
			  <el-table-column   label="关键词" width="140 " sortable="custom"   prop="keywordText">
				   <template #default="scope">
				  <div >{{scope.row.keywordText}} </div>
				  <div><span class="font-extraSmall"> {{scope.row.groupname}}-{{scope.row.market}}</span></div>
					 </template>
			</el-table-column>
			
			
			 <el-table-column     label="匹配类型"  width="200" >
				   <template #default="scope">
						<span v-if="scope.row.matchType=='broad'">广泛匹配</span>
						<span v-else-if="scope.row.matchType=='phrase'">词组匹配</span>
						<span v-else-if="scope.row.matchType=='exact'">精确匹配</span>
						<span v-else-if="scope.row.matchType=='negativeExact'">精确匹配</span>
						<span v-else-if="scope.row.matchType=='negativePhrase'">词组匹配</span>
				   </template>
			 </el-table-column>
			 
			<el-table-column   label="曝光量" width="140 " sortable="custom"   prop="impressions"/>
			<el-table-column   label="点击次数" width="130 " sortable="custom"   prop="clicks"/>
			<el-table-column   label="点击率" width="130 " sortable="custom"  prop="CTR" />
			<el-table-column   label="每次点击费用" width="130" sortable="custom"   prop="avgcost"/>
			<el-table-column   label="广告费"  width="130 " sortable="custom"  prop="cost"/>
			<el-table-column   label="广告销售额" width="130 " sortable="custom"    prop="sumSales" />
			<el-table-column   label="销量" width="130 " sortable="custom"  prop="sumUnits" />
			<el-table-column   label="订单量" width="130 " sortable="custom" prop="attributedUnitsOrdered" />
			<el-table-column   label="Acos" width="140 " sortable="custom"    prop="ACOS"/>
			<el-table-column   label="RoAS" width="120 " sortable="custom"  prop="ROAS"/>
			<el-table-column   label="转化率" width="120 "  prop="CSRT"/>
			<el-table-column   label="目标商品-销售额" sortable="custom"  width="180 "  prop="attributedSalesSameSKU"/>
			<el-table-column   label="其它商品-销售额" sortable="custom"  width="180 "  prop="attributedSales"/>
			<el-table-column   label="目标商品-转化量" sortable="custom"  width="160"   prop="attributedConversionsSameSKU"/>
			<el-table-column   label="其它商品-转化量" sortable="custom"  width="160"   prop="attributedConversions"/> -->
	 </template>
	 </GlobalTable>
 </div>
 <columnSet ref="columnSetRef"  @change="getFieldData" />
	<AdChart ref="adchartRef" :summary="summary" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Menu,Edit,Histogram,WarningFilled,Close,Tools} from '@element-plus/icons-vue';
	import {Copy,AddOne,ReduceOne} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
	import columnSet from '@/components/Table/column_set.vue';
	import AdOrderCycle from '@/views/amazon/advertisement/manager/components/common/ad_order_cycle.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import filtericon from "@/components/icon/filtericon.vue";
	import Category from '@/components/header/category.vue';
	import AdChart from '@/views/amazon/advertisement/manager/components/common/ad_chart.vue';
	import DataFilter from '@/views/amazon/advertisement/manager/components/common/data_filter.vue';
	import {loadList,} from '../javascript/keywordQuery.js';
	import {showAdChart,changeTimes,showColumnSet} from '../javascript/common.js';
	import {adgroupsuggestFormatter,} from '../javascript/formatter.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const columnSetRef = ref();
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
		defaultSort:{ prop: 'query', order: 'ascending' },
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
		admatchOptions:[
				{name:'全部匹配类型',value:'all',},
				{name:'广泛匹配',value:'broad',},
				{name:'词组匹配',value:'phrase',},
				{name:'精确匹配',value:'exact',},
			],
		admatch:'all',
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
		admatchOptions,
		admatch,
	}=toRefs(state)
	const emits = defineEmits(['selectRow','change','bindData']);
	state.emits=emits;
	function addQuery(item){
		var keywordlist=[];
		 if(!item.keywordFlag){
			var param={'keywords':item.query,'matchType':item.matchType.toUpperCase()};
			 keywordlist.push(JSON.stringify(param));
		 }
		router.push({
			path:"/a/a/sp",
			query:{
				title:'添加商品关键词',
				path:"/a/a/sp",
				type:"adkey",
				campaignid:state.queryParams.campaignid,
				adGroupid:state.queryParams.adGroupid,
				querylist:keywordlist,
			},
		})
	}
	function delQuery(item){
		var keywordlist=[];
		 if(!item.keywordNegativaFlag){
			keywordlist.push({'keywords':item.query,'matchType':item.matchType.toUpperCase()});
		 }
		router.push({
			path:"/a/a/sp",
			query:{
				title:'添加否定关键词',
				path:"/a/a/sp",
				type:"adnkey",
				campaignid:state.queryParams.campaignid,
				adGroupid:state.queryParams.adGroupid,
				querylist:keywordlist,
			},
		})
	}
	function showOrderby(row){
		//展示图表
	}
	function changedate(dates,val,type){
			state.queryParams.fromDate=dates.start;
			state.queryParams.endDate=dates.end;
			if(type!="load"){
				handleQuery();
			}
	}
	function changeDataFilter(paralist){
		if(paralist!="" && paralist!=undefined && paralist!=null){
			state.queryParams.paralist=paralist;
			handleQuery();
		}else{
			state.queryParams.paralist=null;
			handleQuery();
		}
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
		state.bidVisible=false;
		if(state.queryParams.profileid){
			if(state.adstate=='all'){
				state.queryParams.state=null;
			}else{
				state.queryParams.state=state.adstate;
			}
			state.queryParams.targetingType=state.adType;
		    state.queryParams.name=state.queryParams.search;
			state.queryParams.matchType=state.admatch;
			 globalTable.value.loadTable(state.queryParams);
		}
	}
	function getFieldData(fdata){
		loadField(fdata);
	}
	function loadField(id){
		globalTable.value.refreshField();
	}
	 function show(params){
		 state.queryParams=Object.assign(state.queryParams, params);
		 state.activeName=props.activeName;
		var timer=setTimeout(function(){
					 handleQuery();
					 loadField();
					 clearTimeout(timer);
		},100);
	 }
	 function selectChange(seletion){
	 	state.keywords=seletion;
	 }
	 function handleAddKeyword(){
		 if(state.keywords&&state.keywords.length>0){
			 var keywordlist=[];
			 state.keywords.forEach(item=>{
				 if(!item.keywordFlag){
				 	var param={'keywords':item.query,'matchType':item.matchType.toUpperCase()};
				 	 keywordlist.push(JSON.stringify(param));
				 }
			 })
			 router.push({
			 	path:"/a/a/sp",
			 	query:{
			 		title:'添加商品关键词',
			 		path:"/a/a/sp",
			 		type:"adkey",
			 		campaignid:state.queryParams.campaignid,
			 		adGroupid:state.queryParams.adGroupid,
			 		querylist:keywordlist,
			 	},
			 })
		 }else{
			 //请选择一行后操作
			 ElMessage.info("请选择一行后操作");
		 }
	
	 }
	 function handleAddNeKeyword(){
	 		 if(state.keywords&&state.keywords.length>0){
	 			 var keywordlist=[];
	 			 state.keywords.forEach(item=>{
					 if(!item.keywordNegativaFlag){
						keywordlist.push({'keywords':item.query,'matchType':item.matchType.toUpperCase()});
					 }
	 			 })
	 			 router.push({
	 			 	path:"/a/a/sp",
	 			 	query:{
	 			 		title:'添加否定关键词',
	 			 		path:"/a/a/sp",
	 			 		type:"adnkey",
	 			 		campaignid:state.queryParams.campaignid,
	 			 		adGroupid:state.queryParams.adGroupid,
	 			 		querylist:keywordlist,
	 			 	},
	 			 })
	 		 }else{
	 			 //请选择一行后操作
				 ElMessage.info("请选择一行后操作");
	 		 }
	 	
	 }
	 
	 function copySelectRows(){
	 		 var names="";
	 		 var rows=globalTable.value.getSelectionRows();
	 		 if(rows && rows.length>0){
	 			 rows.forEach(item=>{
	 				names=names+item.query+"\n";
	 			 });
	 		 }
	 		 CopyText(names);
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