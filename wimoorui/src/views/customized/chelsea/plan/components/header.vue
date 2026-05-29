<template>
	<div class="con-header">
		<el-tabs v-model="activeName"  @tab-change="handleChange">
		  <el-tab-pane v-for="item in orderState"  :name="item.name" :key="item.name">
			  <template #label>
				<span :class="item.name=='50'?'font-extraSmall':''">  {{item.label}}</span>
			  </template>
		  </el-tab-pane>
		  </el-tabs>
		  <el-row>
			  <el-col :span="24">
				  <el-space>
					      <el-radio-group class="radioWrapper" v-model="queryParams.paystatus">
					        <el-radio-button label="全部"  />
					        <el-radio-button label="待结清"  />
					        <el-radio-button label="已结清"  />
					      </el-radio-group>
						  <Group ref="groups" defaultValue="onlyEU" @change="groupChange" />
						  <Supplier  ref="supplierRef"   @change="changeSupplier" />
						  <el-input  v-model="queryParams.search" placeholder="请输入订单号"
						   clearable @input="handleQuery"  style="min-width:200px;" >
						     <template #append>
						       <el-button @click="handleQuery">
						           <el-icon style="font-size: 16px;align-itmes:center">
						           <search />
						        </el-icon>
						       </el-button>
						     </template>
						   </el-input>
						   <Datepicker longtime="ok" style="max-width:260px;" ref="datepickersRef" @changedate="changedate" />
				  </el-space>
				  <el-button
				  @click="clearSearch"
				  >重置</el-button>
			  </el-col>
		  </el-row>
		  <el-row>
			  <el-col :span="24">
				  <div class="flex-between">
					  <el-space>
					   <el-button type="primary" @click="handleCreate">新建订单</el-button>
					   <el-button type="primary" @click="handleComPlete">完成订单</el-button>
					   <el-button @click="handlePay">完成付款</el-button>
					   <el-button @click="handleDelete">删除</el-button>
					  </el-space>
					  <div>
						   <el-button @click="downloadReport">导出报告</el-button>
					  </div>
				  </div>
				  
			  </el-col>
		  </el-row>
	</div>
		<CreateDialog  ref="createDialogRef" @change="handleQuery" />
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	import {Search,} from '@element-plus/icons-vue'
	import Group from '@/components/header/group.vue';
	import Supplier from '@/components/header/supplier.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import CreateDialog  from './create_dialog.vue';
	import productformApi from "@/views/customized/chelsea/api/productionform/productformApi.js";
	const emit = defineEmits(["completeOrder","pay","delete","change"]);
	const supplierRef =ref();
	const createDialogRef =ref();
	const datepickersRef =ref();
	const state = reactive({
		orderState:[
			{label:'全部订单',name:'',},
			{label:'待提交',name:'0',},
			{label:'待生产',name:'1',},
			{label:'已出货',name:'3',},
			{label:'已结束',name:'4',},
			{label:'已归档',name:'50',},
		],
		activeName:"0",
		queryParams:{
			paystatus:'',
			search:'',
			
		},
		isload:false,
	})
	const {
		queryParams,
		orderState,
		activeName,
		isload,
	} = toRefs(state)

	function handleCreate(){
		createDialogRef.value.show();
	}
   function handleChange(val){
	   state.queryParams.auditstatus=val;
	   handleQuery();
   }
   function downloadReport(){
	   productformApi.downloadReport(state.queryParams);
   }
	//日期改变
	function changedate(dates){
		state.queryParams.startdate=dates.start;
		state.queryParams.enddate=dates.end;
		if(state.isload==true){
			handleQuery();
		}
	}
	//选择供应商
	function changeSupplier(value,type){
		state.queryParams.supplierid=value;
	   if(state.isload==true){
	    	handleQuery();
	   }
	}
	
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
		state.queryParams.auditstatus=state.activeName;
		handleQuery();
	}
	
	const handleComPlete = ()=>{
		emit("completeOrder")
	}
	const handlePay =()=>{
		emit("pay")
	}
	const handleDelete = ()=>{
		emit("delete")
	}
	
	function clearSearch(){
		state.queryParams.paystatus ="";
		supplierRef.value.reset();
		state.queryParams.search="";
		datepickersRef.value.reset();
		handleQuery();
	}
	function handleQuery(){
		emit("change",state.queryParams);
		if(state.isload==false){
			state.isload=true;
		}
		
	}
</script>

<style >
	.radioWrapper{
		flex-wrap: nowrap;
	}
</style>