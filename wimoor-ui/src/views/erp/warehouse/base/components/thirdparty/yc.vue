<template>
	<el-scrollbar class="localoversea">
						  <el-row style="padding-bottom:20px">
							  <div class='rt-btn-group'>
								<el-space>
									<div class="font-extraSmall">更新时间：{{refreshtime}}</div>
									<el-input clearable  placeholder="请输入SKU" v-model="queryParam.sku" @input="handleQueryInv"/>
									<el-button @click="handleSyncInv">同步库存</el-button>
									<el-button @click="handleSyncOrder">同步订单</el-button>
								</el-space>
								</div>
						    </el-row>
							<el-table :data="invlist" style="width: 100%" height="calc(100vh - 200px)">
										  <el-table-column   prop="image" label="图片" width="60" >
										     <template #default="scope">
										  	<el-popover
										  	    placement="top-start"
										  	    :width="200"
										  	    trigger="hover"
										  		v-if="scope.row.image"
										  	  >
										  	    <template #reference>
										  	      <el-image  :src="scope.row.image"   width="40" height="40"  ></el-image>
										  	    </template>
										  		<el-image  :src="scope.row.image"    ></el-image>
										  	  </el-popover>
										  	<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
										    </template>
										  </el-table-column>
										  <el-table-column prop="sku" label="SKU编码"  sortable    >
											 <template #default="scope"> 
											   <div class='name'>{{scope.row.name}}</div>
											   <div class='sku'>{{scope.row.sku}}
											     <copy class="" @click="CopyText(scope.row.sku)" title='复制名称' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
												 </div>
											 </template>
										  </el-table-column>
										  <el-table-column prop="quality" label="状态"   width="120"   />
										  <el-table-column prop="housename" label="仓库名称"    width="180" />
										  <el-table-column label="海外仓库存">
										  <el-table-column prop="quantity" label="总库存"   width="120"  sortable  />
										  <el-table-column prop="available" label="可用库存"   width="120"   sortable />
										  <el-table-column prop="processing" label="处理中库存"   width="120" sortable />
										  <el-table-column prop="inspection" label="验货中库存"   width="120"   sortable />
										  </el-table-column>
										  <el-table-column prop="transportation" label="在途库存"  width="120"    sortable />
										</el-table>	
	  </el-scrollbar>
</template>

<script setup>
	import { onMounted, reactive, ref, toRefs,nextTick } from 'vue';
	import {Copy} from '@icon-park/vue-next';
	import { Plus,Search, Edit, Refresh, Delete,MoreFilled,Setting } from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import CopyText from"@/utils/copy_text.js";
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import thirdpartyWarehouseApi from "@/api/erp/thirdparty/thirdpartyWarehouseApi.js";
	import { useRoute,useRouter } from 'vue-router';
	let router = useRouter();
	const state = reactive({apilist:[],apiid:"",refreshtime:"无",warehouselist:[],defaultActive:1,queryParam:{},invlist:[]});
	const { apilist,apiid,warehouselist,defaultActive,queryParam,invlist,refreshtime} = toRefs(state);
	function query(queryParam){
		state.queryParam=queryParam;
		handleQueryInv();
	}
	function handleQueryInv(){
		state.refreshtime="";
		thirdpartyWarehouseApi.searchStock(state.queryParam).then(res=>{
			 state.invlist=res.data;
			 if(res.data&&res.data.length>0){
			 	  state.refreshtime=res.data[0].refreshtime;
			 }else{
				  state.refreshtime="";
			 }
		});
	}
	function handleSyncOrder(){
		var currentDate = new Date();
		currentDate.setDate(currentDate.getDate() - 3); 
		thirdpartyWarehouseApi.order({"api":state.queryParam.apiid,"createdStartTime":currentDate}).then(res=>{
			 console.log(res);
		});
	}
	function handleSyncInv(){
		thirdpartyWarehouseApi.searchStock({"api":state.queryParam.apiid,"houseid":state.queryParam.houseid,"action":"sync"}).then(res=>{
			 state.invlist=res.data;
			 if(res.data&&res.data.length>0){
				 state.refreshtime=res.data[0].refreshtime;
			 }else{
				  state.refreshtime="";
			 }
		});
	}
	defineExpose({query})
</script>

<style>
</style>