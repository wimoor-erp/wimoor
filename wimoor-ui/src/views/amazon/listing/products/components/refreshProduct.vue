<template>
		<el-dialog  v-model="refreshProVisable" title="产品同步" destroy-on-close='true'   width="500px" >
			 <Group ref="groupRef"  @change="groupChange" defaultValue="only" :init="true"></Group>
			 <br/>
			 <el-input style="margin-top: 10px;width:375px" type="text" placeholder="输入要同步的平台SKU" v-model="state.queryParams.sku">
				  <template #prepend>
				         <el-button :icon="DocumentAdd" @click="handleToAddPage()" />
				       </template>
			 </el-input>
			 <br/>
              <el-button  v-loading="loading"  class="m-t-16" type="primary" @click.stop="submitProduct" >提交</el-button>
		</el-dialog>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs, nextTick } from "vue";
	import {DocumentAdd} from '@element-plus/icons-vue';
	import {ElMessage} from 'element-plus';
	import { useRoute,useRouter } from 'vue-router';
	import Group from '@/components/header/group.vue';
	import productRefreshApi from '@/api/amazon/product/productRefreshApi.js';
	const groupRef=ref();
	let router = useRouter();
	let state=reactive({
		refreshProVisable:false,
		queryParams:{"sku":''},
		loading:false,
	});
	const {
	  refreshProVisable,
	  queryParams,
	  loading,
	} = toRefs(state);
	defineExpose({
	  show
	})
	function show(groupid,marketplaceid){
		state.refreshProVisable=true;
		nextTick(()=>{
		   groupRef.value.init(groupid,marketplaceid);
		})
	}
	
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
	}
	function handleToAddPage(){
		router.push({
			path:'/amazon/listing/edit',
			query:{
			  title:"刊登",
			  path:'/amazon/listing/edit',
			}
		})
	}
	function submitProduct(){
		state.loading=true;
		productRefreshApi.refreshItemBySKU(state.queryParams).then((res)=>{
			  state.loading=false;
			 if(res.data){
				 ElMessage.success( '同步成功！');
				 state.refreshProVisable=false;
			 }else{
				 ElMessage.error( '同步失败！');
			 }
		});
	}
	
	
</script>

<style>
</style>
