<template>
	<el-form-item >
		<div class="flex-center" >
			<el-tabs v-model="activeName" type="border-card" class="p-l">
			<el-tab-pane label="排除品牌" name="asinBrandSameAs">
				<el-input placeholder="搜索品牌" v-model="brand">
					 <template #suffix>
					  <el-icon><Search @click.stop="loadBrand" /></el-icon>
					 </template>
				</el-input>
				<el-table class="m-t-8"   v-loading="loading"  :data="suggestlist" height="368px">
					 <el-table-column label="您的品牌" prop="name"/>
					 <el-table-column label="操作" width="110" >
						 <template #header>
							 <el-button   type="primary" size="small" link>全部添加</el-button>
						 </template>
						 <template #default="scope">
							 <el-button  @click="addBrand(scope.row)">添加</el-button>
						 </template>
					 </el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="排除ASIN" name="asinSameAs">
		  <el-input
		    v-model="asins"
		    type="textarea"
		 	:autosize="{ minRows:8, maxRows:8}"
		    placeholder="输入ASIN,用逗号隔开"
		  />
	      	<el-button class="m-t-8" @click="addAsin">添加</el-button> 
			</el-tab-pane>
			</el-tabs>
			<div class="p-r">
				 <span class="font-extraSmall m-l-8">已否定 <span> {{tableData?.length}} </span> 个商品</span>
				<el-table  :data="tableData" height="448px">
						  <el-table-column label="否定目标" prop="name">
						  </el-table-column>
							  <el-table-column label="操作" width="60">
								  <template #header>
									  <el-button type="primary" class="font-small" link @click="clearAsin">清空</el-button>
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
	import advTargetApi from '@/api/amazon/advertisement/report/advTargetApi.js';
	import {splitStr} from '@/views/amazon/advertisement/manager/components/javascript/common.js';
	const emit = defineEmits(['change',]);
	const state = reactive({
		brand:'',
		asins:'',
		queryParams:{},
		loading:false,
		activeName:"asinBrandSameAs",
		addList:[],
		tableData:[],
		suggestlist:[],
	});
	
	const {
		brand,
		asins,
		loading,
		queryParams,
		activeName,
		addList,
		tableData,
		suggestlist,
	}=toRefs(state)
 
	function show(params){
		state.queryParams=params;
		 loadBrand();
	}
	defineExpose({
		show,getData,
	})
	function clearAsin(){
		state.addList=[];
		state.tableData=[];
		emit("change",getData());
	}
	function loadBrand(){
		state.loading=true;
		advTargetApi.getNegativeTargetsBrandsRecommendations(state.queryParams.profileid,state.queryParams.campaignType,{keyword:state.brand}).then(res=>{
			var data=JSON.parse(res.data);
			state.suggestlist=data.brands;
			state.loading=false;
		})
	}
	function deleteRow(index){
		 state.tableData.splice(index,1);
	     state.addList.splice(index,1);
		 emit("change",getData());
	}
	function addBrand(item){
		if(state.addList.includes(item)==false){
			var row={"type":state.activeName,"value":item.brandRefinementId,"name":item.name};
			state.tableData.push(row);
			state.addList.push(item);
			emit("change",getData());
		}
	}
	function getData(){
		var list=[];
		state.tableData.forEach(item=>{
			var row={"type":item.type,"value":item.value};
			list.push(row);
		});
		return list;
	}
	function addAsin(){
		if(state.asins){
			var list=splitStr(state.asins);
			list.forEach(item=>{
				if(item && item.length==10){
					if(state.addList && state.addList.length>0){
						if(state.addList.includes(item)==false){
							var row={"type":state.activeName,"value":item,"name":item};
							state.tableData.push(row);
							state.addList.push(item);
						}
					}else{
						var row={"type":state.activeName,"value":item,"name":item};
						state.tableData.push(row);
						state.addList.push(item);
					}
				}
			});
			emit("change",getData());
		}
	}
</script>

<style scoped>
	.p-l{
		width:450px;
		height:480px;
	}
	.p-r{
		width:450px;
		height:480px;
		background-color: var(--el-color-info-light-7);
	}
	.m-l-8{
		margin-left:8px;
	}
</style>