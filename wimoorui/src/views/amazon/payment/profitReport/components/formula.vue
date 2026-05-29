<template>
	<el-dialog
	top="5vh"
	v-model="visable"
	title="毛利润计算公式"
	>
		 <el-alert class="m-b-16" title="修改毛利润计算公式后，对归档数据不会产生影响" type="warning" :closable="false" show-icon />
		 <el-row :gutter="16" class="m-b-16">
			 <el-col :span="8">
				 <el-card shadow="never" class="yellow-top-border">
					<template #header>
					        <span class="font-bold">亚马逊收入</span>
					    </template>
					<ul >
						<li v-for="(item,index) in list.pay" >
						<el-checkbox
						@change = "handleChange"
						v-model="item.check" 
						:label="item.name"
						:disabled="item.disabled" />
						</li>
					</ul>	
				 </el-card>
			 </el-col>
			 <el-col :span="8" >
				 <el-card shadow="never" class="green-top-border">
					<template #header>
					        <span class="font-bold ">亚马逊支出</span>
					    </template>
					<ul >
						<li v-for="(item,index) in list.incom" >
						<el-checkbox
						@change = "handleChange"
						v-model="item.check" 
						:label="item.name"
						:disabled="item.disabled" />
						</li>
					</ul>		
				 </el-card>
			 </el-col>
			 <el-col :span="8">
				 <el-card shadow="never"  class="blue-top-border" >
					<template #header>
					        <span class="font-bold">非亚马逊费用</span>
					    </template>
						<ul >
							<li v-for="(item,index) in list.other" >
							<el-checkbox
							v-model="item.check" 
							:label="item.name"
							:disabled="item.disabled" />
							</li>
						</ul>		
				 </el-card>
			 </el-col>
		 </el-row>
			 <div class="flex-center-between m-b-16">
			    <h5 >毛利润计算公式</h5>
				<el-button size="small" bg   text>恢复默认
				<el-icon><RefreshRight /></el-icon>
				</el-button>
			 </div>
			 <div>
				 <p >毛利润 =</p>
				 <el-card class="m-t-8 card-body-no-padding" shadow="never" >
					 <el-space v-for="(item,index) in localData">
					 <el-tag class="m-t-b-8" effect="plain" :type="item.color" @close="handleClose(item)" :closable="!item.disabled">{{item.name}}</el-tag>
					 <el-icon class="font-extraSmall" v-if="calculatedField.length-index>1"><Plus /></el-icon>
					 </el-space>
				 </el-card>
				 </div>
				 <template #footer>
					 <el-button @click="visable=false">取消</el-button>
					 <el-button type="primary">保存</el-button>
				 </template>
	</el-dialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,computed} from 'vue'
	import {RefreshRight,Plus} from '@element-plus/icons-vue';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox} from 'element-plus';
	import settlementFormulaApi from '@/api/amazon/finances/settlementFormulaApi.js';
	import financesItemApi from '@/api/amazon/finances/financesItemApi.js';
 
	const state = reactive({
		localData:{},
		downloading:false,
		itemlist:[],
		visable:false,
		list:{
			pay:[
				{name:'销售额',check:true,disabled:true,color:'warning'},
				{name:'FBA赔偿',check:true,disabled:true,color:'warning'},
				{name:'买家运费',check:true,disabled:true,color:'warning'},
				{name:'其他收入',check:true,disabled:true,color:'warning'},
			],
			incom:[
				{name:'平台费',check:true,disabled:true,color:'success'},
				{name:'fba配送费',check:true,disabled:true,color:'success'},
				{name:'销售佣金',check:true,disabled:true,color:'success'},
				{name:'广告费',check:true,disabled:false,color:'success'},
				{name:'仓储费',check:true,disabled:false,color:'success'},
				{name:'促销折扣',check:true,disabled:false,color:'success'},
				{name:'推广费',check:true,disabled:false,color:'success'},
				{name:'其它费用',check:true,disabled:false,color:'success'},
			],
			other:[
				{name:'采购成本',check:true,disabled:false,color:'blue'},
				{name:'物流成本',check:true,disabled:false,color:'blue'},
				{name:'自定义费用',check:false,disabled:false,color:'blue'},
			],
		},
	})
	const{
		visable,
		tableData,
		localData,
		downloading,
		itemlist,
		list,
	}=toRefs(state)
 
	const calculatedField = computed(()=>{
		var arr = []
		arr.push(...state.list.pay,...state.list.incom,...state.list.other)
		arr = arr.filter (item=> item.check != false)
		return arr
	})
	
	function handleClose(val){
		state.list.incom.forEach((item)=>{
			if(item==val){
				item.check=false
			}
		})
		state.list.pay.forEach((item)=>{
			if(item==val){
				item.check=false
			}
		})
		state.list.other.forEach((item)=>{
			if(item==val){
				item.check=false
			}
		})
	}
	
	
	 
	function submitForm(){
		state.downloading=true;
		if(state.localData.formulaData.indexOf("运费预估")>=0&&state.localData.formulaData.indexOf("货件头程运费")>=0){
			ElMessage.error("【运费预估】和【货件头程运费】属于同一费用，只是计算方式不一样，不能同时加入计算公式。");
			state.downloading=false;
			return;
		}
		settlementFormulaApi.formulaSave({"formuladata":state.localData.formulaData,"pricetype":state.localData.pricetype}).then((res)=>{
			if(res.data){
				ElMessage.success("操作成功");
			}
			state.downloading=false;
		});
	}
	 
	function loadData(){
		settlementFormulaApi.loadformula().then((res)=>{
			state.localData=res.data;
		});
		
		financesItemApi.getFinListNoPage().then((res)=>{
			state.itemlist=res.data;
		});
	}
	 
		
	 
	
	function show(){
		state.visable = true;
		 loadData();
	}
    defineExpose({
		show,
	})
	
</script>

<style scoped="scoped">
	.m-b-16{
		margin-bottom:16px;
	}
	.yellow-top-border{
		border-top: 2px solid #e6a23c;
	}
	.green-top-border{
		border-top: 2px solid #67c23a;
	}
	.blue-top-border{
		border-top: 2px solid #409eff;
	}
	ul {
		list-style-type:none;
	}
	.m-t-b-8{
		margin-top: 8px;
		margin-bottom: 8px;
	}
</style>
<style>
	.card-body-no-padding .el-card__body{
		padding:8px 16px;
	}
</style>