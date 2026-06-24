<template>
	<el-dialog
	    v-model="dialogVisible"
	    title="添加箱标对应收货地址"
	    width="60%"
	    :before-close="handleClose"
	  >
	  <div class="text-red" style="margin-top:-20px;margin-bottom:20px">请注意，
	  只有当箱标地址与系统获取的收货地址不一致时，且您希望使用箱标地址，才需要使用此功能。注意：当不一致时两个地址亚马逊都可以正常收货</div>
	    <el-table :data="tableData" @current-change="handleCurrentChange" highlight-current-row style="width: 100%">
	        <el-table-column prop="code" label="编码" width="80"/>
	        <el-table-column prop="name" label="名称" />
	        <el-table-column prop="addressLine1" label="地址行1" />
			<el-table-column prop="addressLine2" label="地址行2" />
			<el-table-column prop="city" label="城市" />
			<el-table-column prop="postalCode" label="邮编" width="120"/>
	      </el-table>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="dialogVisible = false">取消</el-button>
	        <el-button :disabled="!currentRow" type="primary" @click="handleSubmit">
	          使用箱标地址
	        </el-button>
	      </div>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import { ref ,reactive,toRefs} from 'vue';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	const emit = defineEmits(["change"]);
	let state =reactive({
		dialogVisible:false,
		tableData:[],
		currentRow:null,
	})
	let{dialogVisible,tableData,currentRow
		}=toRefs(state);
	function handleClose(){
		state.dialogVisible=false;
	}
	 
	function handleCurrentChange (row){
		state.currentRow=row;
	}
	function handleSubmit(){
		ElMessageBox.confirm(
		   '您确认要使用箱标地址吗?',
		   '注意',
		   {
		     confirmButtonText: '确定',
		     cancelButtonText: '取消',
		     type: 'warning',
		   }
		 )
		   .then(() => {
			shipmentPlacementApi.saveDestinationBox({"shipmentid":state.shipmentid,"destinationBox":state.currentRow.code}).then(res=>{
				 state.dialogVisible = false;
				 ElMessage.success("操作成功");
				 emit("change");
			})
		     
		   })
		   .catch(() => {
		      
		   })
		
	}
	function show(shipmentid,countryCode){
		state.dialogVisible=true;
		state.shipmentid=shipmentid;
		shipmentPlacementApi.listDestination({"country":countryCode}).then(res=>{
			state.tableData=res.data;
		});
	}
	 defineExpose({show});
	 
</script>

<style>
</style>