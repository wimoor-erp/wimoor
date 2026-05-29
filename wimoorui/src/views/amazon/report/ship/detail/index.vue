<template>
	<div   class="main-sty">
		<el-tabs v-model="selecttype"  @tab-change="handleQuery">
		  <el-tab-pane  label="按货件汇总" name="shipment" key="shipment">
		  </el-tab-pane>
		  <el-tab-pane  label="按SKU汇总" name="sku" key="sku">
		  </el-tab-pane>
		</el-tabs>
		  <el-row>
			  	<div class="con-header" style="margin-bottom:20px;">
					<el-space >
				 <Group  @change="changeGroup"   />
				 <el-select v-model="queryParam.datetype" style="width:100px">
					 <el-option value="createdate" key="createdate" label="创建日期"></el-option>
					 <el-option value="deliverydate" key="deliverydate"  label="发货日期"> </el-option>
				 </el-select>
				  <Datepicker longtime="ok" ref="datepickers" @changedate="changedate" />
				 
				   <Warehouse @changeware="getWarehouse" defaultValue="all"  />
				 <el-input  v-model="queryParam.search" clearable @input="handleQuery" placeholder="请输入货件编码" class="input-with-select" >
					<template #append>
					  <el-button @click="handleQuery">
						 <el-icon style="font-size: 16px;align-itmes:center">
						  <search />
					   </el-icon>
					  </el-button>
					</template>
				  </el-input>
				  <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible" :width="450" trigger="click">
				  	<template #reference>
				  	
				  		      <el-button :color="filterBtnColor" plain class='ic-btn'>
				  		      	 <Filtericon></Filtericon>
				  		      </el-button>
				  	</template>
				  	 <el-form :model="form"  label-width="auto"  label-position="left">
				  				 <el-form-item label="接收异常">
				  				 		 <el-select v-model="queryParam.hasexceptionnum"   @change="handleQuery">
														<el-option   value="all" key="all" label="全部"></el-option>
														<el-option   value="true" key="true" label="是"></el-option>
														<el-option   value="false" key="false" label="否"></el-option>
				  				 		 </el-select>
				  				   </el-form-item>
				  				   <el-form-item label="选择承运商">
				  				   		 	<el-select v-model="queryParam.companyid" filterable placeholder="" @change="handleQuery">
				  				   		 		<el-option v-for="item in companylist" :value="item.id" :key="item.id" :label="item.name"></el-option>
				  				   		 	</el-select> 
				  				     </el-form-item>
				  			    <el-form-item>
				  			  <el-button type="primary" @click="submitSearch(formRef)">搜索</el-button>
				  			  <el-button @click="moreSearchVisible=false">取消</el-button>
				  			    </el-form-item>
				  			
				  	</el-form>
				  	
				    </el-popover>
				   </el-space>
		   	</div>
		   
		   <div class='rt-btn-group' style="margin-bottom:20px;">
			   <el-space v-if="selecttype=='sku'&&skusummary" class="font-extraSmall text-center">
				  <div>总发货:{{skusummary.QuantityShipped}} </div>
				  <div>总接收：{{skusummary.QuantityReceived}} </div>
			   </el-space>
			   <el-dropdown v-if="selecttype=='sku'" style="margin-right: 5px;" split-button type="primary" @click="downloadList('mainlist')">
			       导出
			         <template #dropdown>
			           <el-dropdown-menu>
			            <el-dropdown-item @click.stop="downloadList('other')">含子SKU</el-dropdown-item>
						<el-dropdown-item @click.stop="downloadList('shipqty')">发货数量简约版</el-dropdown-item>
						<el-dropdown-item @click.stop="downloadList('shiptask')">发货处理任务量</el-dropdown-item>
			           </el-dropdown-menu>
			         </template>
			       </el-dropdown>
				   <el-dropdown v-else style="margin-right: 5px;" split-button type="primary" @click.stop="downloadList('shipment')">
				       导出
				         <template #dropdown>
				           <el-dropdown-menu>
				            <el-dropdown-item @click.stop="downloadList('shipqty')">发货数量简约版</el-dropdown-item>
							<el-dropdown-item @click.stop="downloadList('shiptask')">发货处理任务量</el-dropdown-item>
				           </el-dropdown-menu>
				         </template>
				       </el-dropdown>
		   <el-button class='ic-btn'  title='自定义列'>
		      <setting-two theme="outline" size="16"  :strokeWidth="3"/>
		   </el-button>
		    <el-button   class='ic-btn' title='帮助文档'>
		     <help theme="outline" size="16" :strokeWidth="3"/>
		   </el-button>
		   </div>
		   
		</el-row>
		
	<el-row  v-if="selecttype=='shipment'" >
	<GlobalTable
	  ref="globalTable" :tableData="tableData"   height="calc(100vh - 260px)" @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="ShipmentId"   sortable="custom"    label="货件信息"   >
			  <template #default="scope">
				  <div>{{scope.row.ShipmentId}}</div>
				  <div><span class="font-extraSmall">状态:</span>{{scope.row.shipmentstatus}}</div>
				  <div>{{scope.row.warehouse}}</div>
			 </template>
		 </el-table-column>
		 <el-table-column prop="createdate"  sortable="custom"    label="订单日期" width="150" >
		 			 <template #default="scope">
		 				  <div><span class="font-extraSmall">创建日期:</span>{{dateFormat(scope.row.createdate)}}</div>
		 				  <div><span class="font-extraSmall">发货日期:</span>{{dateFormat(scope.row.shiped_date)}}</div>
		 				   <div><span class="font-extraSmall">开始接收日期:</span>{{dateFormat(scope.row.start_receive_date)}}</div>
						   <div><span class="font-extraSmall">完成日期:</span>{{dateFormat(scope.row.status6date)}}</div>
						   
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="groupname"   sortable="custom"    label="收货站点" width="150" >
		 			 <template #default="scope">
		 				  <div> {{scope.row.groupname}}({{scope.row.market}})</div>
		 				  <div><span class="font-extraSmall">配送中心:</span>{{scope.row.center}}</div>
		 				   <div><span class="font-extraSmall">预计到货:</span>{{dateFormat(scope.row.arrivalTime)}}</div>
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="company"   sortable="custom" label="承运商"   >
		 			 <template #default="scope">
		 				  <div> {{scope.row.company}}</div>
		 				  <div><span class="font-extraSmall">渠道:{{scope.row.channame}}</span></div>
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="qtyshipped"   sortable="custom"  label="接收数量" width="100" >
		 			 <template #default="scope">
		 				  <div> {{scope.row.qtyreceived}}</div>
		 				  <div><span class="font-extraSmall">发货数量:</span>{{scope.row.qtyshipped}}</div>
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="totalprice"    sortable="custom"    label="物流总费用数量" width="140" >
		 			 <template #default="scope">
		 				  <div> {{scope.row.totalprice}}</div>
		 				  <div><span class="font-extraSmall">运输费用:</span>{{scope.row.price}}</div>
						  <div><span class="font-extraSmall">其它费用:</span>{{scope.row.otherfee}}</div>
		 			 </template>
		 </el-table-column>
		 <el-table-column prop="transweight"   sortable="custom"   label="运输重量"   >
		 			 <template #default="scope">
		 				  <div><span class="font-extraSmall">实际计费重量(kg):</span>{{scope.row.transweight}}</div>
		 				   <div><span class="font-extraSmall">预估发货重量(kg):</span>{{scope.row.weightkg}}</div>
						    <div><span class="font-extraSmall">装箱实际重量(kg):</span>{{scope.row.boxweight}}</div>
							 <div><span class="font-extraSmall">装箱材积重量:</span>{{scope.row.boxvolume}}</div>
		 			 </template>
		 </el-table-column>
	  </template>
	 </GlobalTable>
	</el-row>
	
	<el-row v-else>
	<GlobalTable  
	  ref="skuglobalTable" :tableData="skutableData"   height="calc(100vh - 260px)" @loadTable="skuloadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
		 <template #field>
		 <el-table-column prop="sku"    sortable="custom"  label="SKU"  ></el-table-column>
		 <el-table-column prop="ShipmentId"   sortable="custom"   label="货件编码" width="150" >
		 			 <template #default="scope">
		 				  <div> {{scope.row.ShipmentId}}</div>
		 				  <div><span class="font-extraSmall">{{scope.row.number}}</span></div>
		 			 </template>
		 </el-table-column>
			  <el-table-column prop="groupname"   sortable="custom"    label="发货店铺" width="100" ></el-table-column>
			   <el-table-column prop="market"     sortable="custom"  label="收货站点" width="100" >
				   <template #default="scope">
							  <div> {{scope.row.market}}<span class="font-extraSmall"> ({{scope.row.center}})</span></div>
				   </template>
			   </el-table-column>
			    <el-table-column prop="warehouse"    sortable="custom"  width="180"  label="发货仓库"  ></el-table-column>
				<el-table-column prop="shiped_date"    sortable="custom"   label="发货日期" width="100" >
						   <template #default="scope">
									  <div> {{dateFormat(scope.row.shiped_date)}}</div>
						   </template>
				</el-table-column>
				<el-table-column prop="market"    sortable="custom"   label="发货渠道"  >
						   <template #default="scope">
									  <div> {{scope.row.channame}}</div>
									 <div><span class="font-extraSmall">预计到货日期:{{dateFormat(scope.row.arrivalTime)}}</span></div>
						   </template>
				</el-table-column>
				<el-table-column prop="QuantityShipped"     sortable="custom"  label="发货数量" width="100" ></el-table-column>
				<el-table-column prop="QuantityReceived"     sortable="custom"  label="到货数量" width="100" ></el-table-column>
				<el-table-column prop="shipmentstatus"     sortable="custom"  label="状态" width="130" ></el-table-column>
				<el-table-column prop="createdate"     sortable="custom"  label="创建日期" width="100" >
					<template #default="scope">
									 <div> {{dateFormat(scope.row.createdate)}} </div>
					</template>
				</el-table-column>
	  </template>
	 </GlobalTable>
	 </el-row>
 </div>
	 
</template>
<script>
    export default{ name:"发货详情" };
</script>
<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs,nextTick} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElDivider } from 'element-plus'
	import Filtericon from '@/components/icon/filtericon.vue';
	import Warehouse from '@/components/header/warehouse.vue';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import reportApi from '@/api/amazon/inbound/reportApi.js';
	import {findProcessHandle } from '@/api/erp/assembly/assemblyApi.js'
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import {ElMessage } from 'element-plus';
	import Datepicker from '@/components/header/datepicker.vue';
	import transportationApi from '@/api/erp/ship/transportationApi.js';
	import Group from '@/components/header/group.vue';
	import inventoryRptApi from "@/api/erp/inventory/inventoryRptApi.js";
	const globalTable=ref();
	const skuglobalTable=ref();
	let state=reactive({
		 queryParam:{
			 search:"",
			 marketplaceid:"",
			 datetype:"createdate",
			 companyid:"",
			 hasexceptionnum:"all"
		 },
		 skusummary:null,
		 isload:true,
		 tableData:{records:[],total:0},
		 skutableData:{records:[],total:0},
		 snapshotDate:'',
		 
		companylist:[],
		channellist:[],
		moreSearchVisible:false,
		selecttype:"shipment",
	});
	let {
	   queryParam,
	   isload,
	   tableData,
	   companylist,
	   skusummary,
	   channellist,
	   moreSearchVisible,
	   selecttype,
	   skutableData
	} =toRefs(state);
 
	function submitSearch(){
		 handleQuery();
		 state.moreSearchVisible=false;
	}
	function changeGroup(data){
		state.queryParam.groupid=data.groupid;
		state.queryParam.marketplaceid=data.marketplaceid;
		state.isload=false;
		handleQuery();
	}
	function getWarehouse(val){
		state.queryParam.warehouseid=val;
		if(state.isload==false){
			handleQuery();
		}
	}
	//日期改变
	function changedate(dates){
		state.queryParam.fromdate=dates.start;
		state.queryParam.enddate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
		 
	}
	function handleQuery(){
		nextTick(()=>{
			if(state.selecttype=="shipment"){
				var timer=setTimeout(function(){
					globalTable.value.loadTable(state.queryParam);
					clearTimeout(timer);
				},500)
				
			}else{
				var timer=setTimeout(function(){
					skuglobalTable.value.loadTable(state.queryParam);
					clearTimeout(timer);
				},500)
			}
		})
		
	}
	 
	function loadTableData(params){
		reportApi.getShipmentReport(params).then((res)=>{
			state.isload=false;
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
	
	function skuloadTableData(params){
		reportApi.getShipmentDetailReport(params).then((res)=>{
			state.isload=false;
			state.skutableData.records = res.data.records;
			if(params.currentpage==1){
				state.skusummary=res.data&&res.data.records&&res.data.records.length>0?res.data.records[0].summary:null;
			}
			state.skutableData.total =res.data.total;
		});
	}
	
	
	function downloadList(ftype){
		if(ftype=="shiptask"){ 
			findProcessHandle({"fromdate":state.queryParam.fromdate,"enddate":state.queryParam.enddate});
		}else if(ftype=="shipqty"){
			inventoryRptApi.downloadOutstockformOut({"fromdate":state.queryParam.fromdate,"enddate":state.queryParam.enddate});
		}else{
			state.queryParam.downloadType=ftype;
			reportApi.downShipmentExcel(state.queryParam,()=>{
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
 
	onMounted(()=>{
		loadCompany()
	})
</script>

<style>
</style>
