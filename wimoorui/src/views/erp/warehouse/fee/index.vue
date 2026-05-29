<template>
	<div   class="main-sty">
		<el-tabs v-model="selectable"  @tab-change="handleQuery">
		  <el-tab-pane  label="SKU头程" name="sku" key="sku">
		  </el-tab-pane>
		  <el-tab-pane  label="SKU头程明细" name="detail" key="detail">
		  </el-tab-pane>
		</el-tabs>
		  <el-row>
			  	<div class="con-header" style="margin-bottom:20px;">
					<el-space >
				  <!-- <Group @change="groupChange" defaultValue="all"/> -->
				  <Warehouse
				  @changeware="changeWarehouse" 
				   warehouseType="self_usable"
				   defaultText="全部本地仓库"
				  :isform="true" />
				  <Warehouse
				  @changeware="changeOverseaWarehouse" 
				   warehouseType="oversea_usable"
				   defaultText="全部海外仓库"
				  :isform="true" />
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
	  ref="globalTable" :tableData="tableData"   height="calc(100vh - 260px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column label="产品信息" width="260" prop="msku" v-if="selectable!='history'" sortable="custom"  show-overflow-tooltip   >
			<template #default="scope">
			<div class="flex-center">
				   <el-image v-if="scope.row.location" :src="scope.row.location" class="img-40"  width="40" height="40"  ></el-image>
				   <el-image v-else :src="$require('empty/noimage40.png')"  class="img-40"  width="40" height="40"  ></el-image>
				   <div >
					   <div v-if="scope.row.NAME">{{scope.row.NAME}}</div>
					     <div v-else>{{scope.row.name}}</div>
					   <p class="sku" v-if="scope.row.msku">{{scope.row.msku}} 
					   </p>
					   <p class="sku" v-else>{{scope.row.sku}}
					   </p>
				   </div>
			</div>
			</template>
		 </el-table-column>
		 <el-table-column prop="ownername"      v-if="selectable!='history'" sortable="custom"  label="产品负责人" width="160"   >
			  <template #default="scope">
				     <div>{{scope.row.ownername}}</div>
			   <div><span class="font-extraSmall" v-if="selectable=='detail'">发货人:</span>{{scope.row.optname}}</div>
			   </template>
			   </el-table-column>
		
		 <el-table-column prop="warehouse"    v-if="selectable=='detail'" sortable="custom" min-width="140"  label="发货信息"  >
			 <template #default="scope">
				 <div>{{scope.row.warehouse}}</div>
				   <div><span class="font-extraSmall">状态:</span>{{scope.row.statusname}}</div>
			  </template>
		 </el-table-column>
		 <el-table-column prop="channame"    v-if="selectable=='detail'" sortable="custom" min-width="110"  label="物流渠道"  >
		 			 <template #default="scope">
		 				 <div>{{scope.row.channame}}</div>
						 <div class="font-extraSmall">{{scope.row.shipcompany}}</div>
		 			  </template>
		 </el-table-column>
		 <el-table-column prop="number"    v-if="selectable=='detail'"   sortable="custom" min-width="140" label="货件信息"   >
			 <template #default="scope">
				  <div>{{scope.row.number}}</div>
				  <div><span class="font-extraSmall">发货日期:</span>{{dateFormat(scope.row.opttime)}}</div>
			 </template>
		 </el-table-column>
		 <el-table-column prop="towarehousename"    v-if="selectable=='detail'"  sortable="custom"  label="收货站点"   >
				 <template #default="scope">
					  <div>{{scope.row.towarehousename}} </div>
				 </template>
		 </el-table-column>
		 <el-table-column prop="qty"   sortable="custom"    label="发货数量"   />
		 <el-table-column prop="skufee"    v-if="selectable=='sku'"  sortable="custom"  label="SKU头程费用(元)"   />
		 <el-table-column prop="avgfee"    v-if="selectable=='sku'"  sortable="custom"  label="平均单件头程费用(元)"   />
		 <el-table-column prop="transweight"    v-if="selectable=='detail'" sortable="custom"  min-width="100"  label="实际结算重量"   >
				 <template #default="scope">
					  <div>{{scope.row.transweight}}(kg)</div>
					   <div><span class="font-extraSmall">SKU材积:</span>{{scope.row.skuweight}}</div>
				 </template>
		 </el-table-column>
		  <el-table-column prop="shipfee"      v-if="selectable=='detail'"  sortable="custom"  label="货件运费(元)"   />
		 <el-table-column prop="skufee"    v-if="selectable=='detail'"   sortable="custom" label="SKU费用分摊"   >
					 <template #default="scope">
						  <div>￥{{formatFloat(scope.row.skufee)}}</div>
						   <div><span class="font-extraSmall">￥{{formatFloat(scope.row.skufeeavg)}}/个</span></div>
					 </template>
		 </el-table-column>
		  <el-table-column prop="msku"    v-if="selectable=='sku'"   label="操作"   >
			  <template #default="scope">
					 <el-button type="primary" link @click.stop="showDialog(scope.row)">历史详情</el-button>
			  </template>
		  </el-table-column>  
	  </template>
	 </GlobalTable>
	</el-row>
 </div>
 <Dialog ref="dialogRef"></Dialog>
</template>
<script>
    export default{ name:"费用分摊" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import tranCompanyApi from   '@/api/erp/ship/transportationApi.js';
	import {dateFormat,getCurrencyMark,formatFloat} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Datepicker from '@/components/header/datepicker.vue';
	import Group from '@/components/header/group.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import Dialog from './detail_dialog.vue';
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
			 ftype:"new",
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
	function changeWarehouse(value){
		state.queryParam.warehouseid=value;
		handleQuery();
	}
	function changeOverseaWarehouse(value){
		state.queryParam.towarehouseid=value;
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
		if(state.selectable=="sku"){
			tranCompanyApi.getShipFeeReport(params).then((res)=>{
				state.isload=false;
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			});
		}else if(state.selectable=="detail"){
			tranCompanyApi.getShipFeeDetailReport(params).then((res)=>{
				state.isload=false;
				state.tableData.records = res.data.records;
				state.tableData.total =res.data.total;
			});
		}
		
	}
	function downloadList(){
		state.downLoading=true;
		if(state.selectable=='sku'){
			tranCompanyApi.downShipFeeReportExcel(state.queryParam,()=>{
				state.downLoading=false;
			});
		}else{
			tranCompanyApi.downShipFeeDetailReportExcel(state.queryParam,()=>{
				state.downLoading=false;
			});
		}
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

