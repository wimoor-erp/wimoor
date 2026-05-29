<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
		  <Warehouse @changeware="getWarehouse" defaultValue="only" :isform="true" :isparent="true" />
		  <el-date-picker
		          v-model="queryParam.fromDate"
		          type="month"
				  @change="handleQuery"
				  value-format="YYYY-MM"
		          placeholder="选择日期"
				  :clearable="false"
				  :editable="false"
		        />
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
			   <div style="margin-right:20px;padding-top:12px;" class="font-extraSmall">
			   更新时间:<span v-if="refreshtime">{{refreshtime}}</span><span v-else>无</span></div>
				<el-button @click.stop="downloadList" :loading="downLoading">导出</el-button>
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
		 <el-table-column prop="warehousename" label="仓库"  sortable="custom"  width="160" />
		 <el-table-column prop="startqty" label="期初"  sortable="custom"  width="100" />
		 <el-table-column prop="endqty" label="期末"  sortable="custom"  width="100" />
		 <el-table-column prop="purchase" label="采购"  sortable="custom"  width="100" />
		 <el-table-column prop="otherin" label="入库"  sortable="custom"  width="100" />
		 <el-table-column prop="otherout" label="出库"  sortable="custom"  width="100" />
		 <el-table-column prop="shipment" label="发货"  sortable="custom"  width="100" />
		 <el-table-column prop="dispatch" label="调库"  sortable="custom"  width="100" />
		 <el-table-column prop="assembly" label="组装"  sortable="custom"  width="100" />
		 <el-table-column prop="stock" label="盘点"  sortable="custom"  width="100" />
		 <el-table-column prop="diff" label="库存差异"  sortable="custom"  width="100" />
		 <el-table-column prop="period" label="周转个数"  sortable="custom"  width="100" />
		 <el-table-column prop="turndays" label="周转天数"  sortable="custom"  width="100" />
	  </template>
	 </GlobalTable>
	</el-row>
  </div>
	 
</template>

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
		 queryParam:{
			 startDate:"",
			 endDate:"",
			 groupid:'',
			 search:"",
			 days:540,
			 marketplaceid:"",
			 fromDate:"",
		 },
		 refreshtime:"",
		 isload:true,
		 tableData:{records:[],total:0},
	});
	let {
	   queryParam,
	   isload,
	   tableData, 
	   refreshtime,
	   downLoading,
	} =toRefs(state);
	onMounted(()=>{
		var oidtime = new Date().getTime() - 3600 * 1000 * 24 * 35
		state.queryParam.fromDate = new Date(oidtime).format("yyyy-MM")
	})
	 
 
	function getWarehouse(val){
		state.queryParam.warehouseid=val;
		handleQuery();
	}
	function handleQuery(){
		state.queryParam.sku=state.queryParam.search;
		state.queryParam.monthDate=state.queryParam.fromDate;
		globalTable.value.loadTable(state.queryParam);
	}
	 
	function loadTableData(params){
		inventoryRptApi.getAllInventorySummaryMonth(params).then((res)=>{
			state.isload=false;
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(res.data&&res.data.records){
				res.data.records.forEach(item=>{
					if(item.refreshtime){
					  state.refreshtime=item.refreshtime.substring(0,10);
					}
				})
			}
		});
	}
	function downloadList(){
		state.downLoading=true
		inventoryRptApi.downExcelInventorySummaryMonth(state.queryParam,()=>{
			state.downLoading=false;
		});
	}
	 
</script>

<style>
</style>
