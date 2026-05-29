<template>
	 <el-dialog
	    v-model="dialog.visible"
	    :title="title"
		
	    width="500"
	    :before-close="handleClose"
	  >
	  <el-form label-width="auto">
	   <el-form-item label="名 称">
	   	<el-input v-model="supplier.name" placeholder="填写供应商名称"></el-input>
	   </el-form-item>
	   <el-form-item label="地 址">
	   	 <el-input v-model="supplier.address" placeholder="填写地址信息"></el-input>
	   </el-form-item>
	   <el-form-item label="手机号/账号">
	   	 <el-input v-model="supplier.mobile" placeholder="填写手机号"></el-input>
	   </el-form-item>
	   <el-form-item label="密 码">
	   	 <el-input v-model="supplier.password"  type="password" show-password  placeholder="填写密码"></el-input>
	   </el-form-item>
	   <el-form-item label="联系人">
	   	 <el-input v-model="supplier.contact" placeholder="填写联系人"></el-input>
	   </el-form-item>
	   </el-form>
	    <template #footer>
	      <div class="dialog-footer">
			   <el-button @click="supplier={},title='新增'"  >重置</el-button>
			   <el-button @click="saveSupplier"   type="primary">{{title}}</el-button>
	      </div>
	    </template>
	  </el-dialog>
	
</template>

<script setup>
		import {ref,reactive,toRefs,onMounted,computed} from "vue"
		import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
		import {formatFloat} from '@/utils/index.js';
		import {Plus,Edit,Delete} from '@element-plus/icons-vue'
	    import {ElMessage } from 'element-plus'
		import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
		import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
		import orderApi from '@/api/quote/orderApi.js';
		import supplierApi from '@/api/quote/supplierApi.js';
		const emit = defineEmits(['change']);
		const state = reactive({
		  // 弹窗属性
		  dialog: { visible: false }  ,
		  supplier:{},
		  token:"",
		  title:"",
		  submitloading:false,
		});
		const {
		  dialog,
		  supplier,
		  title,
		  token,
		  submitloading,
		} = toRefs(state);
  
		function show(token,supplier,title){
			state.title=title;
			state.supplier=  JSON.parse(JSON.stringify(supplier));
			state.token=token;
			state.dialog.visible=true;
		}
		defineExpose({show});
 
	function saveSupplier(){
			 if(state.title=="保存"){
				 supplierApi.updatesupplier(state.supplier).then((res)=>{
				 	 ElMessage.success("修改供应商成功!");
				 	 state.supplier={};
				 	 state.title="新增";
				 	 emit("change");
					 state.dialog.visible=false;
				 });
			 }else{
				supplierApi.addsupplier(state.token,state.supplier).then((res)=>{
					 ElMessage.success("添加供应商成功!");
					 state.supplier={};
					 state.title="新增";
					 emit("change");
					 state.dialog.visible=false;
				});
			 }
	}
</script>

<style>
</style>