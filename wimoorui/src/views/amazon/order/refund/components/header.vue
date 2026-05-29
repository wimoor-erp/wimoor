<template>
	<div class="con-header" >
	  <el-row>
	    <el-space >
	  <Group ref="groups" @change="changeGroup" defaultValue="only"/>
	  <Region ref="regionRef" @change="changeRegion" defaultValue="only"/>
	  <Datepicker ref="datepickers" @changedate="changedate" />
	   <el-input  v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入" class="input-with-select" >
	      <template #prepend> 
	        <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
	          <el-option label="SKU" value="sku"></el-option>
	          <el-option label="ASIN" value="asin"></el-option>
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
 
	  <div class='rt-btn-group'>
		  <el-dropdown style="margin-right:20px;" :hide-on-click="false">
			    <el-button >
			         高级搜索<el-icon class="el-icon--right"><arrow-down /></el-icon>
			    </el-button>
		     <template #dropdown>
		       <el-dropdown-menu>
		  				<el-dropdown-item disabled>退货原因</el-dropdown-item>
						<el-checkbox-group v-model="types.issue" @change="handleChange">
							<el-dropdown-item><el-checkbox label="issue-quality" value="issue-quality" ><div class="itemlist">质量问题</div></el-checkbox></el-dropdown-item>
							<el-dropdown-item><el-checkbox label="issue-un" value="issue-un" ><div class="itemlist">非质量问题</div></el-checkbox></el-dropdown-item>
							<el-dropdown-item><el-checkbox label="issue-other" value="issue-other" ><div class="itemlist">不确定</div></el-checkbox></el-dropdown-item>
						</el-checkbox-group>
		  		        <el-dropdown-item divided disabled>退货利润</el-dropdown-item>
						<el-checkbox-group v-model="types.profit" @change="handleChange">
							<el-dropdown-item> <el-checkbox label="profit-no" value="profit-no" ><div class="itemlist">不影响利润</div></el-checkbox></el-dropdown-item>
							<el-dropdown-item> <el-checkbox  label="profit-yes" value="profit-yes"><div class="itemlist">影响利润</div></el-checkbox></el-dropdown-item>
						</el-checkbox-group>
		  				<el-dropdown-item divided disabled>退货时间</el-dropdown-item>
						<el-checkbox-group v-model="types.days" @change="handleChange">
							<el-dropdown-item><el-checkbox  label="day30" value="day30"><div class="itemlist">30天内退货</div></el-checkbox></el-dropdown-item>
							<el-dropdown-item><el-checkbox  label="daymore" value="daymore"><div class="itemlist">超过30天退货</div></el-checkbox></el-dropdown-item>
						</el-checkbox-group>
		       </el-dropdown-menu>
		     </template>
		   </el-dropdown>
		    <el-button @click="downloadExcel">导出</el-button>
	  </div>
	  </el-row>
	   <!--功能区域 -->
 
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
	let types = ref({});
	let moreSearchVisible =  ref()
	let regionRef=ref(Region);
	var queryParam={};
	let isload=true;
	//日期改变
	function changedate(dates){
		queryParam.startDate=dates.start;
		queryParam.endDate=dates.end;
		if(isload==false){
			emit("getdata",queryParam);
		}
	}
	function handleChange(){
		var days=types.value.days;
		queryParam.daytype=null;
		queryParam.profit=null;
		queryParam.issueQuality=null;
		queryParam.issueUn=null;
		queryParam.issueOther=null;
		if(days&&days.length==1){
			queryParam.daytype=days[0];
		} 
		var profit=types.value.profit;
		if(profit&&profit.length==1){
			queryParam.profit=profit[0];
		} 
		var issue=types.value.issue;
		if(issue&&issue.length==1){
			issue.forEach(item=>{
				if(item==="issue-quality"){
					queryParam.issueQuality='true';
				}
				if(item==="issue-un"){
					queryParam.issueUn='true';
				}
				if(item==="issue-other"){
					queryParam.issueOther='true';
				}
			})
		}
		emit("getdata",queryParam);
	}
	function changeGroup(data){
		queryParam.groupid=data.groupid;
		regionRef.value.getData(data.groupid)
	}
	function changeRegion(authid){
		queryParam.searchtype=selectlabel.value;
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
	function resetForm(){
		moreSearchVisible.value = false
	}
	function downloadExcel(){
		emit("download");
	}
</script>

<style scoped>
.cilcle-red{
	width:16px;
	height:16px;
	background-color:#F56C6C;
	display: inline-block;
	border-radius: 8px;
}
.itemlist{
	width:200px;
	padding-right:20px;
}
.color-select{
	display: flex;
	align-items: center;
	justify-content: space-between;
}	
</style>
