<template>
	  <el-select   v-model="country" style="width: 100px"  :multiple="multiple"  placeholder="全部国家" @change="marketChange">
	        <el-option  v-for="item in marketList"   :key="item.market"  :label="item.name" :value="item.market"   >
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
			let country = ref("");
		 	const emit = defineEmits(['change']);
			
			let props = defineProps({multiple:false });
			const { multiple} = toRefs(props);
			//获取国家列表
			function show(){ 
					marketApi.getMarketAll().then(res=>{
						marketList.value=res.data;
						if(res.data.length>0){
							country.value =res.data[0].market;
						    emit("change",country.value);
						}
						
					})
			}
			function reset(){
				country.value = "";
			}
			defineExpose({ show ,reset})	 
			function marketChange(val){
				emit("change",val)
			}
		 
			 
	 
</script>

<style>
</style>
