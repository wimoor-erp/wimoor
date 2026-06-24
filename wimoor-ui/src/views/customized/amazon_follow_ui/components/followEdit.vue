<template>
	<el-dialog
	title="编辑"
	v-model="visable"
	width="800px"
	>
		   <el-form 
		   :rules="rules"
		   v-model="form"
		   label-width="80px"
		   >
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="ASIN" prop="asin" >
				   <el-input v-model="form.asin"  disabled></el-input>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="SKU">
				   <el-input v-model="form.sku" disabled placeholder="不填系统自动生成"></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="跟卖时间" prop="time">
				   <el-select v-model="form.timeid"   style="width:100%;">
				   	<el-option v-for="item in timelist" :key="item.id" :label="item.fullname"   :value="item.id">
				   	</el-option>
				   </el-select>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="初始库存" prop="inventory">
				   <el-input v-model="form.fulfillable" ></el-input>
				   
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="库存水位" prop="day">
			   			<el-input v-model="form.lowest_quantity" ></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="初始价格" prop="inventory">
			   				   <el-input v-model="form.startprice" ></el-input>
			   				   
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="承受价" prop="day">
			   				  <el-input v-model="form.overprice" ></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
			   <el-row :gutter="24">
			   <el-col :span="12">
			   <el-form-item label="备货天数" prop="day">
			   				  <el-input v-model="form.cycle" ></el-input>
			   </el-form-item>
			   </el-col>
			   <el-col :span="12">
			   <el-form-item label="运费模板" prop="day">
				   <el-select  style="width:100%;" filterable   v-model="form.templateid"  >
				   	<el-option v-for="item in shipgrouplist" :key="item.id"  :label="item.name" :value="item.id"></el-option>
				   </el-select>
			   </el-form-item>
			   </el-col>
			    </el-row>
			   <el-row :gutter="24">
			   <el-col :span="24">
			   <el-form-item label="备注" prop="day">
			   		<el-input type="textarea" v-model="form.remark" ></el-input>
			   </el-form-item>
			   </el-col>
			   </el-row>
		   </el-form>
	<template #footer>
		<el-button @click="visable=false">取消</el-button>
		<el-button type="primary" @click="submitForm">提交</el-button>
	</template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted,}from"vue";
	import {ElMessageBox ,ElMessage } from 'element-plus';
	import mfnApi from '@/api/amazon/listing/mfnApi.js';
	import followTimeApi from '@/views/customized/amazon_follow_ui/js/followTimeApi.js';
	import followListApi from '@/views/customized/amazon_follow_ui/js/followListApi.js';
	const emit = defineEmits(['change']);
	const state=reactive({
		visable:false,
		form:{
			asin:'',
			sku:'',
		},
		shipgrouplist:[],
		timelist:[],
	})
	const {
		visable,
		shipgrouplist,
		form,
		timelist
	}=toRefs(state)
 
	function getShipGroup(){
		mfnApi.listShipGroup(state.form).then(res=>{
			if(res.data){
				res.data.push({id:"",name:"不设置"});
			}else{
				res.data={id:"",name:"不设置"};
			}
			state.shipgrouplist=res.data;
		});
	}
	function show(row){
		row.fulfillable=row.quantity;
		row.overprice=row.assumeprice;
		row.startprice=row.price;
		row.cycle=row.delivery_cycle;
		followTimeApi.list().then((res)=>{
			if(res.data){
				res.data.forEach(item=>{
					if(!item.endtime){
						item.fullname=item.name+" ("+item.starttime+"--"+")";
					}else{
						item.fullname=item.name+" ("+item.starttime+"-"+item.endtime+")";
					}
				});
			}
			state.timelist=res.data;
		});
		state.form=row;
		state.visable = true;
		getShipGroup();
	}
	function submitForm(){
		if(!state.form.asin || state.form.asin=="" || state.form.asin==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写ASIN!',
			})
			return;
		}
		if(!state.form.cycle || state.form.cycle=="" || state.form.cycle==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写发货天数!',
			})
			return;
		}
		if(!state.form.fulfillable || state.form.fulfillable=="" || state.form.fulfillable==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写库存!',
			})
			return;
		}
		if(!state.form.startprice || state.form.startprice=="" || state.form.startprice==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写价格!',
			})
			return;
		}
		if(!state.form.overprice || state.form.overprice=="" || state.form.overprice==undefined){
			ElMessage({
			  type: 'error',
			  message: '必须填写承受价格!',
			})
			return;
		}
		state.form.lowestquantity=state.form.lowest_quantity;
		followListApi.updateProfuctFollow(state.form).then((res)=>{
			ElMessage({
			  type: 'success',
			  message: '更改成功!',
			})
			state.visable = false;
			emit("change");
		});
	}
	defineExpose({
		show,
	})
</script>

<style scoped>
</style>