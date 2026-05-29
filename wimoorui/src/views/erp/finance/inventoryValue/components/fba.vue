<template>
 
	<div class=" flex-center-between" style="margin-bottom:10px;">
	<el-space>
	  <el-button @click="download">导出</el-button>
	  <Group  ref="groups" @change="changeGroup" isproduct="ok" defaultValue="onlyEU"/>
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
	<el-input  v-model="queryParams.sku" :clearable="true" placeholder="请输入平台SKU" @input="handleQuery" class="input-with-select" >
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
		<el-table-column label="名称/SKU" prop="sku" sortable="custom"  show-overflow-tooltip>
			<template #default="scope" >
				<div class="name">{{scope.row.name}}</div>
				<div class="sku">{{scope.row.sku}}</div>
			</template>
		</el-table-column>
		 <el-table-column label="可用库存" sortable="custom"  prop="fulfillable"></el-table-column>
		 <el-table-column label="预留库存" sortable="custom" prop="reserved"></el-table-column>
		 <el-table-column label="待入库库存" sortable="custom" prop="inbound"></el-table-column>
	     <el-table-column label="总库存(可用)" sortable="custom" prop="quantity"></el-table-column>
		<el-table-column label="采购单价(￥)" prop="price">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.price)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="总货值(￥)" prop="totalprice">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.totalprice)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="其它费用(￥)" prop="otherFeer">
			<template #default="scope" >
				<div >{{outputmoney(scope.row.otherFeer)}}</div>
			</template>
		</el-table-column>
		<el-table-column label="头程运费(￥)" prop="firstLegCharges">	
		<template #default="scope" >
			<div >{{outputmoney(scope.row.firstLegCharges)}}</div>
		</template>
		</el-table-column>
		<el-table-column label="备注" prop="remark" show-overflow-tooltip>
		</el-table-column>
		</template>
	</GlobalTable>  
</template>

<script setup>
	import {h,ref,reactive,toRefs,onMounted} from 'vue';
	import {Help} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	import inventoryRptApi from '@/api/amazon/inventory/inventoryRptApi.js';
	const globalTable=ref();
	const state=reactive({
		tableData: {records:[],total:0},
		summary:{},
		queryParams:{sku:"",byday:new Date().format("yyyy-MM-dd")}
	})
	const{
		tableData,queryParams
	}=toRefs(state)
	
	 
	function loadTableData(params){
		inventoryRptApi.inventoryCostFBA(params).then((res)=>{
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
			if(index>=2&&state.summary[item.property]){
				 arr[index]=outputmoney(state.summary[item.property]);
				 
			}
		})
		return  arr
	}
 
	function download(){
			//FBA仓下载
			inventoryRptApi.downloadInvCostFBA(state.queryParams);
	}
	function handleQuery(){
		globalTable.value.loadTable(state.queryParams);
	}
	function changeGroup(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		handleQuery();
	}
	function dateChange(val){
		 state.queryParams.byday=val.format("yyyy-MM-dd");
		 handleQuery();
	}
 
</script>

<style>

</style>
