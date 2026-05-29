<template>
	<el-dialog title="发货统计"
	           v-model="dialog.visible"
	           :append-to-body="true" 
			   class="afterpdailog"
			   top="2vh"
			   width="80%">
			   <el-row :gutter="24" style="margin-top:-20px;">
			       <el-col :span="8">
					   <div class="card-header flex-center">
						   <el-space>
						   <el-icon color="#67c23a"><SuccessFilled /></el-icon>
					     <span class="font-bold "> 已发货数量</span>
						 </el-space>
					   </div>
			         <el-card shadow="never" :class="'cardSty'+shadow.ship" @click="handleCardChange('ship')">
						 <div ref="shipSummaryChart" style="width: 100%;height:260px"></div>
				     </el-card>
					 <div class="card-header flex-center cell-t">
						   <el-space>
						   <el-icon color="#ff7315"><RemoveFilled /></el-icon>
					        <span class="font-bold "> 预估将发货数量</span>
						</el-space>
					 </div>
					 <el-card shadow="never" :class="'cardSty'+shadow.plan"  @click="handleCardChange('plan')">
						 <div ref="planSummaryChart" style="width: 100%;height:260px"></div>
					  </el-card>
			       </el-col>
			       <el-col :span="16">
					        <div class="card-header">
								<div class="flex-center-between">
								  <div v-if="state.shadow.ship==='never'" class="font-bold">预估发货产品列表</div>
								  <div v-else class="font-bold">已发货产品列表</div>
								  <div>
								  <el-input 
								   :suffix-icon="Search"
								   v-model="queryParams.search" @change="handleQuery" clearable placeholder="搜索SKU或名称"></el-input>
							   </div>
							   </div>
					        </div>
						  <GlobalTable ref="globalTableRef"
						  		 :tableData="tableData"  
						  		 @loadTable="loadTableData" 
								 :inDialog="true"
								 height="calc(100vh - 320px)"
						  		 >
						  		<template #field>
						  		<el-table-column label="SKU" prop="sku" width="300" sortable='custom' show-overflow-tooltip>
									 <template #default="scope">
										 <el-space>
											 <img v-if="scope.row.image" :src="scope.row.image"   class="pointer" style="width:40px; height:40px"  />
											 <img v-else :src="$require('empty/noimage40.png')"  style="width:40px; height:40px"/>
						  		             <div>
												 <div>{{scope.row.sku}}</div>
												 <div>{{scope.row.name}}</div>
											 </div>
									   </el-space>
									</template>
								</el-table-column>
								<el-table-column label="店铺" prop="marketname" sortable='custom' width="90" show-overflow-tooltip>
									 <template #default="scope">
												 <div>{{scope.row.groupname}}</div>
												 <div>{{scope.row.marketname}}</div>
									</template>
								</el-table-column>
								<el-table-column :prop="item" :label="item" sortable='custom'  v-for="item in state.fieldList">
									
								</el-table-column>
						      </template>
						  </GlobalTable>
			       </el-col>
			     </el-row>
			 <template #footer style="margin-top:-20px;">
				<el-button @click="dialog.visible=false" type="primary" >关闭</el-button>
			</template>	
		</el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted} from"vue"
	import {Search,SuccessFilled,RemoveFilled} from '@element-plus/icons-vue'
	import planafterApi from '@/api/amazon/inbound/planafterApi.js';
	import * as echarts from 'echarts';
	const planSummaryChart = ref()
	const shipSummaryChart = ref()
	const state=reactive({
			  dialog:{visible:false},
			  queryParams:{},
			  shipData:[],
			  planData:[],
			  fieldList:[],
			  shadow:{ship:"never",plan:"always"},
			  tableData:{records:[],total:0}
	    });
		const {
		  queryParams,dialog,shipData,planData,tableData,fieldList,shadow
		} = toRefs(state);
		const globalTableRef=ref();
		function loadTableData(param){
			state.queryParams.sort=param.sort;
			state.queryParams.order=param.order;
			if(param.ftype=="plan"){
				 state.fieldList=[];
				 state.planData.forEach(item=>{
					 state.fieldList.push(item.m);
				 })
			}else{
				state.fieldList=[];
				state.shipData.forEach(item=>{
				    state.fieldList.push(item.m);
				})
			}
			param.fieldlist=state.fieldList;
			planafterApi.list(param).then(res=>{
				state.tableData.records=res.data.records;
				state.tableData.total=res.data.total;
			});
		}
		function handleCardChange(type){
			state.queryParams.ftype=type;
			if(type=="ship"){
				state.shadow.ship="always";
				state.shadow.plan="never";
			}else{
				state.shadow.ship="never";
				state.shadow.plan="always";
			}
			state.queryParams.sort="";
			handleQuery();
		}
		function handleQuery(){
			
			globalTableRef.value.loadTable(state.queryParams);
		}
		function show(groupid){
			state.dialog.visible=true;
			state.shipData=[];
			state.planData=[];
			state.queryParams={'groupid':groupid};
			planafterApi.summary(state.queryParams).then(res=>{
				if(res.data){
					state.fieldList=[];
					res.data.forEach(item=>{
						if(item.ftype=="needship"){
							state.planData.push(item);
						}else{
							state.shipData.push(item);
						}
					});
					const newShipData = state.shipData.reduce((acc,{qty,m})=>{
						acc.values.push(qty);
						acc.time.push(m);
						return acc
					},{values:[],time:[]})
					const newPlanData = state.planData.reduce((acc,{qty,m})=>{
						acc.values.push(qty);
						acc.time.push(m);
						return acc
					},{values:[],time:[]})
					if(newPlanData.values.length>0){
						loadFChart(newPlanData,'plan')
					}
					if(newShipData.values.length>0){
						loadFChart(newShipData,'ship')
					}
					
					state.queryParams.ftype="plan";
					if(globalTableRef.value&&globalTableRef.value.loadTable){
					   globalTableRef.value.loadTable(state.queryParams);
					}else{
						var timer=setTimeout(function(){
							globalTableRef.value.loadTable(state.queryParams);
							clearTimeout(timer);
						},1000);
					}
				}
			});
		}
		const loadFChart = (datas,type)=>{
			if(type==='plan'){
				var myChart = echarts.init(planSummaryChart.value);
				var text = '预估发货数量';
				var color="#ff7315";;
			}else{
				var myChart = echarts.init(shipSummaryChart.value);
				var text = '已发货数量';
				var color="#67c23a";
			}
			var option = {
			     tooltip : {
				  			trigger : 'axis',
				  		     },
			      legend: {
				  				top:16,
				  				icon: "circle",
				  				itemWidth:6,
				  				itemHeight:6,
			      },
			         xAxis: {
				  		data:datas.time,
				  		axisLine:{
			            show: false
				  				},
				  				axisTick:{
				  				show: false
				  				},
				  				axisLabel:{
			           color:"#acb0b9"
				  				},
				  			},
				  			grid:{
				  				right:10,
				  				left:45,
			          bottom:32
				  			},
			      yAxis: {
				  				axisLabel:{
			           color:"#acb0b9"
				  				},
				  				splitLine:{
				  					lineStyle:{
				  					color:"#F2F3F6"
				  					}
				  				}
				  			},
			      series: [
					  {
					  name:text,
					  type:'bar',
					  data:datas.values,
					     label: {
					          show: true,
					          position: 'top'
					        },
							itemStyle:{
								color:color,
							},
					  }
				  ],
			}
			 myChart.setOption(option);
			 window.addEventListener('resize',()=>{
				  myChart.resize();
			 })
		}
		
		defineExpose({show});
</script>
<style>
	.afterpdailog .el-dialog__body{
		margin-bottom:0px;
		padding-bottom:5px !important;
	}
</style>
<style scoped>
	.cell-t{
		margin-top:24px;
	}
	.card-header{
		margin-bottom: 16px;
	}
	.cardStyalways{
		border-color:#ff7315;
		cursor: pointer;
	}
	.cardStynever{
		cursor: pointer;
	}
	
</style>