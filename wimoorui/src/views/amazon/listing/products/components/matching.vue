<template>
	<div class="matchingDailog">
	<el-dialog v-model="matchingVisable" :title="title" destroy-on-close='true' width="800px"  >
	  	
	 <el-input v-model="searchKeywords"  @change="searchConfirm"  placeholder="请输入" class="input-with-select">
	 	<template #prepend>
	 		<el-select v-model="searchtype" @change='searchTypeChange' placeholder="SKU"  
	 			style="width: 110px;"   >
	 			<el-option   label="本地SKU" value="sku">本地SKU</el-option>
	 			<el-option   label="名称" value="name">名称</el-option>
	 		</el-select>
	 	</template>
	 	<template #append>
	 		<el-button @click="searchConfirm">
	 			<el-icon class="ic-cen font-medium">
	 				<search />
	 			</el-icon>
	 </el-button>
	 	</template>
	 </el-input>
	  <el-table :data="tableData" border class="m-t-16">
			<el-table-column prop="image" label="图片" width="65"> 
				<template #default="scope">
					<el-image :src="scope.row.image" width="40" height="40"></el-image>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="名称/SKU"  show-overflow-tooltip>
				<template #default="scope">
					<div class='name'>{{scope.row.name}}</div>
					<div class='sku'>{{scope.row.sku}} </div>
				</template>
			</el-table-column>
		  <el-table-column label="类型" width="90" prop="issfg" >
			  <template #default="scope">
			  	<span v-if="scope.row.issfg=='0'">单独成品</span>
				<span v-if="scope.row.issfg=='1'">组装成品</span>
				<span v-if="scope.row.issfg=='2'">半成品</span>
			  </template>
		  </el-table-column>
		  <el-table-column label="操作" width="90" prop="issfg" >
		  <template #default="scope">
		  		<el-button link type="primary" @click.stop="matchSKU(scope.row.sku)">配对</el-button>
		  	</template>
		  </el-table-column>
	  </el-table>
	</el-dialog>
</div>	
</template>

<script>
	import {ref,reactive,onMounted} from "vue"
	import {Search} from '@element-plus/icons-vue';
	import {ElMessage,ElDivider} from 'element-plus';
	import materialApi from '@/api/erp/material/materialApi.js';
	import productinopt from '@/api/amazon/product/productinoptApi.js';
	export default{
		components:{Search},
		setup(){
			let matchingVisable =ref(false);
			let searchtype=ref("sku");
			let tableData=ref([]);
			let title=ref("产品配对(当前平台SKU:)");
			let searchKeywords=ref("");
			let psku=ref("");
			let pid=ref("");
			onMounted(()=>{
				
			})
			function loadData(msku,pskus,pids){
				//查询本地SKU的信息 materialApi
				var sku="";var name="";
				if(searchtype.value=="sku"){
					name='';
					if(msku){
						sku=msku;
						psku.value=pskus;
						pid.value=pids;
						searchKeywords.value=sku;
					}else{
						sku=searchKeywords.value;
					}
				}else{
					sku='';
					name=searchKeywords.value;
				}
				materialApi.getMaterialByInfo({"sku":sku,"name":name}).then((res)=>{
					tableData.value=res.data;
				});
			}
			function searchTypeChange(){
				loadData();
			}
			function searchConfirm(){
				loadData();
			}
			function matchSKU(sku){
				productinopt.updateOptMsku({"pid":pid.value,"msku":sku}).then((res)=>{
					if(res.data=="isok"){
						ElMessage.success('更新成功！');
						title.value="产品配对(当前平台SKU:"+psku.value+",本地SKU:"+sku+")";
					}else{
						ElMessage.error('更新失败！');
					}
				});
			}
			return{
				//value
				matchingVisable,searchtype,tableData,title,searchKeywords,psku,pid,
				//ref
				
				//function
				loadData,searchTypeChange,searchConfirm,matchSKU,
			}
		}	
	}
</script>

<style>
	.m-t-16{
		margin-top:16px;
	}
</style>
