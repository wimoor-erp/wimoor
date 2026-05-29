<!-- 添加仓库时，共用的仓库弹窗 -->

<template>
	<div class="thirdparty-bind-dailog">
		<el-dialog v-model="dialog.visible" title="选择仓库" top="6vh" width="60%"  :before-close="handleClose">
			<div class="con-header"  style="margin-bottom:20px">
				<el-alert  v-if="queryParams.thirdpartyWarehouseId"  type="info" effect="dark"  :title="row.name" :description="row.ext"  @close="closeWarehouse" show-icon size="large" >
					</el-alert>
			</div>
			<div class="con-header">   
			 
				
				<el-row>
					<Warehouse
					 @changeware="changeOverseaWarehouse" 
					 warehouseType="oversea_usable"
					 defaultText="全部海外仓库"
					:isform="true" /> 
					<el-button @click="handleAdd" v-if="queryParams.thirdpartyWarehouseId"> 添加</el-button>
					<el-button @click="handleWarehouse" v-else> 指定平台仓库</el-button>
				</el-row>
			</div>
			 <el-table
			   highlight-current-row
			   :data="tableData"
			   v-loading="loading"
			    height="calc(100vh - 370px)" 
			   @selection-change="handleSelectionChange"
			   @current-change="currentChange"
			   border
			 >
			   <el-table-column label="平台海外仓" prop="thirdpartyname" width="220" />
			   <el-table-column label="本地海外仓" prop="localname" width="220" />
			   <el-table-column label="操作"   > 
			     <template #default="scope">
				   <el-button @click="handleDelete(scope.row)" type="danger">删除</el-button>
			     </template>
			   </el-table-column>
			 </el-table>
		</el-dialog>
	</div>
</template>

<script setup>
	// Vue依赖
	import { onMounted, reactive, ref, toRefs } from 'vue';
	// API依赖
	import thirdpartyWarehouseApi from '@/api/erp/thirdparty/thirdpartyWarehouseApi.js';
	// 组件依赖
	import { Search, Plus, Edit, Refresh, Delete ,ArrowDown,Close} from '@element-plus/icons-vue';
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
    const state= reactive({ dialog: {visible: false }  ,
							search:'',
							tableData:[],
							queryParams:{ftype:"self_usable"},
							loading:false,
							row:{}
			              });
								 
    const { 
			loading,
			tableData,
			queryParams,
			row,
			search,
			dialog,
		  } = toRefs(state);
	 const emit = defineEmits(['change']);
	 
			function handleQuery() {
				state.loading=true;
				thirdpartyWarehouseApi.bindlist({"thirdpartyWarehouseId":state.queryParams.thirdpartyWarehouseId}).then(res=>{
					state.loading=false;
					state.tableData=res.data;
				})
			}
			function currentChange(row){
				state.warehouse =row;
			}
            function handleClose(){
				state.dialog.visible = false;
				emit("change");
			}
			function changeOverseaWarehouse(id,type,warehouse){
				if(id){
					state.queryParams.localWarehouseId=id;
				}else{
					state.queryParams.localWarehouseId=null;
				}
				handleQuery();
			}
				
			function handleWarehouse(){
				state.queryParams.thirdpartyWarehouseId=state.row.id;
					handleQuery();
			}
			function handleAdd(){
				thirdpartyWarehouseApi.savebind({"thirdpartyWarehouseId":state.row.id,"localWarehouseId":state.queryParams.localWarehouseId}).then(res=>{
					handleQuery();
				})
			}
			function handleDelete(row) { 
				ElMessageBox.confirm(
				   '您确定要删除该绑定信息吗?',
				   '删除',
				   {
				     confirmButtonText: '确定',
				     cancelButtonText: '取消',
				     type: 'warning',
				   }
				 ).then(() => {
							thirdpartyWarehouseApi.deletebind(row).then(res=>{
								handleQuery();
							})
				          }).catch(() => {
								 ElMessage.info( '取消操作');
					   })
					
			}
			function show(mrow){
				state.dialog.visible=true;
				state.queryParams.thirdpartyWarehouseId=mrow.id;
				state.row=mrow;
				handleQuery();
			}
			function closeWarehouse(){
				state.queryParams.thirdpartyWarehouseId=null;
				handleQuery();
			}
		 function handleSelectionChange(selection) {
		     state.ids = selection.map((item) => item.id);
		 }
		 defineExpose({show})
		 onMounted(()=>{
			 
		 })
</script>

<style>
 
</style>
