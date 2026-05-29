<template>
	 <el-dialog
	    v-model="visible"
	    title="创建多渠道订单"
	    width="70%"
		top="10vh"
	    :before-close="handleClose"
	  >
	 <el-divider direction="vertical" style="border-width:3px;border-color:var(--el-color-primary)" /> 基础信息
	 <el-divider  style="margin-top:8px"/>
	  <el-form  label-width="auto" style="padding-left:40px;padding-right:40px">
		<el-row :gutter="20">
		  <el-col :span="12">
			  <el-form-item label="平台选择" >
				<el-select v-model="formData.platformId"  class="form-item-width"   placeholder="全部平台" @change="handleChange">
				      <el-option v-for="item in options"   :label="item.name" :value="item.id"   >
				      </el-option>
				</el-select>
			  </el-form-item>
		  </el-col>
	      <el-col :span="12">
			  <el-form-item label="订单号">
				 <el-input v-model="formData.orderId"  class="form-item-width"/>
			  </el-form-item>
		   </el-col>
	      </el-row>
		  <el-row :gutter="20">
		    <el-col :span="12">
		  	  <el-form-item label="海外仓" >
		  	    <Warehouse
		  	    @changeware="gettoWarehouse" 
		  	     warehouseType="oversea_usable"
		  	     defaultValue="only" 
				 style="width:300px"
		         />
		  	  </el-form-item>
		    </el-col>
		    </el-row>
			<el-row :gutter="20">
			  <el-col :span="12">
				  <el-form-item label="产品" >
				    <el-input readonly  class="form-item-width"  v-model="formData.sku"   @click="handleAdd"  />
				  </el-form-item>
			  </el-col>
			  </el-row>
	    </el-form>
	<el-divider direction="vertical"  style="border-width:3px;border-color:var(--el-color-primary)" />  订单信息
	  <el-divider style="margin-top:8px"/>
		 <el-form  label-width="auto" style="padding-left:40px;padding-right:40px">
				<el-row :gutter="20">
				  <el-col :span="12">
					  <el-form-item label="销量" >
						<el-input v-model="formData.quantity"  class="form-item-width" />
					  </el-form-item>
				  </el-col>
				  <el-col :span="12">
					  <el-form-item label="销售额">
						 <el-input  v-model="formData.price"  class="form-item-width"
						            :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
						            :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" 
						  />
						 
					  </el-form-item>
				   </el-col>
				  </el-row>
				  <el-row :gutter="20">
					<el-col :span="12">
					  <el-form-item label="下单日期"   >
						<el-date-picker
						        v-model="formData.purchaseDate"
						        type="date"
								style="width:300px"
						        placeholder="请选择日期"
						      />
					  </el-form-item>
					</el-col>
					<el-col :span="12">
					  <el-form-item label="邮费" >
						<el-input  class="form-item-width" 
						            v-model="formData.shipFee"
									:formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
									:parser="(value) => value.replace(/\$\s?|(,*)/g, '')" 
						/>
					  </el-form-item>
					</el-col>
					</el-row>
					<el-row :gutter="20">
					<el-col :span="12">
					  <el-form-item label="平台佣金" >
						<el-input  class="form-item-width"  
						           v-model="formData.referralFee"
						           :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                   :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" 
						/>
					  </el-form-item>
					</el-col>
					<el-col :span="12">
					  <el-form-item label="佣金百分比" >
						<el-input v-model="formData.referralRate" class="form-item-width" />
					  </el-form-item>
					</el-col>
				  </el-row>
				</el-form>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="visible = false">取消</el-button>
	        <el-button type="primary" @click="handleSubmit" >
	          确定
	        </el-button>
	      </div>
	    </template>
	  </el-dialog>
	  	<MaterialDialog ref = "materialRef"   @getdata="getRows" />
</template>

<script setup>
	import {ref,reactive,onMounted,watch,h,toRefs} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import {Search,ArrowDown,UploadFilled,House} from '@element-plus/icons-vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import orderApi from "@/api/erp/order/orderApi.js";
	import { useRouter } from 'vue-router';
	const emit = defineEmits(['change']);
	const router = useRouter();
	const materialRef=ref();
	const state =reactive({
		visible:false,
		options:[],
		formData:{},
	})
	const {
		visible,options,formData
	}=toRefs(state)
	function show(){
		state.visible=true;
		loadData();
	}
	function handleAdd(){
		materialRef.value.show();
	}
	function loadData(){
		orderApi.listPlatform().then(res=>{
			state.options=res.data;
		})
	}
	function gettoWarehouse(warehouseid){
		state.formData.warehouseid=warehouseid;
	}
	function handleSubmit(){
		orderApi.save(state.formData).then(res=>{
			ElMessage.success("保存成功");
			state.visible=false;
			emit("change");
		})
	}
	async function getRows(rows){
		if(rows && rows.length>0){
			if(rows.length>1){
				ELMessage.error("最多选择一个SKU");
			}else{
				state.formData.sku=rows[0].sku;
			}
		}else{
			ElMessage.error("请选择一个SKU");
		}
		 
	}
	defineExpose({
		show,
	})
</script>

<style>
	.form-item-width{
		 width:300px;
	}
</style>