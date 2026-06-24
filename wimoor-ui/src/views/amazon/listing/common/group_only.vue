<template>
	 <div style="width:100%"> 
	 <div class="flex-center  ">
	  <div  style="flex-wrap:wrap;">
		<el-space>
			<div class=" radiohead">店铺</div> 
			<div> <RadioButton ref="groupRadioRef" :list="groupList"    @change="groupChange"></RadioButton></div>
		</el-space>
	 </div>
  </div>
 	   
	</div>
</template>

<script >
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js'
	import { ref,reactive,onMounted,watch } from 'vue'
    import RadioButton from '@/components/Radio/RadioButton/index.vue';
	export default{
		name:"group",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,RadioButton},
		emits:["change"],
		props:["defaultValue","isproduct"],
		setup(props,context){
			let groupList =ref([])
			let marketList =ref([])
			let groupRadioRef=ref(RadioButton);
 
			let groupid=ref("")
 
			onMounted(()=>{
				getGroupData()
			})
		 
			//获取店铺列表
			function getGroupData(){
				groupApi.getAmazonGroup().then((res)=>{
					 if(props.defaultValue!="only"){
						 res.data.unshift({"id":"","name":"不限"})
					 }
					if(res.data&&res.data.length>1){
						    groupid.value = res.data[1].id;
							res.data[1].isActive=true;
					}
				    groupList.value=res.data;
					context.emit("change",getData());
				})
			}
 
			function groupChange(val){
				groupid.value=val;
				context.emit("change",getData())

			}
			
 
			function setGroup(value){
				 groupid.value=value.groupid;
				 groupRadioRef.value.setValue(value.groupid);
				 context.emit("change",getData())
			}
		    function getData(){
				var group={};
				groupList.value.forEach(function(item){
					if(groupid.value==item.id){
						group=item;
					}
				})
				return {'groupid':groupid.value,"group":group};
			}
			return{
				 groupList,marketList,groupChange,groupid,
				 getData,groupRadioRef,getGroupData,setGroup
			}
		}
	}
</script>

<style>
	.radiohead{
		padding-bottom: 16px;
		font-size:12px;
		min-width:40px;
		padding-right: 16px;
		color: #999999;
	}
</style>
