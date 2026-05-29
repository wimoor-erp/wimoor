<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
		  <Group  @change="changeGroup"  ref="groupRef" />
		   <el-input  v-model="searchKeywords" clearable @input="searchConfirm" placeholder="请输入" class="input-with-select" >
		      <template #prepend> 
		        <el-select v-model="selectlabel"  placeholder="SKU" style="width: 110px">
		          <el-option label="SKU" value="sku"></el-option>
		          <!-- <el-option label="ASIN" value="asin"></el-option>
		          <el-option label="订单号" value="number"></el-option> -->
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
			<el-select v-model="selectfield" @change='searchFieldChange' placeholder="请选择筛选条件" style="width: 170px">
				<el-option v-for="item in fieldList" :key="item.value" :label="item.label" :value="item.value"></el-option>
			</el-select>
		    <el-button @click.stop="clearSearch">重置</el-button>
		   </el-space>
		   <div class='rt-btn-group'>
			   <div style="padding-right:20px;padding-top:10px;">
			   	  <span class="font-extraSmall">更新时间:{{snapshotDate}}</span> 
			    </div>
			   <el-button @click.stop="downloadList" :loading="downLoading">导出</el-button>
		   <el-button class='ic-btn'  title='列配置'>
		      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
		    <el-button   class='ic-btn' title='帮助文档'>
		     <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </div>
		</el-row>
		</div>
	<el-row>
	<GlobalTable
	  show-summary
	  :summary-method="getSummaries"
	  ref="globalTable" :tableData="tableData"   height="calc(100vh - 200px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="image" fixed label="图片" width="60" >
			<template #default="scope">
			 <el-image v-if="scope.row.image" :src="scope.row.image"   width="40" height="40"  ></el-image>
			 <el-image v-else  :src="$require('empty/noimage40.png')" width="40" height="40"></el-image>
		   </template>
		 </el-table-column>
		 <el-table-column prop="name" fixed label="商品信息"  show-overflow-tooltip  width="240" >
		    <template #default="scope">
		       <div class='name'>{{scope.row.pname}}</div>
		       <div class='sku'>{{scope.row.sku}} 
		       </div>
		   </template>
		 </el-table-column>
		 <el-table-column prop="name" label="站点信息"  show-overflow-tooltip  width="80" >
		    <template #default="scope">
		       <div >{{scope.row.gname}}</div>
		       <div class='font-extraSmall'>{{scope.row.market}}  </div>
		   </template>
		 </el-table-column>
		 <el-table-column prop="purchasecost" label="采购成本"    width="80" >
		 <template #default="scope">
			  ￥{{scope.row.purchasecost}}
			 </template>
			</el-table-column> 
		  <el-table-column prop="available"  :sort-orders="sortOrders"   sortable="custom" label="可用库存" width="100"   >
			  <template #default="scope">
				  
				  <el-tooltip  placement="top" :hide-after="0" :show-after="200">
				  		 <template #content>
							  <div><span class="font-extraSmall">存储体积</span> {{ scope.row.storageVolume}} </div>
							  <div><span class="font-extraSmall">商品体积</span> {{ scope.row.itemVolume}} </div>
							  <div><span class="font-extraSmall">体积计量单位</span> {{ scope.row.volumeUnitMeasurement}} </div>
							  <div><span class="font-extraSmall">仓储类型</span> {{ scope.row.storageType}} </div>
							  <div><span class="font-extraSmall">健康库存级别</span> {{ scope.row.healthyInventoryLevel}} </div>
							  <div><span class="font-extraSmall">建议操作</span> {{ scope.row.recommendedAction}} </div>
							  <div><span class="font-extraSmall">售价</span>{{getCurrencyMark(scope.row.currency)}} {{ scope.row.price}} </div>
							  <div><span class="font-extraSmall">建议售价</span>{{getCurrencyMark(scope.row.currency)}}{{ scope.row.recommendedSalesPrice}} </div>
							  <div><span class="font-extraSmall">建议售价天数</span> {{ scope.row.recommendedSaleDurationDays}} </div>
				  			  <div><span class="font-extraSmall">建议移除数量</span> {{ scope.row.recommendedRemovalQuantity}} </div>
							  <div><span class="font-extraSmall">预估可节约成本</span> {{ scope.row.estimatedCostSavingsOfRecommendedActions}} </div>
						 </template>
				   <div> {{scope.row.available}}</div>
				   </el-tooltip>
				 <el-tooltip placement="top" :hide-after="0" :show-after="200">
				 <template #content>
				 						  <div><span class="font-extraSmall">发货中</span> {{ scope.row.inboundWorking}} </div>
				 						    <div><span class="font-extraSmall">已发货</span> {{ scope.row.inboundShipped}} </div>
				 						 <div><span class="font-extraSmall">接收中</span> {{ scope.row.inboundReceived}} </div>
				 						</template>
				   <div><span class="font-extraSmall">待入库</span> {{scope.row.inboundQuantity}}</div>
				  </el-tooltip>
				
			  </template>
			  </el-table-column>	
	   
		<el-table-column prop="sellThrough" label="持续供货" width="120"   :sort-orders="sortOrders"  sortable="custom" >
			<template #default="scope">
				<el-tooltip  placement="top" :hide-after="0" :show-after="200">
						 <template #content>
							     <div><span class="font-extraSmall">售罄率</span>   {{scope.row.sellThrough}} </div>
								  <div><span class="font-extraSmall">销售排名</span> {{ scope.row.salesRank}} </div>
								  <div><span class="font-extraSmall">产品分类</span> {{scope.row.productGroup}}</div>
						 </template>
				 <div> {{scope.row.daysOfSupply}} 天 </div>
				 </el-tooltip>
				  
			  
				 
			</template>
		</el-table-column>
		<el-table-column prop="fbaInventoryLevelHealthStatus"  :sort-orders="sortOrders"   sortable="custom" label="库存健康状况" width="130"   >
			  <template #default="scope">
				  {{scope.row.fbaInventoryLevelHealthStatus}}
			  </template>
		</el-table-column>	
		<el-table-column prop="historicalDaysOfSupply"  :sort-orders="sortOrders"   sortable="custom" label="历史供货天数" width="130"   >
			  <template #default="scope">
				  {{scope.row.historicalDaysOfSupply}}
			  </template>
		</el-table-column>
		<el-table-column prop="fbaMinimumInventoryLevel"  :sort-orders="sortOrders"   sortable="custom" label="低库存水平量" width="130"   >
			  <template #default="scope">
				  {{scope.row.fbaMinimumInventoryLevel}}
			  </template>
		</el-table-column>
		
			<el-table-column  label="周转数量"   >
				 <el-table-column prop="weeksOfCoverT30" label="30天" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
				 <el-table-column prop="weeksOfCoverT90" label="90天"  width="80"  :sort-orders="sortOrders"   sortable="custom"/>
			</el-table-column>
			<el-table-column label="滞销费用" width="100" prop="estimatedLtsfNextCharge"  :sort-orders="sortOrders"  sortable="custom" >
				<template #default="scope">
					<el-tooltip placement="top" :hide-after="0" :show-after="200">
					<template #content>
						  <div><span class="font-extraSmall">11个月滞销数量</span> {{ scope.row.qtyToBeChargedLtsf11Mo}} </div>
						  <div><span class="font-extraSmall">11个月费用</span> {{getCurrencyMark(scope.row.currency)}}{{ scope.row.projectedLtsf11Mo}} </div>
						 <div><span class="font-extraSmall">12个月滞销数量</span> {{ scope.row.qtyToBeChargedLtsf12Mo}} </div>
						</template>
					<div>{{getCurrencyMark(scope.row.currency)}} {{ scope.row.estimatedLtsfNextCharge}} </div>
					 </el-tooltip>
				</template>	
			</el-table-column>
			<el-table-column  label="下月仓储费" prop="estimatedStorageCostNextMonth"   :sort-orders="sortOrders"  sortable="custom" width="120" >
				 <template #default="scope">
					 {{getCurrencyMark(scope.row.currency)}}{{ scope.row.estimatedStorageCostNextMonth}}
				 </template>
			</el-table-column>
	    <el-table-column  label="库龄数量(单位:天)-季度视角"   >
			 <el-table-column prop="invAge0To90Days" label="0-90" width="78"  	:sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge91To180Days" label="≤180"  width="86"  :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge181To270Days" label="≤270"  width="86" :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge271To365Days" label="≤365"   width="86" :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge365PlusDays" label="365+"   width="85" :sort-orders="sortOrders"   sortable="custom"/>
		</el-table-column>	
		<el-table-column  label="库龄数量-30(单位:天)-月度视角"   >
			 <el-table-column prop="invAge0To30Days" label="0-30"  width="78" :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge31To60Days" label="≤60"   width="75" :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge61To90Days" label="≤90"   width="75" :sort-orders="sortOrders"   sortable="custom"/>
			 <el-table-column prop="invAge181To330Days" label="181-330"  width="110" :sort-orders="sortOrders"    sortable="custom"/>
			 <el-table-column prop="invAge331To365Days" label="≤365"  width="86" :sort-orders="sortOrders"    sortable="custom"/>
			 
		</el-table-column>
		<el-table-column prop="salesShippedLast7Days" label="销售额(站点币种)"   >
			<el-table-column prop="salesShippedLast7Days" label="7天" 
			width="120"  :sort-orders="sortOrders"  sortable="custom">
				<template #default="scope">
									 {{getCurrencyMark(scope.row.currency)}}{{ scope.row.salesShippedLast7Days}}
				</template>
			</el-table-column>	
			<el-table-column prop="salesShippedLast30Days" label="30天" 
			 width="120"  :sort-orders="sortOrders" sortable="custom">
			 <template #default="scope">
			 						 {{getCurrencyMark(scope.row.currency)}}{{ scope.row.salesShippedLast30Days}}
			 	</template>
			 </el-table-column>	
			<el-table-column prop="salesShippedLast60Days" label="60天"  
			width="120"  :sort-orders="sortOrders"  sortable="custom">
			     <template #default="scope">
					 {{getCurrencyMark(scope.row.currency)}}{{ scope.row.salesShippedLast60Days}}
				</template>
			</el-table-column>	
			<el-table-column prop="salesShippedLast90Days" label="90天"   
			width="120"  :sort-orders="sortOrders"  sortable="custom">
			<template #default="scope">
					 {{getCurrencyMark(scope.row.currency)}}{{ scope.row.salesShippedLast90Days}}
				</template>
			</el-table-column>	
		</el-table-column>	
		<el-table-column prop="unitsShippedT7" label="最近发货(单位:天)"   >
			<el-table-column prop="unitsShippedT7" label="7" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
			<el-table-column prop="unitsShippedT30" label="30"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
			<el-table-column prop="unitsShippedT60" label="60"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
			<el-table-column prop="unitsShippedT90" label="90"   width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		</el-table-column>	
		  <el-table-column prop="estimatedExcessQuantity"  :sort-orders="sortOrders"  sortable="custom" label="冗余库存" width="100"   > </el-table-column>
	  <el-table-column  label="龄库存附加费数量"   >
	  	<el-table-column prop="quantityToBeChargedAis181T210Days" label="180-210"  width="100"  :sort-orders="sortOrders"  sortable="custom"/>
	  	<el-table-column prop="quantityToBeChargedAis211T240Days" label="≤240" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
	  	<el-table-column prop="quantityToBeChargedAis241T270Days" label="≤270" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
	  	<el-table-column prop="quantityToBeChargedAis271T300Days" label="≤300" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="quantityToBeChargedAis301T330Days" label="≤330" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="quantityToBeChargedAis331T365Days" label="≤365" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="quantityToBeChargedAis365PlusDays" label="365+" width="80"  :sort-orders="sortOrders"  sortable="custom"/>
	  </el-table-column>
	  <el-table-column  label="超龄库存附加费"   >
		<el-table-column prop="estimatedAis181T210Days" label="181-210"   width="100"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis211T240Days" label="≤240"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis241T270Days" label="≤270"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis271T300Days" label="≤300"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis301T330Days" label="≤330"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis331T365Days" label="≤365"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
		<el-table-column prop="estimatedAis365PlusDays" label="365+"  width="80"  :sort-orders="sortOrders"  sortable="custom"/>
	  </el-table-column>	
	  </template>
	 </GlobalTable>
	</el-row>
		</div>
</template>
<script>
	export default{ name:"FBA滞销" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import Group from '@/components/header/group.vue';
	import Region from '@/components/header/region.vue';
	import inventoryRptApi from "@/api/amazon/inventory/inventoryRptApi.js";
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus'
	const globalTable=ref();
	const groupRef=ref();
	let state=reactive({
		 downLoading:false,
		 dateValue:"",
		 searchKeywords:"",
		 selectlabel:"sku",
		 queryParam:{
			 startDate:"",
			 endDate:"",
			 groupid:'',
			 search:"",
			 marketplaceid:"",
		 },
		 sortOrders:[ 'descending','ascending', null],
		 isload:true,
		 tableData:{records:[],total:0},
		 selectfield:"",
		 fieldList:[
			 {value:"",label:"无条件(有则数量大于0)"},
			 {value:"available",label:"可用库存"},
			 //{value:"pending_removal_quantity",label:"等待移除库存"},
			 {value:"inv_age_0_to_90_days",label:"90天库龄"},
			 {value:"inv_age_91_to_180_days",label:"91-180天库龄"},
			 {value:"inv_age_181_to_270_days",label:"181-270天库龄"},
			 {value:"inv_age_271_to_365_days",label:"271-365天库龄"},
			 {value:"inv_age_365_plus_days",label:"365天库龄"},
			 // {value:"qty_to_be_charged_ltsf_11_mo",label:"长期仓储费：超过11个月的仓库费"},
			 // {value:"projected_ltsf_11_mo",label:"预估下期仓储费：超过11个月的"},
			 // {value:"qty_to_be_charged_ltsf_12_mo",label:"长期仓储费：超过12个月的仓库费"},
			 //{value:"estimated_ltsf_next_charge",label:"预估下期长期仓储费"},
			 {value:"units_shipped_t7",label:"最近7天发货"},
			 {value:"units_shipped_t30",label:"最近30天发货"},
			 {value:"units_shipped_t60",label:"最近60天发货"},
			 {value:"units_shipped_t90",label:"最近90天发货"},
			 //{value:"alert",label:"低流量或低转化率提醒"},
			 //{value:"your_price",label:""},
			 //{value:"sales_price",label:""},
			 //{value:"lowest_price_new_plus_shipping",label:""},
			 //{value:"lowest_price_used",label:""},
			 //{value:"recommended_action",label:""},
			 //{value:"healthy_inventory_level",label:"健康库存级别"},
			 //{value:"recommended_sales_price",label:"建议售价"},
			 //{value:"recommended_sale_duration_days",label:"建议售价的天数"},
			 //{value:"recommended_removal_quantity",label:"建议移除数量"},
			 //{value:"estimated_cost_savings_of_recommended_actions",label:"可以节约成本"},
			 //{value:"sell_through",label:"售罄率"},
			 //{value:"item_volume",label:"商品的体积"},
			 //{value:"volume_unit_measurement",label:"商品体积的计量单位"},
			 //{value:"storage_type",label:"仓储类型分类"},
			 //{value:"storage_volume",label:"存储体积"},
			 //{value:"product_group",label:"产品分类"},
			 //{value:"sales_rank",label:"销售排名"},
			 //{value:"days_of_supply",label:"持续供货(天）"},
			 //{value:"estimated_excess_quantity",label:"预计冗余商品数量"},
			 {value:"weeks_of_cover_t30",label:"30天周转"},
			 {value:"weeks_of_cover_t90",label:"90天周转"},
			 //{value:"featuredoffer_price",label:"价格"},
			 {value:"sales_shipped_last_7_days",label:"7天销售额"},
			 {value:"sales_shipped_last_30_days",label:"30天销售额"},
			 {value:"sales_shipped_last_60_days",label:"60天销售额"},
			 {value:"sales_shipped_last_90_days",label:"90天销售额"},
			 {value:"inv_age_0_to_30_days",label:"30天库龄"},
			 {value:"inv_age_31_to_60_days",label:"31-60天库龄"},
			 {value:"inv_age_61_to_90_days",label:"61-90天库龄"},
			 {value:"inv_age_181_to_330_days",label:"181-330天库龄"},
			 {value:"inv_age_331_to_365_days",label:"331-365天库龄"},
			 //{value:"estimated_storage_cost_next_month",label:"预估下个月仓储费"},
			// {value:"inbound_quantity",label:"待入库总数"},
			// {value:"inbound_working",label:"待入库发货中"},
			// {value:"inbound_shipped",label:"待入库已发货"},
			// {value:"inbound_received",label:"待入库接收中"},
			 //{value:"no_sale_last_6_months",label:"6个月内没有售出的数量"},
			 //{value:"reserved_quantity",label:"总的预留库存呢"},
			 //{value:"unfulfillable_quantity",label:"不可售库存"},
			 //{value:"afn_researching_quantity",label:"正在找回的库存"},
			 //{value:"afn_reserved_future_supply",label:"预留库存"},
			 //{value:"afn_future_supply_buyable",label:"未来可售"}
		 ],
		 snapshotDate:'',
		 summaryData:{},
	});
	let {
	   dateValue,
	   searchKeywords,
	   selectlabel,
	   queryParam,
	   isload,
	   sortOrders,
	   tableData,
	   fieldList,
	   snapshotDate,
	   selectfield,
	   summaryData,
	   downLoading,
	} =toRefs(state);
	
	function changeGroup(data){
		state.queryParam.groupid=data.groupid;
		state.queryParam.marketplaceid=data.marketplaceid;
		state.isload=false;
		loadData(state.queryParam);
	}
	//搜索内容
	function searchConfirm(){
		state.queryParam.search=state.searchKeywords;
		if(state.isload==false){
			loadData(state.queryParam);
		}
	}
	function searchFieldChange(){
	   state.queryParam.field=state.selectfield;
			if(state.isload==false){
				loadData(state.queryParam);
			}
	}
	function clearSearch(){
		groupRef.value.groupid="";
		groupRef.value.marketplaceid="";
		state.queryParam.groupid="";
		state.queryParam.marketplaceid="";
		state.searchKeywords="";
		state.queryParam.field="";
		state.selectfield="";
		searchConfirm();
	}
	//搜索类型
	// function searchTypeChange(){
	// 	state.queryParam.searchtype=state.selectlabel;
	// 	if(state.isload==false){
	// 		loadData(state.queryParam);
	// 	}
	// }
	function resetForm(){
		state.moreSearchVisible = false;
	}
	 
	
	function loadData(params){
		loadTableSummary();
		globalTable.value.loadTable(params);
	}
	
	function loadTableData(params){
		inventoryRptApi.planninglist(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
			if(res.data&&res.data.records&&res.data.records.length>0){
				state.snapshotDate=dateFormat(res.data.records[0].snapshotDate);
			}else{
				state.snapshotDate="无";
			}
		});
	}
	function downloadList(){
		state.downLoading=true
		var data={};
		data.groupid=state.queryParam.groupid;
		data.marketplaceid=state.queryParam.marketplaceid;
		data.search=state.queryParam.search;
		data.field=state.selectfield;
		if(data.search==undefined){
			data.search==null;
		}
		inventoryRptApi.downloadPlanList(data,()=>{
			state.downLoading=false;
		});
	}
	 
	function loadTableSummary(){
		var data={};
		data.groupid=state.queryParam.groupid;
		data.marketplaceid=state.queryParam.marketplaceid;
		data.startDate=state.queryParam.startDate;
		data.endDate=state.queryParam.endDate;
		data.search=state.queryParam.search;
		data.field=state.selectfield;
		if(data.search==undefined){
			data.search==null;
		}
		inventoryRptApi.summaryPlanning(data).then((res)=>{
			if(res.data){
				state.summaryData=res.data;
			}
		});
	}
	onMounted(()=>{
		 loadTableSummary()
	})
	/* 合计行数据 */
	function getSummaries(){
		var arr = ["合计"]
		arr[10]=state.summaryData.invAge0To90Days;
		arr[11]=state.summaryData.invAge91To180Days;
		arr[12]=state.summaryData.invAge181To270Days;
		arr[13]=state.summaryData.invAge271To365Days;
		arr[14]=state.summaryData.invAge365PlusDays;
		arr[15]=state.summaryData.invAge0To30Days;
		arr[16]=state.summaryData.invAge31To60Days;
		arr[17]=state.summaryData.invAge61To90Days;
		arr[18]=state.summaryData.invAge181To330Days;
		arr[19]=state.summaryData.invAge331To365Days;
		if(state.queryParam.marketplaceid&&state.queryParam.marketplaceid!=""&&state.queryParam.marketplaceid!="all"){
			arr[8]=getCurrencyMark(state.summaryData.currency)+state.summaryData.estimatedLtsfNextCharge ;
			arr[9]=getCurrencyMark(state.summaryData.currency)+state.summaryData.estimatedStorageCostNextMonth ;
			arr[20]=getCurrencyMark(state.summaryData.currency)+state.summaryData.salesShippedLast7Days;
			arr[21]=getCurrencyMark(state.summaryData.currency)+state.summaryData.salesShippedLast30Days;
			arr[22]=getCurrencyMark(state.summaryData.currency)+state.summaryData.salesShippedLast60Days;
			arr[23]=getCurrencyMark(state.summaryData.currency)+state.summaryData.salesShippedLast90Days;
			arr[36]=state.summaryData.estimatedAis181T210Days
			arr[37]=state.summaryData.estimatedAis211T240Days
			arr[38]=state.summaryData.estimatedAis241T270Days
			arr[39]=state.summaryData.estimatedAis271T300Days
			arr[40]=state.summaryData.estimatedAis301T330Days
			arr[41]=state.summaryData.estimatedAis331T365Days
			arr[42]=state.summaryData.estimatedAis365PlusDays
		}
		arr[24]=state.summaryData.unitsShippedT7;
		arr[25]=state.summaryData.unitsShippedT30;
		arr[26]=state.summaryData.unitsShippedT60;
		arr[27]=state.summaryData.unitsShippedT90;
		arr[28]=state.summaryData.estimatedExcessQuantity;
		
		arr[29]=state.summaryData.quantityToBeChargedAis181T210Days
		arr[30]=state.summaryData.quantityToBeChargedAis211T240Days
		arr[31]=state.summaryData.quantityToBeChargedAis241T270Days
		arr[32]=state.summaryData.quantityToBeChargedAis271T300Days
		arr[33]=state.summaryData.quantityToBeChargedAis301T330Days
		arr[34]=state.summaryData.quantityToBeChargedAis331T365Days
		arr[35]=state.summaryData.quantityToBeChargedAis365PlusDays
		

		return  arr
	}
</script>

<style>
</style>
