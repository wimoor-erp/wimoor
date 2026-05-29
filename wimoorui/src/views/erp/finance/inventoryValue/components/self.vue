<template>
	<div class="flex-center-between" style="margin-bottom:10px;">
	<el-space>
	  <el-button @click="download">导出</el-button>
	  <Warehouse defaultValue="only" :isAll="true"  @changeware="getWarehouse" />
	  <el-space :size="4">
	  <span class="font-extraSmall">库存日期：</span>
	  <el-date-picker
	        v-model="queryParams.byday"
	        type="date"
			:clearable="false"
			:editable="false"
			@change = "dateChange"
	        placeholder="请选择"
	      /></el-space>
	<el-input  v-model="queryParams.sku" :clearable="true" placeholder="请输入本地SKU" @input="handleQuery" class="input-with-select" >
	   <template #append>
	     <el-button @click="handleQuery">
	        <el-icon style="font-size: 16px;align-itmes:center">
	         <search />
	      </el-icon>
	     </el-button>
	   </template>
	 </el-input>
	  </el-space>
	  <el-button   class='ic-btn' title='帮助文档'>
			<help theme="outline" size="16" :strokeWidth="3"/>
	  </el-button>
 </div>
	   
	<GlobalTable    
	 show-summary
	 :summary-method="getSummaries"
	  :tableData="tableData"
	  ref="globalTable"
	 height="calc(100vh - 248px)" border @loadTable="loadTableData" >
		<template #field>
			<el-table-column label="图片" prop="image"  width="60"   >
				<template #default="scope">
					   <el-image v-if="scope.row.image" :src="scope.row.image" class="img-40"     ></el-image>
					   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  ></el-image>
				</template>
			</el-table-column>
	<el-table-column label="名称/SKU" prop="sku"    sortable="custom"  show-overflow-tooltip>
		<template #default="scope">
		<div class="flex-center">
			   <div >
				   <div   >{{scope.row.name}}</div>
				   <p class="sku">{{scope.row.sku}}</p>
			   </div>
		</div>
		</template>
	</el-table-column>
		 
		<el-table-column label="现有库存" sortable="custom"  prop="quantity">
			<template #header>
				<el-space :size="4">
				  <span>库存</span>
				   <el-tooltip
				          effect="dark"
				          content="可用+待出库"
				          placement="top-start"
				        >
				          <el-icon class="ic-cen font-small"><InfoFilled /></el-icon>
				        </el-tooltip>
				</el-space>
			</template>
		</el-table-column>
		<el-table-column v-if="queryParams.today" label="待入库库存" sortable="custom" prop="inbound"></el-table-column>
		<el-table-column label="采购成本单价(￥)" prop="price">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.price)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="总货值(￥)" prop="totalprice">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.totalprice)}}</div>
			</template>
		</el-table-column>
		<el-table-column v-if="queryParams.today" label="在途货值(￥)" sortable="custom" prop="inpasscost">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.inpasscost)}}</div>
			</template>
		</el-table-column>
		<el-table-column v-if="queryParams.today" label="采购待付款(￥)" sortable="custom" prop="inpasspay">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.inpasspay)}}</div>
			</template>
		</el-table-column>
		<el-table-column   label="其它成本(￥)" prop="otherFeer">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.otherFeer)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="备注" prop="remark" show-overflow-tooltip>
		</el-table-column>
		</template>
	</GlobalTable>
</template>

<script setup>
	import {h,ref,reactive,toRefs, onMounted} from 'vue';
	import {Search,ArrowDown,InfoFilled,} from '@element-plus/icons-vue';
	import {Help} from '@icon-park/vue-next';
	import Warehouse from '@/components/header/warehouse.vue';
	import {dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	import inventoryRptApi from '@/api/erp/inventory/inventoryRptApi.js';
	const globalTable=ref();
	const state=reactive({
		tableData: {records:[],total:0},
		summary:{},
		queryParams:{sku:"",byday:new Date().format("yyyy-MM-dd"),today:true}
	})
	const{
		tableData,queryParams
	}=toRefs(state)
 
	function loadTableData(params){
		inventoryRptApi.inventoryCost(params).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total
			if(params.currentpage==1){
				if(res.data&&res.data.records&&res.data.records.length>0){
					state.summary=res.data.records[0].summary;
				}else{
					state.summary={};
				}
			}
		})
	}
	/* 合计行数据 */
	function getSummaries({columns,data}){
		var arr = ["合计"];
		columns.forEach((item,index)=>{
			if(index>=2){
				 arr[index]=outputmoney(state.summary[item.property]);
				 
			}
		})
		return  arr
	}
 
	function download(){
			inventoryRptApi.downLoadInvCost(state.queryParams);
	}
	function handleQuery(){
			globalTable.value.loadTable(state.queryParams);
	}
	function getWarehouse(val){
		state.queryParams.warehouseid=val;
		handleQuery();
	}
	function dateChange(val){
		 state.queryParams.byday=val.format("yyyy-MM-dd");
		  const today = new Date().format("yyyy-MM-dd");
		  if(today==state.queryParams.byday){
			  state.queryParams.today=true;
		  }else if(!state.queryParams.byday){
			  state.queryParams.today=true;
		  }else{
			  state.queryParams.today=false; 
		  }
		 handleQuery();
	}
	 
</script>

<style scoped>
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>
