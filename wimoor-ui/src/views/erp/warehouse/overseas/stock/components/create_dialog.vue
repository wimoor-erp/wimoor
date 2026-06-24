<template>
	<el-dialog v-model="visible" class="invheader" title="盘点单" top="8vh" width="80%">
		<template #title>
			盘点单 
		</template>
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
			  <el-table-column label="可用库存" prop="fulfillable" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>{{scope.row.fulfillable}}</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="采购成本" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>¥{{scope.row.price}}</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="矫正库存" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-input v-model="scope.row.amount"></el-input>
			  		</template>
			  </el-table-column>
			  <el-table-column label="操作" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-button link type="primary"   @click="handleDelete(scope.$index)" >删除</el-button>
			  		</template>
			  </el-table-column>
		   </el-table>
		   <template #footer>
		   	<span class="dialog-footer">
		   		<el-button @click="visible=false">取消</el-button>
		   		<el-button type="primary" @click="submitForm">确认</el-button>
		   	</span>
		   </template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import stocktakingApi from '@/api/erp/inventory/stocktakingApi.js';
	const emit = defineEmits(['change']);
	const state =reactive({
		visible:false,
		tableData:[],
		skulist:{},
		query:{},
	})
	const {
		visible,
		query,
		skulist,
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
		var param={"id":0,"ftype":1};
		param.warehouselist=[{stocktakingid:"",warehouseid:state.query.warehouseid}];
		stocktakingApi.startAction(param).then(async (res)=>{
			 if(res.data && res.data.isok==true){
				var  id=res.data.id;
				 for(var i=0;i<=state.tableData.length;i++){
					 var row=state.tableData[i];
					 if(row&& parseInt(row.amount)>=0){
					 	var data={};
					 	data.id=id;
					 	data.warehouseid=state.query.warehouseid;
					 	data.materialid=row.id;
					 	data.amount=row.amount;
					    data.itemid="";
					 	var fulfillable=row["fulfillable"]?row.fulfillable:0;
					 	var outbound=row["outbound"]?row.outbound:0;
					 	var overamount =parseInt(row.amount)- parseInt(fulfillable+outbound);
					 	data.overamount=overamount;
					 	await stocktakingApi.saveItem(data);
					 }
				 }
				 stocktakingApi.endAction(res.data).then((res)=>{
				 	if(res.data.isok==true){
				 		ElMessage.success(  "盘点完成！");
						state.visible=false;
				 		 emit('change');
				 	}
				 });
			 }
		});
	
	 
		
	
	}
	function handleAdd(){
		MaterialRef.value.show();
	}
	function handleDelete(index){
		state.tableData.splice(index,1);
	}
	function show(data,query){
		state.visible=true;
		state.tableData=JSON.parse(JSON.stringify(data));
		state.query=query;
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