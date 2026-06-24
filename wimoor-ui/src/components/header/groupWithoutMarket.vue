<template>
	<el-space>
	  <el-select v-model="groupid"  style="width: 200px"    placeholder="全部店铺" @change="groupChange">
	        <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  </el-space> 
</template>
<script>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import { ref,reactive,onMounted,watch } from 'vue'
	export default{
		name:"groupWithoutMarket",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,},
		emits:["change"],
		props:["defaultValue"],
		setup(props,context){
			let groupList =ref([])
			let marketList =ref([])
			let groupid=ref()
			let marketplaceid = ref()
			onMounted(()=>{
				getGroupData()
			})
		 
			//获取店铺列表
			function getGroupData(){
				groupApi.getAmazonGroup().then((res)=>{
					if(props.defaultValue!='only'){
					   res.data.push({"id":"","name":"全部"})
					}
					groupList.value=res.data;
					if(res.data&&res.data.length>0){
							groupid.value = res.data[0].id;
                            context.emit("change",getData());
					}
				})
			}
			function groupChange(val){
			     context.emit("change",getData())
			}
		    function getData(){
				return {'groupid':groupid.value};
			}
			return{
				 groupList,groupChange,groupid,getData
			}
		}
	}
</script>

<style>
</style>
