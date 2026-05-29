<template>
	<div class="main-sty">
		<div class="con-header" >
		  <el-row>
		    <el-space >
				<Group @change="getGroup"    ref="groupRef" />
				<Datepicker ref="datepickersRef" :shortIndex="2"  @changedate="changedate" />
		   <el-input  v-model="searchKeywords" clearable @input="handleQuery" placeholder="请输入" class="input-with-select" >
		      <template #prepend> 
		        <el-select v-model="selectlabel"  placeholder="SKU" style="width: 110px">
		          <el-option label="SKU" value="sku"></el-option>
				  <el-option label="MSKU" value="msku"></el-option>
		        </el-select>
		      </template>
		      <template #append>
		        <el-button @click="handleQuery">
		          <el-icon style="font-size: 16px;align-itmes:center"><Search /></el-icon>
		        </el-button>
		      </template>
		    </el-input>
		   </el-space>
		   <div class='rt-btn-group'>
			   <el-button @click.stop="downloadList" :loading="downLoading"  type="primary">导出</el-button>  
		   </div>
		</el-row>
		</div>
	<el-row>
	<GlobalTable ref="globalTable" height="calc(100vh - 200px)" :tableData="tableData"   style="width: 100%;margin-bottom:16px;"  @loadTable="loadTableData"   >
		 <template #field>
			 <el-table-column prop="name" fixed label="图片"   width="65"   >
			    <template #default="scope">
			       <img :src="scope.row.image" style="width:40px; height:40px"></img>
			   </template>
			 </el-table-column>
			<el-table-column prop="name" fixed label="商品信息" sortable="custom" show-overflow-tooltip   >
			   <template #default="scope">
			      <div class='name' v-if="scope.row.name" >{{scope.row.name}}</div>
			      <div class='sku'>{{scope.row.sku}} 
				  <span class="font-extraSmall">本地SKU：{{scope.row.msku}}</span>
			      </div>
			  </template>
			</el-table-column>
			<el-table-column prop="wname"   label="店铺站点" sortable="custom"  width="170" >
				 <template #default="scope">
					<div>{{scope.row.gname}}-{{scope.row.marketname}}</div>
					<div><span class="font-extraSmall">货件:</span>{{scope.row.shipmentid}}</div>
				 </template>
			</el-table-column>
			<el-table-column prop="shipmentid"   label="发货仓库" sortable="custom"  width="140" >
				 <template #default="scope">
					 <div> {{scope.row.wname}}</div>
					 <div><span class="font-extraSmall">sku发货数量：</span>{{scope.row.qty}}</div>
				 </template>
			</el-table-column>
			<el-table-column prop="createdate"   label="计划时间"  sortable="custom" width="160" >
				<template #default="scope">
				    <p>计划时间:{{dateFormat(scope.row.createdate)}}</p>
					<p>审核时间:{{dateFormat(scope.row.approveDate)}}</p>
				</template>
			</el-table-column>
			<el-table-column prop="createdate"   label="配货时间" sortable="custom"  width="160" >
				<template #default="scope">
				   <p>配货:{{dateTimesWithoutYearFormat(scope.row.shipDate)}}</p>
				   <p>装箱:{{dateTimesWithoutYearFormat(scope.row.boxDate)}}</p>
				   <p>打标:{{dateTimesWithoutYearFormat(scope.row.labelDate)}}</p>
				   
				</template>
			</el-table-column>
			<el-table-column prop="shippedDate"   label="出库时间" sortable="custom"  width="160" >
				<template #default="scope">
					<p>出库时间:{{dateFormat(scope.row.shippedDate)}}</p>
					<p>收货时间:{{dateFormat(scope.row.receiveDate)}}</p>
				</template>
			</el-table-column>
			<el-table-column prop="shippedDate"   label="物流信息" sortable="custom"  width="260" >
				<template #default="scope">
					<p><span v-if="scope.row.subarea">{{scope.row.subarea}}</span>
					<span v-if="scope.row.subarea&&scope.row.transtype" class="font-extraSmall"> | </span>
					<span v-if="scope.row.transtype">{{scope.row.transtype}}</span></p>
					<p>{{scope.row.channelname}}<span class="font-extraSmall"> | {{scope.row.channame}}</span></p>
					<p class="font-extraSmall">{{scope.row.logitics}}</p>
				</template>
			</el-table-column>
			<el-table-column prop="shippedDate"   label="完成时间" sortable="custom"   width="160"  >
				<template #default="scope">
					<p>完成时间:{{dateFormat(scope.row.endDate)}}</p>
				</template>
			</el-table-column>
			<el-table-column prop="shipdays"   label="发货时效"  sortable="custom"  width="120"  >
				<template #default="scope">
					<p>{{scope.row.shipdays}} 天 </p>
				</template>
			</el-table-column>
			<el-table-column prop="receivedays"   label="收货时效"  sortable="custom"  width="120"  >
				<template #default="scope">
					<p>{{scope.row.receivedays}} 天 </p>
				</template>
			</el-table-column>
			<el-table-column prop="days"   label="总时效"  sortable="custom"   width="120"  >
				<template #default="scope">
					<p>{{scope.row.days}} 天 </p>
				</template>
			</el-table-column>
	     </template>
	 </GlobalTable>
	</el-row>
		</div>
</template>

<script setup>
	import{ref,reactive,toRefs,onMounted,inject}from"vue";
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat,dateTimesWithoutYearFormat} from "@/utils/index.js";
	import Datepicker from '@/components/header/datepicker.vue';
	import shipmentApi from "@/api/amazon/inbound/shipmentApi.js";
	import Group from '@/components/header/group.vue';
	let state=reactive({
		 downLoading:false,
		 tableData:{records:[],total:0},
		 searchKeywords:"",
		 selectlabel:'sku',
		 queryParam:{},
	});
	let {
	   tableData,
	   downLoading,
	   searchKeywords,
	   selectlabel,
	   queryParam,
	} =toRefs(state);
	const globalTable=ref();
	function getGroup(value){
		state.queryParam.groupid=value.groupid;
		state.queryParam.marketplaceid=value.marketplaceid;
		handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.queryParam.startDate=dates.start;
		state.queryParam.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	
	function handleQuery(){
		if(state.selectlabel=='sku'){
			state.queryParam.sku=state.searchKeywords;
		}else{
			state.queryParam.msku=state.searchKeywords;
		}
		globalTable.value.loadTable(state.queryParam);
	}
	
	function loadTableData(params){
		shipmentApi.getShipTimeList(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		});
	}
	
	function downloadList(){
		state.downLoading=true;
		if(state.selectlabel=='sku'){
			state.queryParam.sku=state.searchKeywords;
		}else{
			state.queryParam.msku=state.searchKeywords;
		}
		shipmentApi.downloadTimeList(state.queryParam,()=>{
			state.downLoading=false;
		});
	}
	onMounted(()=>{  })
</script>

<style>
</style>