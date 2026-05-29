<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
		  <Warehouse @changeware="getWarehouse" defaultValue="only" :isform="true" :isparent="true" />
		 <el-input  v-model="queryParam.search" @input="handleQuery" clearable placeholder="请输入SKU" style="width: 250px;" class="input-with-select" >
		    <template #append>
		      <el-button @click="handleQuery" >
		         <el-icon class="ic-cen font-medium">
		          <search/>
		       </el-icon>
		      </el-button>
		    </template>
		  </el-input>
		   </el-space>
		   <div class='rt-btn-group'>
			   <div style="padding-right:20px;padding-top:10px;">
			   	  <span class="font-extraSmall">更新时间:{{snapshotDate}}</span> 
			    </div>
			   <el-button @click.stop="downloadList" :loading="downLoading">导出</el-button>
			    <el-button @click.stop="showSearchDialog" :loading="longloading" >导出超长库龄</el-button>
		   <el-button class='ic-btn'  title='列配置'>
		      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
		    <el-button   class='ic-btn' title='帮助文档'>
		     <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </div>
		</el-row>
		</div>
	<el-row>
		
	<GlobalTable
	  show-summary
	  :summary-method="getSummaries"
	  ref="globalTable" :tableData="tableData"   height="calc(100vh - 200px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="image" fixed label="图片" width="60" >
			<template #default="scope">
			 <el-image v-if="scope.row.image" :src="scope.row.image"   width="40" height="40"  ></el-image>
			 <el-image v-else  :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
		   </template>
		 </el-table-column>
		 <el-table-column prop="sku"  label="产品信息" sortable="custom"  show-overflow-tooltip   >
		    <template #default="scope">
		       <div class='name'>{{scope.row.name}}</div>
		       <div class='sku'>{{scope.row.sku}} 
		       </div>
		   </template>
		 </el-table-column>
		 <el-table-column prop="qtysum" label="当前库存"  sortable="custom"  width="100" >
		 <template #default="scope">
			  {{scope.row.qtysum}}
			 </template>
		 </el-table-column> 
		<el-table-column label="库龄"   >
		 <el-table-column prop="qtysum0" label="0~30天"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.qtysum0}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="qtysum30" label="30~60天"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.qtysum30}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="qtysum60" label="60~90天"   sortable="custom"  width="100" >
		 <template #default="scope">
		 			  {{scope.row.qtysum60}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="qtysum90" label="90~180天"  sortable="custom"   width="120" >
		 <template #default="scope">
		 			  {{scope.row.qtysum90}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="qtysum180" label="180~365天"   sortable="custom"  width="120" >
		 <template #default="scope">
		 			  {{scope.row.qtysum180}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="qtysum365" label="365天以上" sortable="custom"    width="120" >
		 <template #default="scope">
		 			  {{scope.row.qtysum365}}
		 			 </template>
		 </el-table-column> 
		 </el-table-column> 
		 <el-table-column  label="出库数量"    >
		 <el-table-column prop="salesum30" label="30天"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.salesum30}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="salesum60" label="60天"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.salesum60}}
		 			 </template>
		 </el-table-column> 
		 <el-table-column prop="salesum90" label="90天"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.salesum90}}
		 			 </template>
		 </el-table-column> 
		  </el-table-column> 
		 <el-table-column prop="nowinv90" label="冗余库存"  sortable="custom"   width="100" >
		 <template #default="scope">
		 			  {{scope.row.nowinv90}}
		 			 </template>
		 </el-table-column> 
		 
	  </template>
	 </GlobalTable>
	</el-row>
		</div>
	<el-dialog title="超过365天库龄计算器" v-model="visable" width="60%">
		   <el-space>
			   库存天数大于<el-input width="100" v-model="queryParam.days"></el-input>
		   </el-space>
		   <template #footer>
		   		<el-button type="info" @click="visable=false">关闭</el-button>
				<el-button type="primary" @click="loadlongtableData" :loading="longloading">下载</el-button>
		   </template>
	</el-dialog>
</template>
<script>
export default {
    	name: "本地滞销" 
		};
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import inventoryRptApi from '@/api/erp/inventory/inventoryRptApi.js';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	const globalTable=ref();
	let state=reactive({
		 downLoading:false,
		 longloading:false,
		 dateValue:"",
		 selectlabel:"sku",
		 queryParam:{
			 startDate:"",
			 endDate:"",
			 groupid:'',
			 search:"",
			 days:540,
			 marketplaceid:"",
		 },
		 isload:true,
		 tableData:{records:[],total:0},
		 snapshotDate:'',
		 summary:{},
		 visable:false,
	
		 longtableData:[],
	});
	let {
	   dateValue,
	   selectlabel,
	   queryParam,
	   isload,
	   tableData,
	   snapshotDate,
	   summary,
	   downLoading,
	   visable,
	   longloading,
	   longtableData,
	} =toRefs(state);
	function searchLongList(){
		
	}
	function loadlongtableData(){
		state.longloading=true;
		if(!state.queryParam.days){
			ElMessage.error('库存天数不能为空或0');
			return ;
		}
		inventoryRptApi.findUnsalableReportByDayExcel(state.queryParam,()=>{
			state.longloading=false;
		})
	}
	function showSearchDialog(){
		state.visable=true;
	}
	function getWarehouse(val){
		state.queryParam.warehouseid=val;
		handleQuery();
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParam);
	}
	 
	function loadTableData(params){
		inventoryRptApi.findUnsalableReport(params).then((res)=>{
			state.isload=false;
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(params.currentpage==1){
				 if(res.data.total>0){
					  state.summary=res.data.records[0].summary;
				 }
			}
			if(res.data&&res.data.records&&res.data.records.length>0){
				state.snapshotDate=res.data.records[0].datename;
			}else{
				state.snapshotDate="无";
			}
		});
	}
	function downloadList(){
		state.downLoading=true
		inventoryRptApi.findUnsalableReportExcel(state.queryParam,()=>{
			state.downLoading=false;
		});
	}
	 
	/* 合计行数据 */
	function getSummaries({columns,data}){
		var arr = ["合计"];
		columns.forEach((item,index)=>{
			if(index>=2){
				 arr[index]=state.summary["all"+item.property];
				 
			}
		})
		return  arr
	}
</script>

<style>
</style>
