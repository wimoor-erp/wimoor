<template>
	<el-dialog
	    v-model="warningVisible"
	    title="指标警告"
		top="1vh"
	    width="80%"
		class="indicatordetail"
	  >
	     <GlobalTable ref="globalTableRef" :tableData="tableData" @selectionChange="selectionChange"  
		 height="calc(100vh - 250px)" @loadTable="loadTableData" 
		 :defaultSort="{ prop: 'acos0', order: 'descending' }" 
		  :stripe="true"  style="margin-bottom:16px;">
	     		   <template #field>
					    <el-table-column prop="market" label="站点" width="80">
						   <template #default="scope">
							  <div>{{scope.row.groupName}}</div>  
							  <div>{{scope.row.market}}</div> 
						   </template>
						</el-table-column>
						<el-table-column prop="campaignName" label="广告组" width="180"    sortable="custom" />
						<el-table-column prop="adGroupName" label="广告组" width="180"    sortable="custom" />
						
						<el-table-column v-if="wardatatype=='keywords'"     sortable="custom"  prop="keywordText" label="关键词" width="160" >
						  <template #default="scope">
							  <div>{{scope.row.keywordText}}</div> 
						   </template>
						</el-table-column>
						<el-table-column v-else prop="sku" label="SKU" width="160"    sortable="custom" >
						  <template #default="scope">
							  <div>{{scope.row.sku}}</div> 
							  <div class="font-extraSmall">ASIN:{{scope.row.asin}}</div> 
						   </template>
						</el-table-column>
						<template v-if="downtype=='co'">
						<el-table-column label="曝光量">
						<el-table-column prop="impressions0" label="昨天" width="80"    sortable="custom" />
						<el-table-column prop="impressions2" label="上周同期" width="80"    sortable="custom" />
						<el-table-column prop="impressionscorate" 
						                 label="突降比例" width="120"  
						                 sortable="custom"  />
						</el-table-column>
						<el-table-column label="点击量">
						<el-table-column prop="click0" label="昨天" width="80" />
						<el-table-column prop="click2" label="上周同期" width="120" />
						<el-table-column prop="clickcorate" label="突降比例" width="120"  sortable="custom"  />
						</el-table-column>
						 <el-table-column label="订单">
						 <el-table-column prop="orders0" label="昨天" width="80"    sortable="custom" />
						 <el-table-column prop="orders2" label="上周同期" width="120"    sortable="custom" />
						 <el-table-column prop="crcorate" label="突降比例" width="120"   sortable="custom" />
						 </el-table-column>
						 <el-table-column label="ACOS">
						 <el-table-column prop="acos0" label="昨天" width="80"    sortable="custom" />
						 <el-table-column prop="acos2" label="上周同期" width="120"    sortable="custom" />
						 <el-table-column prop="acoscorate" label="突降比例" width="120"  sortable="custom"  />
						 </el-table-column>
						 </template>
						 <template v-if="downtype=='yesterday'">
						 <el-table-column label="曝光量">
						 <el-table-column prop="impressions0" label="昨日" width="80" />
						 <el-table-column prop="impressions2" label="前天" width="80" />
						 <el-table-column prop="impressionsyesterdayrate" label="突降比例" width="120"  sortable="custom"  />
						 </el-table-column>
						 <el-table-column label="点击量">
						 <el-table-column prop="click0" label="昨日" width="80"    sortable="custom" />
						 <el-table-column prop="click2" label="前天" width="80"    sortable="custom" />
						 <el-table-column prop="clickyesterdayrate" label="突降比例" width="120"  sortable="custom"  />
						 </el-table-column>
						  <el-table-column label="订单">
						  <el-table-column prop="orders0" label="昨日" width="80"    sortable="custom" />
						  <el-table-column prop="orders2" label="前天" width="80"    sortable="custom" />
						  <el-table-column prop="cryesterdayrate" label="突降比例" width="120"   sortable="custom" />
						  </el-table-column>
						  <el-table-column label="ACOS">
						  <el-table-column prop="acos0" label="昨日" width="80"    sortable="custom" />
						  <el-table-column prop="acos2" label="前天" width="80"    sortable="custom" />
						  <el-table-column prop="acosyesterdayrate" label="突降比例" width="120"  sortable="custom"  />
						  </el-table-column>
						  </template>
						  <template v-if="downtype=='sequent'">
						  <el-table-column label="曝光量">
						  <el-table-column prop="impressions0" label="昨日" width="80"    sortable="custom" />
						  <el-table-column prop="impressions1" label="昨日" width="80"    sortable="custom" />
						  <el-table-column prop="impressions2" label="3天前" width="80"    sortable="custom" />
						  <el-table-column prop="impressionssequentrate" label="突降比例" width="120"  sortable="custom"  />
						  </el-table-column>
						  <el-table-column label="点击量">
						  <el-table-column prop="click0" label="昨日" width="80"    sortable="custom" />
						  <el-table-column prop="click1" label="前天" width="80"    sortable="custom" />
						  <el-table-column prop="click2" label="3天前" width="80"    sortable="custom" />
						  <el-table-column prop="clicksequentrate" label="突降比例" width="120"  sortable="custom"  />
						  </el-table-column>
						   <el-table-column label="订单">
						   <el-table-column prop="orders0" label="昨日" width="80"    sortable="custom" />
						   <el-table-column prop="orders1" label="前天" width="80"    sortable="custom" />
						   <el-table-column prop="orders2" label="3天前" width="80"    sortable="custom" />
						   <el-table-column prop="crsequentrate" label="突降比例" width="120"   sortable="custom" />
						   </el-table-column>
						   <el-table-column label="ACOS">
						   <el-table-column prop="acos0" label="昨日" width="80"    sortable="custom" />
						   <el-table-column prop="acos1" label="前天" width="80"    sortable="custom" />
						   <el-table-column prop="acos2" label="3天前" width="80"    sortable="custom" />
						   <el-table-column prop="acossequentrate" label="突降比例" width="120"  sortable="custom"  />
						   </el-table-column>
						   </template>
	     	  		 </template>
		  </GlobalTable>
	    <template #footer>
	        <el-button @click="warningVisible = false">关闭</el-button>
	    </template>
	  </el-dialog>
</template>

<script setup>
	import{ref,reactive,toRefs,nextTick}from'vue';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {formatFloat} from '@/utils/index.js';
	const globalTableRef=ref();
	const state = reactive({
		warningVisible:false,
		tableData:{records:[],total:0},
		ftype:"",
		downtype:"",
		wardatatype:"",
	})
	const{
		 warningVisible,
		 tableData,ftype,downtype,wardatatype
	}=toRefs(state)
	function show(ftype,downtype,wardatatype){
		state.ftype=ftype;
		state.downtype=downtype;
		state.wardatatype=wardatatype;
		state.warningVisible=true;
		nextTick(()=>{
		    globalTableRef.value.loadTable()
		})
	}
	defineExpose({ show,  });
	function loadTableData(param){
		if(state.wardatatype=="productads"){
			summaryApi.getProductWarningIndicatorDetail(state.ftype,state.downtype,param).then(res=>{
				if(res.data&&res.data.records){
					res.data.records.forEach(row=>{
						for (let key in row) {
						  if (row.hasOwnProperty(key)&&(key.indexOf("rate")>=0||key.indexOf("acos")>=0)) {
							  if(row[key]){
						         row[key]=formatFloat(row[key]);
							  }
						  }
						}
					})
				}
				state.tableData.records=res.data.records;
				state.tableData.total=res.data.total;
			});
		}else{
			summaryApi.getKeywordsWarningIndicatorDetail(state.ftype,state.downtype,param).then(res=>{
				if(res.data&&res.data.records){
					res.data.records.forEach(row=>{
						for (let key in row) {
						  if (row.hasOwnProperty(key)&&(key.indexOf("rate")>=0||key.indexOf("acos")>=0)) {
						    if(row[key]){
						       row[key]=formatFloat(row[key]);
						    }
						  }
						}
					})
				}
				state.tableData.records=res.data.records;
				state.tableData.total=res.data.total;
			});
		}
	}
	
</script>

<style>
		.indicatordetail .el-dialog__body{
			padding-top:4px;
			padding-bottom:4px;
		}
 
</style>