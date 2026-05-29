<template>
	<el-dialog
	   v-model="dialogVisible"
	   title="维护本地菜鸟云打印服务器IP："
	   width="500"
	   :before-close="handleClose"
	 >
	 <el-form   label-width="auto" style="max-width: 600px">
		 <el-form-item label="仓位地址">
		 			 <el-select v-model="addressid"  @change="loadIP"  placeholder="请选择仓位地址">
		 			    <el-option v-for="address in addressOptions" :label="address.name" :value="address.id" />
		 			 </el-select>
		 </el-form-item>
		 
	     <el-form-item label="打印机电脑IP">
	        <el-input v-model="ip"></el-input>
	     </el-form-item>
		 
	     <el-form-item label="打印纸张类型">
	       <el-select v-model="paper">
	       	<el-option   value="100" label="100*100"></el-option>
	       	<el-option    value="80"  label="80*60"></el-option>
	       </el-select>
	     </el-form-item>
	   </el-form>
	  
		
	   <template #footer>
	     <div class="dialog-footer">
	       <el-button @click="dialogVisible = false">取消</el-button>
	       <el-button type="primary" @click="handleSubmit">
	         确认
	       </el-button>
	     </div>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {reactive,toRefs}from"vue";
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import warehouseAddressApi from '@/api/erp/warehouse/warehouseAddressApi.js';
	const state = reactive({
		dialogVisible:false,
		ip:"",
		paper:"100",
		addressid:"",
		addressOptions:[]
	})
	const {
		dialogVisible,
		ip,
		paper,
		addressid,
		addressOptions
	}=toRefs(state)
	function loadIP(){
		purchaselistApi.getPrintIpByAddress({'addressid':state.addressid}).then(res=>{
			if(res.data){
				 state.ip=res.data.ip;
				 if(res.data.paper){
					 state.paper=res.data.paper;
				 }
			}else{
				state.ip="127.0.0.1";
			}
		   state.dialogVisible=true;
		});
	}
	function show(){
		warehouseAddressApi.listWarehouseAddress({pagesize:10000, currentpage:1}).then(({data})=>{
			   state.addressOptions=data.records;
			   state.addressid=data.records[0].id;
			   loadIP();
		});
		
	}
 
	function handleSubmit(){
		purchaselistApi.setPrintIp({"ip":state.ip,"paper":state.paper,"addressid":state.addressid}).then(res=>{
		    state.dialogVisible=false;
			ElMessage.success('操作成功');
		});
	}
	 
	defineExpose({
		show,
	})
</script>

<style>
</style>