<template>
	<el-form-item>
		<div class="flex-center">
		 <el-tabs type="border-card" class="p-l"  v-if="isEdit" >
		    <el-tab-pane label="搜索">
				<el-input  v-model="queryParams.search" placeholder="请输入" clearable @input="handleQuery" class="input-with-select" >
				   <template #prepend>
				     <el-select v-model="queryParams.searchType" @change='searchTypeChange' placeholder="SKU"  style="width:90px">
				       <el-option label="SKU" value="sku"></el-option>
				       <el-option label="ASIN" value="asin"></el-option>
				       <el-option label="产品名称" value="name"></el-option>
				     </el-select>
				   </template>
				   <template #append>
				     <el-button @click="handleQuery">
				        <el-icon class="font-medium ic-cen">
				         <search />
				      </el-icon>
				     </el-button>
				   </template>
				 </el-input>
	            <GlobalTable  ref="globalTable" :tableData="tableData"  @loadTable="loadTableData" :stripe="true" 
				 height="calc(100vh*0.4)"   :inDialog="true" 
				 style="width: 100%;margin-bottom:16px;">
	              <template #field>
					 <el-table-column label="商品信息"  show-overflow-tooltip>
					  <template #default="scope">
						  <div class="flex-center">
						  	   <el-image v-if="scope.row.image" :src="scope.row.image" class="product-img"   ></el-image>
						  	   <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img" ></el-image>
						  	   <div class="m-l-8">
						  		    <div class="text-omit-1">{{scope.row.pname}}</div>
						  		   <p class="sku">{{scope.row.sku}}   </p>
						  		   <p class="font-extraSmall"><span>ASIN:{{scope.row.asin}} </span>
								   <el-divider direction="vertical"></el-divider>
								   <span>{{getCurrencyMark(currency)}} {{scope.row.itemprice}}</span></p>
						  	   </div>
						  </div>
					  </template>
					 </el-table-column>
					 <el-table-column label="操作" width="110" >
						 <template #header>
							 <el-link type="primary" class="font-small" :underline="false">此页全部添加</el-link>
						 </template>
						 <template #default="scope">
							 <el-button v-if="!scope.row.disabled" @click="handleAdd(scope.row)">添加</el-button>
							 <el-button v-else disabled>已添加</el-button>
						 </template>
					 </el-table-column>
					 	 </template>
				 </GlobalTable>
			</el-tab-pane>
		    <el-tab-pane label="手动输入">
				<el-input
				    v-model="asinlist"
				    type="textarea"
					:autosize="{ minRows:8, maxRows:8}"
				    placeholder="输入ASIN"
				  />
				<el-button class="m-t-8" type="primary" @click="addAsin">添加</el-button>  
			</el-tab-pane>
		  </el-tabs>
		  <div class="p-r">
			 <el-table :data="addedList"  :height="isEdit==true?'560px':'100%'">
			 					 <el-table-column label="商品信息" >
									<template #header>
											<span class="font-small">已添加 <span> {{addedList.length}} </span> 商品</span>
									</template> 
			 					  <template #default="scope">
			 						  <div class="flex-center">
			 						  	   <el-image v-if="scope.row.image" :src="scope.row.image" class="product-img"   ></el-image>
			 						  	   <el-image v-else :src="$require('empty/noimage40.png')"  class="product-img" ></el-image>
			 						  	   <div class="m-l-8">
			 						  		    <div class="text-omit-1">{{scope.row.name}}</div>
			 						  		   <p class="sku">{{scope.row.sku}}   </p>
			 						  		   <p class="font-extraSmall"><span>ASIN:{{scope.row.asin}}</span>
			 								   <el-divider direction="vertical"></el-divider>
			 								   <span>{{getCurrencyMark(currency)}} {{scope.row.price}}</span></p>
			 						  	   </div>
			 						  </div>
			 					  </template>
			 					 </el-table-column>
			 					 <el-table-column label="操作" width="60" >
			 						 <template #header>
			 							 <el-link type="primary" class="font-small" @click="deleteAll" :underline="false">清空</el-link>
			 						 </template>
			 			 <template #default="scope">
			 							 <el-button link type="primary" @click="deleteRow(scope.$index)">删除</el-button>
			 			 </template>
			 	 </el-table-column>
			 </el-table>
		  </div>
		  </div>
	</el-form-item>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs,watch} from 'vue';
	import {Search,} from '@element-plus/icons-vue';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	let props = defineProps({  isEdit:"", })
	const { isEdit } = toRefs(props);
	import {getCurrencyMark,formatFloat} from '@/utils/index.js';
	const emit = defineEmits(['change',]);
	const globalTable=ref();
	const state = reactive({
		tableData:{records:[],total:0},
		addedList:[],
		queryParams:{search:''},
		currency:'$',
		asinlist:null,
	})
	const{
		tableData,
		addedList,
		queryParams,
		currency,
		asinlist,
	}=toRefs(state)
	
	function deleteAll(){
		state.addedList=[];
		state.tableData.records.forEach(item=>{
			 item.disabled=false;
		});
		emit("change",state.addedList);
	}
	function deleteRow(index){
		var row=state.addedList[index];
		 state.tableData.records.forEach(item=>{
			 if(row.asin==item.asin){
				 item.disabled=false;
			 }
		 });
	     state.addedList.splice(index,1);
		 emit("change",state.addedList);
	}
	function handleAdd(row){
		row.disabled = true
		row.price=row.itemprice;
		state.addedList.push(row);
		emit("change",state.addedList);
	}
	function handleQuery(){
		if(props.isEdit){
		   globalTable.value.loadTable(state.queryParams);
		}
	}
	function loadTableData(params){
		productinfoApi.getProductInfoList(params).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total
		})
	}
	function show(params){
		state.queryParams=params;
		state.currency=params.currency;
		handleQuery();
		if(!props.isEdit){
			loadSimpleList();
		}
	}
	function loadSimpleList(){
		state.queryParams.sellerId=state.queryParams.sellerid;
		productinfoApi.getProductInfoSimple(state.queryParams).then((res)=>{
			state.addedList=res.data;
			emit("change",state.addedList);
		});
	}
	function addAsin(){
		var list=splitStr(state.asinlist);
		list.forEach(item=>{
			if(item.length==10){
				state.addedList.push({"asin":item});
			}
		});
	}
	defineExpose({
		show,
	})
	 
</script>

<style scoped>
	.m-l-8{
		margin-left:8px;
	}
	.p-l{
		width:450px;
		height:560px;
	}
	.p-r{
		width:450px;
		background-color: var(--el-color-info-light-7);
	}
	.input-with-select{
		margin-bottom:16px;
	}
</style>