<template>
	<el-dialog
	   v-model="dialog.visiable"
	   title="添加仓位"
	   top="8vh"
	   width="60%"
	   v-loading="dialog.loading"
	   @open="handleLoading"
	 >
	<el-form ref="dataFormRef" :model="formData"
	  :rules="rules"
	 label-width="120px">
		<el-form-item label="仓库">
		  <el-select v-model="formData.parentid" :disabled="formData.disabled" placeholder="请选择仓位地址">
		     <el-option v-for="warehouse in warehouseOptions" :label="warehouse.name" :value="warehouse.id" />
		  </el-select>
		</el-form-item>
	    <el-form-item label="仓位编码">
	      <span>{{formData.number}}</span>
	    </el-form-item>
	    <el-form-item label="仓位名称"  prop="name">
	      <el-input v-model="formData.name" :disabled="formData.disabled"/>
	    </el-form-item>
		<el-form-item v-if="props.ftype!='oversea'" label="地址">
			<el-space>
		  <el-select v-model="formData.addressid" :disabled="formData.disabled" placeholder="请选择仓位地址">
		     <el-option v-for="address in addressOptions" :label="address.name" :value="address.id" />
		  </el-select>
		 <el-button @click="editWarehouseAdress"  type="primary" link>
			 <el-icon  class="font-medium" ><edit/></el-icon>
		 </el-button>
		 <div class="font-extraSmall">(仅用于库位绑定)</div>
		 </el-space>
		</el-form-item>
		<el-form-item label="排序号">
			<el-space>
			<el-input-number :controls="false" v-model="formData.findex" :disabled="formData.disabled" :step="1" />
			</el-space>
		</el-form-item>
		<el-form-item label="国家" v-if="props.ftype=='oversea'">
			<el-select   v-model="formData.country"    placeholder="国家" @change="marketChange">
			      <el-option  v-for="item in marketList"   :key="item.market"  :label="item.name" :value="item.market"   >
			      </el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="店铺" v-if="props.ftype=='oversea'">
			<el-select v-model="formData.groupid"     placeholder="店铺" @change="groupChange">
				  <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
				  </el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="库存支持负数">
			  <el-switch
			    v-model="formData.ishungry"
				:disabled="formData.disabled"
			    inline-prompt
			    active-text="是"
			    inactive-text="否"
			  />
	    </el-form-item>
		<el-form-item label="默认仓位">
			  <el-switch
			    v-model="formData.isdefault"
			    inline-prompt
				:disabled="formData.disabled"
			    active-text="是"
			    inactive-text="否"
			  />
	    </el-form-item>
		<el-form-item label="收货地址">
			 <el-input v-model="formData.address"  :disabled="formData.disabled" type="textarea" />
		</el-form-item>
		<el-form-item label="备注">
			 <el-input v-model="formData.remark"  :disabled="formData.disabled" type="textarea" />
	    </el-form-item>
	  </el-form>
	  <template #footer>
		  <el-button @click="dialog.visiable=false">取消</el-button>
		  <el-button v-if="formData.disabled" type="primary" @click="formData.disabled=false">修改</el-button>
		  <el-button v-else type="primary" @click="submitForm">确认</el-button>
	  </template>
	  </el-dialog>
</template>

<script setup>
	import { onMounted, reactive, ref, toRefs,nextTick } from 'vue';
	import {Edit,Plus } from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox } from 'element-plus'
	import {useRouter} from "vue-router";
	import serialnumberApi from "@/api/erp/common/serialnumberApi.js";
	import warehouseApi from "@/api/erp/warehouse/warehouseApi.js";
	import warehouseAddressApi from '@/api/erp/warehouse/warehouseAddressApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	const emit = defineEmits(['confirm']);
	let props = defineProps({  ftype:"" ,  marketList:[], groupList:[],})
	const{
	  marketList,
	  groupList,
	} = toRefs(props);
	const router = useRouter()
	const dataFormRef=ref();
	const state= reactive({
		formData:{
			adress:'',
			name:'',
			number:'',
			disabled :false,
			country:'',
			groupid:'',
		},
		dialog:{
			visiable:false,
			loading:false,
		},
		 // 表单参数校验
		rules: {
		      name: [{ required: true, message: '仓位名称不能为空', trigger: 'blur' }],
		},
		addressOptions:[],
		warehouseOptions:[],
	})
	const{
	  formData,
	  dialog,
	  rules,
	  addressOptions,
	  warehouseOptions,
	} = toRefs(state);
    async function loadSerialNumber(){
		await  serialnumberApi.getSerialNumber({"ftype":"M",isfind:"true"}).then(res=>{
			state.formData.number=res.data;
		});
	}
	async function loadAddress(){
		await  warehouseAddressApi.listWarehouseAddress({pagesize:10000, currentpage:1}).then(({data})=>{
			   state.addressOptions=data.records;
		});
	}
	async function loadWarehouse(){
		await warehouseApi.getWarehouseListPage({pagesize:10000,  currentpage:1,ftype:props.ftype}).then(({data})=>{
				   state.warehouseOptions=data.records;
		})
	}
	
   function handleLoading(){
		loadAddress();
		loadWarehouse();
	}
 
	function editWarehouseAdress(){
		router.push({
			path:'/erp/warehouse/address',
			query:{
				title:'仓位地址',
				path:'/erp/warehouse/address',
			}
		})
	}
	function submitForm(){
		 dataFormRef.value.validate((isValid) => {if(isValid){
			 if(state.formData.ftype
			 &&state.formData.ftype.indexOf("oversea")>-1
			 && (state.formData.country==null
			    ||state.formData.country==undefined
			    ||state.formData.country=="")
			 ){
				  ElMessage.error('国家不能为空');
				  return ;
			 }
			 if(!state.formData.parentid){
				 ElMessage.error('父级仓库不能为空');
				 return ;
			 }
			 if(state.formData.id){
			 	warehouseApi.updateData(state.formData.id,state.formData).then(res=>{
			 		ElMessage.success('更新成功');
					state.dialog.visiable=false;
					emit("confirm");
			 	});
			 }else{
			 	warehouseApi.saveData(state.formData).then(res=>{
			 		ElMessage.success('新增成功');
					state.dialog.visiable=false;
					emit("confirm");
			 	});
			 }
		 }})
	}
	function detail(id,disabled){
		state.dialog.loading=true;
		warehouseApi.detail(id).then(({data})=>{
			state.formData=data;
			state.dialog.loading=false;
		})
	}
	function show(parentid,ftype,id){
		state.dialog.visiable=true;
		state.dialog.loading=false;
		loadSerialNumber();
		loadWarehouse();
		if(id){
				detail(id);
		}else{
			state.formData={
							adress:'',
							name:'',
							number:'',
							disabled :false,
		        };
			state.formData.parentid=parentid;
			state.formData.ftype=ftype;
		}
	}
	defineExpose({
		show,
	})
</script>

<style>
</style>
