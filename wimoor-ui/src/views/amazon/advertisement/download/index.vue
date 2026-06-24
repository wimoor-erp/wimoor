<template>
	<div class="main-sty">
		
		<el-row>
			<el-col :span="10" :offset="7">
		   <el-form :model="queryParams" label-width="120px">
			   <el-form-item >
				<div class="card-header">
					<el-image style="width: 120px; height:120px;margin-top:80px" :src="$require('file.png')" />
				  <p class="font-bold font-large m-b-8">广告报表下载</p>
				</div>
			   	</el-form-item> 
		       <el-form-item label="店铺站点" >
				   <div class="fill-grow">
		         <AdGroup   :border="true"   @change="changeGroup" />
				 </div>
		       </el-form-item>
			   <el-form-item label="日&#12288;&#12288;期">
			    <Datepicker ref="datepickersRef" :days="1" class="date-picker-width"   @changedate="changedate" />
			   </el-form-item>
			   <el-form-item label="汇总方式">
				   <el-radio-group v-model="queryParams.dateType" class="ml-4">
				         <el-radio label="daily" >Daily</el-radio>
				         <el-radio label="total" >Total</el-radio>
				    </el-radio-group>
			   </el-form-item>
			   <el-form-item label="广告类型">
				   <el-radio-group v-model="queryParams.campaigntype" class="ml-4">
				         <el-radio label="Sponsored Products" >Sponsored Products</el-radio>
				         <el-radio label="Sponsored Brands" >Sponsored Brands</el-radio>
						 <el-radio label="Sponsored Display" >Sponsored Display</el-radio>
				    </el-radio-group>
			   </el-form-item>
			   <el-form-item label="报表类型">
			     <el-select  v-model="queryParams.reporttype" class="fill-grow"   placeholder="报表类型"   >
					 <el-option key="Campaign"  label="Campaign" value="Campaign"></el-option>
					 <el-option key="Campaign-placement"  label="Campaign-placement" value="Campaign-placement" ></el-option>
					 <el-option key="Targeting-Keyword"  label="Targeting-Keyword" value="Targeting-Keyword"></el-option>
					 <el-option key="Targeting-ProductTarget"  label="Targeting-ProductTarget" value="Targeting-ProductTarget"></el-option>
					 <el-option key="Advertised product"  label="Advertised product" value="Advertised product"></el-option>
					 <el-option key="Purchased product"  label="Purchased product" value="Purchased product"></el-option>
					 <el-option key="Search term-Keyword"  label="Search term-Keyword" value="Search term-Keyword"></el-option>
					 <el-option key="Search term-ProductTarget"  label="Search term-ProductTarget" value="Search term-ProductTarget"></el-option>
			     </el-select>
			   </el-form-item>
			   <el-form-item >
				<el-button class="button m-t-8" @click="handleDownload" size="large" type="primary">下载报表</el-button>
				</el-form-item>   
		     </el-form>
			 </el-col>
			 </el-row>
             </div>
</template>
<script>
    export default{ name:"广告报表下载" };
</script>
<script setup>
	import{ref,reactive,toRefs}from'vue';
	import AdGroup from '@/components/header/ad_group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import {formatFloat,dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	const state = reactive({
		queryParams:{dateType:"daily",reporttype:"Campaign",campaigntype:"Sponsored Products"},
	})
	const{	
		 queryParams
	}=toRefs(state)
	 function changedate(dates,value,opt){
	 	state.queryParams.fromDate=dates.start;
	 	state.queryParams.endDate=dates.end;
	 }
	 function changeGroup(data){
	 		state.queryParams.groupid=data.groupid;
	 		state.queryParams.profileid=data.profileid;
	 		state.queryParams.marketplaceid=data.profile.marketplaceid;
	 		state.queryParams.profile=data.profile;
	 }
	 function handleDownload(){
		 summaryApi.downExcelDate(state.queryParams);
	 }
</script>

<style>
.fill-grow .flex-center .el-select,
.fill-grow .flex-center,
.fill-grow{
	flex:1;
}

</style>