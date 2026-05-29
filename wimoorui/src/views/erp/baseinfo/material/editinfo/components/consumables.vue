<template>
	<h3 id="consumables" class="tab-scroll">辅料关联</h3>
	<el-form-item label="辅料">
	  <el-table  :data="dataForms" border >
		 <el-table-column width="50"  type="index"> 
		 			 <template #header>
		 				   <el-link :underline="false" type="primary" @click="addConsumables">
		 				   <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
		 				   </el-link>
		 			 </template>
		 </el-table-column>
		 <el-table-column width="80" label="图片">
			 <template #default="scope">
			 	<el-image v-if="scope.row.image" :src="scope.row.image" width="40" height="40"></el-image>
				<el-image v-else :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
			 </template>
		 </el-table-column>
		 <el-table-column label="辅料名称/辅料SKU" width="400">
			 <template #default="scope">
			 	<div class='name'>{{scope.row.name}}</div>
			 	<div class='sku'>{{scope.row.sku}} </div>
			 </template>
		 </el-table-column>
		 <el-table-column label="关联数量">
			 <template #default="scope">
			 <el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputFloat(scope.row.amount)"></el-input>
			  </template>
		 </el-table-column>
		 <el-table-column label="操作">
			 <template #default="scope">
			 	<el-link title="删除"  type="primary" :underline="false" @click="removeConsum(scope.$index)">
			 		<minus  class="ic-cen" theme="outline" size="24"  :strokeWidth="3"/>
			 	</el-link>
			 </template>
		 </el-table-column>
	 </el-table>
	</el-form-item>
	<!-- 辅料选择弹窗 -->
	 <MaterialDialog ref="materialDailogRef" @getdata="getRows" type="consumable"  ></MaterialDialog>
</template>

<script setup>
	import {} from '@element-plus/icons-vue'
	import {Plus,Minus} from '@icon-park/vue-next';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	let props = defineProps({
	  dataForms:Object
	})
	let materialDailogRef=ref();
	let state=reactive({
		
	});
	let {dataForms} =toRefs(props);
	let {   } =toRefs(state);
	 
	function addConsumables(){
		materialDailogRef.value.show();
	}
	function consumChecked(rows){
		props.dataForms.push(rows)
	}
	function removeConsum(index){
		props.dataForms.splice(index,1)
	}
	function getRows(selecteds){
		 //emit("getrows",selecteds);
		 if(selecteds && selecteds.length>0){
			  selecteds.forEach(function(item){
				  var row={};
				  row.id=item.id;
				  row.sku=item.sku;
				  row.image=item.image;
				  row.name=item.name;
				  row.amount=1;
				  var ispush=true;
				  props.dataForms.forEach(function(items){
					  if(items.sku==row.sku){
						 ispush=false; 
					  }
				  });
				  if(ispush==true){
					   props.dataForms.push(row);
				  }
			  });
		 }
		 
	}
	 
	 // defineExpose({
	 //   tableData,
	 // }); 
 
		 
</script>

<style>
</style>
