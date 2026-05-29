<template>
	<div class="con-header" >
	  <el-row>
	    <el-space >
	  <Group ref="groups" @change="changeGroup" defaultValue="only"/>
	  <Region ref="regionRef" @change="changeRegion" defaultValue="only"/>
	  <el-select v-model="selectdate" @change='searchDateChange' placeholder="日期类型" style="width: 110px">
	    <el-option label="订单时间" value="purchase"></el-option>
	    <el-option label="更新时间" value="updates"></el-option>
	  </el-select>
	  <Datepicker ref="datepickers" @changedate="changedate" />
	   <el-input  v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入" class="input-with-select" >
	      <template #prepend> 
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="订单号" value="number"></el-option>
	        </el-select>
	      </template>
	      <template #append>
	        <el-button @click="searchConfirm">
	           <el-icon style="font-size: 16px;align-itmes:center">
	            <search />
	         </el-icon>
	        </el-button>
	      </template>
	    </el-input>
	   
	  </el-space>
	  </el-row>
	   <!--功能区域 -->
	  <el-row>
	   <el-space >
		  <el-button @click="downloadExcel">导出</el-button>
	   </el-space>
	   <div class='rt-btn-group'>
	   <el-button class='ic-btn'  title='列配置'>
	      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
	   </el-button>
	    <el-button   class='ic-btn' title='帮助文档'>
	     <help theme="outline" size="16" :strokeWidth="3"/>
	   </el-button>
	   </div>
	</el-row>
	</div>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import Group from '@/components/header/groupWithoutMarket.vue';
	import Region from '@/components/header/region.vue';
	import Datepicker from '@/components/header/datepicker.vue';
 
	 	const emit = defineEmits(['getdata',"download"]);
			let dateValue= ref();
			let storeList = ref()
			let conturyList = ref()
			let searchKeywords =ref()
			let selectlabel = ref("sku")
			let selectdate = ref("purchase")
			let moreSearchVisible =  ref()
			let regionRef=ref(Region);
			var queryParam={};
			let isload=true;
			onMounted(()=>{
				
			})
			//日期改变
			function changedate(dates){
				queryParam.startDate=dates.start;
				queryParam.endDate=dates.end;
				if(isload==false){
				   emit("getdata",queryParam);
				}
			}
			function changeGroup(data){
				queryParam.groupid=data.groupid;
				regionRef.value.getData(data.groupid)
				
			}
			function changeRegion(authid){
				queryParam.searchtype=selectlabel.value;
				queryParam.datetype=selectdate.value;
				queryParam.amazonauthid=authid;
				isload=false;
				emit("getdata",queryParam);
			}
			//搜索内容
			function searchConfirm(){
				queryParam.search=searchKeywords.value;
				if(isload==false){
					emit("getdata",queryParam);
				}
			}
			//搜索类型
			function searchTypeChange(){
				queryParam.searchtype=selectlabel.value;
				if(isload==false){
					emit("getdata",queryParam);
				}
			}
			function searchDateChange(){
				queryParam.datetype=selectdate.value;
				if(isload==false){
					emit("getdata",queryParam);
				}
			}
			
			function resetForm(){
				moreSearchVisible.value = false
			}
			function downloadExcel(){
				emit("download");
			}
			 
 
</script>

<style>
.cilcle-red{
	width:16px;
	height:16px;
	background-color:#F56C6C;
	display: inline-block;
	border-radius: 8px;
}
.color-select{
	display: flex;
	align-items: center;
	justify-content: space-between;
}	
</style>
