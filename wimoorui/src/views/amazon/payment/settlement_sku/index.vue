<template >
	<div class="main-sty con-header "> 
	<el-row>
		<Group @change="getGroup"    ref="groupRef" />
	 <el-space>
		 <el-select style="width:100px;" v-model="queryParams.currency" @change="handleQuery">
		 	<el-option label="人民币" value="CNY"></el-option>
		 	<el-option label="美元" value="USD"></el-option>
		 	<el-option label="站点币种" value="market"></el-option>
		 </el-select>
		 <div class="date-picker-group">
		 	<el-select v-model="queryParams.datetype" @change="handleQuery">
		 		<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
		 	</el-select>
			<Datepicker :shortIndex="1"  @changedate="changedate" />
		 </div> 
		<Owner  ref="ownerRef" @owner="getOwner" />
		 <el-input v-model="queryParams.search" @input="handleQuery" placeholder="搜索平台SKU" clearable></el-input>
		 <el-button type="primary" @click="handleQuery">查询</el-button>
		 <el-button type="primary" plain @click="showAccDialog" title="用于锁定汇率">结账</el-button>
	  </el-space>
	  <div class='rt-btn-group' >
		  <el-button    @click="recalculation" > 重算 </el-button>
		<el-button   @click="downloadList" :loading="downloading">导出</el-button>
	  </div>
	  </el-row>
      <TableHorizontal v-if="tabletype=='horizontal'"   @changeTable="handleChnageTable" ref="tableRef"/>
      <TableVertical v-else   @changeTable="handleChnageTable" ref="tableRef"/>
	</div>
	<AccDialog   ref="accDialogRef"></AccDialog>
	<RefreshDataDialog ref="refreshDataDialogRef" @change="handleQuery" :needdeep="true"  ></RefreshDataDialog>
</template>
<script>
    export default{ name:"账期SKU" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,computed,nextTick} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled} from '@element-plus/icons-vue';
	import Datepicker from '@/components/header/datepicker.vue';
  import '@/assets/css/packbox_table.css';
	import {dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	import Group from '@/views/amazon/listing/common/group.vue';
	import Owner from '@/components/header/owner.vue';
	import AccDialog from './account_dialog.vue';
	import TableHorizontal from './table.vue';
	import TableVertical from './table_vertical.vue';
	import RefreshDataDialog from '@/views/amazon/payment/common/refresh_data_dialog.vue';
	import settlementSkuRptApi from '@/api/amazon/finances/settlementSkuRptApi.js';
	const tableRef=ref();
	const ownerRef=ref();
    const accDialogRef=ref();
	const calcDialogRef=ref();
	const refreshDataDialogRef=ref();
	const  state=reactive({
		isload:true,
		dateOptions:[{label:'结算日期',value:''},{label:'转账日期',value:'acc'}],
		loading:false,
		tableData: {records:[],total:0}  ,
		queryParams:{datetype:"acc",currency:"CNY"},
		summary:{},
		tabletype:"horizontal",
		downloading:false,
	})
	const{
		isload,
		loading,tabletype,
		dateOptions,
		tableData,
		queryParams,
		summary,
		downloading,
	}=toRefs(state)
	
	function showAccDialog(){
		accDialogRef.value.show(state.queryParams);
	}
	 function handleChnageTable(value){
			state.tabletype=value;
			nextTick(()=>{
			handleQuery();
			})
		}
	
	 function downloadList(){
	 	state.downloading=true;
	 	settlementSkuRptApi.downDataExcel(state.queryParams,()=>{
	 		state.downloading=false;
	 	})
	 }
	 function recalculation(){
	 	refreshDataDialogRef.value.show();
	 }
	 function handleQuery(){
	 	tableRef.value.show(state.queryParams);
		state.isload=false;
	 }
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
		 
	}
	function getOwner(id){
		state.queryParams.ownerid=id;
		if(state.isload==false){
			handleQuery();
		}
	}
	function getGroup(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		handleQuery();
	}  
     function parseFloatValue(value){
		 if(value)return parseFloat(value);
		 else return 0.0;
	 }
		  
</script>
<style>
	.bg-group-y{
		background-color:#efefef !important;
	}
	.dark .bg-group-y{
		background-color:#434343 !important;
	}
</style>
<style scoped >
	 .l-h-none{
	 	line-height:14px;
	 }

</style>