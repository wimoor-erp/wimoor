<template>
	<div   class="main-sty ">
	
			<el-row>
				  	<div >
						<el-space >
							<el-checkbox-group v-model="queryParam.groupby" @change="handleQuery" size="large">
							   <el-checkbox    label="channeldetailid"  value="channeldetailid">
							    物流承运商(汇总)
							    </el-checkbox>
										  <el-checkbox    label="warehouse"  value="warehouse">
										  FBA仓库(汇总)
										  </el-checkbox>
										  <el-checkbox    label="warehouseid"  value="warehouseid">
										  本地仓库(汇总)
										  </el-checkbox>
										  <el-checkbox    label="groupid"  value="groupid">
										  店铺(汇总)
										  </el-checkbox>
										  <el-checkbox    label="marketplaceid"  value="marketplaceid">
										  站点(汇总)
										  </el-checkbox>
										  <el-checkbox    label="sku"  value="sku">
										  SKU(汇总)
										  </el-checkbox>
										   <el-checkbox    label="shipmentid"  value="shipmentid">
										   货件(汇总)
										   </el-checkbox>
							  </el-checkbox-group>
							</el-space>
							</div>
								
							   <div class='rt-btn-group ' >
								   <el-button @click.stop="downloadList" :loading="downLoading">导出</el-button>
							   <el-button class='ic-btn'  title='自定义列'>
							      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
							   </el-button>
							    <el-button   class='ic-btn' title='帮助文档'>
							     <help theme="outline" size="16" :strokeWidth="3"/>
							   </el-button>
							   </div>
							</el-row>
			  <el-divider />	 
		<div class="con-header" style="padding-top:10px">
		<el-row>
			  	<div >
					<el-space >
				 <Group @change="getData" defaultValue="all" />
				 <Warehouse @changeware="getWarehouse" defaultValue="all"  />
				 <el-select v-model="queryParam.datetype" style="width:100px">
					 <el-option value="createdate" key="createdate" label="创建日期"></el-option>
					 <el-option value="deliverydate" key="deliverydate"  label="发货日期"> </el-option>
				 </el-select>
				  <Datepicker longtime="ok" ref="datepickers" @changedate="changedate" />
				  <el-select v-model="queryParam.companyid" placeholder="选择承运商" @change="companyChange">
							 <el-option v-for="item in companylist" :value="item.id" :key="item.id" :label="item.name"></el-option>
				  </el-select>
				  <el-select v-model="queryParam.channelid" placeholder="选择物流渠道">
							  <el-option v-for="item in channellist" :value="item.id" :key="item.id" :label="item.channame"></el-option>
				  </el-select>
				 <el-input   v-model="queryParam.search" clearable @input="handleQuery" placeholder="请输入SKU" class="input-with-select" >
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
		</el-row>
		
		<el-row class="gary-bg pie-chart">
			<el-col :span="24">
				<el-space class="pie-title">
		<div class="font-extraSmall">汇总指标:</div>
		<el-select v-model="fieldkey.value"  >
		  	  <el-option v-for="item in fieldoptions" :value="item.key" :key="item.key" :label="item.name"></el-option>
		  </el-select>
		  </el-space>
		  <div class="flex-center" style="flex-wrap: wrap;">
			   <div  v-for="(value,key) in chartdata.value">
					<PieChart :name="key" :data="value" :keyvalue="fieldkey" :chartdata="chartdata"></PieChart>
			   </div>
		  </div>
		  </el-col>
		</el-row>
		</div>
	<GlobalTable
	  show-summary
	  :summary-method="getSummaries"
	  ref="globalTable" 
	  :tableData="tableData"   
	  @loadTable="loadTableData" 
	  :stripe="true"  
	  :border="false"
	  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="gname"    v-if="queryParam.groupby.indexOf('groupid')>=0" fixed="left"   label="店铺" width="180"  sortable="custom" >
			 <template #default="scope">
				  <div>{{scope.row.gname}}</div>
			 </template>
		</el-table-column>
		<el-table-column prop="marketplaceid"    v-if="queryParam.groupby.indexOf('marketplaceid')>=0" fixed="left"   label="站点" width="80"  sortable="custom" >
			 <template #default="scope">
				    {{scope.row.market}}  
			 </template>
		</el-table-column>
		<el-table-column prop="warehouseid"    v-if="queryParam.groupby.indexOf('warehouseid')>=0" fixed="left"   label="本地仓" width="120"  sortable="custom" >
			 <template #default="scope">
				    {{scope.row.warehousename}}  
			 </template>
		</el-table-column>
		 <el-table-column prop="logitics"    v-if="queryParam.groupby.indexOf('channeldetailid')>=0" fixed="left"   label="物流承运商" width="180"  sortable="custom" />
		 <el-table-column prop="channame"    v-if="queryParam.groupby.indexOf('channeldetailid')>=0"  fixed="left" label="物流渠道" min-width="180"  sortable="custom" >
			  <template #default="scope">
				  <div>{{scope.row.channame}}</div>
				  <div class="font-extraSmall"><span>{{scope.row.subarea}} </span>-<span>{{scope.row.channelname}}</span>-<span>{{scope.row.transtype}} </span></div>
			  </template>
		  </el-table-column>
		  <el-table-column prop="warehouse"  v-if="queryParam.groupby.indexOf('warehouse')>=0"  fixed="left" label="FBA仓库" min-width="120"  sortable="custom" />
		  <el-table-column prop="sku"        v-if="queryParam.groupby.indexOf('sku')>=0"  fixed="left" label="SKU" min-width="120"  sortable="custom" />
		   <el-table-column prop="shipmentid"        v-if="queryParam.groupby.indexOf('shipmentid')>=0"  fixed="left" label="货件" min-width="140"  sortable="custom" />
		   <el-table-column   label="发货信息" >
		    <el-table-column prop="totalqty"    label="计划发货" min-width="100"  sortable="custom" />
		    <el-table-column prop="totalout"   label="实际发货" min-width="100"  sortable="custom" >
				<template #header>
					 <el-tooltip placement="top" content="已发货出库的货件上的发货数量.(等于 实际接收数量 + 待接收数量 +接收差值)">
					  <span>实际发货</span>  
					  </el-tooltip>
				</template>
			</el-table-column>
		 
		    <el-table-column prop="totalrec"   label="实际接收" min-width="100"  sortable="custom" >
				<template #header>
						 <el-tooltip placement="top" content="已发货出库的货件中, 亚马逊已接收的产品数量, 包括正在接收和接收完成的货件">
						  <span>实际接收</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
		    <el-table-column prop="lessrec"    label="接收差值" min-width="100" sortable="custom"  >
		    <template #header>
		    		 <el-tooltip placement="top" content="已发货出库且已完成的货件中, 实际发货数量与亚马逊接收数量的差值">
		    		  <span>接收差值</span>  
		    		  </el-tooltip>
		    	</template>
		    </el-table-column>
		    <el-table-column prop="needout"    label="待发货" min-width="100"  sortable="custom" >
				<template #header>
						 <el-tooltip placement="top" content="已发货出库且已完成的货件中, 实际发货数量与亚马逊接收数量的差值">
						  <span>接收差值</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
		     <el-table-column prop="needrec"   label="待接收" min-width="90"  sortable="custom" />
			 <el-table-column prop="worth"      label="实际发货货值" min-width="125"  sortable="custom" >
				 <template #header>
				 		 <el-tooltip placement="top" content="已发货出库的货件上的产品的货值">
				 		  <span>实际发货货值</span>  
				 		  </el-tooltip>
				 	</template>
		    </el-table-column>
			 </el-table-column>
		   <el-table-column   label="运输信息" v-if="queryParam.groupby.indexOf('sku')<0"  >
			  <el-table-column prop="readweight"   v-if="queryParam.groupby.indexOf('sku')<0"     label="预估运输重量(KG)" min-width="150"  sortable="custom" >
				  <template #header>
				  	 		 <el-tooltip placement="top" content="产品的重量*发货数量">
				  	 		  <span>预估运输重量(KG)</span>  
				  	 		  </el-tooltip>
				  	 	</template>
				  </el-table-column>
			  <el-table-column prop="transweight_kg"  v-if="queryParam.groupby.indexOf('sku')<0"  label="发货运输重量(KG)" min-width="150"  sortable="custom" >
				  <template #header>
				  	 		 <el-tooltip placement="top" content="货件计费重量">
				  	 		  <span>发货运输重量(KG)</span>  
				  	 		  </el-tooltip>
				  	 	</template>
				  </el-table-column>
			  <el-table-column prop="transweight_cbm"  v-if="queryParam.groupby.indexOf('sku')<0"  label="发货运输重量(CBM)" min-width="160"  sortable="custom" >
				  <template #header>
				  	 		 <el-tooltip placement="top" content="货件计费体积">
				  	 		  <span>发货运输重量(CBM)</span>  
				  	 		  </el-tooltip>
				  	 	</template>
			   </el-table-column>
			  <el-table-column prop="totalbox"  v-if="queryParam.groupby.indexOf('sku')<0"  label="货件箱数" min-width="100"  sortable="custom" >
				  <template #header>
						 <el-tooltip placement="top" content="货件中包含的箱子数量">
						  <span>货件箱数</span>  
						  </el-tooltip>
					</template>
				  </el-table-column>
			  <el-table-column prop="shipfee"    v-if="queryParam.groupby.indexOf('sku')<0"   label="运输费用" min-width="100"  sortable="custom" >
				  <template #header>
						 <el-tooltip placement="top" content="货件运费">
						  <span>运输费用</span>  
						  </el-tooltip>
					</template>
				  </el-table-column>
			  <el-table-column prop="totalotherfee" v-if="queryParam.groupby.indexOf('sku')<0"  label="关税/其他费用" min-width="140"  sortable="custom" >
				  <template #header>
						 <el-tooltip placement="top" content="货件的税费和其它费用">
						  <span>关税/其他费用</span>  
						  </el-tooltip>
					</template>
				  </el-table-column>
			  <el-table-column prop="avgtime"  v-if="queryParam.groupby.indexOf('sku')<0"  label="平均物流时效(天)" min-width="150"  sortable="custom" >
				      <template #header>
						 <el-tooltip placement="top" content="货件从确认出库到开始接收的平均时效">
						  <span>平均物流时效(天)</span>  
						  </el-tooltip>
					</template>
			  </el-table-column>
			  </el-table-column>
			 <el-table-column v-if="queryParam.groupby.indexOf('sku')<0"  label="货件信息" >
				  <el-table-column prop="shipmentnum" v-if="queryParam.groupby.indexOf('sku')<0"  label="货件票数" min-width="100"  sortable="custom" />
				  <el-table-column prop="problem"  v-if="queryParam.groupby.indexOf('sku')<0"  label="异常货件票数 " min-width="140"  sortable="custom" />
		     </el-table-column>
	  </template>
	 </GlobalTable>
 </div>

</template>
<script>
    export default{ name:"发货统计" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import reportApi from '@/api/amazon/inbound/reportV2Api.js';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Group from '@/components/header/group.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import PieChart from './component/piechart.vue';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	const globalTable=ref();
	let state=reactive({
		 downLoading:false,
		 queryParam:{
			 search:"",
			 marketplaceid:"",
			 searchtype:"company",
			 datetype:"createdate",
			 type:"logitics",
			 groupby:["channeldetailid"],
			 companyid:"",
			 channelid:"",
		 },
		 isload:true,
		 tableData:{records:[],total:0},
		 snapshotDate:'',
		 summary:{},
		 fieldkey:{value:"transweight_kg"},
		chartdata:{value:[]},
		companylist:[],
		channellist:[],
		fieldoptions:[
					 {name:"预估运输重量",key:'readweight'},
		             {name:"计划发货",key:'totalqty'},
					 {name:"实际发货",key:'totalout'}, 
					 {name:"实际接收",key:'totalrec'},
					 {name:"接收差值",key:'lessrec'}, 
					 {name:"实际发货货值",key:'worth'},
					 {name:"待接收",key:'needrec'},
					 {name:"待发货",key:'needout'},
					 {name:"发货运输重量(KG)",key:'transweight_kg'},
					 {name:"发货运输重量(CBM)",key:'transweight_cbm'},
					 {name:"运输费用",key:'shipfee'},
					 {name:"货件箱数",key:'totalbox'},
					 {name:"关税/其他费用",key:'totalotherfee'},
					 {name:"货件票数",key:'shipmentnum'},
					 {name:"平均物流时效(天)",key:'avgtime'},
					  ],
	});
	let {
	   queryParam,
	   fieldkey,
	   isload,
	   fieldoptions,
	   tableData,
	   chartdata,
	   summary,
	   downLoading,
	   companylist,
	   channellist
	} =toRefs(state);
    function getData(data){
		state.queryParam.groupid=data.groupid;
		state.queryParam.marketplaceid=data.marketplaceid;
		handleQuery();
	}
	function getWarehouse(val){
		state.queryParam.warehouseid=val;
		handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromDate=dates.start;
		state.queryParam.toDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
		 
	}
	function handleQuery(){
		if(state.queryParam.groupby && state.queryParam.groupby.length>0){
			globalTable.value.loadTable(state.queryParam);
		}
	}
	 
	function loadTableData(params){
		reportApi.getShipmentReportByLoistics(params).then((res)=>{
			state.isload=false;
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(params.currentpage==1){
				 if(res.data.total>0){
					  state.summary=res.data.records[0].summary;
				 }
			}
		});
		reportApi.getShipmentReportByWarehouseLoistics(params).then((res)=>{
			state.chartdata.value=res.data;
		});
	}
	function downloadList(){
		state.downLoading=true
		reportApi.downExcelShipmentReportByLoistics(state.queryParam,()=>{
			state.downLoading=false;
		});
	}
	 
	/* 合计行数据 */
	function getSummaries({columns,data}){
		console.log(state.summary);
		console.log(columns);
		var arr = ["合计"];
		var hasqty=false;
		columns.forEach((item,index)=>{
			if(item.property=="totalqty" || hasqty){
				arr[index]=state.summary[item.property];
				hasqty=true;
			}
		})
		return  arr
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
		state.queryParam.channelid="";
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
	.pie-chart{
		padding:16px 8px 8px 8px;
	}
	.pie-title{
		padding-left:8px;
		padding-bottom:8px;
	}
	@media screen and (max-width:1600px){
	 .rt-btn-group{
		 margin-top:8px;
	 }
		}
</style>
