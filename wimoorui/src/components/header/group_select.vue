<template>
	  <el-select v-model="groupid"   style="width: 200px"   placeholder="全部店铺" @change="groupChange">
	        <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
</template>
<script>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import { ref,reactive,onMounted,watch } from 'vue'
	export default{
		name:"groupselect",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,},
		emits:["change"],
		props:["defaultValue"],
		setup(props,context){
			let groupList =ref([])
			let marketList =ref([])
			let groupid=ref()
			let marketplaceid = ref()
			onMounted(()=>{
				loadData()
			})
		 	function loadData(){
			 	groupApi.getAmazonGroup().then((res)=>{
					if(props.defaultValue!='only'){
					   res.data.push({"id":"","name":"全部店铺"})
					}
					groupList.value=res.data;
					if(res.data&&res.data.length>0){
						    if(props.defaultValue=='all'){
								groupid.value="";
							}else if(props.defaultValue&&props.defaultValue!='all'&&props.defaultValue!='only'){
								groupid.value=props.defaultValue;
							}else{
								groupid.value = res.data[0].id;
							}
			                context.emit("change",groupid.value,true);
					}
				})
			}
			//获取店铺列表
			function groupChange(val){
			     context.emit("change",val)
			}
			return{
				 groupList,groupChange,groupid
			}
		}
	}
</script>

<style>
</style>
