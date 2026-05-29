<template>
	<h3 id="assemble" class="tab-scroll">组合信息</h3>
	<el-form-item label="子产品列表" class="grid-row">
		<el-table border :data="dataForms.list">
			<el-table-column width="50" type="index">
						 <template #header >
							   <el-link :underline="false" type="primary" @click="addSubSku">
							   <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
							   </el-link>
						 </template>
			</el-table-column>
			<el-table-column width="62" label="图片">
						 <template #default="scope">
						 	<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
						 	<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
						 </template>
			</el-table-column>
			<el-table-column label="名称/子SKU">
				<template #default="scope">
					<div class='name'>{{scope.row.mname}}</div>
					<div class='sku'>{{scope.row.sku}} </div>
				</template>
			</el-table-column>
			<el-table-column label="成本单价" width="300" >
				<template #default="scope">
				 	<el-popover
					    placement="top-start"
					    title="历史价格"
					    :width="200"
					    trigger="hover"
					  >
					    <template #reference>
					     <span class="pointer" v-html="scope.row.subprice"></span> 
					    </template>
						<span  v-html="scope.row.historyprice"> </span>
					  </el-popover>
				</template>
			</el-table-column>
			<el-table-column label="单位数量" prop="subnumber" width="120" >
				<template #default="scope">
					<el-input v-model="scope.row.subnumber" @input="scope.row.subnumber=CheckInputInt(scope.row.subnumber)"  ></el-input>
				</template>
			</el-table-column>
			<el-table-column label="可用库存" prop="fulfillable"  width="150" />
			<el-table-column fixed="right" label="操作" width="80">
				<template #default="scope">
					<el-link title="删除"  type="primary" :underline="false" @click="removeSubSku(scope.$index)">
						<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
					</el-link>
				</template>
			</el-table-column>
		</el-table>
	</el-form-item>
	<el-form-item label="组装周期(天)" class="grid-row">
		<el-input  v-model="dataForms.assemblyTime" @input="dataForms.assemblyTime=CheckInputInt(dataForms.assemblyTime)"    style="width: 150px;" placeholder="组装周期天数"></el-input>
	</el-form-item>	
	<!-- 子产品选择弹窗 -->
	 <MaterialDialog ref="materialDailogRef" @getdata="getRows" :isAssemblyItem="true" type="product" ></MaterialDialog>
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import {useRouter } from 'vue-router';
	let props = defineProps({
	  dataForms:Object,
	}) 
	let {dataForms} =toRefs(props);
	let router = useRouter();
	let materialDailogRef=ref();
	function addSubSku(){
		materialDailogRef.value.show();
	}
	function removeSubSku(index){
		props.dataForms.list.splice(index,1)
	}
	function getRows(selecteds){
		 if(selecteds && selecteds.length>0){
			  selecteds.forEach(function(item){
				  var row={};
				  row.submid=item.id;
				  row.sku=item.sku;
				  row.image=item.image;
				  row.subprice=item.price;
				  row.mname=item.name;
				  row.subnumber=1;
				  row.historyprice="";
				  row.fulfillable=item.fulfillable;
				  var ispush=true;
				  props.dataForms.list.forEach(function(items){
					  if(items.sku==row.sku){
						 ispush=false; 
					  }
				  });
				  if(ispush==true){
					  props.dataForms.list.push(row);
				  }
			  });
		 }
		 
	}
	function changeIssemi(val){
		if(val==2){
			props.dataForms.list=[];
			props.dataForms.assemblyTime=0;
		}
	}
		 
		 
</script>

<style>
	.position-edit{
		position: relative;
		border: 1px dashed #a4d8ff;
		border-color: transparent;
		cursor: pointer;
	}
	.position-edit .el-icon{
		position: absolute;
		left:0;
		top:0;
		z-index: 2;
		color:#007dff;
		font-size: 14px;
		opacity: 0;
	}
	.position-edit:hover{
		border-color:#a4d8ff;
	}
	.position-edit:hover .el-icon{
		opacity: 1;
	}
</style>
