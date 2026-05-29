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
 	 <div class="flex-center" >
	   <div style="flex-wrap:wrap">
		   <el-space>
	        <div class=" radiohead">站点</div> 
		    <div>
			   <RadioButton ref="marketRadioRef"  :list="marketList"   @change="marketChange"  keyName="marketplaceid" ></RadioButton>
		    </div>
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
			let marketRadioRef=ref(RadioButton);
			let groupid=ref("")
			let marketplaceid = ref("")
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
						    getMarketData(groupid.value);
					}
				    groupList.value=res.data;
				
				})
			}
			//获取国家列表
			function getMarketData(id){
				marketApi.getMarketByGroup({'groupid':id}).then((res)=>{
					if(props.isproduct=='ok'){
						res.data.push({"id":"IEU","name":"欧洲(不含UK)","marketplaceid":"IEU"})
					}
					if(props.defaultValue!="only"){
						res.data.unshift({"id":"","name":"不限","marketplaceid":""})
					}
					if(res.data&&res.data.length>1){
	        			marketplaceid.value = res.data[1].marketplaceid;
						res.data[1].isActive=true;
					}
					marketList.value=res.data;
					context.emit("change",getData());
				})
			}
			function groupChange(val){
				groupid.value=val;
				getMarketData(val);
			}
			
			function marketChange(val){
				marketplaceid.value=val;
				context.emit("change",getData())
			}
			function setGroup(value){
				 groupid.value=value.groupid;
				 marketplaceid.value=value.marketplaceid;
				 groupRadioRef.value.setValue(value.groupid);
				 marketRadioRef.value.setValue(value.marketplaceid);
				 context.emit("change",getData())
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
				 groupList,marketList,groupChange,marketChange,groupid,marketplaceid,
				 getData,groupRadioRef,marketRadioRef,getGroupData,setGroup
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
