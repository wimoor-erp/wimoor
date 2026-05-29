<template>
	<el-row>
	    <el-col :span="12" >
			<div class="m-b-8">
		  <div class="font-medium font-bold">{{planData.number}}	<el-button :icon="DocumentCopy" style="margin-left: 20px;" link type="primary" @click="copyForNewForm">复制新增</el-button></div>
		  <div class="font-extraSmall">单据编码</div> 
		  <div  style="font-size: 14px;padding-top:4px">
			   <span v-if="step==0"> 第 1 步：确定要发货的商品及其数量</span>
			   <span v-if="step==1"> 第 2 步：商品装箱打包<el-button style="margin-left: 20px;" size="small" type="primary" plain @click="donePlanBox" v-if="step==1&&!planData.submitbox">跳过装箱(仅LTL,先分票后装箱)</el-button></span>
			   <span v-if="step==2"> 第 3 步：FBA入库配置与地区选择</span>
			   <span v-if="step==3"> 第 4 步：提交装箱信息和配送方式</span>
			   <span v-if="step==4"> 第 5 步：FBA货件已出库</span>
			  </div>
			</div>
		</el-col>
	    <el-col :span="12">
			<div >
			<el-row>
			<el-col :span="7" >
				
			
			</el-col>
			<el-col :span="17">
				<el-steps :active="step" align-center  >
				  <el-step class="pointer"  @click="stepChange(0)" title="配货" />
				  <el-step class="pointer"  @click="stepChange(1)" title="装箱"  />
				  <el-step class="pointer"  @click="stepChange(2)" title="配置" />
				  <el-step class="pointer"  @click="stepChange(3)" title="发货" />
				  <el-step class="pointer"  @click="stepChange(4)" title="完成" />
				</el-steps>
			</el-col>
		   </el-row>
			</div>
		</el-col>
	  </el-row>
		<el-divider></el-divider>
</template>

<script setup> 
	import { ref,reactive,onMounted,toRefs,nextTick } from 'vue';
	import {Warning,DocumentCopy} from '@element-plus/icons-vue';
	import { useRoute,useRouter } from 'vue-router';
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	let router = useRouter();
	const questionRef=ref();
	const infoRef=ref();
	const transRef=ref();
	const formid=router.currentRoute.value.query.id;
	let state =reactive({
		planData:{}, 
		step:0,
		mystep:0,
	})
	let{planData,step,mystep}=toRefs(state);
	function copyForNewForm(){
		router.push({
			path:'/invoice/new/addshipment',
			query:{
			  formid:formid,
			  title:"添加新版货件",
			  path:'/invoice/new/addshipment',
			  replace:true
			}
		})	
	}
	function donePlanBox(){
		shipmentBoxApi.donePlanBox({"formid":formid,"type":"skip"}).then(res=>{
			router.push({
				path:'/e/s/s/placement',
				query:{
				  id:formid,
				  title:"发货处理_配置",
				  path:'/e/s/s/placement',
				  replace:true
				}
			})	
		})
		
	}
	function stepChange(val){
		state.step=val;
		if(val==0){
			router.push({
				path:'/e/s/s/one',
				query:{
				  id:formid,
				  title:"发货处理_配货",
				  path:'/e/s/s/one',
				  replace:true
				}
			})	
		}
		if(val==1){
			router.push({
				path:'/e/s/s/two',
				query:{
				  id:formid,
				  title:"发货处理_装箱",
				  path:'/e/s/s/two',
				  replace:true
				}
			})	
		}
		if(val==2){
			router.push({
				path:'/e/s/s/placement',
				query:{
				  id:formid,
				  title:"发货处理_配置",
				  path:'/e/s/s/placement',
				  replace:true
				}
			})	
		}
		if(val==3){
			router.push({
				path:'/e/s/s/three',
				query:{
				  id:formid,
				  title:"发货处理_发货",
				  path:'/e/s/s/three',
				  replace:true
				}
			})	
		}
		if(val==4){
			router.push({
				path:'/e/s/s/end',
				query:{
				  id:formid,
				  title:"发货处理_结束",
				  path:'/e/s/s/end',
				  replace:true
				}
			})	
		}
		
		 
	}
	
	function show(infoData,step){
		state.planData=infoData;
		state.step=step;
	}
	
	 defineExpose({ show })
</script>

<style>
	.m-b-8{
		margin-bottom: 8px;
	}
</style>