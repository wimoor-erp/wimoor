<template>
	<el-space>
	  <el-select   v-model="region"   style="width: 100px"  placeholder="全部区域" @change="regionChange">
	        <el-option  v-for="item in regionList"   :key="item.id"  :label="item.region" :value="item.id"   >
	        </el-option>
	  </el-select>
	  </el-space> 
</template>

<script setup>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import authApi from '@/api/amazon/auth/authApi.js';
	import { onMounted, reactive, ref, toRefs,watch} from 'vue';
	const emit = defineEmits(['change']);
    const state=reactive({
		regionList:[],
		groupid:"",
		region:"",
	});
	const { regionList,groupid,region} = toRefs(state);
	let props = defineProps({ defaultValue:"" })
	const { defaultValue } = toRefs(props);
    function getData(id){
				authApi.getRegionByGroup({'groupid':id}).then((res)=>{
					if(props.defaultValue!="only"){
						res.data.push({"id":"","name":"全部","region":""})
					}
					state.regionList=res.data;
					if(res.data&&res.data.length>0){
	        			if(props.defaultValue!="all"){
	        			    state.region = res.data[0].id;
						}else{
							state.region = "";
						}
					}
					emit("change",state.region);
				})
			}
			
	function regionChange(val){
		emit("change",state.region)
	}
	defineExpose({
	  getData,regionChange,
	})	 
</script>

<style>
</style>
