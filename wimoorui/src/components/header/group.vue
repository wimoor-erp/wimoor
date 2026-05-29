<template>
	<el-space>
	  <el-select v-model="groupid" style="width: 200px"    placeholder="全部店铺" @change="groupChange">
	        <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	
	  <el-select  style="width:100px;" v-model="marketplaceid"    placeholder="全部国家" @change="marketChange">
	        <el-option  v-for="item in marketList"  :key="item.marketplaceid"  :label="item.name" :value="item.marketplaceid"   >
	        </el-option>
	  </el-select>
	    </el-space> 
</template>

<script>
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js'
	import { ref,reactive,onMounted,watch } from 'vue'
	export default{
		name:"group",
		components:{MenuUnfold,Search,ArrowDown,SettingTwo,Help,Copy,MoreOne,},
		emits:["change"],
		props:["defaultValue","isproduct","init"],
		setup(props,context){
			let groupList =ref([])
			let marketList =ref([])
			let groupid=ref("")
			let marketplaceid = ref("")
			onMounted(()=>{
				if(!props.init){
					getGroupData()
				}
			})
		    function init(defaultgroupid,defaultmarket){
				 getGroupData(defaultgroupid,defaultmarket);
			}
			//获取店铺列表
			function getGroupData(defaultgroupid,defaultmarket){
				groupApi.getAmazonGroup().then((res)=>{
					 if(props.defaultValue!="only"&&props.defaultValue!="onlyEU"){
						 res.data.push({"id":"","name":"全部店铺"})
					 }
					groupList.value=res.data;
					if(res.data&&res.data.length>0){
						    if(defaultgroupid){
								 groupid.value =defaultgroupid;
							 }
							 else if(props.defaultValue!="all"){
						         groupid.value = res.data[0].id;
							}else{
								 groupid.value ="";
							}
							getMarketData(groupid.value,defaultmarket);
					}
				})
			}
			//获取国家列表
			function getMarketData(id,defaultmarket){
				marketApi.getMarketByGroup({'groupid':id}).then((res)=>{
					if(props.isproduct=='ok'){
						res.data.push({"id":"IEU","name":"欧洲(不含UK)","marketplaceid":"IEU"})
					}
					if(props.defaultValue=="onlyEU"){
						 var hasEu=false;
						if(res.data&&res.data.length>1){
								var list=[]
							 for(var i=0;i<res.data.length;i++){
								 if(res.data[i].region=='EU'){
									 res.data.splice(i,1);
									 hasEu=true;
								 }else{
									 list.push(res.data[i]);
								 }
							 }
							 if(hasEu){
								list.push({"id":"EU","name":"欧洲(不含UK)","marketplaceid":"EU"}); 
							 }
							res.data=list;
						}
					
					} 
					if(props.defaultValue!="only"&&props.defaultValue!="onlyEU"){
						res.data.push({"id":"","name":"全部国家","marketplaceid":""})
					}
					marketList.value=res.data;
					if(res.data&&res.data.length>0){
						if(defaultmarket){
							 marketplaceid.value =defaultmarket;
						}
	        			else if(props.defaultValue!="all"){
	        			  marketplaceid.value = res.data[0].marketplaceid;
						}else{
							marketplaceid.value = "";
						}
					}
					context.emit("change",getData());
				})
			}
			function groupChange(val){
				getMarketData(val);
			}
			
			function marketChange(val){
				context.emit("change",getData())
			}
			function setData(option){
				if(option){
					
				}
			}
		    function getData(){
				var market={};
				marketList.value.forEach(function(item){
					if(marketplaceid.value==item.marketplaceid){
						market=item;
					}
				});
				var group={};
				groupList.value.forEach(function(item){
					if(groupid.value==item.id){
						group=item;
					}
				})
				return {'groupid':groupid.value,"marketplaceid":marketplaceid.value,"market":market,"group":group};
			}
	
			 
			return{
				init,groupList,marketList,groupChange,marketChange,groupid,marketplaceid,getData
			}
		}
	}
</script>

<style>
</style>
