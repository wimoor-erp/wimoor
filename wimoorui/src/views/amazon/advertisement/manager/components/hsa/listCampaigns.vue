<template>
	<el-row>
		<el-space>
			<el-select v-model="adstate" placeholder="广告状态" @change="handleQuery"  >
				<el-option v-for="item in adStatesOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
			<el-select v-model="adType" placeholder="投放类型" @change="handleQuery"  >
				<el-option v-for="item in adTypeOptions" :label="item.name" :value="item.value"></el-option>
			</el-select>
            <DateSelect @setDate="setDate" @dateTypeSwitch="handleQuery"/>
			<el-input v-model="queryParams.search" clearable @input="handleQuery" placeholder="搜索广告活动名称">
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
		<el-button  type="primary"  class="im-but-one" @click="handleAdd()">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>新建广告活动</span>
		</el-button>
		<el-dropdown trigger="click">
		  <el-button :disabled="disabled">
		             批量操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
		  </el-button>
		  <template #dropdown>
		    <el-dropdown-menu >
		      <el-dropdown-item  @click="handleValue('budget','调整预算')">调整预算</el-dropdown-item>
			  <el-dropdown-item  @click="handleUpdate(globalTable,state,'status','ENABLED')">启用</el-dropdown-item>
			  <el-dropdown-item  @click="handleUpdate(globalTable,state,'status','PAUSED')">暂停</el-dropdown-item>
			  <el-dropdown-item  @click="handleUpdate(globalTable,state,'status','ARCHIVED')">归档</el-dropdown-item>
		      <el-dropdown-item  @click="handleValue('enddate','调整结束日期')">调整结束日期</el-dropdown-item>
		      <el-dropdown-item  >移入广告组合</el-dropdown-item>
		      <el-dropdown-item  >移出广告组合</el-dropdown-item>
			  <el-dropdown-item  >备注</el-dropdown-item>
			  <el-dropdown-item  >策略</el-dropdown-item>
		    </el-dropdown-menu>
		  </template>
		</el-dropdown>
		<el-space v-show="batch.input">
			<el-button @click="batch.input=false">取消</el-button>
			<el-text class="font-base">{{batch.text}}</el-text>
			<el-input type="number" v-if="batch.type=='budget'" v-model="batch.value"></el-input>
			<el-select v-if="batch.type=='portfolioid'" v-model="batch.value">
				
			</el-select>
			<el-date-picker
			   v-if="batch.type=='enddate'"
				v-model="batch.value"
			    type="date"
			    placeholder="选择日期"
			  />
			<el-button type="primary" @click.stop="handleUpdate(globalTable,state,batch.type,batch.value)">确认</el-button>
		</el-space>
		</el-space>
		<div class='icon-group'>
			<el-space>
			<el-dropdown >
			    <el-button class='ic-btn'  title='其他'>
			      <el-icon><Menu /></el-icon>
			    </el-button>
			    <template #dropdown>
			      <el-dropdown-menu>
			        <el-dropdown-item  @click="handleExpendAdContent('placement')">位置</el-dropdown-item>
			      </el-dropdown-menu>
			    </template>
			  </el-dropdown>
			  <el-button   @click="showAdChart(adchartRef,state)" class='ic-btn'  title='展示图表'>
			   <el-icon><Histogram /></el-icon>
			  </el-button>
			</el-space>
		</div>
	</el-row>
 <div class="summary-top expend-table" v-loading="selectionloding">
	 <GlobalTable  ref="globalTable" :tableData="tableData"
	   @loadTable="loadTableData" :stripe="false" 
	   @selectionChange = "selectChange"
	   :defaultSort="defaultSort"
	   show-summary
	   :lazy="false"
	   :load="handleExpend"
	   :treeProps="{ children: 'children', hasChildren: 'hasChildren' }"
	   :summary-method="getSummaries"
	    rowKey="id"
	   :defaultExpandAll="true"
	   >
		  <template #field>
		 <el-table-column  fixed type="selection" width="60" />
		 <el-table-column fixed   label="广告活动"  width="200" >
		 			   <template #default="scope">
						   <div v-loading="scope.row.nameloading">
		 				   <span v-if="scope.row.status=='none'"  style="font-size: 12px;"  >{{scope.row.name}}</span>
						   <el-link     @click.stop="bindAdvCams(scope.row.id,scope.row.name)" v-else :underline="false" type="primary">
						   {{scope.row.name}}
						   <span class="table-edit-flex" @click.stop="e=>nameEdit(e,scope.row)"><el-icon  ><Edit/></el-icon></span>
						   </el-link>
							</div>
					   </template>
		  </el-table-column>
		   <el-table-column   label="状态"  >
			   <template #default="scope">
				  
				   <div >
				    <el-switch v-if="scope.row.status?.toUpperCase()=='ARCHIVED'" disabled
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
			<el-table-column   label="投资组合" width="120 "  prop="portfolioid">
				<template #default="scope">
				   		 <span v-if="scope.row.status!='none'">{{getValue(scope.row.portfoliosName)}}</span>
				  </template>
			</el-table-column>
			<el-table-column   label="策略"  width="200 "  prop="status"  show-overflow-tooltip >
			<template #default="scope">
				   <div v-if="scope.row.status!='none'" class="table-edit-flex" @click="scheduleSet(scope.row)">
					   <el-icon ><Edit/></el-icon>
					   <el-tag   v-if="scope.row.rulename && scope.row.rulename.length>0"   v-for="item in scope.row.rulename"       type="primary">
					  {{item}}
					   </el-tag>
				   </div>
				  </template>
			</el-table-column>
			<el-table-column   label="投放"  width="60" >
			 			   <template #default="scope">
							   <span v-if="scope.row.status!='none'">
								   <span v-if="scope.row.targetingType=='auto' || scope.row.targetingType=='AUTO'">自动</span>
								   <span v-else>手动</span>
							   </span>
			 			   </template>
			  </el-table-column>
			<el-table-column   label="开始日期"  width="120 "  >
				<template #default="scope">
							<span v-if="scope.row.status!='none'" >{{dateFormat(scope.row.startDate)}} </span>
				</template>
			</el-table-column>
			<el-table-column   label="结束日期" width="120 "  >
				<template #default="scope">
					<div v-loading="scope.row.enddateloading" >
					   <span v-if="scope.row.status!='none'" class="table-edit-flex" @click="e=>pickDate(e,scope.row)">
							 <span>{{dateFormat(scope.row.endDate)}}</span>
							<el-icon ><Edit/></el-icon>
					   </span>
				   </div>
				  </template>
			</el-table-column>
			<el-table-column    label="预算"  width="80" prop="budget">
			<template #default="scope">
					<div v-loading="scope.row.budgetloading" >
				  <el-link v-if="scope.row.status!='none'" type="primary" @click="e=>budgetEdit(e,scope.row)" :underline="false">
					  ${{scope.row.budget}}
				  </el-link>
				  </div>
				  </template>
			</el-table-column>
			<el-table-column   label="竞价策略" width="190 "  prop="bidding">
				<template #default="scope">
					<span v-if="scope.row.status!='none'" class="table-edit-flex" @click="biddingEdit(state,biddingRef,scope.row)">
						 <span class="font-extraSmall" v-html="bidplusFormatter(scope.row)"></span>
						 <el-icon ><Edit/></el-icon>
					</span>
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
			<el-table-column   label="其它商品-转化量" sortable="custom"  width="160"   prop="attributedConversions"/>
 
	 </template>
	 </GlobalTable>
 </div>
 <SchedulePlan  ref="scheduleRef" />
 <BiddingDialog ref="biddingRef" @change="loadList(state)"/>
<!-- 结束日期选择 -->
   <el-popover
    :visible="visible"
    :virtual-ref="endtimeRef"
    trigger="click"
    virtual-triggering
	width="246px"
   >
    <el-date-picker
        v-model="editRow.endDate"
        type="date"
        placeholder="选择日期"
      />
	  <div class="popover-footer ">
		  <el-button @click="visible=false">关闭</el-button>
		  <el-button type="primary" @click="handleSubmitUpdate(state,'enddate',editRow)">确认</el-button>
	  </div>
</el-popover>
<!-- 预算设置 -->
   <el-popover
    :visible="budgetVisible"
    :virtual-ref="budgetRefRef"
    trigger="click"
    virtual-triggering
	width="246px"
   >
   <el-input v-model="editRow.budget" type="number">
	   <template #suffix>$</template>
   </el-input>
	  <div class="popover-footer">
		  <el-button @click="budgetVisible=false">关闭</el-button>
		  <el-button type="primary" @click="handleSubmitUpdate(state,'budget',editRow)">确认</el-button>
	  </div>
</el-popover>
<el-popover
    :visible="nameVisible"
    :virtual-ref="nameRefRef"
    trigger="click"
    virtual-triggering
	width="246px"
   >
   <el-input v-model="editRow.name" type="textarea">
   </el-input>
	  <div class="popover-footer">
		  <el-button @click="nameVisible=false">关闭</el-button>
		  <el-button type="primary" @click="handleSubmitUpdate(state,'name',editRow)">确认</el-button>
	  </div>
</el-popover>
	<AdChart ref="adchartRef" :summary="summary" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Menu,Edit,Histogram,WarningFilled,Close} from '@element-plus/icons-vue';
	import BiddingDialog from './components/bidding_dialog.vue';
	import AdOrderCycle from '@/views/amazon/advertisement/manager/components/common/ad_order_cycle.vue';
	import SchedulePlan from '@/views/amazon/advertisement/manager/components/common/schedule_plan.vue';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import advAdgroupApi from '@/api/amazon/advertisement/report/advAdgroupApi.js';
	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import productAnysApi from '@/api/amazon/product/productAnysApi.js';
	import advKeywordsApi from '@/api/amazon/advertisement/report/advKeywordsApi.js';
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import filtericon from "@/components/icon/filtericon.vue";
	import Category from '@/components/header/category.vue';
	import SearchCache from '@/components/header/search.vue';
	import DateSelect from '../../components/common/date_select.vue'
	import DataFilter from '@/views/amazon/advertisement/manager/components/common/data_filter.vue';
	import {handleSubmitUpdate,bidplusFormatter,loadList,handleExpandChange,
	biddingEdit,handleUpdate,handleAdvPlacementData} from '../javascript/campaigns.js';
	import {showAdChart,handleAdvDaysData,} from '../javascript/common.js';
	import {keysuggestFormatter,adgroupsuggestFormatter,targetsuggestFormatter,
	targetnameFormatter,faltngatargetFormatter} from '../javascript/formatter.js';
	import AdChart from '@/views/amazon/advertisement/manager/components/common/ad_chart.vue';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const endtimeRef = ref()
	const budgetRefRef = ref()
	const biddingRef = ref()
	const globalTable =ref()
	const scheduleRef = ref()
	const adchartRef=ref();
	const categoryRef=ref();
	const nameRefRef=ref();
	let props = defineProps({ activeName:"" })
	const { activeName } = toRefs(props);
	const state = reactive({
		visible:false,
		budgetVisible:false,
		nameVisible:false,
		editRow:{endDate:'',budget:''},
		tableData:{records:[
		],total:0},
		summary:{},
		expandType:"",
		queryParams:{},
		batch:{text:'',input:false,value:'',},
		emits:null,
		defaultSort:{ prop: 'startDate,name', order: 'descending,descending' },
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
		adTypeOptions:[
			{name:'自动投放',value:'auto',},
			{name:'手动投放',value:'manual',},
			{name:'手动及自动',value:'all',},
		],
		adType:'all',
		selectionloding:false,
	})
	const{
		tableData,
		visible,
		budgetVisible,
		nameVisible,
		editRow,
		batch,
		summary,
		expandType,
		queryParams,
		defaultSort,
		bindParams,
		adStatesOptions,
		adstate,
		adTypeOptions,
		adType,
		selectionloding,
	}=toRefs(state)
	const emits = defineEmits(['selectRow','change','bindData']);
	state.emits=emits;
	
	function showOrderby(row){
		//展示图表
	}
	function handleSearchChange(value,type){
		state.queryParams.search=value;
		if(type!="load"){
        		handleQuery();
		}
	}
    function setDate(dates,dateType,isload,tabName){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(isload!="load"){
			if(tabName===''||tabName===null||tabName===undefined||tabName==='adcams'){
				handleQuery();
			}
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
		if(categoryRef.value){
			categoryRef.value.reset();
		}
		handleQuery();
	}
 
	function handleAdd(){
		router.push({
			path:"/a/a/sb",
			query:{
				title:'创建商品广告',
				path:"/a/a/sb",
				type:state.activeName,
				campaignid:state.queryParams.campaignid,
				adGroupid:state.queryParams.adGroupid
			},
		})
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
	function pickDate(e,row){
		endtimeRef.value = e.currentTarget;
		state.editRow=row;
		state.editRow.endDate = row.endDate;
		state.visible=true;
	}
	function budgetEdit(e,row){
		budgetRefRef.value = e.currentTarget;
		state.editRow=row;
		state.editRow.budget = row.budget;
		state.budgetVisible=true;
	}
	function nameEdit(e,row){
		nameRefRef.value = e.currentTarget;
		state.editRow=row;
		state.editRow.name = row.name;
		state.nameVisible=true;
	}
	function scheduleSet(row){
		scheduleRef.value.show(row);
	}
	function selectChange(seletion){
		emits('selectRow',seletion)
	}
	
	function handleExpend(row,treeNode,resolve){
		handleExpandChange(state,row,treeNode,resolve)
	}
	function handleExpendAdContent(ftype){
		if(ftype=="days"){
			//广告成交周期展开
			state.expandType="days";
		}else if(ftype=="placement"){
			//广告位置展开
			state.expandType="placement";
		}
		var records=JSON.parse(JSON.stringify(state.tableData.records)); 
		records.forEach(item=>{
			handleExpandChange(state,item);
		});
		state.tableData.records=[];
		var time=setTimeout(function(){
			state.tableData.records=records;
			clearTimeout(time);
		},1000);
		
	 
	}
 
	function loadTableData(params){
		if(params&&params.profileid){
			state.queryParams=params;
			loadList(state);
		}
	}
	function handleQuery(){
		if(state.queryParams.profileid){
			state.queryParams.campaignStatus=state.adstate;
			state.queryParams.targetingType=state.adType;
			state.queryParams.campaignName=state.queryParams.search;
			globalTable.value.loadTable(state.queryParams);
		}
	    
	}
	
	  
	 function show(params){
		 if(state.queryParams.profileid!=params.profileid){
			 state.queryParams=Object.assign(state.queryParams, params);
			 state.activeName=props.activeName;
			 var timer=setTimeout(function(){
						 handleQuery();
						 clearTimeout(timer);
			 },100);
		 }
	 }
	 function changeCategory(val){
	 	state.queryParams.categoryid=val;
	 }
	 function handleValue(type,name){
	 	state.batch.input = true;
	 	state.batch.text = name;
		state.batch.type=type;
	 }
	 function getSummaries({columns,data}){
	 		var arr = ["合计"];
	 				columns.forEach((item,index)=>{
	 						if(index>=10){
	 							arr[index]=state.summary[item.property];
	 						}
	 				})
	 		return  arr;
	 	}
	defineExpose({
		  show,
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