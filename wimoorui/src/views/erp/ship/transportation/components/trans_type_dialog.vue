<template>
	<!-- 运输方式 -->
	<el-dialog v-model="dialog.visible"   title="编辑运输方式">
		<el-table border :data="formData">
			<el-table-column type="index"  width="60">
				<template #header >
				   <el-link :underline="false" type="primary" @click="handleAdd">
				       <plus  class="ic-cen" theme="outline" size="24" :strokeWidth="3"/>
				   </el-link>
				</template>
			</el-table-column>
			<el-table-column label="运输方式">
				<template #default="scope">
				<el-input  v-if="!scope.row.issystem"  v-model="scope.row.name"></el-input>
					<div v-else >{{scope.row.name}}</div>
				</template>
			</el-table-column>
			<el-table-column label="发货处理天数">
				<template #default="scope">
				<el-input v-model="scope.row.day"></el-input>
				</template>
			</el-table-column>
			<el-table-column label="操作">
				<template #default="scope">
					<el-link title="删除" v-if="!scope.row.issystem"  type="primary" :underline="false" @click="handleDelete(scope.$index)">
						 删除
					</el-link>
				</template>
			</el-table-column>
		</el-table>
		<template #footer>
		      <span class="dialog-footer">
		        <el-button @click="dialog.visible = false">取消</el-button>
		        <el-button type="primary" @click="submitForm()"
		          >保存</el-button >
		      </span>
		    </template>
	</el-dialog>
</template>

<script setup>
	import { ref ,reactive,onMounted,toRefs} from 'vue'
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import {ElMessage } from 'element-plus'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	const state=reactive({
		      queryParams:{transtype:""},
			  dialog:{visible:false},
			  formData:[],
	    });
		const {
		  queryParams,dialog,formData,countryOptions,transtypeOptions
		} =toRefs(state);
	const emit = defineEmits(['confirm']);
	function submitForm(){
		if(state.formData){
			var error=false;
			state.formData.forEach(item=>{
				if(item.name.length>20){
					error=true;
				}
			})
			if(error){
			   ElMessage.error('名称过长！');
			   return ;
			}
		}
		transportationApi.saveTransType(state.formData).then((res)=>{
			 ElMessage.success( '已提交成功！');
			 emit("confirm");
			 state.dialog.visible = false;
		});
	}
	function handleAdd(){
		state.formData.push({
			label:'',
			isnew:true,
		})
	}
	function handleDelete(index){
		state.formData.splice(index,1)
	}
	function handleQuery(){
		transportationApi.getTransTypeAll().then((res)=>{
			state.formData=res.data;
		});
	}
	function show(){
		state.dialog.visible=true;
		handleQuery();
	}
	defineExpose({ show });
</script>

<style>
</style>