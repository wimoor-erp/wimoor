<template>
	<el-dialog v-model="visible" class="invheader" title="备货单" top="8vh" width="80%">
		   <div style="padding-bottom:10px"><el-button @click="handleAdd" type="primary">添加商品</el-button></div>
		   <el-space style="padding:20px" wrap  :size="40">
			 <div>订单号：{{number}}</div>  
			 <div>海外仓库：{{warehousename}}</div>  
		   </el-space>
	       <el-table :data="tableData" >
			   <el-table-column label="图片" prop="image" width="70">
			   	<template #default="scope">
			   		<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
			   	</template>
			   </el-table-column>
			  <el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>{{scope.row.name}}</div>
			  			<div class="font-extraSmall">{{scope.row.sku}}</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="仓库" prop="warehousename" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>{{scope.row.warehousename}}</div>
			  		</template>
			  </el-table-column>
			 <el-table-column label="库存" prop="qty" width="120"> </el-table-column>
			 <el-table-column label="箱规" prop="boxnum" width="120">  </el-table-column>
			 <el-table-column label="单箱尺寸" prop="quantity" width="120">
			 	<template #default="scope">
			 		 <div v-if="scope.row.plength">
			 			 {{scope.row.plength}}*{{scope.row.pwidth}}*{{scope.row.pweight}}
			 		 </div>
			 		 <div v-else>--</div> 
			 	</template>
			 </el-table-column>
			 <el-table-column label="单箱体积cm³" prop="quantity" width="120">
			 	<template #default="scope">
			 		 <div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight}}</div>
			 		 <div v-else>--</div> 
			 	 </template>
			 </el-table-column>
			 <el-table-column label="单箱体积m³" prop="quantity" width="120">
			 	<template #default="scope">
			 		<div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight/1000000}}</div> 
			 		<div v-else>--</div> 
			 	 </template>
			 </el-table-column>
			  <el-table-column label="计划备货数量" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-input v-model="scope.row.planamount"></el-input>
			  		</template>
			  </el-table-column>
			  <el-table-column label="总体积" prop="price" show-overflow-tooltip>
			          <template #default="scope">
						  <div v-if="scope.row.planamount&&scope.row.plength">
						     {{parseFloat(scope.row.planamount)*parseFloat(scope.row.plength)*parseFloat(scope.row.pwidth)*parseFloat(scope.row.pweight)/1000000}}
						  </div>
			  			  <div v-else>--</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="操作" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-button link type="primary"   @click="handleDelete(scope.$index)" >删除</el-button>
			  		</template>
			  </el-table-column>
		   </el-table>
		   <div style="float:right;font-weight: 600;padding:20px;margin-bottom:20px" v-if="summaryVolumn">总体积： {{summaryVolumn}}</div>
		   <template #footer>
		   	<span class="dialog-footer">
		   		<el-button @click="visible=false">取消</el-button>
		   		<el-button type="primary" @click="submitForm">确认</el-button>
		   	</span>
		   </template>
	</el-dialog>
			<MaterialDialog  ref = "MaterialRef"   :warehouseid="query.warehouseid"   @getdata="getRows" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs,computed} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import orderApi from "@/api/erp/order/orderApi.js";
	import materialApi from "@/api/erp/material/materialApi.js";
	import planFormApi from "@/api/erp/order/planFormApi.js";
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	const MaterialRef=ref();
	const state =reactive({
		visible:false,
		tableData:[],
		skulist:{},
		number:"",
		warehouseid:"",
		warehousename:"",
		query:{},
	})
	const {
		visible,
		query,
		skulist,
		warehouseid,
		warehousename,
		number,
		tableData,
	}=toRefs(state)
	function getRows(rows){
		if(rows && rows.length>0){
			if(state.tableData && state.tableData.length>0){
				var skus="";
				state.tableData.forEach(function(datas){
					 skus+=(datas.sku)+(",");
				});
				rows.forEach(function(item){
					if(skus.indexOf((item.sku+","))<0){
						state.tableData.push(item);
					}
				});
			}else{
				state.tableData=rows;
			}
		}
	}
	const summaryVolumn = computed(() => {
		var summary=0;
		state.tableData.forEach(row=>{
			 summary=summary+parseFloat(row.planamount)*parseFloat(row.plength)*parseFloat(row.pwidth)*parseFloat(row.pweight)/1000000
		});
		return summary;
	    }
	)
	function getSKUList() {
		    state.skulist={};
			var flag=true;
			state.tableData.forEach(function(item){
				var sku = item.id;
				var amount = item.outamount;
				if(state.skulist[sku]){
					ElMessage.error( "不能重复添加SKU！ ");
					flag=false;
				}
				if (typeof(sku)!="undefined"&&sku!=""&&amount!="") {
					state.skulist[sku]=amount;
				}
			});
			return flag;
	}
	function submitForm(){
		var isok=getSKUList();
		if(isok==true){
			var data={};
			data.warehouseid=state.query.warehouseid;
		    var entry=[];
			state.tableData.forEach(item=>{
				var entryitem={'materialid':item.id,"quantity":item.planamount};
				entry.push(entryitem);
			})
			data.entryList=entry;
			planFormApi.savePlanFrom(data).then((res)=>{
				isok=true;
				if(res.data){
					ElMessage.success( "操作成功！");
					if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
						state.visible=false;
						emit('change');
					}
				}else{
					ElMessage.error(res.data);
				}
			});
		}
	}
	function handleAdd(){
		MaterialRef.value.show();
	}
	function handleDelete(index){
		state.tableData.splice(index,1);
	}
	function show(data,query){
		materialApi.getSerialNumber({"ftype":"MP","isfind":"true"}).then(res=>{
			state.number=res.data;
		});
		state.warehouseid=query.warehouseid;
		state.warehousename=query.warehousename;
		state.visible=true;
		state.tableData=JSON.parse(JSON.stringify(data));
		state.query=query;
		console.log(data,query);
	}
	defineExpose({
		show,
	})
</script>

<style>
	.invheader .el-dialog__body{
		padding-top:0px;
	}
</style>