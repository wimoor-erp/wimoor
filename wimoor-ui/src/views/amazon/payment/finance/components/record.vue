<template>
	<el-space>
	  <Group  @change="groupChange" defaultValue="only"  ></Group>
   <el-date-picker
			v-model="queryParams.fromDate"
			type="month"
			placeholder="选择月份"
		    @change="changedate"
  />
  </el-space>
	<GlobalTable  ref="globalTable2"
	:tableData="tableData"
	  @loadTable="loadtableData"
	  :inDialog="true"
	  >
	   <template #field>
	      <el-table-column label="图片" width="65" >
			  <template #default="scope">
				  <el-image
				   class="product-img"
				   v-if="scope.row.image"
				   :src="scope.row.image"></el-image>
				   <el-image v-else 
				   :src="$require('empty/noimage40.png')"
				  class="product-img"
				   ></el-image>
			  </template>
		  </el-table-column>
	      <el-table-column  label="SKU/名称"  >
			  <template #default="scope">
				  <div class="text-omit-1">{{scope.row.pname}}</div>
				  <span class="font-extraSmall">{{scope.row.sku}}</span>
			  </template>
		  </el-table-column>
	      <el-table-column  label="店铺/国家"  >
			  <template #default="scope">
				  <div >{{scope.row.groupname}}</div>
				  <span class="font-extraSmall">{{scope.row.marketname}}</span>
			  </template>
		  </el-table-column>
		  <el-table-column label="月份" width="120" prop="byday" >
			  <template #default="scope">
			                    {{dateFormat(scope.row.byday)}}
			  				  </template>
			  </el-table-column>
		  <el-table-column label="费用类型" width="120" prop="itemname" />
		  <el-table-column label="币种" width="120" prop="currency" />
		  <el-table-column label="金额(￥)" width="120" prop="amount" />
		  <el-table-column label="操作" width="100" >
		  				  <template #default="scope">
		                     <el-link type="primary" :underline="false">删除</el-link>
		  				  </template>
		  </el-table-column>
		   </template>
	   </GlobalTable>  
</template>

<script setup>
import { reactive,toRefs,ref } from 'vue';
import financesItemDataApi from '@/api/amazon/finances/financesItemDataApi.js';
import exchangeRateAPI from '@/api/amazon/finances/exchangeRateAPI.js';
import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
import Group from '@/components/header/group.vue';
import {dateFormat} from '@/utils/index.js';
    const globalTable2 =ref()
	  const state =reactive({
		queryParams:{fromDate:new Date()},
		tableData:{
			records:[{
				
			}],
			total:1,
		},
	})
	const {
		tableData,queryParams
	}=toRefs(state)
	function handleQuery(){
		var mydate=state.queryParams.fromDate;
		state.queryParams.endDate=getLastDayOfMonth(mydate);
		state.queryParams.fromDate=getFirstDayOfMonth(mydate);
	    globalTable2.value.loadTable(state.queryParams);
	}
	function getFirstDayOfMonth(date) {
	    let firstDay = new Date(date.getFullYear(), date.getMonth(), 2);
	    return firstDay;
	}
	function getLastDayOfMonth(date) {
	    let lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 1);
	    return lastDay;
	}
	function loadtableData(param){
		param.ftype="other";
		financesItemDataApi.getFinDataMonthList(param).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
	function changedate(dates){
		 state.queryParams.fromDate=dates;
		 handleQuery();
	}
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.market=obj.market;
		
		handleQuery();
	}
	function show(){
	 
	}
	defineExpose({
		show,
	}) 
</script>

<style>
</style>