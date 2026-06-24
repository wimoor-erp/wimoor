<template>
	<el-dialog
	   v-model="dialogVisible"
	   title="广告提醒编辑"
	   width="60%"
	 >
	<el-form
	    ref="dataFormRef"
	    :model="formData"
	    :rules="rules"
		label-width="70px"
	  >
	    <el-form-item label="店铺站点" prop="name">
			<AdGroup  :border="true"/>
	    </el-form-item>
		<el-form-item label="提醒对象" prop="remark">
			<el-radio-group v-model="formData.radio">
			      <el-radio-button label="广告活动" />
			      <el-radio-button label="广告组" />
			      <el-radio-button label="商品" />
			      <el-radio-button label="关键词" />
			    </el-radio-group>
		</el-form-item>
		<el-form-item label="广告活动" prop="campagin">
			<el-select v-model="campagin" placeholder="请选择广告活动" clearable filterable >
				<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="广告组" prop="group" v-if="formData.radio!='广告活动'">
			<el-select v-model="campagin" placeholder="请选择广告组" clearable filterable >
				<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="商品广告" prop="group" v-if="formData.radio=='商品'">
			<el-select v-model="campagin" placeholder="请选择商品" clearable filterable >
				<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="关键词" prop="group" v-if="formData.radio=='关键词'">
			<el-select v-model="campagin" placeholder="请选择关键词" clearable filterable >
				<el-option v-for="item in campaginsData" :label="item.name" :value="item.value"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="计算周期" prop="remark">
			<el-select v-model="formData.cycle" placeholder="请选择广告活动" >
				<el-option label="昨天" value="1"></el-option>
				<el-option label="近七天" value="2"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="触发条件" prop="remark">
			<el-space >
			<el-select v-model="formData.name"  >
				<el-option label="点击数" value="1"></el-option>
				<el-option label="点击率" value="2"></el-option>
				<el-option label="花费" value="3"></el-option>
				<el-option label="ACOS" value="4"></el-option>
				<el-option label="转化率" value="5"></el-option>
				<el-option label="曝光量" value="6"></el-option>
			</el-select>
			<el-select  v-model="formData.rule" >
				<el-option label="超过" value="1"></el-option>
				<el-option label="低于" value="2"></el-option>
			</el-select>
			<el-select  v-model="formData.rule" >
				<el-option label="常值" value="1"></el-option>
				<el-option label="前一日" value="2"></el-option>
				<el-option label="近七天平均值" value="3"></el-option>
			</el-select>
			<el-input v-model="formData.val"></el-input>
			</el-space>
		</el-form-item>
	  </el-form>
	   <template #footer>
	     <span class="dialog-footer">
	       <el-button @click.stop="cancel">取消</el-button>
	       <el-button type="primary" @click.stop="submitForm">
	         提交
	       </el-button>
	     </span>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue";
	import AdGroup from '@/components/header/ad_group.vue';
	const state = reactive({
			dialogVisible:false,
			formData:{
				radio:'广告活动',
				cycle:'1',
				name:'1',
				rule:'1',
			},
		})
	const{
		dialogVisible,
		formData,
	}=toRefs(state)	
	defineExpose({
		show,
	})
	function show(){
		state.dialogVisible = true
	}
</script>

<style>
</style>