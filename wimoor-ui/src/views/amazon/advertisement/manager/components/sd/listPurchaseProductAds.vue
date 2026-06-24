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
		   <el-table-column fixed   label="活动名称" prop="campaignName" width="200"  >
		   			   <template #default="scope">
		   				   <div  >
		   				     <el-link  :underline="false" type="primary">
		   				   			 <span> {{scope.row.campaignName}}</span>
		   				     </el-link>
		   				    <copy class="" @click.stop="CopyText(scope.row.campaignName)" title='复制广告活动' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
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
		   <el-table-column    label="品牌效应ASIN" prop="asinBrandHalo"    width="140"  >
			   <template #default="scope">
					{{scope.row.asinBrandHalo}}
			   </template>
		    </el-table-column>
			
			<el-table-column    label="加入心愿单数量" prop="addToList"   width="140" >
				   <template #default="scope">
						{{scope.row.addToList}}
				   </template>
			 </el-table-column>
			 <el-table-column    label="点击带来的心愿单数量" prop="addToListFromClicks"  width="150" >
			 	   <template #default="scope">
			 			{{scope.row.addToListFromClicks}}
			 	   </template>
			  </el-table-column>
			  <el-table-column    label="点击带来的合格借阅量" prop="qualifiedBorrowsFromClicks" width="150"  >
			  	   <template #default="scope">
			  			{{scope.row.qualifiedBorrowsFromClicks}}
			  	   </template>
			   </el-table-column>
			   <el-table-column    label="点击带来的版税合格借阅量" prop="royaltyQualifiedBorrowsFromClicks"  width="170"   >
			   	   <template #default="scope">
			   			{{scope.row.royaltyQualifiedBorrowsFromClicks}}
			   	   </template>
			    </el-table-column>
				<el-table-column    label="展示带来的加入心愿清单数量" prop="addToListFromViews" width="180"    >
					   <template #default="scope">
							{{scope.row.addToListFromViews}}
					   </template>
				 </el-table-column>
				 <el-table-column    label="总合格借阅量" prop="qualifiedBorrows" width="150"    >
				 	   <template #default="scope">
				 			{{scope.row.qualifiedBorrows}}
				 	   </template>
				  </el-table-column>
				  <el-table-column    label="展示带来的合格借阅量" prop="qualifiedBorrowsFromViews"  width="150"  >
				  	   <template #default="scope">
				  			{{scope.row.qualifiedBorrowsFromViews}}
				  	   </template>
				   </el-table-column>
				   <el-table-column    label="总版税合格借阅量" prop="royaltyQualifiedBorrows"   width="120" >
				   	   <template #default="scope">
				   			{{scope.row.royaltyQualifiedBorrows}}
				   	   </template>
				    </el-table-column>
					<el-table-column    label="展示带来的版税合格借阅量" prop="royaltyQualifiedBorrowsFromViews"  width="170"  >
						   <template #default="scope">
								{{scope.row.royaltyQualifiedBorrowsFromViews}}
						   </template>
					 </el-table-column>
					 <el-table-column    label="货币代码" prop="campaignBudgetCurrencyCode"  width="120"  >
					 	   <template #default="scope">
					 			{{scope.row.campaignBudgetCurrencyCode}}
					 	   </template>
					  </el-table-column>
					<el-table-column    label="品牌效应总转化量" prop="conversionsBrandHalo"  width="120"  >
					  	   <template #default="scope">
					  			{{scope.row.conversionsBrandHalo}}
					  	   </template>
					</el-table-column>
					<el-table-column    label="点击带来的品牌效应转化量" prop="conversionsBrandHaloClicks"   width="170" >
					   	   <template #default="scope">
					   			{{scope.row.conversionsBrandHaloClicks}}
					   	   </template>
					</el-table-column>
					<el-table-column    label="ASIN" prop="promotedAsin"   width="120" >
					   	   <template #default="scope">
					   			{{scope.row.promotedAsin}}
					   	   </template>
					</el-table-column>
				    <el-table-column    label="SKU" prop="promotedSku"   width="120" >
						   <template #default="scope">
								{{scope.row.promotedSku}}
						   </template>
					</el-table-column>
					<el-table-column    label="品牌效应总销售额" prop="salesBrandHalo"   width="120" >
						   <template #default="scope">
								{{scope.row.salesBrandHalo}}
						   </template>
					</el-table-column>
					<el-table-column    label="点击带来的品牌效应销售额" prop="salesBrandHaloClicks"  width="170"  >
					   	   <template #default="scope">
					   			{{scope.row.salesBrandHaloClicks}}
					   	   </template>
					</el-table-column>
					<el-table-column    label="品牌效应总销量" prop="unitsSoldBrandHalo"   width="150" >
					   	   <template #default="scope">
					   			{{scope.row.unitsSoldBrandHalo}}
					   	   </template>
					</el-table-column>
					<el-table-column    label="点击带来的品牌效应销量" prop="unitsSoldBrandHaloClicks"  width="170"  >
						   <template #default="scope">
								{{scope.row.unitsSoldBrandHaloClicks}}
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
		defaultSort:{ prop: 'promotedAsin', order: 'ascending' },
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