<template>
    <div class="main-sty">
		<el-tabs v-model="queryParams.ftype"  @tab-change="handleQuery">
		  <el-tab-pane  label="按SKU汇总" name="sku" key="sku">
		  </el-tab-pane>
		  <el-tab-pane  label="按供应商汇总" name="supplier" key="supplier">
		  </el-tab-pane>
		</el-tabs>
        <div class="con-header">
        <el-row>
            <el-space >
                <el-button @click="downLoadExcel">
                    <span>导出</span>
                </el-button>
				 <Warehouse    @changeware="wareChange" defaultValue="all" />
				 	<Datepicker longtime="ok" ref="datepickers" @changedate="changedate" />
                <el-input  v-model="queryParams.search" @clear="handleQuery" :placeholder="ftypePlace" class="input-with-select" clearable>
                    <template #append>
                        <el-button @click="handleQuery">
                            <el-icon style="font-size: 16px;align-itmes:center">
                                <search />
                            </el-icon>
                        </el-button>
                    </template>
                </el-input>
            </el-space>
            <div class='rt-btn-group'>
				<el-button   class='ic-btn' title='帮助文档'>
                    <help theme="outline" size="16" :strokeWidth="3"/>
                </el-button>
            </div>
        </el-row>
        </div>
        <!--表单-->
        <el-row>
			<GlobalTable ref="globalTable"  
			show-summary
			 :summary-method="getSummaries"
			 :tableData="tableData"  height="calc(100vh - 260px)" 
			 :defaultSort="{ prop: 'pprice', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			 style="width: 100%;margin-bottom:16px;">
				<template #field>
				<el-table-column v-if="queryParams.ftype=='sku'" fixed="left" prop="sku" label="SKU" width="170" sortable="custom" ></el-table-column>
				<el-table-column v-else prop="supplier" label="供应商" fixed="left" width="170" sortable="custom" ></el-table-column>
					
						<el-table-column prop="orderamount" width="100"    fixed="left" sortable="custom" >
							<template #header>
								 <el-tooltip placement="top" content="已审核的采购订单上的产品采购数量.(等于 实际入库数量+入库差额+待入库数)">
							      <span>采购数量</span>  
								  </el-tooltip>
							</template>
						</el-table-column>
						<el-table-column prop="totalorderprice"   fixed="left" width="120"  sortable="custom" >
							<template #header>
								 <el-tooltip placement="top" content="已审核的采购订单上的产品采购金额">
							      <span>采购金额(元)</span>  
								  </el-tooltip>
							</template>
						</el-table-column>
						<el-table-column prop="itemprice"  fixed="left"  width="120"  sortable="custom" >
							<template #header>
								 <el-tooltip placement="top" content="已审核的采购订单上, 产品的单价平均值">
							      <span>采购单价(元)</span>  
								  </el-tooltip>
							</template>
							<template #default="scope">
								<span>{{formatFloat(scope.row.itemprice)}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="weight"  fixed="left"  width="120"  sortable="custom" >
							<template #header>
							      <span>预估重量(kg)</span>  
							</template>
						</el-table-column>
				 
				 <el-table-column   label="付款信息">
					 <el-table-column prop="totalcostfee" width="140"    sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="已审核的采购订单中, 已经付款的采购金额, 包括付款未完成和已完成的订单">
					 	      <span>付款金额(元)</span>  
					 		  </el-tooltip>
					 	</template>
					 </el-table-column>
					 <el-table-column prop="waitPay" width="130"    sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="待付款金额=待申请付款+已申请付款">
					 	      <span>待付款金额(元)</span>  
					 		  </el-tooltip>
					 	</template>
					 </el-table-column>
					 <el-table-column prop="pprice"  width="130"  sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="已审核且入库, 付款都执行完成的采购订单里, 按实际支出的采购金额与实际入库的产品数量计算">
					 	      <span>实际单价(元)</span>  
					 		  </el-tooltip>
					 	</template>
						<template #default="scope">
							<span>{{formatFloat(scope.row.pprice)}}</span>
						</template>
					 </el-table-column>
					 <el-table-column prop="totalshipfee"  width="120"   sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="已审核的采购订单的运费总额">
					 	      <span>采购运费(元)</span>  
					 		  </el-tooltip>
					 	</template>
					 </el-table-column>
					 <el-table-column prop="totalotherfee"  width="130"   sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="款项退收时所填写的所有自定义费用项目的总和">
					 	      <span>其它费用(元)</span>  
					 		  </el-tooltip>
					 	</template>
					 </el-table-column>
					 <el-table-column prop="lesspay"  width="130"   sortable="custom" >
					 	<template #header>
					 		 <el-tooltip placement="top" content="已审核且付款已完成的采购订单中, 实际付款的金额与其订单上原金额的差额">
					 	      <span>付款差额(元)</span>  
					 		  </el-tooltip>
					 	</template>
					 </el-table-column>
				</el-table-column>
			  <el-table-column   label="入库信息">
				<el-table-column prop="actual_totalin"  width="130"   sortable="custom" >
					<template #header>
						 <el-tooltip placement="top" content="已审核的采购订单中, 已经入库的产品数量(不含退货), 包括入库未完成和已完成的订单">
					      <span>入库(不含退货)</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
				<el-table-column prop="totalin"  width="120"   sortable="custom" >
					<template #header>
						 <el-tooltip placement="top" content="已审核的采购订单中, 所有的入库数量, 包括入库未完成和已完成的订单">
					      <span>入库(含退货)</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
				
				<el-table-column prop="needin"  width="120"   sortable="custom" >
					<template #header>
						 <el-tooltip placement="top" content="已审核但入库未完成的采购订单中, 待入库的产品数量">
					      <span>待入库数</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
				<el-table-column prop="totalre" width="100"    sortable="custom" >
					<template #header>
						 <el-tooltip placement="top" content="已审核的采购订单的退货总数">
						  <span>退货数</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
				
				<el-table-column prop="lessrec"  width="120"   sortable="custom" >
					<template #header>
						 <el-tooltip placement="top" content="已审核且入库已完成的采购订单中, 实际入库的产品数量与其订单上原采购数量的差额">
					      <span>入库差额</span>  
						  </el-tooltip>
					</template>
				</el-table-column>
				</el-table-column>
				
				 
			</template>
			</GlobalTable>
        </el-row>

    </div>
</template>
<script>
    export default{ name:"采购统计" };
</script>
<script setup>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
    import { ref,reactive,onMounted,toRefs} from 'vue'
    import {Search,ArrowDown} from '@element-plus/icons-vue'
	import listApi from '@/api/erp/purchase/form/listApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import { ElMessageBox,ElMessage } from 'element-plus';
	import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	 let globalTable=ref();
	 const state = reactive({
	 		tableData:{records:[],total:0},
	 		queryParams:{
	 			ftype:"sku",
	 			search:""
	 		},
			selecttype:'sku',
			summary:{
				
			},
			ftypePlace:"请输入产品SKU",
	 });
	 const { queryParams,tableData,summary,selecttype,ftypePlace } = toRefs(state);
	  function handleQuery(){
		  if(state.queryParams.ftype=='sku'){
			  state.ftypePlace="请输入产品SKU";
		  }else{
			  state.ftypePlace="请输入供应商名称";
		  }
	 	  globalTable.value.loadTable(state.queryParams);
	  }
	  function loadTableData(params){
	  		  listApi.getPurchaseFormReport(params).then(res=>{
	  				 state.tableData.records=res.data.records;
	  				 state.tableData.total=res.data.total;
					 if(params.currentpage==1){
							 if(res.data.total>0){
								 state.summary=res.data.records[0].summary;
							 }else{
								 state.summary={};
							 }
					 }
					 
	  		  })
	  }
	  
	 function downLoadExcel(){
	 	listApi.downExcelPurchaseFormReport(state.queryParams); 
	 }
	 
	 function wareChange(val){
		 state.queryParams.warehouseid=val;
		 handleQuery();
	 }
	 //日期改变
	 function changedate(dates){
	 	state.queryParams.fromDate=dates.start;
	 	state.queryParams.toDate=dates.end;
	 	 handleQuery();
	 	 
	 }
	 
	/* 合计行数据 */
	function getSummaries({columns,data}){
		var arr = ["合计"];
			columns.forEach((item,index)=>{
				if(index>=2){
					arr[index]=state.summary[item.property];
				}
			})
		return  arr;
	}
	 
	 
</script>
<style>
</style>