<template>
	<el-select v-model="ownerid"  filterable placeholder="产品负责人" @change="ownerChange">
		  <el-option  v-for="item in ownerList"    :label="item.name" :value="item.id"   >
		  </el-option>
	</el-select>
</template>

<script>
	import materialApi from '@/api/erp/material/materialApi.js';
	import { ref,reactive,onMounted,watch } from 'vue'
	export default{
		name:"owner",
		components:{ },
		emits:["owner"],
		props:["defaultValue","notAll","isInForm"],
		setup(props,context){
			let ownerList =ref([])
			let ownerid=ref()
			onMounted(()=>{
				getOwnerData();
				watch(props,function(){
					ownerid.value =props.defaultValue;
				});
			})
		    function reset(){
				ownerid.value ="";
				if(props.notAll!="isNotAll"){
					ownerid.value ="";
				}
				if(props.defaultValue){ 
					ownerid.value =props.defaultValue;
				}
			}
			//获取负责人列表
			function getOwnerData(){
				materialApi.getOwnerList().then((res)=>{
					if(res.data&&res.data.length>0){
						if(props.notAll!="isNotAll"){
							res.data.push({"id":"","name":"全部负责人"})
						}
						ownerList.value=res.data;
						ownerList.value.forEach((item,index)=>{
							if(!item){
								ownerList.value.splice(index,1)
							}
						})
						if(props.notAll!="isNotAll"){
							ownerid.value ="";
						}
						if(ownerid.value&&ownerid.value!=""){
							context.emit("owner",ownerid.value);
						}
					}
				})
			}
			//改变负责人
			function ownerChange(id){
				 context.emit("owner",id);
			}
			 
			return{
				 ownerid,ownerList,ownerChange,getOwnerData,reset,
			}
		}
	}
</script>

<style>
</style>
