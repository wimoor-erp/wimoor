<template>
	<el-row>
		<el-space>
          <DateSelect @setDate="setDate" @dateTypeSwitch="handleQuery"/>
			<el-input v-model="queryParams.search" clearable @input="handleQuery" placeholder="搜索SKU">
					 <template #suffix>
					  <el-icon @click.stop="handleQuery"><Search /></el-icon>
					 </template>
			</el-input>	
			<el-button @click="clearSearch">重置</el-button>
		</el-space>
	</el-row>
	 
  <div  class="summary-top expend-table" v-loading="selectionloding">
	 <GlobalTable  ref="globalTable" :tableData="tableData"
	   @loadTable="loadTableData" :stripe="false" 
	   :defaultSort="defaultSort"
	   >
	   <template #field>
		 <el-table-column   fixed  label="活动名称" prop="campaignName"  width="200" >
			 <template #default="scope">
				 <div  >
					 <div  >
					   <el-link  :underline="false" type="primary">
						  <span> {{scope.row.campaignName}}</span>
					   </el-link>
					  <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
					  </div>
				 </div>
			 </template>
		</el-table-column>
		 <el-table-column fixed   label="组名称" prop="adGroupName" width="200"  >
			   <template #default="scope">
				   <div  >
				     <el-link  :underline="false" type="primary">
				   			 <span> {{scope.row.adGroupName}}</span>
				     </el-link>
				    <copy class="" @click.stop="CopyText(scope.row.adGroupName)" title='复制广告组' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
				    </div>
			   </template>
		  </el-table-column>
		   <el-table-column    label="预算货币代码" prop="campaignBudgetCurrencyCode" width="120"  >
			   <template #default="scope">
					{{scope.row.campaignBudgetCurrencyCode}}
			   </template>
		    </el-table-column>
			
			<el-table-column    label="价格类型代码" prop="campaignPriceTypeCode" width="120"  >
				   <template #default="scope">
						{{scope.row.campaignPriceTypeCode}}
				   </template>
			 </el-table-column>
			 <el-table-column    label="归因类型" prop="attributionType"  width="140" >
			 	   <template #default="scope">
			 			{{scope.row.attributionType}}
			 	   </template>
			  </el-table-column>
			  <el-table-column    label="ASIN" prop="purchasedAsin" width="100"  >
			  	   <template #default="scope">
			  			{{scope.row.purchasedAsin}}
			  	   </template>
			   </el-table-column>
			   <el-table-column    label="14天内点击产生的订单" prop="ordersClicks14d"  width="150"   >
			   	   <template #default="scope">
			   			{{scope.row.ordersClicks14d}}
			   	   </template>
			    </el-table-column>
			   <el-table-column label="14天内销售额" prop="sales14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.sales14d !== null ? '¥' + scope.row.sales14d.toFixed(2) : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="点击销售额(14天)" prop="salesClicks14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.salesClicks14d !== null ? '¥' + scope.row.salesClicks14d.toFixed(2) : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="14天内订单量" prop="orders14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.orders14d !== null ? scope.row.orders14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="14天内销量" prop="unitsSold14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.unitsSold14d !== null ? scope.row.unitsSold14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌销售额(14天)" prop="newToBrandSales14d" width="170" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandSales14d !== null ? '¥' + scope.row.newToBrandSales14d.toFixed(2) : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌购买量(14天)" prop="newToBrandPurchases14d" width="170" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandPurchases14d !== null ? scope.row.newToBrandPurchases14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌销量(14天)" prop="newToBrandUnitsSold14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandUnitsSold14d !== null ? scope.row.newToBrandUnitsSold14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌销售占比" prop="newToBrandSalesPercentage14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandSalesPercentage14d !== null ? (scope.row.newToBrandSalesPercentage14d  ).toFixed(2) + '%' : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌购买占比" prop="newToBrandPurchasesPercentage14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandPurchasesPercentage14d !== null ? (scope.row.newToBrandPurchasesPercentage14d ).toFixed(2) + '%' : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="新品牌销量占比" prop="newToBrandUnitsSoldPercentage14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.newToBrandUnitsSoldPercentage14d !== null ? (scope.row.newToBrandUnitsSoldPercentage14d  ).toFixed(2) + '%' : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="点击销量(14天)" prop="unitsSoldClicks14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.unitsSoldClicks14d !== null ? scope.row.unitsSoldClicks14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="Kindle阅读页数(14天)" prop="kindleEditionNormalizedPagesRead14d" width="170" sortable>
			       <template #default="scope">
			           {{ scope.row.kindleEditionNormalizedPagesRead14d !== null ? scope.row.kindleEditionNormalizedPagesRead14d : '-' }}
			       </template>
			   </el-table-column>
			   
			   <el-table-column label="Kindle版税(14天)" prop="kindleEditionNormalizedPagesRoyalties14d" width="150" sortable>
			       <template #default="scope">
			           {{ scope.row.kindleEditionNormalizedPagesRoyalties14d !== null ? '¥' + scope.row.kindleEditionNormalizedPagesRoyalties14d.toFixed(2) : '-' }}
			       </template>
			   </el-table-column>
			</template>
	 </GlobalTable>
  </div>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,toRefs,computed} from 'vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,Menu,Edit,Histogram,WarningFilled,Close,Tools} from '@element-plus/icons-vue';
	import {Copy} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
    import DateSelect from '../../components/common/date_select.vue'
	import {loadList} from '../javascript/purchaseProductAds.js';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const endtimeRef = ref()
	const budgetRefRef = ref()
	const globalTable =ref()
	let props = defineProps({ activeName:"" })
	const { activeName } = toRefs(props);
	const state = reactive({
		editRow:{endDate:'',budget:''},
		tableData:{records:[
		],total:0},
		summary:{},
		expandType:"",
		queryParams:{},
		batch:{text:'',input:false,value:'',},
		emits:null,
		defaultSort:{ prop: 'purchasedAsin', order: 'ascending' },
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
		selectionloding:false,
	})
	const{
		tableData,
		editRow,
		batch,
		summary,
		expandType,
		queryParams,
		defaultSort,
		bindParams,
		adStatesOptions,
		adstate,
		selectionloding,
	}=toRefs(state)
	const emits = defineEmits(['selectRow','change','bindData']);
	state.emits=emits;
	
	 
    function setDate(dates,dateType,isload,tabName){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(isload!="load"){
			if(tabName===''||tabName===null||tabName===undefined||tabName==='ProductAds'){
				handleQuery();
			}
		}
	}
	 
	function clearSearch(){
		state.adstate='all';
		state.adType='all';
		state.queryParams.search='';
		state.bindParams={};
		state.queryParams.changeRate='';
		state.queryParams.skuStr=null;
		handleQuery();
	}

	onMounted(()=>{
		//加载汇总数值
	
	});
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
			state.queryParams.targetingType=state.adType;
		    state.queryParams.sku=state.queryParams.search;
			globalTable.value.loadTable(state.queryParams);
		}
	    
	}
	 
	 function show(params){
		 if(state.queryParams.profileid!=params.profileid
		  ||state.queryParams.campaignid!=params.campaignid
		  ||state.queryParams.adGroupid!=params.adGroupid){
		 state.queryParams=Object.assign(state.queryParams, params);
		 state.activeName=props.activeName;
			var timer=setTimeout(function(){
						 handleQuery();
						 clearTimeout(timer);
			},100);
			}
	 }
	 
	 function handleValue(type,name){
	 	state.batch.input = true;
	 	state.batch.text = name;
		state.batch.type=type;
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