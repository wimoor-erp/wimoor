<template>
	<div   class="main-sty">
		  <el-row>
			  	<div class="con-header" style="margin-bottom:20px;">
					<el-space >
				   <Group @change="groupChange" defaultValue="all"/>
				  <Datepicker longtime="ok" ref="datepickers" @changedate="changedate" />
				  <el-select v-model="queryParam.company" placeholder="选择承运商" @change="companyChange">
							 <el-option v-for="item in companylist" :value="item.id" :key="item.id" :label="item.name"></el-option>
				  </el-select>
				  <el-select v-model="queryParam.channel" placeholder="选择物流渠道">
							  <el-option v-for="item in channellist" :value="item.id" :key="item.id" :label="item.channame"></el-option>
				  </el-select>
				 <el-input  v-model="queryParam.search" clearable @input="handleQuery" :placeholder="searchPlaceholder" class="input-with-select" >
					<template #append>
					  <el-button @click="handleQuery">
						 <el-icon style="font-size: 16px;align-itmes:center">
						  <search />
					   </el-icon>
					  </el-button>
					</template>
				  </el-input>
				   </el-space>
		   	</div>
		   
		   <div class='rt-btn-group' style="margin-bottom:20px;">
			   
			   <el-button @click.stop="downloadList" :loading="downLoading">导出</el-button>
		   <el-button class='ic-btn'  title='自定义列'>
		      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
		    <el-button   class='ic-btn' title='帮助文档'>
		     <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </div>
		   
		</el-row>
		
	<el-row>
		
	<GlobalTable
	  ref="globalTable" :tableData="tableData"   height="calc(100vh - 200px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="groupname"     sortable="custom"  label="收货站点"   >
						 <template #default="scope">
							 <div>{{scope.row.marketname}}<span v-if="scope.row.subarea">-{{scope.row.subarea}}</span></div>
							  <div  class="font-extraSmall">{{scope.row.groupname}}</div>
						 </template>
		 </el-table-column>
		 <el-table-column prop="shipmentid"  sortable="custom" min-width="140" label="货件信息"   >
		 			 <template #default="scope">
		 				  <div>{{scope.row.shipmentid}}</div>
		 				  <div class="font-extraSmall"> {{scope.row.shipname}}</div>
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="warehousename"   sortable="custom" label="仓库名称"   >
		 					 <template #default="scope">
		 						  <div>{{scope.row.warehousename}}</div>
		 					 </template>
		 </el-table-column>
		 <el-table-column prop="channame"    sortable="custom" min-width="110"  label="物流渠道"  >
		 			 <template #default="scope">
		 				 <div>{{scope.row.channame}}</div>
						 <div class="font-extraSmall">{{scope.row.name}}</div>
		 			  </template>
		 </el-table-column>
		 <el-table-column prop="channame"    sortable="custom" min-width="110"  label="类型" width="120"  >
		 			 <template #default="scope">
		 				 <div>{{scope.row.transtype}}</div>
		 						 <div class="font-extraSmall">{{scope.row.channelname}}</div>
		 			  </template>
		 </el-table-column>
		<el-table-column prop="destination"    sortable="custom" min-width="110"  label="配送中心" width="120" >
					 <template #default="scope">
						 <div>{{scope.row.destination}}</div>
					  </template>
		</el-table-column>
		 <el-table-column prop="transweight"    sortable="custom"  min-width="100"  label="重量"   width="120">
				 <template #default="scope">
					  <div>{{scope.row.transweight}}({{scope.row.wunit}})</div>
					   <div><span class="font-extraSmall">单价:</span>{{scope.row.singleprice}}</div>
				 </template>
		 </el-table-column>
		 <el-table-column prop="totalfee"   sortable="custom" label="货件运费(元)"   width="120">
					 <template #default="scope">
						  <div>￥{{formatFloat(scope.row.totalfee)}}</div>
						   <div><span class="font-extraSmall">其他费用:￥{{formatFloat(scope.row.otherfee)}}/个</span></div>
					 </template>
		 </el-table-column>
	
		 <el-table-column prop="remark"   sortable="custom" label="备注"   >
		 					 <template #default="scope">
		 						  <div>{{scope.row.remark}}</div>
		 					 </template>
		 </el-table-column>
		 <el-table-column prop="opttime"   sortable="custom" label="操作时间"  width="120" >
		 					 <template #default="scope">
		 						  <div>{{dateFormat(scope.row.opttime)}}</div>
		 						   <div><span class="font-extraSmall">{{scope.row.operator}}</span></div>
		 					 </template>
		 </el-table-column>
	  </template>
	 </GlobalTable>
	</el-row>
 </div>
</template>
<script>
    export default{ name:"费用详情" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import tranCompanyApi from '@/api/amazon/inbound/tranCompanyApi.js';
	import {dateFormat,getCurrencyMark,formatFloat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Datepicker from '@/components/header/datepicker.vue';
	import Group from '@/components/header/group.vue';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	const globalTable=ref();
	const dialogRef=ref();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 search:"",
			 marketplaceid:"",
			 searchtype:"company",
			 datetype:"createdate",
			 companyid:"",
			 channelid:"",
		 },
		isload:true,
		tableData:{records:[],total:0},
		snapshotDate:'',
		summary:{},
		searchPlaceholder:"请输入SKU",
		companylist:[],
		channellist:[],
		selectable:'sku',
	});
	let {
	   queryParam,
	   isload,
	   tableData,
	   summary,
	   downLoading,
	   companylist,
	   channellist,
	   searchPlaceholder,
	   selectable
	} =toRefs(state);
 
	function showDialog(row){
		dialogRef.value.show(state.queryParam,row.sellersku);
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromdate=dates.start;
		state.queryParam.todate=dates.end;
		state.queryParam.enddate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
		 
	}
	function groupChange(obj){
		state.queryParam.groupid=obj.groupid;
		state.queryParam.marketplaceid=obj.marketplaceid;
		handleQuery();
	}
	function handleQuery(){
		if(state.selectable=="sku"){
			state.searchPlaceholder="请输入SKU";
		}else{
			state.searchPlaceholder="请输入SKU或货件编码";
		}
		globalTable.value.loadTable(state.queryParam);
	}
	 
	function loadTableData(params){
			tranCompanyApi.getShipmentFeeRecordReport(params).then((res)=>{
				state.isload=false;
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			});
	}
	function downloadList(){
		state.downLoading=true;
			tranCompanyApi.downShipmentFeeRecordReportExcel(state.queryParam,()=>{
				state.downLoading=false;
			});
		 
	}
	 
	function loadCompany(){
		transportationApi.getTranlist().then((res)=>{
			res.data.push({"id":"","name":"全部"})
			state.companylist=res.data;
		})
	}
	function getchannelList(val){
		var companyid=val;
		var market="";
		if(val!=""){
			transportationApi.getChannel({"company":companyid,"marketplaceid":market,"transtype":""}).then((res)=>{
				res.data.push({"id":"","channame":"全部"});
				state.channellist=res.data;
			})
		}else{
			state.channellist=[];
		}
	}
	function companyChange(val){
		getchannelList(val);
		handleQuery();
	}
	onMounted(()=>{
		loadCompany()
	})
</script>

<style scoped>
	.img-40{width: 40px;
	height: 40px;flex: none;
	margin-right: 8px;}
</style>

