<template>
	<el-dialog title="发货库存不足列表" 
	           v-model="dialog.visible"
	           :append-to-body="true" 
			   top="3vh"
			   width="80%">
			<el-table border :data="tableData"  height="calc(100vh - 350px)">
				 <el-table-column prop="sku" label="子SKU信息"   >
				    <template #default="scope">
						<el-space>
				           <img :src="scope.row.simage"   width="100" height="100"  />
						   <div>
							   <div class='name'>{{scope.row.sname}}</div>
							   <el-space>
							    <el-link type="primary" class="font-small">{{scope.row.ssku}} </el-link>
							   </el-space>
							   <div>
								   库存：{{scope.row.squantity}}
							   </div>
						   </div>
						</el-space>
				   </template>
				 </el-table-column>
				 <el-table-column prop="msku" label="父SKU"   show-overflow-tooltip>
				    <template #default="scope">
						<el-card v-for="item in scope.row.mainlist" style="margin-bottom:10px;">
							<el-space>
							   <img  :src="item.mimage"   width="100" height="100"  />
							   <div>
								   <div class='name'>{{item.mname}}</div>
								   <el-space>
								    <el-link type="primary" class="font-small">{{item.msku}} </el-link>
								   </el-space>
								   <div>
									   库存：{{item.mquantity}}
								   </div>
								   <div>
								   	   计划：{{item.plan.amount}} （<span v-if="item.plan.formqty" class="font-extraSmall" title="含未审核发货单数量">未审核[{{item.plan.formnumber}}]：{{item.plan.formqty}}</span>）对应关系1-{{item.subnumber}} ,需求量：{{item.plan.amount*item.subnumber}}
								   </div>
								   
							   </div>
							</el-space>
						</el-card>
				      
					  
				   </template>
				 </el-table-column>
				  
			</el-table>
			<template #footer>
				<el-button @click="dialog.visible=false">关闭</el-button>
				<el-button @click="handleAdd" type="primary" >确认发货</el-button>
			</template>	
		</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted}from"vue"
	import {Close} from '@element-plus/icons-vue';
	import AssemblyDialog from "@/views/erp/components/assembly_dialog.vue";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {sublit} from "@/api/erp/assembly/assemblyApi.js";
	const state=reactive({
		  dialog:{visible:false},tableData:[] ,warehouseid:"",
	})
	const emit = defineEmits(['change']);
	const {
	   dialog ,tableData,loading,warehouseid,
	} = toRefs(state);
	function show(warehouseid,data){
		state.dialog.visible=true;
		state.warehouseid=warehouseid;
		state.tableData=data;
	}
	function getAssembliyList(row){
		if(!row["assemblyList"]){
			row.assloading=true;
			sublit({materialid:row.id,warehouseid:state.warehouseid}).then(res=>{
				row.assemblyList=res.data;
				row.assloading=false;
			});
		}
	}
	function handleAdd(){
		state.dialog.visible=false;
		emit("change");
	}
	defineExpose({
	  show
	})
</script>

<style>
	.badShip{
		 background-color: #fff3ec;
	}
</style>