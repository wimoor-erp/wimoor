<template>
	<h3 id="logistics" class="tab-scroll">物流信息</h3>
	
	<el-row>
		<el-col :span="8">
			<el-form-item label="附加费">
				<el-input v-model="dataForms.addfee" type="text" @input="dataForms.addfee=CheckInputFloat(dataForms.addfee)" placeholder="请输入采购成本"></el-input>
			</el-form-item>
		</el-col>
	 
	</el-row>
	<el-form-item label="物流报关">
	 <el-table border :data="listForms">
		 <el-table-column width="50"  type="index">
				 <template #header>
					   <el-link :underline="false" type="primary" @click="addCustItem">
					   <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
					   </el-link>
				 </template>
		 </el-table-column>
		 <el-table-column label="国家" prop="country">
			 <template #default="scope">
				 <el-select v-model="scope.row.country">
					 <el-option v-for="item in marketList"   :key="item.market"  :label="item.name" :value="item.market" ></el-option>
				 </el-select>
			  </template>
		 </el-table-column>
		 <el-table-column label="海关编码(必填)" >
			 <template #default="scope">
				<el-input v-model="scope.row.code"> </el-input>
			 </template>
		 </el-table-column>
		 <el-table-column label="中文名" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.cname"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="英文名" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.ename"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="报价" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.price"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="材质(英文)" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.material"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="材质(中文)" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.materialcn"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="用途" >
		 			 <template #default="scope">
		 				<el-input v-model="scope.row.application"> </el-input>
		 			 </template>
		 </el-table-column>
		 <el-table-column label="税率">
			  <template #default="scope">
			 <el-input v-model="scope.row.rate" @input="scope.row.rate=CheckInputFloat(scope.row.rate)" >
					 <template #append>
						%
					 </template>
			 </el-input>
			 </template> 
		 </el-table-column>
		  <el-table-column label="操作">
			  <template #header>
				  <span>操作</span>
				  <el-button type="primary" link @click="clearForms()">清除</el-button>
			 </template> 
			  <template #default="scope">
			  	<el-link title="删除"  type="primary" :underline="false" @click="removeLogtis(scope.$index)">
			  		<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
			  	</el-link>
			  </template>
		  </el-table-column>
		 <!-- 附加费用 -->
	 </el-table>
	</el-form-item>
</template>

<script setup>
	import {Plus,Minus} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	const emit = defineEmits(['clear']);
	let props = defineProps({
	  listForms:Object,dataForms:Object,
	})	 
	let {listForms,dataForms} =toRefs(props);
	let state=reactive({
		marketList:[]
	});
	let { marketList  } =toRefs(state);
	
	function clearForms(){
		emit("clear");
	}
	function addCustItem(){
		var row={};
		row.country="US";
		row.price=0;
		row.code="";
		row.rate=0;
		props.listForms.push(row);
	}
	function loadAllMarket(){
		marketApi.getMarketAll().then((res)=>{
			state.marketList=res.data;
		});
	}
	function removeLogtis(index){
		props.listForms.splice(index,1)
	}
	onMounted(()=>{
		loadAllMarket();
	})
		 
		 
</script>

<style>
	.wi-100{
		width:100px;
	}
	
</style>
