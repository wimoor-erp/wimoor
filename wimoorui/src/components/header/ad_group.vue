<template>
	<div class="flex-center"  :class="{'no-border':!border}" >
	  <el-select v-model="groupid"  :size="selectSize"   placeholder="全部店铺" @change="groupChange">
	        <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
	        </el-option>
	  </el-select>
	  <el-divider v-if="!border" direction="vertical"></el-divider>
	  <span v-else class="m-r-8"></span>
	  <el-select   v-model="marketplaceid"  :size="selectSize"  placeholder="全部国家" @change="marketChange">
	        <el-option  v-for="item in marketList"  :key="item.id"  :label="item.name"  :value="item.id"   >
				{{item.name}}
				<span v-if="item.ftype">-{{item.ftype}}</span>
	        </el-option>
	  </el-select>
	  </div>
</template>

<script setup>
	import { ref,reactive,onMounted,watch,toRefs} from 'vue';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import advertApi from '@/api/amazon/advertisement/report/advertApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js'
	const emit = defineEmits(['change']);
	const props = defineProps({
		selectSize:'',
		border:'',
		isAll:'',
	})
	const{
		selectSize,
		border,
		isAll
	}=toRefs(props)
	const state = reactive({
		groupList:[],
		groupid:'',
		marketList:[],
		marketplaceid:'',
	})
	const{
		groupList,
		groupid,
		marketList,
		marketplaceid,
	}=toRefs(state)
	
	//获取店铺列表
	function getGroupData(){
		state.groupid=sessionStorage.getItem("ad_group_com_groupid");
		groupApi.getAmazonGroup().then((res)=>{
			if(props.isAll&&res.data&&res.data.length>0){
				res.data.push({id:"",groupid:"",name:"全部店铺"})
			}
			state.groupList=res.data;
			if(res.data&&res.data.length>0){
				if(state.groupid){
					var hasitem=false;
					res.data.forEach(item=>{
						if(item.id==state.groupid){
							hasitem=true;
						}
					})
					if(hasitem==false){
						state.groupid = res.data[0].id;
					}
				}else{
					state.groupid = res.data[0].id;
				}
 				 getMarketData(state.groupid,"load");
			}else{
				state.groupid='';
				emit("change",getData(),"load");
			}
		})
	}
	
	
	//获取国家列表
	function getMarketData(id,type){
		if(id){
			state.marketplaceid=sessionStorage.getItem("ad_group_com_profileid");
			advertApi.loadProfile({'groupid':id}).then((res)=>{
				if(props.isAll&&res.data&&res.data.length>0){
					res.data.push({id:"",name:"全部站点"})
				}
				state.marketList=res.data;
				if(res.data&&res.data.length>0){
					if(state.marketplaceid){
						var hasitem=false;
						res.data.forEach(item=>{
							if(item.id==state.marketplaceid){
								hasitem=true;
							}
						});
						if(hasitem==false){
							state.marketplaceid = res.data[0].id;
						}
					}else{
					  state.marketplaceid = res.data[0].id;
					}
				}else{
					 state.marketplaceid ='';
				}
				emit("change",getData(),type);
			})
		}else{
				marketApi.getMarketByGroup({'groupid':id}).then((res)=>{
					if(props.isAll&&res.data&&res.data.length>0){
						res.data.push({"id":"","name":"全部站点","marketplaceid":"",profileid:""})
					}
					res.data.forEach(item=>{
						item.id=item.marketplaceid;
					})
					state.marketList=res.data;
					state.marketplaceid ='';
					emit("change",getData(),type);
				})
		}
		
	}
	function groupChange(val){
		getMarketData(val);
	}
	
	function marketChange(val){
		emit("change",getData())
	}
	function getData(){
		sessionStorage.setItem("ad_group_com_groupid",state.groupid);
		sessionStorage.setItem("ad_group_com_profileid",state.marketplaceid);
		var profile={};
		var ismarket=false;
		state.marketList.forEach(item=>{
			if(item.id==state.marketplaceid){
				profile=item;
				if(item.endPoint){
					ismarket=true;
				}
			}
		})
		if(ismarket){
			return {'groupid':state.groupid,"marketplaceid":state.marketplaceid,"profile":profile};
		}else{
			return {'groupid':state.groupid,"profileid":state.marketplaceid,"profile":profile};
		}
		
	}
	onMounted(()=>{
		getGroupData()
	})
</script>

<style >
	.no-border .el-input__wrapper{
		box-shadow:none!important;
	}
	.m-r-8{
		margin-right:8px;
	}
</style>