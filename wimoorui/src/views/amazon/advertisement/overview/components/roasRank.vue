<template>
	<div class='pag-radius-bor '>
		<div class="flex-center-between">
			<h4 class="nowrap">ROAS后五名</h4>
			<el-space>
            <AdGroup :selectSize="'small'" :border="true"  @change="changeGroup"/>
			<el-radio-group class="el-radio-group-button" v-model="ftype" size="small">
			     <el-radio-button label="adgroups" name="adgroups" key="adgroups"> 广告组</el-radio-button>
			     <el-radio-button label="productads" name="productads" key="productads">商品广告</el-radio-button>
			     <el-radio-button label="keywords" name="keywords" key="keywords">关键词</el-radio-button>
			  </el-radio-group>
			  </el-space>
		</div>
		<el-table height="444" class="m-t-16"   :data="tabledata">
			<el-table-column label="广告组" prop="adGroupName"/>
			<el-table-column label="所属广告活动" prop="campaignName"/>
			<el-table-column label="花费" prop="cost"/>
			<el-table-column label="广告销售额" prop="sales"/>
			<el-table-column label="ROAS" prop="roas"/>
		</el-table>
	</div>	
</template>

<script setup>
	import{ref,reactive,toRefs}from'vue';
	import AdGroup from '@/components/header/ad_group.vue';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	const state = reactive({
		ftype:"adgroups",
		tabledata:[],
	})
	const{	
		 ftype,tabledata
	}=toRefs(state)
	 
	function changeGroup(data){
		var queryParams={};
			queryParams.groupid=data.groupid;
			queryParams.profileid=data.profileid;
			queryParams.marketplaceid=data.marketplaceid;
			queryParams.profile=data.profile;
			queryParams.ftype=state.ftype;
			summaryApi.getTop5(queryParams).then(res=>{
				state.tabledata=res.data;
			})
	}
	
</script>

<style scoped>
.nowrap{
	white-space:nowrap;
	margin-right:16px;
}
.m-t-16{
	margin-top:16px;
}
</style>