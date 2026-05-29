<template>
	<el-dialog v-model="saleFormulaVisable" title="自定义日均销量计算公式" destroy-on-close='true' width="600px"  >
		<h4 class="m-b-8">手动录入计算公式</h4>
		<el-form>
			<el-form-item label="日均销量 =">
				<el-input v-model="formulaText"></el-input>
			</el-form-item>
		</el-form>
		<el-alert
		    title="计算注意事项"
		    type="info"
		    show-icon
			:closable="false"
		  >
		  <template #default>
			  <p class="m-b-8">1.可参与计算的项:
			  <el-tag type="info">30日销量</el-tag>&nbsp;
			  <el-tag type="info">15日销量 </el-tag>&nbsp;
			  <el-tag type="info">7日销量</el-tag>
			  </p>
			  <p>2.计算方案推荐:<span>日均销量 = (30日销量*0.2)/30 + (15日销量*0.3)/15 + (7日销量*0.5)/7</span></p>
		  </template>
		  </el-alert>
	  <template #footer>
	  	<span class="dialog-footer">
	  		<el-button @click="saleFormulaVisable = false">取消</el-button>
	  		<el-button type="primary" @click="submitForm">提交</el-button>
	  	</span>
	  </template>
	</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs} from "vue";
	import productinoptApi from '@/api/amazon/product/productinoptApi.js';
	import {ElMessage} from 'element-plus';
	 let state =reactive({
		 saleFormulaVisable:false,
		 formulaText:"",
	 });
	let {
		saleFormulaVisable,formulaText,
	}=toRefs(state)
	function submitForm(){
		productinoptApi.formulaSave({"formuladata":state.formulaText}).then((res)=>{
			if(res.data){
				ElMessage.success('日均销量计算公式修改成功，将会在下一日8点后生效！');
				state.formulaText=res.data;
				state.saleFormulaVisable=false;
			}
		});
	}
	
	function show(){
		state.saleFormulaVisable=true;
		state.formulaText="";
		productinoptApi.loadformula().then((res)=>{
			if(res.data){
				state.formulaText=res.data;
			}else{
				state.formulaText="(30日销量*0.2)/30 + (15日销量*0.3)/15 + (7日销量*0.5)/7";
			}
		
		});
	}
	 defineExpose({
	 	show,
	 })
			
			 
			
		 
	 
</script>

<style>
	.m-b-8{
		margin-bottom: 8px;
	}
</style>
