<template>
	<el-dialog
	title="商品详情"
	v-model="visiable"
	>
			<div class="con-body"  >
			  <el-table :data="planData.itemlist" stripe   :row-class-name="tableRowClassName">
				  <el-table-column label="图片" width="65"  >
					  <template #default="scope">
						  <el-image :src="scope.row.image" class="product-img"></el-image>
					  </template>
				  </el-table-column>
				  <el-table-column label="名称/SKU" width="220">
					  <template #default="scope">
						 <div class='name  text-omit-1'>{{scope.row.name}}</div>
						 <div class='sku'>{{scope.row.sku}}</div>
					  </template>
				  </el-table-column>
				  <el-table-column label="ASIN / FNSKU"  width="120">
					  <template #default="scope">
						 {{scope.row.asin}}
						 <div class="font-extraSmall">
							 {{scope.row.fnsku}}
						 </div>
					  </template>
				  </el-table-column>
				  <el-table-column label="发货数量" width="80">
					  <template #default="scope">
						  {{scope.row.confirmQuantity}}
					  </template>
				  </el-table-column>
				  <el-table-column  label="预处理" >
					  <template #default="scope">
						  <el-space>
							  <div ><span class="font-extraSmall" >产品类型</span><el-button v-if="preloading" loading link></el-button>
								  <el-select  style="width:130px" :disabled="scope.row.disable"  v-model="scope.row.prepCategory">
								  	  <el-option v-for="item in prepCategoryList" :label="item.name" :value="item.value">
										  <div>
											  <div>{{item.name}} <span class="font-extraSmall">----{{item.description}}</span></div>
										  </div>
									  </el-option>
								  </el-select>
							  </div>
						  <div  ><span class="font-extraSmall">处理方式</span><el-button v-if="preloading" loading link></el-button>
						  <el-select :disabled="scope.row.disable" style="width:130px"  v-model="scope.row.prepTypes"     multiple>
						  	  <el-option v-for="item in prepTypeList" :label="item.name" :value="item.value">
								  <div>
								     <div >{{item.name}} <span class="font-extraSmall">----{{item.description}}</span></div>
								  </div>
							  </el-option>
						  </el-select>
						  </div>
						  </el-space>
						  <div v-if="scope.row.iswarning" class="text-red">需要编辑</div>
					  </template>
				  </el-table-column>
				  <el-table-column label="操作" width="100">
							  <template #default="scope">
								  <el-button v-if="scope.row.disable" @click="scope.row.disable=false" size="small"  >提交</el-button>
								  <el-button v-else @click="scope.row.disable=true" type="warning"  size="small"  >不提交</el-button>
							  </template>
				  </el-table-column>
			  </el-table>
			</div>
		<template #footer>
			<el-button @click="submit" v-loading="submitloading" type="primary">提交</el-button>
			<el-button @click="visiable=false" >关闭</el-button>
		</template>
		</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick,computed } from 'vue';
	import { ElMessage,ElMessageBox } from 'element-plus';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import PrepCategory from "@/model/erp/ship/prepCategory.json";
	import prepType from "@/model/erp/ship/prepType.json";
	let state =reactive({
		visiable:false,
		planData:{},
		preloading:false,
		prepTypeList:prepType,
		submitloading:false,
		prepCategoryList:PrepCategory,
	})
	let{ visiable,planData,prepTypeList,prepCategoryList,submitloading,preloading}=toRefs(state);
	function submit(){
		state.submitloading=true;
		var param=[];
		var data=JSON.parse(JSON.stringify(state.planData));
		data.itemlist.forEach(item=>{
				item.prepOwner=item.prepCategory;
				item.typename=item.prepTypes.toString();
				if(!item.disable){
				   param.push(item);
				}
		})
		data.itemlist=param; 
		shipmentplanApi.setPrepDetails(data).then(res=>{
			ElMessage.success("提交成功");
			state.submitloading=false;
			state.visiable=false;
		}).catch(e=>{
			state.submitloading=false;
		})
	}
	 function tableRowClassName(scope){
	   if (scope.row.disable ) {
	     return 'disable-row'
	   } else  {
	     return ''
	   }
 
	 }
	function show(planData){
		state.planData=JSON.parse(JSON.stringify(planData));
		var data={id:state.planData.id,"marketplaceid":state.planData.marketplaceid,"amazonauthid":state.planData.amazonauthid};
		state.submitloading=false;
		state.preloading=true;
		shipmentplanApi.listPrepDetails(data).then(res=>{
				state.preloading=false;
			if(res.data&&res.data.mskuPrepDetails){
				var mapSKU={};
				res.data.mskuPrepDetails.forEach(item=>{
					mapSKU[item.msku]=item;
				})
				state.planData.itemlist.forEach(item=>{
					if(mapSKU[item.sku]){
						item.prepDetail=mapSKU[item.sku];
						var types=[];
						item.prepDetail.prepTypes.forEach(item=>{
							types.push("ITEM_"+item.toUpperCase())
						})
						item.prepCategory=item.prepDetail.prepCategory;
						item.prepTypes=types;
						item.iswarning=false;
					}else{
						item.prepCategory="NONE";
						item.iswarning=true;
						item.prepTypes=["ITEM_NO_PREP"];
					}
				})
				
			}
		})
		state.visiable=true;
	}
	
	 defineExpose({ show })
</script>

<style>
	.disable-row{
		  filter: grayscale(100%);
		--el-table-tr-bg-color: var(--el-color-warning-light-9);
	}
</style>