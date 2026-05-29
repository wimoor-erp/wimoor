<template>
	  <el-select   v-model="marketplaceid" style="width: 100px"  :multiple="multiple"  placeholder="全部国家" @change="marketChange">
	        <el-option  v-for="item in marketList"   :key="item.marketplaceid"  :label="item.name" :value="item.marketplaceid"   >
	        </el-option>
	  </el-select>
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js'
	import { ref,reactive,onMounted,watch,toRefs } from 'vue'
			let marketList =ref([])
			let marketplaceid = ref("");
		 	const emit = defineEmits(['change']);
			
			let props = defineProps({multiple:false });
			const { multiple} = toRefs(props);
			//获取国家列表
			function show(id,isInit){
				if(id){
					marketApi.getMarketByGroup({'groupid':id}).then((res)=>{
						res.data.push({"id":"IEU","name":"欧洲(不含UK)","marketplaceid":"IEU"})
						if(!props.multiple){
							res.data.push({"id":"","name":"全部国家","marketplaceid":""});
						}
						marketList.value=res.data;
						marketplaceid.value = "";
						if(!isInit){
						   emit("change",getData());
						}
					})
				}else{
					marketApi.getMarketAll().then(res=>{
						res.data.push({"id":"IEU","name":"欧洲(不含UK)","marketplaceid":"IEU"})
						if(!props.multiple){
							res.data.push({"id":"","name":"全部国家","marketplaceid":""});
						}
						marketList.value=res.data;
						marketplaceid.value = "";
						if(!isInit){
						   emit("change",getData());
						}
					})
				}
			
			}
			function reset(){
				marketplaceid.value = "";
			}
			defineExpose({ show ,reset})	 
			function marketChange(val){
				emit("change",val)
			}
		 
			 
	 
</script>

<style>
</style>
