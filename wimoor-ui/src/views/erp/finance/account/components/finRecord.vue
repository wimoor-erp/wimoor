<template>
	<el-dialog
	width="480px"
	title="记账" 
	:rules="rules"
	v-model="dialogVisable"
	>
	<el-form v-model="form" label-width="80px">
		<el-form-item label="费用类型">
		<el-radio-group v-model="form.ftype" class="ml-4">
		  <el-radio label="in" >收入</el-radio>
		  <el-radio label="out" >支出</el-radio>
		</el-radio-group>
		</el-form-item>
		<el-form-item label="收支项目">
	       <el-select  v-model="form.projectid">
			  <el-option v-for="item in feeTypeList" :key="item.id"  :label="item.name" :value="item.id" ></el-option>
		   </el-select>
		</el-form-item>
		<el-form-item label="金额">
	       <el-input type="number" v-model="form.amount">
			   <template #suffix>
			   ￥
			   </template>
		   </el-input>
		</el-form-item>
		<el-form-item label="备注" prop="remark">
			<el-input type="textarea"  v-model="form.remark"></el-input>
		</el-form-item>
	</el-form>
	 <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="dialogVisable = false">取消</el-button>
	        <el-button type="primary" @click="handleConfirm">确认</el-button>
	      </span>
	    </template>
	</el-dialog>
</template>

<script setup>
	import {Plus,} from '@icon-park/vue-next';
	import {Search} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted,watch,toRefs,computed} from 'vue'
	import journalApi from '@/api/erp/finances/journalApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const state = reactive({
		dialogVisable:false,
		feeVisable:false,
		form:{
			ftype:'in',
			amount:'',
			acct:"",
			remark:'',
			feetype:'',
		},
		feeTypeList:[],
	})
	const {
		feeTypeList,
		dialogVisable,
		form,
	} = toRefs(state)
 
 
	function handleConfirm(){
		if(!state.form.projectid){
			ElMessage.error('请选择一个具体的费用类型！');
			return;
		}
		if(!state.form.amount){
			ElMessage.error('请输入金额！');
			return;
		}else{
			if(parseFloat(state.form.amount)<0){
				ElMessage.error('请输入金额必须为正数！');
				return;
			}
		}
		if(!state.form.acct){
			ElMessage.error( '请选择一个具体的账户！');
			return;
		}
		if(state.form.acct){
			var data=state.form;
			journalApi.save(data).then((res)=>{
				ElMessage.success('操作成功！');
				state.dialogVisable=false;
				emit("change");
			});
		} 
	}
   
	function show(acc){
		state.form.acct=acc;
		faccountApi.getProject().then((res)=>{
			state.feeTypeList=res.data;
		});
		state.dialogVisable=true;
	}
	
	defineExpose({ show })
 
</script>

<style>
</style>